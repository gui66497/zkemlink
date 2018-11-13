package com.zzjz.zkemlink;

import com.zzjz.zkemlink.bean.PunchedCard;
import com.zzjz.zkemlink.bean.ZkUserInfo;
import com.zzjz.zkemlink.service.ZkemService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 房桂堂
 * @description ZkemTest
 * @date 2018/11/6 9:03
 */
public class ZkemTest extends ZkemlinkApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkemTest.class);

    @Autowired
    ZkemService zkemService;

    /**
     * 测试连接打卡机获取打卡机器所有用户.
     */
    @Test
    public void testGetAllUserInfo() {
        try {
            List<ZkUserInfo> res = zkemService.getAllUserInfo();
            System.out.println(res);
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试连接打卡机根据用户id获取用户信息.
     */
    @Test
    public void testGetZkUserInfoByUid() {
        try {
            ZkUserInfo zkUserInfo = zkemService.getZkUserInfoByUid("73");
            System.out.println(zkUserInfo);
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试获取本地mysql中的打卡机用户数据.
     */
    @Test
    public void testGetAllUserInfoLocal() {
        List<ZkUserInfo> zkUserInfos = zkemService.getAllUserInfoLocal();
    }


    /**
     * 测试连接打卡机获取今日日志.
     */
    @Test
    public void testGetLogsToday() {
        try {
            List<PunchedCard> res = zkemService.getLogsToday();
            System.out.println(res);
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试连接打卡机获取指定日期日志.
     */
    @Test
    public void testGetOneDayLog() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse("2018-07-25");
            List<PunchedCard> res = zkemService.getOneDayLog(date);
            for (PunchedCard punchedCard : res) {
                LOGGER.info("id:" + punchedCard.getUid()
                        + " 时间:" + punchedCard.getHour() + ":" + punchedCard.getMinute());
            }
        } catch (ParseException | ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试补全指定日期之后的原始打卡信息.
     */
    @Test
    public void testRefillPunchCardAfter() throws ParseException, ConnectException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2018-11-01 00:00:00");
        boolean res = zkemService.refillPunchCardAfter(date);
        System.out.println(res);
    }

    @Test
    public void testRefillOneDayLog() throws ParseException, ConnectException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2018-11-12");
        boolean res = zkemService.refillOneDayLog(date);
        System.out.println(res);
    }
}
