package com.demo.app.infrastructure.repository.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * The type Auto field fill.
 */
@Component
public class AutoFieldFill implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,
                "createTime",
                LocalDateTime.class,
                LocalDateTime.now());
        this.strictInsertFill(metaObject,
                "updateTime",
                LocalDateTime.class,
                LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,
                "updateTime",
                LocalDateTime.class,
                LocalDateTime.now());
    }
}
