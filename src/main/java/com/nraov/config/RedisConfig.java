package com.nraov.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.Serializable;

//@Profile("!dev")
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
@Slf4j
public class RedisConfig {

	/*@Autowired
	private CacheManager cacheManager;*/

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Value("${spring.redis.useSsl}")
	private boolean useSsl;

	@Value("${spring.redis.password}")
	private String password;

	@Bean
	public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Serializable> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	LettuceConnectionFactory lettuceConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisHost);
		redisStandaloneConfiguration.setPort(redisPort);
		redisStandaloneConfiguration.setPassword(password);

		LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder =
				LettuceClientConfiguration.builder();

		if (useSsl){
			/*SslOptions sslOptions = SslOptions.builder()
					.trustManager(resourceLoader.getResource("classpath:redis.pem").getFile())
					.build();

			ClientOptions clientOptions = ClientOptions
					.builder()
					.sslOptions(sslOptions)
					.protocolVersion(ProtocolVersion.RESP3)
					.build();*/

			lettuceClientConfigurationBuilder
					//.clientOptions(clientOptions)
					.useSsl();
		}
		LettuceClientConfiguration lettuceClientConfiguration = lettuceClientConfigurationBuilder.build();
		return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
	}

	@Bean
	public RedisCacheWriter redisCacheWriter() {
		return RedisCacheWriter.lockingRedisCacheWriter(lettuceConnectionFactory());
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		RedisCacheConfiguration redisCacheConfiguration = config
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		/*RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration)
				.build();*/
		RedisCacheManager redisCacheManager = new CustomCacheManager(redisCacheWriter(), redisCacheConfiguration);
		return redisCacheManager;
	}

	@PostConstruct
	public void clearCache() {
		System.out.println("In Clear Cache");
		HostAndPort hp = new HostAndPort(redisHost, redisPort);
		Jedis jedis = new Jedis(hp, DefaultJedisClientConfig.builder()
				.password(password)
				.ssl(useSsl)
				.build());

		//Jedis jedis = new Jedis(redisHost, redisPort, 1000);
		jedis.flushAll();
		jedis.close();
	}
}
