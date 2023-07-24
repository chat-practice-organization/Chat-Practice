package com.practice.chat.listener;

import com.practice.chat.service.UpdatePartitionWasConnectionService;
import domain.PartitionWasConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Component;
import repository.redis.chat.PartitionWasConnectionRepository;

import java.util.Collection;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomConsumerRebalanceListener implements ConsumerRebalanceListener {

    private final UpdatePartitionWasConnectionService updatePartitionWasConnectionService;

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        updatePartitionWasConnectionService.deleteIfExist();
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        log.info("Assigned partitions: "+partitions.stream().map(TopicPartition::partition).toList().toString());
        updatePartitionWasConnectionService.insert(partitions);
    }
}
