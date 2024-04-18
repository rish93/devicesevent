package com.health.devicesevent.listener;

import com.health.devicesevent.util.AppConstants;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
//import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SqsConsumer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @SqsListener("device-aws-queue")
    public void processMessage(String message) {
        try {
            LOGGER.info("Received new SQS message: {}", message );
//            OrderDto orderDto = OBJECT_MAPPER.readValue(message, OrderDto.class);
//
//            this.orderService.processOrder(orderDto);
            kafkaTemplate.send(AppConstants.TOPIC_NAME, message);



        } catch (Exception e) {
            throw new RuntimeException("Cannot process message from SQS", e);
        }
    }

}
