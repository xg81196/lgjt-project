var ioc = {
    config: {
        type: "org.nutz.ioc.impl.PropertiesProxy",
        fields: {
            paths: [ "conf.properties"]
        }
    },
    dataSourceMoments : {
        type: "com.alibaba.druid.pool.DruidDataSource",
        events : {
            create : "init",
            depose : 'close'
        },
        fields: {
            driverClassName : 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
            url : 'jdbc:sqlserver://218.56.161.72:1433;databaseName=lgjtdq',
            username : 'lgjtdqsql',
            password : 'lgjtdqsql'
        }

    },

    filesql: {
        type: "com.ttsx.platform.nutz.dao.sqls.FileSqlManagerEX", //按照文件加载
        args: "sqlmap"
    },
    daoMoments : {
        type : "org.nutz.dao.impl.NutDao",
        args: [
            {
                refer: 'dataSourceMoments'
            },
            {
                refer: 'filesql'
            }
        ]
    }

}