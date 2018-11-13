package com.zzjz.zkemlink.service;

import com.zzjz.zkemlink.bean.PunchedCard;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @author 房桂堂
 * @description PunchCardService
 * @date 2018/11/6 15:12
 */
@Service
public interface PunchCardService {

    /**
     * 获取指定日期之后的打卡原始信息.
     * @param date 日期
     * @return 结果
     */
    List<PunchedCard> queryAfter(Date date);

    /**
     * 插入一条打卡信息.
     * @param punchedCard 打卡信息
     * @return 结果
     */
    boolean insert(PunchedCard punchedCard);

    /**
     * 批量插入打卡信息
     * @param punchedCardList 打卡信息
     * @return 结果
     */
    boolean batchInsert(List<PunchedCard> punchedCardList);

    /**
     * 删除指定日期之后的所有打卡数据
     * @param date 日期
     * @return 结果
     */
    boolean delAfter(Date date);
}
