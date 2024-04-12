package com.health.devicesevent.dao;

import com.health.devicesevent.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDao extends JpaRepository<Device, Integer> {

//    @Query(value = "SELECT tenant_id, device_id, model, manufacturer, device_type, approval_date FROM devicedetail.device WHERE device_id = ?1 and tenant_id = ?2", nativeQuery = true)
//    Device findByDeviceIdAndTenantId(Integer deviceId, String tenantId);



}
