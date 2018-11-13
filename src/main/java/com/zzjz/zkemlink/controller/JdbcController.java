package com.zzjz.zkemlink.controller;

import com.zzjz.zkemlink.bean.ZkUserInfo;
import com.zzjz.zkemlink.mapper.ZkemUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * @author 房桂堂
 * @description JdbcController
 * @date 2018/11/3 11:59
 */
@RestController
@RequestMapping("/jdbc")
public class JdbcController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    ZkemUserMapper zkemUserMapper;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public List<Map<String, Object>> test() {
        String sql = "select * from tb_staff";
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
        return res;
    }

    @RequestMapping(value = "/getAllUser",method = RequestMethod.GET)
    public List<ZkUserInfo> getAllUser() {
        List<ZkUserInfo> userInfos = zkemUserMapper.getAll();
        return userInfos;
    }

}
