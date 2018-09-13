var ioc = {
	common : {
		type : 'lgjt.common.base.CommonInterceptor'
	},
	oplog : {
		type : ''
	},
	txNONE : {
		type : 'org.nutz.aop.interceptor.TransactionInterceptor',
		args : [ 0 ]
	},
	txREAD_UNCOMMITTED : {
		type : 'org.nutz.aop.interceptor.TransactionInterceptor',
		args : [ 1 ]
	},
	txREAD_COMMITTED : {
		type : 'org.nutz.aop.interceptor.TransactionInterceptor',
		args : [ 2 ]
	},
	txREPEATABLE_READ : {
		type : 'org.nutz.aop.interceptor.TransactionInterceptor',
		args : [ 4 ]
	},
	txSERIALIZABLE : {
		type : 'org.nutz.aop.interceptor.TransactionInterceptor',
		args : [ 8 ]
	},

	$aop : {
		type : 'org.nutz.ioc.aop.config.impl.JsonAopConfigration',
		fields : {
			itemList : [ 
			             [ 'lgjt.service\\..*', '(insert|update|delete|tx).*', 'ioc:txREPEATABLE_READ' ],
			             [ 'lgjt.web.moments.module\\..*', '.*', 'ioc:common' ]
						]
		}
	}
}
