package com.example.messageclassifier.service;

import com.example.messageclassifier.producer.PreprocessedChatMessageProducer;
import domain.ChatMessage;
import domain.ChatRoomMember;
import dto.PreprocessedChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repository.jpa.ChatRoomMemberRepository;
import repository.redis.chat.PartitionWasConnectionRepository;
import repository.redis.chat.RoutingTableRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassifyMessageService {

    @Value("${kafka.topic.chat.receive}")
    private String CHAT_MESSAGES_RECEIVE_TOPIC;

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final PreprocessedChatMessageProducer preprocessedChatMessageProducer;
    private final RoutingTableRepository routingTableRepository;
    private final PartitionWasConnectionRepository partitionWasConnectionRepository;

    public void classify(ChatMessage chatMessage) {
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findTop500ChatRoomMemberJpaByChatRoomId(chatMessage.getChatRoomId());
        for (ChatRoomMember chatRoomMember : chatRoomMembers) {
            Runnable runnable = () -> {
                PreprocessedChatMessage preProcessedChatMessage = PreprocessedChatMessage.from(chatMessage, chatRoomMember.getSessionId());
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
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

}
