package com.pengzy.config.base.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author pzy
 * @since 2022-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysLog对象", description="")
@TableName(value = "sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作用户")
    @TableField(value = "username")
    private String username;

    @ApiModelProperty(value = "操作")
    @TableField(value = "operation")
    private String operation;

    @ApiModelProperty(value = "操作类型（1.查询2.新增3.修改4.删除）")
    @TableField(value = "type")
    private String type;

    @ApiModelProperty(value = "IP地址")
    @TableField(value = "ip")
    private String ip;

    @ApiModelProperty(value = "请求方式")
    @TableField(value = "request")
    private String request;

    @ApiModelProperty(value = "操作状态")
    @TableField(value = "status")
    private String status;

    @ApiModelProperty(value = "错误信息")
    @TableField(value = "errorMsg")
    private String errorMsg;

    @ApiModelProperty(value = "操作地址")
    @TableField(value = "dict")
    private String dict;

    @ApiModelProperty(value = "请求地址")
    @TableField(value = "requestDict")
    private String requestDict;

    @ApiModelProperty(value = "操作方法")
    @TableField(value = "method")
    private String method;

    @ApiModelProperty(value = "请求参数")
    @TableField(value = "param")
    private String param;

    @ApiModelProperty(value = "返回参数")
    @TableField(value = "response")
    private String response;

    @ApiModelProperty(value = "耗时（ms）")
    @TableField(value = "time")
    private String time;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
