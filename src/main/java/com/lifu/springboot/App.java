package com.lifu.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Hello world!
 *
 */
@Controller
public class App 
{

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "hello,域名采集客户端...";
    }
}
