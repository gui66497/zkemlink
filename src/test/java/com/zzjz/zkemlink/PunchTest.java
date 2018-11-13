package com.zzjz.zkemlink;

import com.zzjz.zkemlink.bean.PunchedCard;
import com.zzjz.zkemlink.service.PunchCardService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 房桂堂
 * @description PunchTest
 * @date 2018/11/6 15:25
 */
public class PunchTest extends ZkemlinkApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PunchTest.class);

    @Autowired
    PunchCardService punchCardService;

    @Test
    public void testInsert() {
        PunchedCard punchedCard = new PunchedCard();
        punchedCard.setYear("2");
        punchedCard.setMonth("2");
        punchedCard.setDay("2");
        punchedCard.setHour("2");
        punchedCard.setMinute("1");
        punchedCard.setSecond("1");
        punchedCard.setUid("1");
        punchedCard.setMode("1");
        punchedCard.setDate("2018-10-11 00:01:00");
        boolean res = punchCardService.insert(punchedCard);
        LOGGER.info("插入" + (res ? "成功" : "失败"));
    }

    @Test
    public void testBatchInsert() {
        List<PunchedCard> punchedCardList = new ArrayList<>();
        PunchedCard punchedCard = new PunchedCard();
        punchedCard.setYear("12");
        punchedCard.setMonth("12");
        punchedCard.setDay("12");
        punchedCard.setHour("12");
        punchedCard.setMinute("12");
        punchedCard.setSecond("12");
        punchedCard.setUid("12");
        punchedCard.setMode("12");
        punchedCard.setDate("2018-10-11 00:01:00");
        punchedCardList.add(punchedCard);
        boolean res = punchCardService.batchInsert(punchedCardList);
        LOGGER.info("批量插入" + (res ? "成功" : "失败"));
    }

    /**
     * 查询指定日期之后的
     * @throws ParseException ParseException
     */
    @Test
    public void testQueryAfter() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2018-11-03 00:00:00");
        List<PunchedCard> punchedCardList = punchCardService.queryAfter(date);
        System.out.println(punchedCardList);
    }



    /**
     * 删除指定日期之后的
     * @throws ParseException ParseException
     */
    @Test
    public void testDelAfter() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2018-11-01");
        boolean res = punchCardService.delAfter(date);
        System.out.println(res);
    }
}
