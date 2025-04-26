package com.demo.app.interfaces.managment;

import lombok.RequiredArgsConstructor;
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

    private final List<CacheManager> cacheManagers;

    /**
     * Cache object.
     *
     * @return the object
     */
    @GetMapping(value = "/cache")
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

    @GetMapping(value = "/info")
    public String info() {
        return "Hello World!";
    }

}
