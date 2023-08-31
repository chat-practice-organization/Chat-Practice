package repository.redis.chat;


import domain.RoutingTable;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;

public interface RoutingTableRepository extends CrudRepository<RoutingTable, String> {
    @Override
    @Cacheable(value = "routingTable",cacheManager = "cacheManager")
    Optional<RoutingTable> findById(String id);
}