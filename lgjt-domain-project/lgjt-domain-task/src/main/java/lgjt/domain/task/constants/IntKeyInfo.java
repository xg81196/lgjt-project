package lgjt.domain.task.constants;

/**
 * 整型常量类
 * 
 * @Description: TODO(用一句话描述该类作用) 
 * @author daijiaqi
 * @CreateDate:   2016-12-16 下午6:46:20  
 * 
 * @UpdateUser:   daijiaqi 
 * @UpdateDate:   2016-12-16 下午6:46:20  
 * @UpdateRemark: 说明本次修改内容
 */
public class IntKeyInfo {
	private int key;
	private String name;
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public IntKeyInfo(){}
	
	public IntKeyInfo(int key,String name){
		this.key = key;
		this.name = name;
	}
}
