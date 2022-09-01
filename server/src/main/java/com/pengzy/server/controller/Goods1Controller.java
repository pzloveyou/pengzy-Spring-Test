package com.pengzy.server.controller;


import com.pengzy.comment.annotation.ApiLog;
import com.pengzy.comment.enums.OperateEnum;
import com.pengzy.comment.result.CustomException;
import com.pengzy.comment.result.JsonResult;
import com.pengzy.server.service.Goods1Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pzy
 * @since 2022-08-03
 */
@RestController
@RequestMapping("/system/goods1")
@Api(tags = "goodsdb1")
@Slf4j(topic = "api-log")
public class Goods1Controller {

    @Resource
    private Goods1Service goods1Service;



    @RequestMapping(value = "/open",method = RequestMethod.POST)
    @ApiOperation(value = "第一个入口方法")
    @ApiLog(method = "第一个入口方法",type= OperateEnum.QUEY)
    public JsonResult<Object> open(@RequestParam String msg){
        log.info("测试接口。。。。。");
        log.info(msg);
        return JsonResult.success(msg);
    }



}