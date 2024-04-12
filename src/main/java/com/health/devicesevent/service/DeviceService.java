package com.health.devicesevent.service;

import com.health.devicesevent.dto.DeviceDto;

public interface DeviceService{
    DeviceDto getDeviceDetail( Integer deviceId, String tenantId);

}
