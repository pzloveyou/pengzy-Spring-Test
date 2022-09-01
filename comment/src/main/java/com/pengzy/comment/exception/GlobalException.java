package com.pengzy.comment.exception;


import cn.hutool.http.HttpStatus;
import com.pengzy.comment.result.CustomException;
import com.pengzy.comment.result.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.io.IOException;
import java.util.List;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalException {

    private static final Logger log= LoggerFactory.getLogger(GlobalException.class);


    /**
     * 基础异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public JsonResult baseException(CustomException e){
        return JsonResult.error(e.getMessage());
    }

    /**
     * io异常
     * @param e
     * @return
     */
    @ExceptionHandler(IOException.class)
    public JsonResult IOException(IOException e){
        log.error(e.getMessage(),e);
        return JsonResult.error("请求异常，请稍后再试!");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonResult HTTPException(HttpMessageNotReadableException e){
        if(!e.getMessage().contains("Required request body is missing")){
            log.error(e.getMessage(), e);
        }
        return JsonResult.error("请求参数缺失，格式按照application/json");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResult MissingServletRequestParameterException(MissingServletRequestParameterException e){
        if(!e.getMessage().contains("Required request parameter")){
            log.error(e.getMessage(), e);
        }
        return JsonResult.error("请求参数缺失，格式按照application/x-www-form-urlencoded");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public JsonResult handlerNoFoundException(Exception e)
    {
        log.error(e.getMessage(), e);
        return JsonResult.error(HttpStatus.HTTP_NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    /**
     * sql验证异常 唯一键冲突
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public JsonResult validatedSqlKeyException(DuplicateKeyException e)
    {
        log.error(e.getMessage(), e);
        return JsonResult.error(e.getCause().getMessage());
    }


    @ExceptionHandler(Exception.class)
    public JsonResult handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return JsonResult.error("服务器跑路了，请稍后再试！");
    }

    @ExceptionHandler(BindException.class)
    public JsonResult validatedBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        // 参数类型不匹配检验
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = e.getFieldErrors();
        fieldErrors.forEach((oe) ->
                msg.append("参数:[").append(oe.getObjectName())
                        .append(".").append(oe.getField())
                        .append("]的传入值:[").append(oe.getRejectedValue()).append("]与预期的字段类型不匹配.")
        );
        log.error(String.valueOf(msg));
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return JsonResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage());
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return JsonResult.error(1000,message);
    }
}
