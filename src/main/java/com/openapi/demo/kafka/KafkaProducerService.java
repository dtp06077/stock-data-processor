package com.openapi.demo.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class KafkaProducerService {

    private final KafkaProducer<String, String> producer;

    public KafkaProducerService(@Value("${kafka.bootstrap-servers}") String bootstrapServers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    /**
     * 프로듀서로 메시지 전송
     */
    public void sendMessage(String topic, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        producer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                System.err.println("Error while producing message: " + exception.getMessage());
            } else {
                System.out.printf("Message sent to topic %s with offset %d%n", metadata.topic(), metadata.offset());
            }
        });
    }
}
