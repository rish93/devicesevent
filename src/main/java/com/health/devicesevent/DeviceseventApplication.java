package com.health.devicesevent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EntityScan(basePackages = {"com.health.devicesevent.entity"}) // add this so the spring boot context knows where to look after entities


public class DeviceseventApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceseventApplication.class, args);
	}

}
