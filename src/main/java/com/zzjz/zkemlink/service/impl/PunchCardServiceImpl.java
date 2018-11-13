package com.zzjz.zkemlink.service.impl;

import com.zzjz.zkemlink.bean.PunchedCard;
import com.zzjz.zkemlink.mapper.PunchCardMapper;
import com.zzjz.zkemlink.service.PunchCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @author 房桂堂
 * @description PunchCardServiceImpl
 * @date 2018/11/6 15:13
 */
@Service
public class PunchCardServiceImpl implements PunchCardService {

    @Autowired
    PunchCardMapper punchCardMapper;

    @Override
    public List<PunchedCard> queryAfter(Date date) {
        return punchCardMapper.queryAfter(date);
    }

    @Override
    public boolean insert(PunchedCard punchedCard) {
        return punchCardMapper.insert(punchedCard) > 0;
    }

    @Override
    public boolean batchInsert(List<PunchedCard> punchedCardList) {
        return punchCardMapper.addPunchCardList(punchedCardList) > 0;
    }

    @Override
    public boolean delAfter(Date date) {
        return punchCardMapper.delAfter(date) > 0;
    }
}
