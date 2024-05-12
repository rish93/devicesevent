package com.health.devicesevent.listener;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.health.devicesevent.configuration.TenantDataSource;
import com.health.devicesevent.processor.S3Processor;
import com.health.devicesevent.util.AppConstants;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Timestamp;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;

@Component("kafkaconsumer")
public class KafkaConsumer {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Autowired
    S3Processor s3Processor;

    @Autowired
    private ApplicationContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 3000))
    // ,exclude = {NullPointerException.class}
    @KafkaListener(topics = AppConstants.TOPIC_NAME,
            groupId = AppConstants.GROUP_ID)
    public void consume(String message) throws SQLException {
        LOGGER.info(String.format("Message received -> %s", message));

         JSONObject jsonObject = new JSONObject(message);
         JSONObject s3ObjDetail= jsonObject.getJSONArray("Records").getJSONObject(0);
         LOGGER.info(String.format("Object created-> %s", s3ObjDetail));
         JSONObject s3Obj= s3ObjDetail.getJSONObject("s3");
         System.out.println(s3Obj);
         String bucketName= s3Obj.getJSONObject("bucket").getString("name");
         String keyName =  s3Obj.getJSONObject("object").getString("key");
         String region =  s3ObjDetail.getString("awsRegion");
         String path = "";

        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(String.valueOf(Region.of(region)))
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();
            s3Processor.getObjectBytes(s3Client, bucketName, keyName, path);
    }

    @DltHandler
    public void dltHandlerConsumer(Message message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        try {
            LOGGER.info(String.format("Dead letter created for -> %s", message));
            File f = new File("/Users/rishabh/Documents/GitHub/devicesevent/failedmessage.txt");
            f.createNewFile();

            FileUtils.writeStringToFile(
                    f,
                    "\n"+ Instant.now().toString() +"\t"+message.getPayload().toString() +"\t"+topic+"\t"+new String(message.getHeaders().get("kafka_exception-message",byte[].class),StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8,
                    true // == append to end of file
            );
        }catch (IOException e){
            e.printStackTrace();
        }
//        TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
//        DataSource dataSource= tenantDataSource.getDataSource(bucketName);
    }
}