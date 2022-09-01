package com.pengzy.comment.result;


import lombok.Getter;

/**
 * 自定义异常处理
 */
@Getter
public class CustomException extends RuntimeException {

    private int code;

    private String message;

    private Object data;


    public CustomException(String message){
        this.code=1000;
        this.message=message;
        this.data=null;
    }

    public CustomException(String message,Object data){
        this.code=1000;
        this.message=message;
        this.data=data;
    }

    public CustomException(String message,int code){
        this.code=code;
        this.message=message;
        this.data=null;
    }
    public CustomException(String message,int code,Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public CustomException(String message,Throwable e){
        super(message,e);
        this.message=message;
        this.data=null;
    }

    public CustomException(){

    }
}
