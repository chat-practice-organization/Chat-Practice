package com.practice.chat.service;

import domain.PartitionWasConnection;
import domain.RoutingTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repository.redis.chat.PartitionWasConnectionRepository;
import repository.redis.chat.RoutingTableRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePartitionWasConnectionService {


    @Value("${was.id}")
    private String WAS_ID;
    private final PartitionWasConnectionRepository partitionWasConnectionRepository;



    public void insert(Collection<TopicPartition> partitions) {
        PartitionWasConnection partitionWasConnection = PartitionWasConnection.builder()
                .wasId(Integer.valueOf(WAS_ID))
                .partitions(partitions.stream().map(TopicPartition::partition).toList())
                .build();
        partitionWasConnectionRepository.save(partitionWasConnection);
    }

    public void deleteIfExist() {
        partitionWasConnectionRepository.findById(Integer.valueOf(WAS_ID))
                .ifPresent(partitionWasConnectionRepository::delete);
    }


}
