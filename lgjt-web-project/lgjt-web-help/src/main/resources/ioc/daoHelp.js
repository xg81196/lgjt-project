var ioc = {
    config: {
        type: "org.nutz.ioc.impl.PropertiesProxy",
        fields: {
            paths: [ "conf.properties"]
        }
    },
    dataSourceHelp : {
        type: "com.alibaba.druid.pool.DruidDataSource",
        events : {
            create : "init",
            depose : 'close'
        },
        fields: {
            // driverClassName : 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
            // url : 'jdbc:sqlserver://localhost:1433;databaseName=lgjt',
            // username : 'xb',
            // password : '81196'
            // }
            // driverClassName : 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
            // url : 'jdbc:sqlserver://218.56.161.72:1433;databaseName=lgjtdq',
            // username : 'lgjtdqsql',
            // password : 'lgjtdqsql'
            driverClassName : 'com.microsoft.sqlserver.jdbc.SQLServerDriver',
            url : 'jdbc:sqlserver://192.168.100.38:1433;databaseName=lgjt18-08-08',
            username : 'sa',
            password : '1qaz2wsx'
        	}
       
    },

    filesql: {
        type: "com.ttsx.platform.nutz.dao.sqls.FileSqlManagerEX", //按照文件加载
        args: "sqlmap"
    },
    daoHelp : {
        type : "org.nutz.dao.impl.NutDao",
        args: [
            {
                refer: 'dataSourceHelp'
            },
            {
                refer: 'filesql'
            }
        ]
    }

}