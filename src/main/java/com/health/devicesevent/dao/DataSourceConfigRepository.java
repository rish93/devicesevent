package com.health.devicesevent.dao;




import com.health.devicesevent.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSourceConfigRepository extends JpaRepository<Tenant, String> {
}