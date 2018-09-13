var ioc = {
	tmpFilePool : {
		type : 'org.nutz.filepool.NutFilePool',
		args : [ {java:"$config.get('temp-path')"}, 1000 ]
	},
    uploadImageContext : {
        type : 'org.nutz.mvc.upload.UploadingContext',
        singleton : false,
        args : [ {
            refer : 'tmpFilePool'
        } ],
        fields : {
            // 是否忽略空文件, 默认为 false
            ignoreNull : true,
            // 单个文件最大尺寸(大约的值，单位为字节，即 1048576 为 10M)
            maxFileSize : 10485760,
            // 正则表达式匹配可以支持的文件名
		        nameFilter : '^(.+[.])(gif|jpg|png|jpeg|bmp)$'
            // nameFilter : '.*'
        }
    },
    myUpload : {
        type : 'com.ttsx.platform.nutz.mvc.upload.UpAdaptor',
        singleton : false,
        args : [ {
            refer : 'uploadFileContext'
        } ]
    },
	uploadFileContext : {
		type : 'org.nutz.mvc.upload.UploadingContext',
		singleton : false,
		args : [ {
			refer : 'tmpFilePool'
		} ],
		 fields : {
		        // 是否忽略空文件, 默认为 false
		        ignoreNull : true,
		        // 单个文件最大尺寸(大约的值，单位为字节，即 1048576 为 200M)
		        maxFileSize : 209715200,
		        // 正则表达式匹配可以支持的文件名
//		        nameFilter : '^(.+[.])(gif|jpg|png)$' 
		        nameFilter : '.*'
		    } 
	},
	imageUpload : {
        type : 'com.ttsx.platform.nutz.mvc.upload.UpAdaptor',
        singleton : false,
        args : [ {
            refer : 'uploadImageContext'
        } ]
    },
	fileUpload : {
		type : 'com.ttsx.platform.nutz.mvc.upload.UpAdaptor',
		singleton : false,
		args : [ {
			refer : 'uploadFileContext'
		} ]
	},
	json : {
        type : "org.nutz.mvc.view.UTF8JsonView",
        args : [{
    		type : 'org.nutz.json.JsonFormat',
    		fields: {
    			autoUnicode : true
    		}
    	}]
    }
};