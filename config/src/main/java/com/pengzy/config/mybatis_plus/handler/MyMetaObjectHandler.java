package com.pengzy.config.mybatis_plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author pzy
 * @version 1.0
 * 自动填充策略
 */
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增填充策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //设置字段值
        //setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject)
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }

    /**
     * 更新填充策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
