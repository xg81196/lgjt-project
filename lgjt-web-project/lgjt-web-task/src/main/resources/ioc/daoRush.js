var ioc = {
    config: {
        type: "org.nutz.ioc.impl.PropertiesProxy",
        fields: {
            paths: [ "conf.properties"]
        }
    },
    dataSourceRush : {
        type: "com.alibaba.druid.pool.DruidDataSource",
        events : {
            create : "init",
            depose : 'close'
        },
        fields: {
            driverClass: {
                java: "$config.get('rush-db-driver')"
            },
            url: {
                java: "$config.get('rush-db-url')"
            },
            username : {
                java: "$config.get('rush-db-username')"
            },
            password : {
                java: "$config.get('rush-db-password')"
            },
            testWhileIdle :{
                java : "$config.get('rush-db-testWhileIdle')" // 非常重要,预防mysql的8小时timeout问题
            },
            validationQuery : {
                java : "$config.get('rush-db-preferredTestQuery')" // Oracle的话需要改一下
            },
            maxActive : {
                java :"$config.get('rush-db-maxActive')"
            },
            maxWait: {
                java :"$config.get('rush-db-maxWait')"  // 若不配置此项,如果数据库未启动,druid会一直等可用连接,卡住启动过程,
            },
            defaultAutoCommit : {
                java : "$config.get('rush-db-defaultAutoCommit')", // 提高fastInsert的性能,
            },
            filters : {
                java:"$config.get('rush-db-filters')"// SQL监控相关
            },
            connectionProperties : {
                java:"$config.get('rush-db-connectionProperties')"// SQL监控相关
            }

        }
    },

    filesql: {
        type: "com.ttsx.platform.nutz.dao.sqls.FileSqlManagerEX", //按照文件加载
        args: "sqlmap"
    },
    daoRush : {
        type : "org.nutz.dao.impl.NutDao",
        args: [
            {
                refer: 'dataSourceRush'
            },
            {
                refer: 'filesql'
            }
        ]
    }

}