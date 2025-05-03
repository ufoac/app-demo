package com.demo.app.interfaces.managment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * The type App controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dev")
public class ManagementController {
    @Value("${APP_VERSION:1.0}")
    private String version;
    private final List<CacheManager> cacheManagers;

    /**
     * Info string.
     *
     * @return the string
     */
    @GetMapping(value = "/info")
    public String info() {
        return "Hello World! " + version;
    }

    /**
     * Cache object.
     *
     * @return the object
     */
    @GetMapping(value = "/stats")
    public Object cache() {
        var cacheManager = cacheManagers.getFirst();
        return cacheManager.getCacheNames().stream().map(e -> {
            var c = (CaffeineCache) cacheManager.getCache(e);
            if (c == null) {
                return "";
            }
            return Map.of("status", String.format("缓存[%s] size = %d, status = %s", e,
                    c.getNativeCache().estimatedSize(),
                    c.getNativeCache().stats()), "data", c.getNativeCache().asMap());
        }).toList();
    }
}
