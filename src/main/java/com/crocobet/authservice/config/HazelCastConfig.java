package com.crocobet.authservice.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.hazelcast.config.MapConfig;

@Configuration
public class HazelCastConfig {
    @Bean
    @Primary
    public Config hazelcastConfig() {
        Config config = new Config();

        // Group Configuration
        config.setClusterName("dev");

        // Network Configuration
        config.getNetworkConfig().setPort(5701);

        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(true);
        config.getJetConfig().setEnabled(true);

        MapConfig usersCache = new MapConfig();
        config.getMapConfigs().put("userCache", usersCache);

        return config;
    }
}
