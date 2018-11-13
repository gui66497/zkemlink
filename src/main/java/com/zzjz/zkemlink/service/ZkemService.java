package com.zzjz.zkemlink.service;

import com.zzjz.zkemlink.bean.PunchedCard;
import com.zzjz.zkemlink.bean.ZkUserInfo;
import org.springframework.stereotype.Service;
import java.net.ConnectException;
import java.util.Date;
import java.util.List;

/**
 * 打卡机调用service.
 *
 * @author fgt
 * @version 2017/7/5 16:43
 */
@Service
public interface ZkemService {

    /**
     * 获取指定日期之后的打卡数据.
     *
     * @param date 日期 (年月日 时分秒)
     * @return 打卡记录
     * @throws ConnectException ConnectException
     */
    List<PunchedCard> getLogAfter(Date date) throws ConnectException;

    /**
     * 补全指定日期之后的原始打卡信息.
     * @param date 日期
     * @return 结果
     * @throws ConnectException ConnectException
     */
    boolean refillPunchCardAfter(Date date) throws ConnectException;

    /**
     * 获取指定日期的打卡数据.
     *
     * @param date 日期 (年月日 时分秒)
     * @return 打卡记录
     * @throws ConnectException ConnectException
     */
    List<PunchedCard> getOneDayLog(Date date) throws ConnectException;

    /**
     * 获取今日最新打卡数据.
     *
     * @return 打卡记录
     * @throws ConnectException ConnectionException
     */
    List<PunchedCard> getLogsToday() throws ConnectException;

    /**
     * 补录指定日期的打卡数据.
     *
     * @param date 日期 (年月日 时分秒)
     * @return 打卡记录
     * @throws ConnectException ConnectException
     */
    boolean refillOneDayLog(Date date) throws ConnectException;

    /**
     * 获取所有用户.
     *
     * @return List
     * @throws ConnectException ConnectionException
     */
    List<ZkUserInfo> getAllUserInfo() throws ConnectException;

    /**
     * 根据userId获取用户信息.
     *
     * @param userId userId
     * @return 用户信息
     * @throws ConnectException ConnectionException
     */
    ZkUserInfo getZkUserInfoByUid(String userId) throws ConnectException;

    /**
     * 将打卡机中的user数据同步到本地数据库.
     *
     * @return 结果
     */
    boolean synUserToLocal();

    /**
     * 获取本地mysql中的用户打卡机数据.
     *
     * @return 用户数据
     */
    List<ZkUserInfo> getAllUserInfoLocal();

    /**
     * 通过姓名获取其考勤id.
     * @param name 姓名
     * @return id
     */
    String getUserIdByName(String name);

    /**
     * 获取本日的0点0分0秒.
     *
     * @return 时间
     */
    Date getTodayZero();

    /**
     * 连接.
     *
     * @return String
     */
    String onConnect();

}
