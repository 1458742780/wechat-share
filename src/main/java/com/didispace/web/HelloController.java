package com.didispace.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @version 1.0.0
 *
 */
@Controller
public class HelloController {
    
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @RequestMapping(value = {"/","/activity"})
    public String index(ModelMap map) {
        //map.addAttribute("host", "http://blog.didispace.com");
        return "index";
    }

}