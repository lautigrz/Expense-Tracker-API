package com.expanse_tracker.service;

import com.expanse_tracker.enums.RateLimitType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<String, Bucket>();

    public Bucket resolveBucket(String key, RateLimitType type){
        return cache.computeIfAbsent(key + ":" + type, k -> newBucket(type));
    }

    private Bucket newBucket(RateLimitType type) {

        return switch (type) {
            case LOGIN -> Bucket.builder()
                    .addLimit(Bandwidth.classic(
                            5,
                            Refill.intervally(5, Duration.ofMinutes(1))
                    ))
                    .build();

            case AUTHENTICATED -> Bucket.builder()
                    .addLimit(Bandwidth.classic(
                            100,
                            Refill.intervally(100, Duration.ofMinutes(1))
                    ))
                    .build();

            case VERIFY -> Bucket.builder()
                    .addLimit(Bandwidth.classic(
                            3,
                            Refill.intervally(3, Duration.ofMinutes(10))
                    ))
                    .build();
        };

    }
}
