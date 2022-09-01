package com.pengzy.comment.enums;

import lombok.Getter;

public enum OperateEnum {
    /**
     * 操作类别
     */
   ADD(2,"新增"),
   QUEY(1,"查询"),
   UPDATE(3,"修改"),
   DELETE(4,"删除"),
    ;
   @Getter
   private  int code;
   @Getter
   private String info;

   OperateEnum(int code, String info){
       this.code=code;
       this.info=info;
   }

   public static OperateEnum getObj(int code){
       for (OperateEnum value : values()) {
             if(value.getCode()==code){
                 return value;
             }
       }
       return null;
   }

}
