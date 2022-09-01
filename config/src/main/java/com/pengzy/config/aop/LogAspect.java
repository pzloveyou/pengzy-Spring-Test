package com.pengzy.config.aop;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.pengzy.comment.AddressUtils;
import com.pengzy.comment.JsonUtils;
import com.pengzy.comment.ServletUtils;
import com.pengzy.comment.annotation.ApiLog;
import com.pengzy.config.base.mapper.SysLogMapper;
import com.pengzy.config.base.pojo.SysLog;
import com.pengzy.config.base.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author skeqi
 */
@Aspect
@Component
@Slf4j(topic = "api-log")
public class LogAspect
{

    @Resource
    private SysLogMapper sysLogMapper;
    private SysLogService sysLogService;
    private final ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<StopWatch>();
    private final ThreadLocal<String> requestId = new ThreadLocal<>();


    // 配置织入点
    @Pointcut("@annotation(com.pengzy.comment.annotation.ApiLog)")
    public void ApilogPointCut()
    {
    }
//    /**
//     * 处理完请求后执行
//     *
//     * @param joinPoint 切点
//     */
//    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
//    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
//    {
//        handleLog(joinPoint, null, jsonResult);
//    }
//
//    /**
//     * 拦截异常操作
//     *
//     * @param joinPoint 切点
//     * @param e 异常
//     */
//    @AfterThrowing(value = "logPointCut()", throwing = "e")
//    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
//    {
//        handleLog(joinPoint, e, null);
//    }



//    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult)
//    {
//        try
//        {
//            // 获得注解
//            Log controllerLog = getAnnotationLog(joinPoint);
//            if (controllerLog == null)
//            {
//                return;
//            }
//
//            // 获取当前的用户
//            LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
//
//            // *========数据库日志=========*//
//            SysOperLog operLog = new SysOperLog();
//            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
//            // 请求的地址
//            String ip = ServletUtils.getClientIP();
//            operLog.setOperIp(ip);
//            // 返回参数
//            operLog.setJsonResult(JsonUtils.toJsonString(jsonResult));
//
//            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
//            if (loginUser != null)
//            {
//                operLog.setOperName(loginUser.getUsername());
//            }
//
//            if (e != null)
//            {
//                operLog.setStatus(BusinessStatus.FAIL.ordinal());
//                operLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
//            }
//            // 设置方法名称
//            String className = joinPoint.getTarget().getClass().getName();
//            String methodName = joinPoint.getSignature().getName();
//            operLog.setMethod(className + "." + methodName + "()");
//            // 设置请求方式
//            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
//
//            // 处理设置注解上的参数
//            getControllerMethodDescription(joinPoint, controllerLog, operLog);
//            // 保存数据库
//			SpringUtils.getBean(AsyncService.class).recordOper(operLog);
//            if(controllerLog.isLog()){
//                log.info("【返回参数】：[{}]", JSONObject.toJSONString(operLog));
//            }
//        }
//        catch (Exception exp)
//        {
//            // 记录本地异常日志
//            log.error("==前置通知异常==");
//            log.error("异常信息:{}", exp.getMessage());
//            exp.printStackTrace();
//        }
//    }

    @Before(value = "ApilogPointCut()")
    public void befores(){
        requestId.set(IdWorker.getIdStr());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatchThreadLocal.set(stopWatch);
        log.info("before....");
    }

    @AfterReturning(value = "ApilogPointCut()",returning = "var")
    public void afterReturning(JoinPoint joinPoint, Object var) {
        apiLog(joinPoint, null, var);
    }

    @AfterThrowing(pointcut = "ApilogPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint,  Exception e) {
        apiLog(joinPoint, e, null);
    }



