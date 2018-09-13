package lgjt.domain.app.noticemessage;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.sql.Timestamp;
import java.util.List;

/**
* 
*/
@Data
@Table("lg_notice_message")
public class LgNoticeMessage extends BaseEntity {



		/**
	 * 标题
	 */
		@Column("title")
		private String title;
		/**
	 * 消息类型。1:系统消息,2:闯关消息
	 */
		@Column("message_type")
		private Integer messageType;
		/**
	 * 消息内容
	 */
		@Column("content")
		private String content;
		/**
	 * 附件
	 */
		@Column("enclosure")
		private String enclosure;
		/**
	 * 通知有效期开始时间
	 */
		@Column("begin_time")
		private Timestamp beginTime;
		/**
	 * 通知有效期结束时间
	 */
		@Column("end_time")
		private Timestamp endTime;



	/**
	 * 接收人类型 0 全部  1 企业  2 人员
	 */
	@Column("target")
	private Integer target;




	/**
	 * 通知接收人
	 */
		@Column("receive_id")
		private String receiveId;
		/**
	 * 是否发布 未发布为0，发布为1
	 */
		@Column("is_public")
		private Integer isPublic;
		/**
	 * 通知涉及范围，发给哪些单位
	 */
		@Column("notice_rang")
		private String noticeRang;


		/**
	 * 发送状态 0:发送成功.1:发送失败
	 */



		@Column("status")
		private Integer status;




		/**
	 * 发送日期
	 */
		@Column("send_date")
		private String sendDate;
		/**
	 * 
	 */
		@Column("crt_user")
		private String crtUser;
		/**
	 * 
	 */
		@Column("crt_time")
		private Timestamp crtTime;
		/**
	 * 
	 */
		@Column("crt_ip")
		private String crtIp;
		/**
	 * 
	 */
		@Column("upd_user")
		private String updUser;
		/**
	 * 
	 */
		@Column("upd_time")
		private java.util.Date updTime;
		/**
	 * 
	 */
		@Column("upd_ip")
		private String updIp;

	private List<String> userNames;
}