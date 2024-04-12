package com.health.devicesevent.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DeviceDto {
//    private String tenant_id;
    private Integer deviceId;
    private String model;
    private String manufacturer;
    private String deviceType;
    private Date approvalDate;
}
