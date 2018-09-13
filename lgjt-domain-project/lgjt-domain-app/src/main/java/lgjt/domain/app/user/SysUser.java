package lgjt.domain.app.user;

import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonIgnore;

import com.ttsx.platform.nutz.pojo.BaseEntity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * <p>Title: SysUser</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Data
@Table("sys_user")
@ToString
public class SysUser extends BaseEntity {

	/** serialVersionUID*/  
	private static final long serialVersionUID = 1L;
	/**
	 * 启用
	 */
	public static final int STATUS_ENABLED = 0;
	/**
	 * 禁用
	 */
	public static final int STATUS_DISABLE = 1;
	/**
	 * 未知性别
	 */
	public static final int SEX_UNKNOWN=0;
	/**
	 * 男性
	 */
	public static final int SEX_MALE=1;
	/**
	 * 女性
	 */
	public static final int SEX_FEMALE=2;
	/**
	 * 未说明的性别
	 */
	public static final int SEX_UNSPECIFIED=9;
	
	/**
	 * 一般用户
	 */
	public static final int USERTYPE_ORDINARYUSER = 0;
	/**
	 * 认证用户
	 */
	public static final int USERTYPE_AUTHENTICATEDUSER = 1;
	
	/**
	 * 未锁定
	 */
	public static final int LOGINLOCK_UNLOCK = 0;
	/**
	 * 锁定
	 */
	public static final int LOGINLOCK_LOCK = 1;

	/**
	 * 用户名。
	 */
	@Column("user_name")
	private String userName;
	/**
	 * 姓名。
	 */
	@Column("real_name")
	private String realName;
	/**
	 * 头像。
	 */
	@Column("head_portrait")
	private String headPortrait;
	/**
	 * 密码。Md5加密
	 */
	@Column("password")
	private String password;
	/**
	 * 状态。0：启用；1：禁用 取值范围：#[enabled:0:启用$disable:1:禁用]#
	 */
	@Column("status")
	private Integer status;
	/**
	 * 性别。0：未知的性别 1：男性 2：女性 9：未说明的性别
	 */
	@Column("sex")
	private Integer sex;
	/**
	 * 移动电话。
	 */
	@Column("phone_number")
	private String phoneNumber;
	/**
	 * 出生日期。出生日期 yyyyMMdd
	 */
	@Column("birth_date")
	private Date birthDate;

	/**
	 * 邮箱。
	 */
	@Column("email")
	private String email;
	/**
	 * 组织ID。组织ID
	 */
	@Column("org_id")
	private String orgId;
	/**
	 * 有效证件号码。证件号
	 */
	@Column("id_no")
	private String idNo;
	/**
	 * 有效证件类别。与证件号码联合使用,找不到类别内容
	 */
	@Column("id_type")
	private Integer idType;
	/**
	 * 所属企业ID。全总系统中为每个企业分配一个唯一ID
	 */
	@Column("com_id")
	private String comId;
	/**
	 * 所属工会ID。全总系统为每个工会分配一个唯一ID
	 */
	@Column("union_id")
	private String unionId;
	/**
	 * 工会会员号。用户在所属工会的会员号
	 */
	@Column("number_no")
	private String numberNo;
	/**
	 * 工种。
	 */
	@Column("job")
	private String job;
	/**
	 * 企业工号。
	 */
	@Column("job_no")
	private String jobNo;
	/**
	 * 职称。
	 */
	@Column("title")
	private String title;
	/**
	 * 参加工作年份。
	 */
	@Column("work_year")
	private java.util.Date workYear;
	/**
	 * 微信号。用户绑定微信后才有值
	 */
	@Column("wechat")
	private String wechat;
	/**
	 * 支付宝账号。用户邦迪支付宝后才有值
	 */
	@Column("alipay")
	private String alipay;
	/**
	 * 用户类型。0：一般用户；1：认证用户 取值范围：#[ordinaryUser:0:一般用户$authenticatedUser:1:认证用户]#
	 */
	@Column("user_type")
	private Integer userType;
	/**
	 * 防篡改。防篡改
	 */
	@Column("anti_tamper")
	private String antiTamper;

