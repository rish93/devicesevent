package com.health.devicesevent.service;

import com.health.devicesevent.dto.DeviceDto;
import com.health.devicesevent.exception.ResourceNotFoundException;

public interface DeviceService{
    DeviceDto getDeviceDetail( Integer deviceId, String tenantId) throws ResourceNotFoundException;

}
