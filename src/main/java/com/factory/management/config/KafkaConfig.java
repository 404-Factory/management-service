package com.factory.management.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

/**
 * Flink 전용 Kafka Listener 설정.
 *
 * <p>기본 {@code kafkaListenerContainerFactory}(도메인 이벤트용)와 {@code EventDispatcher}/
 * {@code CommonKafkaConsumer}는 각각 Spring Boot {@code KafkaAutoConfiguration}과
 * common {@code SigmaKafkaAutoConfiguration}이 제공하므로 여기서 다시 만들지 않는다.
 *
 * <p>Flink는 exactly-once(트랜잭셔널)로 produce하므로 violation 컨슈머는 {@code read_committed}
 * 격리수준이 필요한데, 이 팩토리는 어느 자동설정도 제공하지 않아 여기서만 정의한다.
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean("flinkKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> flinkKafkaListenerContainerFactory(
            @Value("${app.kafka.consumer.violation-group-id:management-violation-group}") String groupId
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(flinkConsumerFactory(groupId));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }

    private ConsumerFactory<String, String> flinkConsumerFactory(String groupId) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        // Flink exactly-once 대응: 커밋된 메시지만 읽고, 그룹 ID 강제 오버라이드
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
