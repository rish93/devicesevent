package com.health.devicesevent.configuration;


import com.health.devicesevent.dao.DataSourceConfigRepository;
import com.health.devicesevent.entity.Tenant;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Lazy
public class TenantDataSource implements Serializable {

    private static final long serialVersionUID = -2418234625461365801L;
    private Map<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    private DataSourceConfigRepository configRepo;

    @PostConstruct
    public Map<String, DataSource> getAllTenantDS() {
        List<Tenant> dbList =configRepo.findAll();

        Map<String, DataSource> result = dbList.stream()
                .collect(Collectors.toMap(Tenant::getId, db -> getDataSource(db.getId())));

        return result;
    }

    public DataSource getDataSource(String dbId) {
        if (dataSources.get(dbId) != null) {
            return dataSources.get(dbId);
        }
        DataSource dataSource = createDataSource(dbId);
        if (dataSource != null) {
            dataSources.put(dbId, dataSource);
        }
        return dataSource;
    }

    public DataSource getDataSourceWithBucketName(String bucketName) {
        String s3bucketName= bucketName.replaceAll("[-+.^:,]","");
        for(Map.Entry<String,DataSource> stringDataSourceEntry: dataSources.entrySet()){

            if(stringDataSourceEntry.getKey()
                    .replaceAll("[-+._^:,]","")
                    .equalsIgnoreCase(s3bucketName)){

                return stringDataSourceEntry.getValue();
            }

        }
        DataSource dataSource = createDataSource(bucketName);
        if (dataSource != null) {
            dataSources.put(bucketName, dataSource);
        }
        return dataSource;
    }

    private DataSource createDataSource(String dbId) {
        Optional<Tenant> db = configRepo.findById(dbId);
        if (db != null) {
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName("org.postgresql.Driver")
                    .username(db.get().getUsername()).password(db.get().getPassword())
                    .url( db.get().getUrl());
            DataSource ds = (DataSource) factory.build();
            return ds;
        }
        return null;
    }
}