package config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(basePackages = {"repository.redis.chat"},
        redisTemplateRef = "chatRedisTemplate", keyValueTemplateRef = "chatKeyValueTemplate",
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class ChatRedisConfig {

    @Value("${redis.chat.host}")
    private String CHAT_HOST;

    @Value("${redis.chat.port}")
    private int CHAT_PORT;

    @Bean(name = "chatRedisFactory")
    public RedisConnectionFactory chatRedisFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(CHAT_HOST, CHAT_PORT));
    }

    @Bean(name = "chatKeyValueTemplate")
    public KeyValueTemplate chatKeyValueTemplate() {
        RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(
                new RedisKeyValueAdapter(chatRedisTemplate()),
                new RedisMappingContext()
        );
        return keyValueTemplate;
    }


    @Bean(name = "chatRedisTemplate")
    public RedisTemplate<String, Object> chatRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(chatRedisFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
