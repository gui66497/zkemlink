package com.zzjz.zkemlink.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 房桂堂
 * @description HelloController
 * @date 2018/11/3 11:32
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    final static Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "/sayHello",method = RequestMethod.GET)
    public String sayHello() {
        LOGGER.info("测试日志INFO");
        LOGGER.warn("测试日志WARN");
        return "Hello Spring Boot";
    }
}
