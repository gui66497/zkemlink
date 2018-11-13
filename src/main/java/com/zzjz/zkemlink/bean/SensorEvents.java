package com.zzjz.zkemlink.bean;

import com.jacob.com.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.EventObject;

/**
 * SensorEvents.
 *
 * @author Administrator
 * @version 2017/7/11 14:30
 */
public class SensorEvents extends EventObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorEvents.class);

    private static final long serialVersionUID = 7585990581705430682L;

    private static final Integer NUM4 = 4;

    private static final Integer NUM5 = 5;

    private static final Integer NUM53 = 53;

    private String ip;

    /**
     * getIp.
     *
     * @return IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * setIp.
     *
     * @param ip IP
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    public SensorEvents(Object source, String ip1) {
        super(source);
        this.ip = ip1;
    }

    /**
     * 连接.
     * @param arge arge
     */
    public void onConnected(Variant[] arge) {
        LOGGER.info("当成功连接机器时触发该事件，无返回值====" + this.ip);
    }

    /**
     * 断开连接.
     * @param arge arge
     */
    public void onDisConnected(Variant[] arge) {
        LOGGER.info("当断开机器时触发该事件，无返回值" + this.ip);
    }

    /**
     * 报警.
     * @param arge arge
     */
    public void onAlarm(Variant[] arge) {
        LOGGER.info("当机器报警时触发该事件====" + Arrays.asList(arge).toString());
    }

    /**
     * 门.
     * @param arge arge
     * @return long
     */
    public long onDoor(Variant[] arge) {
        LOGGER.info("OnDoorEvent====" + Arrays.asList(arge).toString());
        if (arge[0].getInt() == NUM4) {
            LOGGER.info("开门====" + Arrays.asList(arge).toString());
        } else if (arge[0].getInt() == NUM5) {
            LOGGER.info("关门====" + Arrays.asList(arge).toString());
        } else if (arge[0].getInt() == NUM53) {
            LOGGER.info("出门了====" + Arrays.asList(arge).toString());
        } else if (arge[0].getInt() == 1) {
            LOGGER.info("门被意外打开====" + Arrays.asList(arge).toString());
        }
        return 1;
    }

    /**
     * 验证通过.
     * @param arge arge
     */
    public void onAttTransactionEx(Variant[] arge) {
        LOGGER.info("当验证通过时触发该事件====" + Arrays.asList(arge).toString());
    }

    /**
     * 登记指纹.
     * @param arge arge
     */
    public void onEnrollFingerEx(Variant[] arge) {
        LOGGER.info("登记指纹时触发该事件====" + Arrays.asList(arge).toString());
    }

    /**
     * 检测到有指纹.
     * @param arge arge
     */
    public void onFinger(Variant[] arge) {
        LOGGER.info("当机器上指纹头上检测到有指纹时触发该消息，无返回值");
    }

    /**
     * 指纹按下.
     * @param arge arge
     */
    public void onFingerFeature(Variant[] arge) {
        LOGGER.info("登记用户指纹时，当有指纹按下时触发该消息====" + Arrays.asList(arge).toString());
    }

    /**
     * 刷卡.
     * @param arge arge
     */
    public void onHIDNum(Variant[] arge) {
        LOGGER.info("当刷卡时触发该消息====" + Arrays.asList(arge).toString());
    }

    /**
     * 成功登记新用户.
     * @param arge arge
     */
    public void onNewUser(Variant[] arge) {
        LOGGER.info("当成功登记新用户时触发该消息====" + Arrays.asList(arge).toString());
    }

    /**
     * 用户验证.
     * @param arge arge
     */
    public void onVerify(Variant[] arge) {
        LOGGER.info("当用户验证时触发该消息====" + Arrays.asList(arge).toString());
    }

    /**
     * 机器进行写卡操作.
     * @param arge arge
     */
    public void onWriteCard(Variant[] arge) {
        LOGGER.info("当机器进行写卡操作时触发该事件====" + Arrays.asList(arge).toString());
    }

    /**
     * 清空 MIFARE 卡操作.
     * @param arge arge
     */
    public void onEmptyCard(Variant[] arge) {
        LOGGER.info("当清空 MIFARE 卡操作时触发该事件====" + Arrays.asList(arge).toString());
    }

    /**
     * 向 SDK 发送未知事件.
     * @param arge arge
     */
    public void onEMData(Variant[] arge) {
        LOGGER.info("当机器向 SDK 发送未知事件时，触发该事件====" + Arrays.asList(arge).toString());
    }
}
