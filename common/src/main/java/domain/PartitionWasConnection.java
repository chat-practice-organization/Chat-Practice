package domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@RedisHash(value = "partitionWasConnection")
public class PartitionWasConnection {

    @Id
    private Integer wasId;
    private List<Integer> partitions;

}
