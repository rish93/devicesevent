package com.health.devicesevent.entity;




import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "device",  schema = "devicedetail")
@Data
public class Device {

    @Column(name = "tenant_id")
    private String tenantId;
    @Id
    @Column(name = "device_id")
    private Integer deviceId;

    @Column
    private String model;

    @Column
    private String manufacturer;

    @Column(name = "device_type")
    private String deviceType;

    @Temporal(TemporalType.DATE)
    @Column(name = "approval_date")
    private Date approvalDate;

}