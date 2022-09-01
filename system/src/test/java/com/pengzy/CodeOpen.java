package com.pengzy;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @author pzy
 * @version 1.0
 * 代码自动生成器
 */
public class CodeOpen {
    public static void main(String[] args) {
        //需要构建一个代码自动生成器对象
        AutoGenerator auto = new AutoGenerator();
        //全局配置
        GlobalConfig gc = new GlobalConfig();
        String property = System.getProperty("user.dir");   //获取项目路径
        gc.setOutputDir(property + "\\config\\src\\main\\java");
        gc.setFileOverride(false);  //是否覆盖
        gc.setServiceName("%sService");  //去Service的I前缀
        gc.setIdType(IdType.AUTO);   //主键生成方式
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        gc.setAuthor("pzy");  //标记作者名称
        gc.setOpen(false); //是否打开资源管理器
        auto.setGlobalConfig(gc);
        //数据源配置
        DataSourceConfig dc=new DataSourceConfig();
        dc.setUrl("jdbc:mysql://localhost:3306/goods_db_1?useSSL=false&useUnicode=tru&characterEncoding=utf-8&serverTimezone=GMT%2B8&allowMultiQueries=true");
        //dc.setSchemaName("mybatis_plus");
        dc.setDriverName("com.mysql.cj.jdbc.Driver");
        dc.setUsername("root");
        dc.setPassword("root");
        dc.setDbType(DbType.MYSQL);
        auto.setDataSource(dc);
        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("base");     //父类包名
        pc.setParent("com.pengzy.config");  //包路径
        pc.setEntity("pojo");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        auto.setPackageInfo(pc);
        //策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("sys_log");   //设置要映射的表,可以多个
        strategy.setNaming(NamingStrategy.underline_to_camel);  //表名下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);   //列下划线转驼峰
        strategy.setEntityLombokModel(true);   //自动lombok
        strategy.setLogicDeleteFieldName("deleted"); //配置逻辑删除字段
        //自动填充策略
        ArrayList<TableFill> tableFills = new ArrayList<>();
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);
        //乐观锁
        strategy.setVersionFieldName("version");   //乐观锁字段
        strategy.setRestControllerStyle(true);  //localhost:8080/hello_id_2
        auto.setStrategy(strategy);
        //执行生成
        auto.execute();
    }
}
