package com.health.devicesevent.processor;

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

    public void getObjectBytes(S3Client s3, String bucketName, String keyName, String path) throws SQLException {
        DataSource dataSource = null;

        try {
//            GetObjectRequest getObjectRequest = GetObjectRequest
//                    .builder()
//                    .key(keyName)
//                    .bucket(bucketName)
//                    .build();
//            ResponseInputStream<GetObjectResponse> responseInputStream = s3.getObject(getObjectRequest);

            InputStream stubInputStream =
                    IOUtils.toInputStream("Tenant_id    Device_id  Model              Manufacturer       Device_Type        Approval_Date\n" +
                            "Manipal_01   201        GE-MRI-1000        General Electric   MRI Scanner        2022-01-20\n" +
                            "Manipal_01   202        GE-Xray-5000       General Electric   X-ray Machine      2022-03-15\n" +
                            "Manipal_01   203        GE-Ultrasound-300  Siemens            Ultrasound System  2022-02-15\n" +
                            "Manipal_01   204        GE-CT-Scanner-700  Phillips           CT Scanner         2022-02-10", "UTF-8");
                String res= new String(IOUtils.toInputStream("Tenant_id    Device_id  Model              Manufacturer       Device_Type        Approval_Date\n" +
                        "Manipal_01   201        GE-MRI-1000        General Electric   MRI Scanner        2022-01-20\n" +
                        "Manipal_01   202        GE-Xray-5000       General Electric   X-ray Machine      2022-03-15\n" +
                        "Manipal_01   203        GE-Ultrasound-300  Siemens            Ultrasound System  2022-02-15\n" +
                        "Manipal_01   204        GE-CT-Scanner-700  Phillips           CT Scanner         2022-02-10", "UTF-8").readAllBytes(),StandardCharsets.UTF_8);



              String sqlInsert= tableToSqlQuery(res);
            LOGGER.info("INSERT QUERY : "+sqlInsert);
             TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
             dataSource= tenantDataSource.getDataSource(bucketName);
//             String s="INSERT INTO devicedetail.device(tenant_id, device_id, model, manufacturer, device_type, approval_date) VALUES ('Manipal_01', 204, 'GE-CT-Scanner-700', 'Phillips', 'CT Scanner',  '2022-02-10')";
             dataSource.getConnection().createStatement().execute(sqlInsert);


//            InputStream stream = new ByteArrayInputStream(responseInputStream.readAllBytes());
//            System.out.println("Content :"+ new String(responseInputStream.readAllBytes(), StandardCharsets.UTF_8));


//            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
//            byte[] data = objectBytes.asByteArray();
//
//            // Write the data to a local file.
//            File myFile = new File(path);
//            OutputStream os = new FileOutputStream(myFile);
//            os.write(data);
//            System.out.println("Successfully obtained bytes from an S3 object");
//            os.close();

        } catch (S3Exception | IOException e ) {//| SQLException e
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
//           Stream.of(rowData).filter(s -> !s.isBlank())
//                   .map(s -> s+",").collect(Collectors.toList());
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
        //('Manipal_01', 204, 'GE-CT-Scanner-700', 'Phillips', 'CT Scanner',  '2022-02-10')
//        s=AppUtils.removeTrailingComma(s.toString());
        return  sqlInsert+" "+AppUtils.removeTrailingComma(s.toString());
    }
}
