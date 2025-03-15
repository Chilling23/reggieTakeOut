package com.hwq.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Custom Metadata Object Handler
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    /**
     * Auto-fill fields during insert operations
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Auto-filling common fields [insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getId());
        metaObject.setValue("updateUser",BaseContext.getId());
    }

    /**
     * Auto-fill fields during update operations
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Auto-filling common fields [update]...");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("Thread ID: {}", id);

        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getId());
    }
}
