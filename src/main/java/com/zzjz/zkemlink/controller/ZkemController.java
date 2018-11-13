package com.zzjz.zkemlink.controller;

import com.zzjz.zkemlink.bean.BaseResponse;
import com.zzjz.zkemlink.bean.PunchedCard;
import com.zzjz.zkemlink.bean.ResultCode;
import com.zzjz.zkemlink.service.PunchCardService;
import com.zzjz.zkemlink.service.ZkemService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.net.ConnectException;
import java.util.Date;
import java.util.List;

/**
 * @author 房桂堂
 * @description ZkemController
 * @date 2018/11/6 10:33
 */
@RestController
@RequestMapping("/zkem")
public class ZkemController {

    @Autowired
    ZkemService zkemService;

    @Autowired
    PunchCardService punchCardService;

    /**
     * 补录某天之后的所有打卡原始信息
     * @param dateStr 日期字符串(yyyyMMdd-HHmmss)20181104-000000
     * @return 结果
     */
    @RequestMapping(value = "/refillPunchCardAfter/{dateStr}", method = RequestMethod.POST)
    public BaseResponse<String> refillPunchCardAfter(@PathVariable("dateStr") String dateStr) {
        BaseResponse<String> response = new BaseResponse<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd-HHmmss");
        try {
            boolean res = zkemService.refillPunchCardAfter(DateTime.parse(dateStr, formatter).toDate());
        } catch (ConnectException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("补录失败");
            return response;
        }
        return response;
    }

    /**
     * 删除某天之后的所有打卡原始信息.
     * @param dateStr 日期字符串(yyyyMMdd)20181104
     * @return 结果
     */
    @RequestMapping(value = "/punchCardAfter/{dateStr}", method = RequestMethod.DELETE)
    public BaseResponse<String> recordPunchCardAfter(@PathVariable("dateStr") String dateStr) {
        BaseResponse<String> response = new BaseResponse<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        Date date;
        try {
            date = DateTime.parse(dateStr, formatter).toDate();
        } catch (Exception e) {
            //日期参数不合法
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("日期参数不合法");
            return response;
        }
        boolean res = punchCardService.delAfter(date);
        response.setResultCode(res ? ResultCode.RESULT_SUCCESS : ResultCode.RESULT_ERROR);
        response.setMessage(res ? "删除成功" : "删除失败");
        return response;
    }

    /**
     * 查询指定日期之后的打卡原始信息
     * @param dateStr 日期
     * @return 结果
     */
    @RequestMapping(value = "/queryAfter/{dateStr}", method = RequestMethod.GET)
    public List<PunchedCard> queryAfter(@PathVariable("dateStr") String dateStr) {
        BaseResponse<PunchedCard> response = new BaseResponse<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        Date date;
        try {
            date = DateTime.parse(dateStr, formatter).toDate();
        } catch (Exception e) {
            //日期参数不合法
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("日期参数不合法");
            return null;
        }
        return punchCardService.queryAfter(date);
    }
}
