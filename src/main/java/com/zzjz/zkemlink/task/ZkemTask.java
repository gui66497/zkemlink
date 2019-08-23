package com.zzjz.zkemlink.task;

import com.zzjz.zkemlink.service.PunchCardService;
import com.zzjz.zkemlink.service.ZkemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 房桂堂
 * @description ZkemTask
 * @date 2018/11/5 17:10
 */
@Component
public class ZkemTask {

    final static Logger LOGGER = LoggerFactory.getLogger(ZkemTask.class);

    @Autowired
    ZkemService zkemService;

    @Autowired
    PunchCardService punchCardService;

    /**
     * 每天11点同步打卡机用户信息到tb_zkem_user
     */
    @Scheduled(cron = "0 0 23 * * ? ")//每日23:00触发
    public void synZkemUserToLocal() {
        zkemService.synUserToLocal();
    }

    /**
     * 每日23:50触发 记录当日考勤原始数据到数据库.
     */
    @Scheduled(cron = "0 50 23 * * ? ")//每日23:50触发
    public void addTodayAttendance() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Date date = new Date();
        LOGGER.info("###本定时任务为添加考勤定时任务，当前时间为：" + sdf.format(date));
        try {
            zkemService.refillOneDayLog(zkemService.getTodayZero());
        } catch (ConnectException e) {
            e.printStackTrace();
        }

    }
}
