var ioc = {
    config: {
        type: "org.nutz.ioc.impl.PropertiesProxy",
        fields: {
            paths: [ "conf.properties"]
        }
    },
    dataSourceHelpMysql : {
        type: "com.alibaba.druid.pool.DruidDataSource",
        events : {
            create : "init",
            depose : 'close'
        },
        fields: {
            driverClass: {
                java: "$config.get('db-driver')"
            },
            url: {
                java: "$config.get('db-url')"
            },
            username : {
                java: "$config.get('db-username')"
            },
            password : {
                java: "$config.get('db-password')"
            },
            testWhileIdle :{
                java : "$config.get('db-testWhileIdle')" // 非常重要,预防mysql的8小时timeout问题
            },
            validationQuery : {
                java : "$config.get('db-preferredTestQuery')" // Oracle的话需要改一下
            },
            maxActive : {
                java :"$config.get('db-maxActive')"
            },
            maxWait: {
                java :"$config.get('db-maxWait')"  // 若不配置此项,如果数据库未启动,druid会一直等可用连接,卡住启动过程,
            },
            defaultAutoCommit : {
                java : "$config.get('db-defaultAutoCommit')", // 提高fastInsert的性能,
            },
            filters : {
                java:"$config.get('db-filters')"// SQL监控相关
            },
            connectionProperties : {
                java:"$config.get('db-connectionProperties')"// SQL监控相关
            }

        }
       
    },

    filesql: {
        type: "com.ttsx.platform.nutz.dao.sqls.FileSqlManagerEX", //按照文件加载
        args: "sqlmap"
    },
    daoHelpMysql : {
        type : "org.nutz.dao.impl.NutDao",
        args: [
            {
                refer: 'dataSourceHelpMysql'
            },
            {
                refer: 'filesql'
            }
        ]
    }

}