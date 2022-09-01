package com.pengzy.comment.result;


import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JsonResult<T> {

    private int code;

    private String msg;

    private boolean success;

    private T data;

    public JsonResult(int code,String msg,boolean success){
         this.code=code;
         this.msg=msg;
         this.success=success;
    }

    public static <T> JsonResult<T> success(){
        return JsonResult.success("操作成功！");
    }

    /**
     * 成功信息
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> success(String msg){
        return JsonResult.success(msg,null);
    }


    /**
     * 成功信息
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> success(String msg,T data){
        return new JsonResult<T>(HttpStatus.HTTP_OK,msg,true,data);
    }


    /**
     * @param data
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> success(T data){
        return JsonResult.success("操作成功！",data);
    }


    public static <T> JsonResult<T> error(){
        return JsonResult.error("操作失败！");
    }


    public static <T> JsonResult<T> error(String msg){
        return JsonResult.error(msg,null);
    }

    public static <T> JsonResult<T> error(String msg,T data){
        return new JsonResult<T>(HttpStatus.HTTP_INTERNAL_ERROR,msg,false,data);
    }

    public static <T> JsonResult<T> error(int code,String msg){
        return new JsonResult<T>(code,msg,false,null);
    }
    public static <T> JsonResult<T> error(T data){
        return JsonResult.error("操作失败！",data);
    }


}
