package com.example.Test.App.service;

import com.example.Test.App.service.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @author Andrew Yantsen
 */

@Service
public class Producer {
    private static final String TOPIC = "first_topic";
    public static final Logger logger = Logger.getLogger(Producer.class.getName());

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void sendUser(KafkaMessage kafkaMessage) {
        logger.info("Отправлем сообщение в Kafka");
        kafkaTemplate.send(TOPIC, kafkaMessage);
    }

}
