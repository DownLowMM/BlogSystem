package com.duan.blogos.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created on 2018/9/14.
 *
 * @author DuanJiaNing
 */
@Configuration
public class AppConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${spring.redis.pool.maxTotal}")
    private int maxTotal;

    @Value("${spring.redis.pool.maxWait}")
    private int maxWait;

    @Value("${spring.redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.redis.pool.testOnReturn}")
    private boolean testOnReturn;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);

        return jedisPoolConfig;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig());

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate() {

        final RedisTemplate<String, Long> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory());

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        return template;
    }

}
