package com.health.devicesevent.processor;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.health.devicesevent.configuration.TenantDataSource;
import com.health.devicesevent.dao.DataSourceConfigRepository;
import com.health.devicesevent.entity.Tenant;
import com.health.devicesevent.listener.KafkaConsumer;
import com.health.devicesevent.util.AppUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("s3processor")
public class S3Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private ApplicationContext context;

    public void getObjectBytes(AmazonS3 s3, String bucketName, String keyName, String path) throws SQLException {
        DataSource dataSource = null;
     try {
        S3Object s3Object=    s3.getObject(bucketName,keyName);
        S3ObjectInputStream responseInputStream  =   s3Object.getObjectContent();

             String sqlInsert= tableToSqlQuery( new String(responseInputStream.readAllBytes(), StandardCharsets.UTF_8));
             LOGGER.info("INSERT QUERY : "+sqlInsert);
             TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
             dataSource= tenantDataSource.getDataSource(bucketName);
             dataSource.getConnection().createStatement().execute(sqlInsert);
        } catch (S3Exception | IOException e ) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }


    String tableToSqlQuery(String tableData){
        String sqlInsert= "INSERT INTO devicedetail.device(tenant_id, device_id, model, manufacturer, device_type, approval_date) VALUES";
        StringBuilder s= new StringBuilder();
        String[] sarr=tableData.split("\n");
        for(int i=1;i<sarr.length;i++){
            s.append("(");
           String[] rowData= sarr[i].split("  ");
            for(int j=0;j<rowData.length;j++){
                if(!rowData[j].isEmpty()){
                    if(AppUtils.isNumeric(rowData[j]))
                        s.append(rowData[j]).append(",");
                    else
                        s.append('\'')
                            .append(rowData[j])
                            .append('\'').append(",");

                }
            }
            s = new StringBuilder(AppUtils.removeTrailingComma(s.toString()));
            s.append("),");
        }
        return  sqlInsert+" "+AppUtils.removeTrailingComma(s.toString());
    }
}
