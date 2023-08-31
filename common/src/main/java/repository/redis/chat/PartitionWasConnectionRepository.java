package repository.redis.chat;

import java.util.Optional;
import domain.PartitionWasConnection;
import domain.RoutingTable;
import org.springframework.data.repository.CrudRepository;

import org.springframework.cache.annotation.Cacheable;

public interface PartitionWasConnectionRepository extends CrudRepository<PartitionWasConnection, Integer> {
    @Override
    @Cacheable(value = "partitionWasConnection",cacheManager = "cacheManager")
    Optional<PartitionWasConnection> findById(Integer id);
}