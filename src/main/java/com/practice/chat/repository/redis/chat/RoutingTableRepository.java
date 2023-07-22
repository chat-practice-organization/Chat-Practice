package com.practice.chat.repository.redis.chat;


import com.practice.chat.domain.RoutingTable;
import org.springframework.data.repository.CrudRepository;

public interface RoutingTableRepository extends CrudRepository<RoutingTable,String> {
}
