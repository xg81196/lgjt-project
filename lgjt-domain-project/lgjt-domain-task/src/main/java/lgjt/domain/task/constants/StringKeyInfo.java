package lgjt.domain.task.constants;
/**
 * 字符型常量类
 * 
 * @Description: TODO(用一句话描述该类作用) 
 * @author daijiaqi
 * @CreateDate:   2016-12-16 下午6:46:44  
 * 
 * @UpdateUser:   daijiaqi 
 * @UpdateDate:   2016-12-16 下午6:46:44  
 * @UpdateRemark: 说明本次修改内容
 */
public class StringKeyInfo {
	private String key;
	private String name;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public StringKeyInfo(){}
	
	public StringKeyInfo(String key,String name){
		this.key = key;
		this.name = name;
	}

}
