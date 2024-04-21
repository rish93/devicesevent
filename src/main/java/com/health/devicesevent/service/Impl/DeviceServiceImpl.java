package com.health.devicesevent.service.Impl;

import com.health.devicesevent.dao.DeviceDao;
import com.health.devicesevent.dto.DeviceDto;
import com.health.devicesevent.entity.Device;
import com.health.devicesevent.exception.ResourceNotFoundException;
import com.health.devicesevent.service.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceDao deviceDao;

    @Override
    public DeviceDto getDeviceDetail(Integer deviceId, String tenantId) throws ResourceNotFoundException {
        Device deviceDetail= deviceDao.getReferenceById(deviceId);//deviceDao.findByDeviceIdAndTenantId(deviceId,tenantId);
       try {

           return DeviceDto.builder().deviceId(deviceDetail.getDeviceId())
                   .deviceType(deviceDetail.getDeviceType())
                   .approvalDate(deviceDetail.getApprovalDate())
                   .manufacturer(deviceDetail.getManufacturer())
                   .model(deviceDetail.getModel())
                   .build();
       }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("device with id: "+deviceId+ " Not found");
       }
    }
}
