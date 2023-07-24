package repository.redis.chat;


import domain.PartitionWasConnection;
import domain.RoutingTable;
import org.springframework.data.repository.CrudRepository;

public interface PartitionWasConnectionRepository extends CrudRepository<PartitionWasConnection,Integer> {
}
