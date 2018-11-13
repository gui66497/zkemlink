package com.zzjz.zkemlink.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzjz.zkemlink.bean.PunchedCard;
import com.zzjz.zkemlink.bean.ZkUserInfo;
import com.zzjz.zkemlink.mapper.ZkemUserMapper;
import com.zzjz.zkemlink.service.PunchCardService;
import com.zzjz.zkemlink.service.ZkemService;
import com.zzjz.zkemlink.util.ZkemSDK;
import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ZkemServiceImpl.
 *
 * @author Administrator
 * @version 2017/7/5 17:06
 */
@Service
public class ZkemServiceImpl implements ZkemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkemServiceImpl.class);

    private static final Integer INT4370 = 4370;

    @Value("${zkemIp1}")
    String zkemIp1;

    @Value("${zkemIp2}")
    String zkemIp2;

    @Value("${zkemPort}")
    int zkemPort;

    @Autowired
    PunchCardService punchCardService;

    @Autowired
    ZkemUserMapper zkemUserMapper;

    @Override
    public List<PunchedCard> getLogsToday() throws ConnectException {
        Date todayZeroDate = getTodayZero();
        return getLogAfter(todayZeroDate);
    }

    @Override
    public boolean refillOneDayLog(Date date) throws ConnectException {
        List<PunchedCard> punchedCardList = getOneDayLog(date);
        punchedCardList = fillDate(punchedCardList);
        return punchCardService.batchInsert(punchedCardList);
    }

    @Override
    public List<PunchedCard> getLogAfter(Date date) throws ConnectException {
        ZkemSDK sdk = new ZkemSDK();
        List<PunchedCard> punchedCards = new ArrayList<>();
        //恢复两个打卡机
        String[] zkemIpArr = new String[]{zkemIp1, zkemIp2};

        for (int i = 0; i < zkemIpArr.length; i++) {
            boolean connFlag = sdk.connect(zkemIpArr[i], zkemPort);
            if (connFlag) {
                List<PunchedCard> punchedCardsTemp;
                sdk.readLastestLogData(date);
                List<Map<String, Object>> res = sdk.getGeneralLogData();
                //断开连接
                sdk.disConnect();
                punchedCardsTemp = convert(res);
                punchedCards.addAll(punchedCardsTemp);
            } else {
                LOGGER.error("打卡机" + zkemIpArr[i] + "连接失败！");
                throw new ConnectException("打卡机" + zkemIpArr[i] + "连接失败!");
            }
        }
        return punchedCards;
    }

    @Override
    public boolean refillPunchCardAfter(Date date) throws ConnectException {
        List<PunchedCard> punchedCardList = getLogAfter(date);
        //补填date信息
        punchedCardList = fillDate(punchedCardList);
        return punchCardService.batchInsert(punchedCardList);
    }

    /**
     * 补全日期
     * @param punchedCardList punchedCardList
     * @return 1
     */
    List<PunchedCard> fillDate(List<PunchedCard> punchedCardList) {
        for (PunchedCard punch : punchedCardList) {
            String dateStr = punch.getYear() + "-" + punch.getMonth() + "-" + punch.getDay() + " " +
                    punch.getHour() + ":" + punch.getMinute() + ":" + punch.getSecond();
            punch.setDate(dateStr);
        }
        return punchedCardList;
    }

    @Override
    public List<PunchedCard> getOneDayLog(Date date) throws ConnectException {
        List<PunchedCard> punchedCardList = getLogAfter(date);
        List<PunchedCard> oneDayPunchedCards = new ArrayList<>();
        //剔除当天之后的打卡记录
        for (PunchedCard punchedCard : punchedCardList) {
            String d = punchedCard.getYear() + "-" +
                    (punchedCard.getMonth().length() == 1 ? "0" + punchedCard.getMonth() : punchedCard.getMonth()) + "-" +
                    (punchedCard.getDay().length() == 1 ? "0" + punchedCard.getDay() : punchedCard.getDay());
            DateTime dateTime = new DateTime(date);
            if (d.equals(dateTime.toString("yyyy-MM-dd"))) {
                oneDayPunchedCards.add(punchedCard);
            }
        }
        return oneDayPunchedCards;
    }

    @Override
    public List<ZkUserInfo> getAllUserInfo() throws ConnectException {
        List<ZkUserInfo> zkUserList = new ArrayList<>();
        ZkemSDK sdk = new ZkemSDK();
        boolean connFlag1 = sdk.connect(zkemIp1, zkemPort);

        //连接打卡机1
        if (connFlag1) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接1
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);
            zkUserList.addAll(zkUserList1);
        } else {
            LOGGER.error("打卡机1连接失败！");
            throw new ConnectException("打卡机1连接失败!");
        }

        //连接打卡机2
        boolean connFlag2 = sdk.connect(zkemIp2, zkemPort);
        if (connFlag2) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接2
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);
            zkUserList.addAll(zkUserList1);
        } else {
            LOGGER.error("打卡机2连接失败！");
            throw new ConnectException("打卡机2连接失败!");
        }

        List<ZkUserInfo> uniqueZkUsers = new ArrayList<>();
        //过滤 取唯一
        for (ZkUserInfo zkUserInfo : zkUserList) {
            if (!zkContains(uniqueZkUsers, zkUserInfo)) {
                uniqueZkUsers.add(zkUserInfo);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            LOGGER.info(mapper.writeValueAsString(uniqueZkUsers));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueZkUsers;
    }

    @Override
    public ZkUserInfo getZkUserInfoByUid(String userId) throws ConnectException {

        ZkemSDK sdk = new ZkemSDK();
        boolean connFlag1 = sdk.connect(zkemIp1, zkemPort);

        //连接打卡机1
        if (connFlag1) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接1
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);

            for (ZkUserInfo zkUserInfo : zkUserList1) {
                if (userId.equals(zkUserInfo.getUid())) {
                    return zkUserInfo;
                }
            }

        } else {
            LOGGER.error("打卡机1连接失败！");
            throw new ConnectException("打卡机1连接失败!");
        }

        //连接打卡机2
        boolean connFlag2 = sdk.connect(zkemIp2, zkemPort);

        if (connFlag2) {
            List<Map<String, Object>> userList = sdk.getUserInfo();
            //断开连接2
            sdk.disConnect();
            List<ZkUserInfo> zkUserList1 = convertUserInfo(userList);

            for (ZkUserInfo zkUserInfo : zkUserList1) {
                if (userId.equals(zkUserInfo.getUid())) {
                    return zkUserInfo;
                }
            }

        } else {
            LOGGER.error("打卡机2连接失败！");
            throw new ConnectException("打卡机2连接失败!");
        }

        return null;
    }

    @Override
    public boolean synUserToLocal() {
        try {
            List<ZkUserInfo> zkUserInfos = getAllUserInfo();
            if (!zkUserInfos.isEmpty()) {
                //先删除全部信息 然后重新插入
                zkemUserMapper.deleteAll();
                zkemUserMapper.addAll(zkUserInfos);
            }
        } catch (ConnectException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<ZkUserInfo> getAllUserInfoLocal() {
        return zkemUserMapper.getAll();
    }

    @Override
    public String getUserIdByName(String name) {
        return zkemUserMapper.getUserIdByName(name);
    }

    @Override
    public String onConnect() {
        ZkemSDK sdk = new ZkemSDK();
        boolean connFlag2 = sdk.connect(zkemIp1, zkemPort);
        if (connFlag2) {
            sdk.regEvent();
            //如果是程序调用的这里能监控到OnConnected 事件
            sdk.connect("192.168.1.201", INT4370);

        }
        sdk.disConnect();
        return "";
    }

    private boolean zkContains(List<ZkUserInfo> zkUserInfos, ZkUserInfo zkUserInfo) {
        for (ZkUserInfo zkUser : zkUserInfos) {
            if (zkUser.getUid().equals(zkUserInfo.getUid())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换打卡记录
     */
    private List<PunchedCard> convert(List<Map<String, Object>> res) {
        List<PunchedCard> punchedCards = new ArrayList<>();
        if (!CollectionUtils.isEmpty(res)) {
            for (Map<String, Object> map : res) {
                PunchedCard punchedCard = new PunchedCard();
                if (MapUtils.isNotEmpty(map)) {
                    punchedCard.setYear(String.valueOf(map.get("Year")));
                    punchedCard.setMonth(String.valueOf(map.get("Month")));
                    punchedCard.setDay(String.valueOf(map.get("Day")));
                    punchedCard.setHour(String.valueOf(map.get("Hour")));
                    punchedCard.setMinute(String.valueOf(map.get("Minute")));
                    punchedCard.setSecond(String.valueOf(map.get("Second")));
                    punchedCard.setUid(String.valueOf(map.get("EnrollNumber")));
                    punchedCard.setMode(String.valueOf(map.get("VerifyMode")));
                }
                punchedCards.add(punchedCard);
            }
        }
        return punchedCards;
    }

    /**
     * 转换用户信息
     */
    private List<ZkUserInfo> convertUserInfo(List<Map<String, Object>> res) {
        List<ZkUserInfo> zkUserInfoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(res)) {
            for (Map<String, Object> map : res) {
                ZkUserInfo zkUserInfo = new ZkUserInfo();
                if (MapUtils.isNotEmpty(map)) {
                    zkUserInfo.setName(String.valueOf(map.get("Name")));
                    zkUserInfo.setPassword(String.valueOf(map.get("Password")));
                    zkUserInfo.setPrivilege(String.valueOf(map.get("Privilege")));
                    zkUserInfo.setEnabled(String.valueOf(map.get("Enabled")));
                    zkUserInfo.setUid(String.valueOf(map.get("EnrollNumber")));
                }
                zkUserInfoList.add(zkUserInfo);
            }
        }
        return zkUserInfoList;
    }

    /**
     * @return Date
     */
    @Override
    public Date getTodayZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