    protected  void apiLog(final JoinPoint joinPoint, final Exception es, Object methodResult){
        StopWatch  stopwatch = stopWatchThreadLocal.get();
        SysLog sysLog=new SysLog();
        try {
            ServletRequestAttributes sra = ServletUtils.getRequestAttributes();
            HttpServletRequest request = sra.getRequest();
            //获取全部Parameter参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            // 获得注解
            ApiLog controllerLog = getAnnotationApiLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            String className = joinPoint.getTarget().getClass().getName();
            Method method = ms.getMethod();
            String methodName=method.getName();
            String request_method=className+"."+methodName+"()";
            String params= Arrays.toString(joinPoint.getArgs());
            String returnStr= JsonUtils.toJsonString(methodResult);
            stopwatch.stop();
            double timeConsuming = stopwatch.getTotalTimeSeconds();
            double timeMins=stopwatch.getTotalTimeMillis();
            new Thread(()->{
                sysLog.setUsername("pengzy");
                sysLog.setType(controllerLog.type().getInfo());
                sysLog.setOperation(controllerLog.method());
                sysLog.setIp(ServletUtil.getClientIP(request));
                sysLog.setRequest(request.getMethod());
                sysLog.setStatus("OK");
                if(es!=null){
                    sysLog.setStatus("NO");
                    String msg=es.getMessage();
                    sysLog.setErrorMsg(msg!=null&&msg.length()>2000?msg.substring(0,2000):msg);
                }
                sysLog.setDict(AddressUtils.getRealAddressByIP(sysLog.getIp()));
                sysLog.setRequestDict(request.getRequestURI());
                sysLog.setMethod(request_method);
                sysLog.setParam(params);
                if(StringUtils.isBlank(params)){
                    sysLog.setParam(JsonUtils.toJsonString(parameterMap));
                }
                sysLog.setResponse(returnStr);
                sysLog.setTime(timeMins+"(ms)");
                sysLog.setCreateTime(new Date());
                sysLogMapper.insert(sysLog);
            }).start();
            log.info("【请求ID】: {}" , requestId.get());
            log.info("【请求方法为】：{}" , request_method);
            log.info("【请求参数为(body)】：{}" , params);
            log.info("【请求参数为(param)】：{}",JsonUtils.toJsonString(parameterMap));
            log.info("【返回内容为】：{}" ,returnStr);
            log.info("【耗时(s)】:{}s -【耗时(ms)】:{}ms" ,timeConsuming,timeMins);
        }catch (Exception e){
            // 记录本地异常日志
            log.error("参数获取失败:{}", e.getMessage());
            e.printStackTrace();
        }finally {
            requestId.remove();
            stopWatchThreadLocal.remove();
        }
    }


    /**
     * 插入数据库
     */
    public void add(){

    }




    /**
     * 是否存在注解，如果存在就获取
     */
    private ApiLog getAnnotationApiLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(ApiLog.class);
        }
        return null;
    }
    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0)
        {
			for (Object o : paramsArray) {
				if (Validator.isNotNull(o) && !isFilterObject(o)) {
					params.append(JsonUtils.toJsonString(o)).append(" ");
				}
			}
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
			for (Object value : collection) {
				return value instanceof MultipartFile;
			}
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
			for (Object value : map.entrySet()) {
				Map.Entry entry = (Map.Entry) value;
				return entry.getValue() instanceof MultipartFile;
			}
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }



    /**
     * 获取异常返回参数
     * @param e
     * @return
     */
//    public String returnIng(Exception e){
//        if(e instanceof CilentProductionException){
//            CilentProductionException ces=(CilentProductionException)e;
//            return JsonUtils.toJsonString(AjaxResult.error(ces.getCode(), ces.getMessage(),ces.getData()));
//        }
//        if(e instanceof DuplicateKeyException){
//            DuplicateKeyException ces=(DuplicateKeyException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(ces.getCause().getMessage()));
//        }
//        if(e instanceof NoHandlerFoundException){
//            NoHandlerFoundException ces=(NoHandlerFoundException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(HttpStatus.HTTP_NOT_FOUND,"路径不存在，请检查路径是否正确"));
//        }
//        if(e instanceof AccessDeniedException){
//            AccessDeniedException ces=(AccessDeniedException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(HttpStatus.HTTP_FORBIDDEN,"没有权限，请联系管理员授权"));
//        }
//        if(e instanceof AccountExpiredException){
//            AccountExpiredException ces=(AccountExpiredException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(ces.getMessage()));
//        }
//        if(e instanceof UsernameNotFoundException){
//            UsernameNotFoundException ces=(UsernameNotFoundException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(ces.getMessage()));
//        }
//        if(e instanceof DataIntegrityViolationException){
//            DataIntegrityViolationException ces=(DataIntegrityViolationException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(ces.getCause().getMessage()));
//        }
//        if(e instanceof IOException){
//            //IOException ces=(IOException)e;
//            return  JsonUtils.toJsonString("请求异常，请稍后再试！");
//        }
//        if(e instanceof HttpMessageNotReadableException){
//            //DataIntegrityViolationException ces=(DataIntegrityViolationException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error("请求参数缺失，格式按照application/json"));
//        }
//        if(e instanceof BindException){
//            BindException ces=(BindException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(ces.getAllErrors().get(0).getDefaultMessage()));
//        }
//        if(e instanceof ConstraintViolationException){
//            ConstraintViolationException ces=(ConstraintViolationException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(ces.getConstraintViolations().iterator().next().getMessage()));
//        }
//        if(e instanceof MethodArgumentNotValidException){
//            MethodArgumentNotValidException ces=(MethodArgumentNotValidException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error(1000,ces.getBindingResult().getFieldError().getDefaultMessage()));
//        }
//        if(e instanceof DemoModeException){
//            DemoModeException ces=(DemoModeException)e;
//            return  JsonUtils.toJsonString(AjaxResult.error("演示模式，不允许操作"));
//        }
//        return  JsonUtils.toJsonString(AjaxResult.error("服务器开小差，请稍后再试！"));
//    }
}
