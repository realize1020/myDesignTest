package com.example.design.multiThread.线程安全的自增取模计数器实现轮询;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 来自：Ribbon负载均衡中的轮询算法https://github.com/Netflix/archaius/blob/master/archaius-core/src/main/java/com/netflix/archaius/api/utils/RoundRobin.java
 */
public class Solution {

    private final AtomicInteger nextIndex = new AtomicInteger();

    /**
     * Referenced from RoundRobinRule
     * Inspired by the implementation of {@link AtomicInteger#incrementAndGet()}.
     *
     * @param modulo The modulo to bound the value of the counter.
     * @return The next value.
     */
    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = nextIndex.get();
            int next = (current + 1) % modulo;
            if (nextIndex.compareAndSet(current, next) && current < modulo)
                return current;
        }
    }
}
