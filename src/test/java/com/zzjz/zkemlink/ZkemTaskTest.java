package com.zzjz.zkemlink;

import com.zzjz.zkemlink.task.ZkemTask;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 房桂堂
 * @description ZkemTaskTest
 * @date 2018/11/6 15:48
 */
public class ZkemTaskTest extends ZkemlinkApplicationTests {

    @Autowired
    ZkemTask zkemTask;

    /**
     * 测试记录当日考勤原始数据到数据库.
     */
    @Test
    public void testAddAttendance() {
        zkemTask.addTodayAttendance();
    }

    @Test
    public void testSynZkemUserToLocal() {
        zkemTask.synZkemUserToLocal();
    }
}
