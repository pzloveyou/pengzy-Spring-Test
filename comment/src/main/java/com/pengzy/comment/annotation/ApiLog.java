package com.pengzy.comment.annotation;

import com.pengzy.comment.enums.OperateEnum;

import java.lang.annotation.*;

/**
 * 自定义客户端接口日志注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {


    /**
     * 操作方法
     * @return
     */
    public String method() default "";

    /**
     * 操作类型
     * @return
     */
    public OperateEnum type();


}
