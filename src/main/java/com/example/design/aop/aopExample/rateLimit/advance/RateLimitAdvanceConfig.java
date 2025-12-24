package com.example.design.aop.aopExample.rateLimit.advance;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 限流配置类
 */
@Component
//@ConfigurationProperties(prefix = "rate.limit")
public class RateLimitAdvanceConfig {
    
    /**
     * 是否启用限流
     */
    private boolean enabled = true;
    
    /**
     * 默认限流算法
     */
    private String defaultAlgorithm = "token_bucket";
    
    /**
     * 统计信息保留时间（分钟）
     */
    private int statisticsRetentionMinutes = 60;
    
    /**
     * 是否启用统计信息
     */
    private boolean statisticsEnabled = true;
    
    /**
     * 限流器清理间隔（分钟）
     */
    private int cleanerIntervalMinutes = 30;
    
    // getter和setter方法
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
    
    public int getStatisticsRetentionMinutes() {
        return statisticsRetentionMinutes;
    }
    
    public void setStatisticsRetentionMinutes(int statisticsRetentionMinutes) {
        this.statisticsRetentionMinutes = statisticsRetentionMinutes;
    }
    
    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }
    
    public void setStatisticsEnabled(boolean statisticsEnabled) {
        this.statisticsEnabled = statisticsEnabled;
    }
    
    public int getCleanerIntervalMinutes() {
        return cleanerIntervalMinutes;
    }
    
    public void setCleanerIntervalMinutes(int cleanerIntervalMinutes) {
        this.cleanerIntervalMinutes = cleanerIntervalMinutes;
    }
}