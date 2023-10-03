package com.helo.ming.yu.controller;

import com.helo.ming.yu.model.HaloBlog;
import com.helo.ming.yu.service.HeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private HeloService heloService;

    // http://localhost:8080/yu/demo/get
    @RequestMapping(path = "/get", method = RequestMethod.GET)
    public HaloBlog get() {
        HaloBlog halo = heloService.findByCheckSum("abc");
        return halo;
    }
}
