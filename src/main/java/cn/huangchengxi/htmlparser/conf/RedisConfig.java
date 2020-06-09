package cn.huangchengxi.htmlparser.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Serializable> tmp=new RedisTemplate<>();
        tmp.setKeySerializer(new StringRedisSerializer());
        tmp.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        tmp.setConnectionFactory(redisConnectionFactory);
        return tmp;
    }
}
