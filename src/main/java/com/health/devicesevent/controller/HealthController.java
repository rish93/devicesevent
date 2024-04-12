package com.health.devicesevent.controller;

import com.health.devicesevent.dto.DeviceDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/devices")
public class HealthController {

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String getDeviceHealthDetail() {
        return "Device is up and runnning";
    }

}

