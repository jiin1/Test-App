package com.example.Test.App.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Andrew Yantsen
 */

@Service
public class Producer {
 private static final String TOPIC = "first_topic";

 @Autowired
    private KafkaTemplate<String,KafkaMessage> kafkaTemplate;

 public void sendUser (KafkaMessage kafkaMessage){

     kafkaTemplate.send(TOPIC, kafkaMessage);
 }


    //      .\bin\windows\kafka-server-start.bat .\config\server.properties
    //   .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

}