	/**
	 * 盐值
	 */
	@Column("salt")
	private String salt;
	/**
	 * 民族。本人归属的、国家认可的、在公安户籍管理部门登记注册的民族。见 GB/T 3304 内容参考“相关类别识别代码.xlsx”
	 */
	@Column("nation")
	private String nation;
	/**
	 * 就业状况。1 在岗 6 退职 2 待（下）岗 7 退养（内退） 3 失业 8 病休 4 退休 9 其他 5 离休
	 */
	@Column("work_status")
	private Integer workStatus;
	/**
	 * 户籍类型。本人户籍类型.见 GA/T 2000.27 ？？？
	 */
	@Column("household_register")
	private String householdRegister;
	/**
	 * 学历。由国家认可的本人在国内、外各类教育机构接受正式教育并取得学历证书的学 习经历名称。见 GB/T 4658 参考“相关类别识别代码.xlsx”
	 */
	@Column("education")
	private String education;
	/**
	 * 技术等级。国家职业资格证书所载明的等级 1 职业资格一级（高级技师） 4 职业资格四级（中级） 2 职业资格二级（技师） 5 职业资格五级（初级） 3
	 * 职业资格三级（高级）
	 */
	@Column("technical_level")
	private Integer technicalLevel;
	/**
	 * 会籍变化类型。1 入会 4 转出 2 转入 5 除名 3 保留 6 退会
	 */
	@Column("membership_change_type")
	private Integer membershipChangeType;
	/**
	 * 会籍变化日期。yyyyMMDD
	 */
	@Column("membership_change_date")
	private java.util.Date membershipChangeDate;
	/**
	 * 会籍变化原因。1 劳动（工作）关系发生变化 4 所在企业破产 2 在本单位下岗后待岗 5 个人要求退会 3 失业 6 因犯罪被开除会籍
	 */
	@Column("membership_change_reason")
	private Integer membershipChangeReason;
	/**
	 * 备注。
	 */
	@Column("remark")
	private String remark;
	/**
	 * 用户永久锁定状态。0：未锁定；1：锁定 取值范围：#[unlock:0:未锁定$lock:1:锁定]#
	 */
	@Column("login_lock")
	private Integer loginLock;
	/**
	 * 扩展字段1。扩展字段
	 */
	@Column("extend1")
	private String extend1;
	/**
	 * 扩展字段2。扩展字段
	 */
	@Column("extend2")
	private String extend2;
	/**
	 * 扩展字段3。扩展字段
	 */
	@Column("extend3")
	private String extend3;
	/**
	 * 扩展字段4。扩展字段
	 */
	@Column("extend4")
	private Integer extend4;
	/**
	 * 扩展字段5。扩展字段
	 */
	@Column("extend5")
	private Integer extend5;
	/**
	 * 扩展字段6。扩展字段
	 */
	@Column("extend6")
	private java.util.Date extend6;
	/**
	 * 创建人。
	 */
	@Column("crt_user")
	private String crtUser;
	/**
	 * 创建时间。年-月-日 时:分:秒
	 */
	@Column("crt_time")
	private java.util.Date crtTime;
	/**
	 * 创建IP。示例：192.168.100.172
	 */
	@Column("crt_ip")
	private String crtIp;
	/**
	 * 最后修改人。
	 */
	@Column("upd_user")
	private String updUser;
	/**
	 * 最后修改时间。年-月-日 时:分:秒
	 */
	@Column("upd_time")
	private java.util.Date updTime;
	/**
	 * 最后修改IP。示例：192.168.100.172
	 */
	@Column("upd_ip")
	private String updIp;


	@Column("open_ids")
	private String openIds;

	/**
	 * 所属班组
	 */
	@Column("group_id")
	private String groupId;
}