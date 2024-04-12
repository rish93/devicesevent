package com.health.devicesevent.controller;

import com.health.devicesevent.dto.DeviceDto;
import com.health.devicesevent.entity.Device;
import com.health.devicesevent.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/devices")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public DeviceDto getDeviceDetail(@RequestParam(name = "deviceId") Integer deviceId,
                                     @RequestParam(name = "tenantId") String tenantId ) {
        return deviceService.getDeviceDetail(deviceId, tenantId);
    }
}
