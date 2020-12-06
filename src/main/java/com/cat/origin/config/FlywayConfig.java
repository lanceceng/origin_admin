package com.cat.origin.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean flag = false;

    @PostConstruct
    public void migrate() {
        Flyway flyway = new Flyway();

        flyway.setDataSource(dataSource);
//        flyway.clean();
        flyway.setBaselineOnMigrate(true);
        // 设置flyway扫描sql升级脚本、java升级脚本的目录路径或包路径（表示是src/main/resources/flyway下面，前缀默认为src/main/resources，因为这个路径默认在classpath下面）
        flyway.setLocations("db/migration");
        // 设置sql脚本文件的编码
        flyway.setEncoding("UTF-8");

//        flyway.setOutOfOrder(true);

//        if(flag){
//            flyway.baseline();
//        }
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            flyway.repair();
            logger.error("Flyway配置加载出错",e);
        }
    }
}
