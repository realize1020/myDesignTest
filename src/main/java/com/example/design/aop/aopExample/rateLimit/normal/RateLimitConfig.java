package com.example.design.aop.aopExample.rateLimit.normal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 限流配置类
 */
@Component
@ConfigurationProperties(prefix = "rate.limit")
public class RateLimitConfig {
    
    /**
     * 是否启用全局限流
     */
    private boolean enabled = true;
    
    /**
     * 默认限流策略
     */
    private String defaultAlgorithm = "token_bucket";
    
    /**
     * 监控统计间隔（秒）
     */
    private int monitorInterval = 60;
    
    /**
     * 是否启用限流统计
     */
    private boolean enableStatistics = true;
    
    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getDefaultAlgorithm() {
        return defaultAlgorithm;
    }
    
    public void setDefaultAlgorithm(String defaultAlgorithm) {
        this.defaultAlgorithm = defaultAlgorithm;
    }
    
    public int getMonitorInterval() {
        return monitorInterval;
    }
    
    public void setMonitorInterval(int monitorInterval) {
        this.monitorInterval = monitorInterval;
    }
    
    public boolean isEnableStatistics() {
        return enableStatistics;
    }
    
    public void setEnableStatistics(boolean enableStatistics) {
        this.enableStatistics = enableStatistics;
    }
}