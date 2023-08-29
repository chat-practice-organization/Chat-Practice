package com.example.messageclassifier.service;

import com.example.messageclassifier.producer.PreprocessedChatMessageProducer;
import domain.ChatMessage;
import domain.ChatRoomMember;
import dto.PreprocessedChatMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import repository.jpa.ChatRoomMemberRepository;
import repository.redis.chat.ChatMessageRepository;
import repository.redis.chat.PartitionWasConnectionRepository;
import repository.redis.chat.RoutingTableRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassifyMessageService {

    @Value("${kafka.topic.chat.receive}")
    private String CHAT_MESSAGES_RECEIVE_TOPIC;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final PreprocessedChatMessageProducer preprocessedChatMessageProducer;
    private final RoutingTableRepository routingTableRepository;
    private final PartitionWasConnectionRepository partitionWasConnectionRepository;


    private final ExecutorService executorService = Executors.newFixedThreadPool(500);


    public void classify(ChatMessage chatMessage) {
        //캐싱해야됨
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findTop500ChatRoomMemberJpaByChatRoomId(chatMessage.getChatRoomId());

        for (ChatRoomMember chatRoomMember : chatRoomMembers) {
            executorService.submit(() -> {
                PreprocessedChatMessage preProcessedChatMessage = PreprocessedChatMessage.from(chatMessage, chatRoomMember.getSessionId());

//                preprocessedChatMessageProducer.producePreprocessedChatMessage(CHAT_MESSAGES_RECEIVE_TOPIC, preProcessedChatMessage, 0);
                routingTableRepository.findById(chatRoomMember.getSessionId())
                        .ifPresent(routingTable -> {
                            Integer targetWasId = routingTable.getWasId();
                            partitionWasConnectionRepository.findById(targetWasId)
                                    .ifPresent(partitionWasConnection -> {
                                        for (Integer partition : partitionWasConnection.getPartitions()) {
                                            preprocessedChatMessageProducer.producePreprocessedChatMessage(CHAT_MESSAGES_RECEIVE_TOPIC, preProcessedChatMessage, partition);
                                            break;
                                        }
                                    });
                        });
            });
        }
    }
}