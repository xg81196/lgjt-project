package lgjt.services.books;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.books.LgLetterCategory;
import lgjt.domain.books.LgLetterInfo;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaotianyi
 * 书香莱钢分类
 */
@Log4j
@IocBean
public class LgLetterInfoService extends BaseService {

	@Inject
	LgLetterCategoryService lgLetterCategoryService;

	public PageResult<LgLetterInfo> queryPage(LgLetterInfo obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getBookName())) {
			cri.where().andEquals("book_name", obj.getBookName());
		}
		if(StringTool.isNotNull(obj.getBookAuthor())) {
			cri.where().andEquals("book_author", obj.getBookAuthor());
		}
		if(StringTool.isNotNull(obj.getBookTime())) {
			cri.where().andEquals("book_time", obj.getBookTime());
		}
		if(StringTool.isNotNull(obj.getBookIntroduce())) {
			cri.where().andEquals("book_introduce", obj.getBookIntroduce());
		}
		if(StringTool.isNotNull(obj.getBookPicture())) {
			cri.where().andEquals("book_picture", obj.getBookPicture());
		}
		if(StringTool.isNotNull(obj.getBookContent())) {
			cri.where().andEquals("book_content", obj.getBookContent());
		}
		if(StringTool.isNotNull(obj.getBookCategoryId())) {
			cri.where().andEquals("book_category_id", obj.getBookCategoryId());
		}
		if(StringTool.isNotNull(obj.getIsRecommend())) {
			cri.where().andEquals("is_recommend", obj.getIsRecommend());
		}
		if(StringTool.isNotNull(obj.getRecommendation())) {
			cri.where().andEquals("recommendation", obj.getRecommendation());
		}
		if(StringTool.isNotNull(obj.getSort())) {
			cri.where().andEquals("sort", obj.getSort());
		}
		if(StringTool.isNotNull(obj.getVerifyStatus())) {
			cri.where().andEquals("verify_status", obj.getVerifyStatus());
		}
		if(StringTool.isNotNull(obj.getPublisher())) {
			cri.where().andEquals("publisher", obj.getPublisher());
		}
		if(StringTool.isNotNull(obj.getPublishTime())) {
			cri.where().andEquals("publish_time", obj.getPublishTime());
		}
		if(StringTool.isNotNull(obj.getPublishStatus())) {
			cri.where().andEquals("publish_status", obj.getPublishStatus());
		}
		if(StringTool.isNotNull(obj.getLikeCount())) {
			cri.where().andEquals("like_count", obj.getLikeCount());
		}
		if(StringTool.isNotNull(obj.getCommentCount())) {
			cri.where().andEquals("comment_count", obj.getCommentCount());
		}
		if(StringTool.isNotNull(obj.getPageviews())) {
			cri.where().andEquals("pageviews", obj.getPageviews());
		}
		if(StringTool.isNotNull(obj.getBookType())) {
			cri.where().andEquals("book_type", obj.getBookType());
		}
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		if(StringTool.isNotNull(obj.getBookResource())) {
			cri.where().andEquals("book_resource", obj.getBookResource());
		}
		if(StringTool.isNotNull(obj.getAuditOpinion())) {
			cri.where().andEquals("audit_opinion", obj.getAuditOpinion());
		}

		return super.queryPage(LgLetterInfo.class, obj, cri);
	}

	public List<LgLetterInfo> query(LgLetterInfo obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getBookName())) {
			cri.where().andEquals("book_name", obj.getBookName());
		}
		if(StringTool.isNotNull(obj.getBookAuthor())) {
			cri.where().andEquals("book_author", obj.getBookAuthor());
		}
		if(StringTool.isNotNull(obj.getBookTime())) {
			cri.where().andEquals("book_time", obj.getBookTime());
		}
		if(StringTool.isNotNull(obj.getBookIntroduce())) {
			cri.where().andEquals("book_introduce", obj.getBookIntroduce());
		}
		if(StringTool.isNotNull(obj.getBookPicture())) {
			cri.where().andEquals("book_picture", obj.getBookPicture());
		}
		if(StringTool.isNotNull(obj.getBookContent())) {
			cri.where().andEquals("book_content", obj.getBookContent());
		}
		if(StringTool.isNotNull(obj.getBookCategoryId())) {
			cri.where().andEquals("book_category_id", obj.getBookCategoryId());
		}
		if(StringTool.isNotNull(obj.getIsRecommend())) {
			cri.where().andEquals("is_recommend", obj.getIsRecommend());
		}
		if(StringTool.isNotNull(obj.getRecommendation())) {
			cri.where().andEquals("recommendation", obj.getRecommendation());
		}
		if(StringTool.isNotNull(obj.getSort())) {
			cri.where().andEquals("sort", obj.getSort());
		}
		if(StringTool.isNotNull(obj.getVerifyStatus())) {
			cri.where().andEquals("verify_status", obj.getVerifyStatus());
		}
		if(StringTool.isNotNull(obj.getPublisher())) {
			cri.where().andEquals("publisher", obj.getPublisher());
		}
		if(StringTool.isNotNull(obj.getPublishTime())) {
			cri.where().andEquals("publish_time", obj.getPublishTime());
		}
		if(StringTool.isNotNull(obj.getPublishStatus())) {
			cri.where().andEquals("publish_status", obj.getPublishStatus());
		}
		if(StringTool.isNotNull(obj.getLikeCount())) {
			cri.where().andEquals("like_count", obj.getLikeCount());
		}
		if(StringTool.isNotNull(obj.getCommentCount())) {
			cri.where().andEquals("comment_count", obj.getCommentCount());
		}
		if(StringTool.isNotNull(obj.getPageviews())) {
			cri.where().andEquals("pageviews", obj.getPageviews());
		}
		if(StringTool.isNotNull(obj.getBookType())) {
			cri.where().andEquals("book_type", obj.getBookType());
		}
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
        if(StringTool.isNotNull(obj.getBookResource())) {
            cri.where().andEquals("book_resource", obj.getBookResource());
        }
		if(StringTool.isNotNull(obj.getAuditOpinion())) {
			cri.where().andEquals("audit_opinion", obj.getAuditOpinion());
		}
		return super.query(LgLetterInfo.class, cri);
	}

   	public LgLetterInfo get(String id) {
		return super.fetch(LgLetterInfo.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgLetterInfo.class, cri);
		}
		return 0;
	}

	public LgLetterInfo checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgLetterInfo.class,cri);
	}

	public List<Map<String, Object>> queryListWithCategory() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		SimpleCriteria cri = Cnd.cri();
		List<LgLetterCategory> lgLetterCategories = lgLetterCategoryService.query(LgLetterCategory.class, cri);
		for(LgLetterCategory lgLetterCategory: lgLetterCategories) {
			Map<String, Object> onese = new HashMap<>();
			String categoryId = lgLetterCategory.getId();
			cri = Cnd.cri();
			cri.where().andEquals("book_category_id", categoryId);
			List<LgLetterInfo> lgLetterInfos = super.query(LgLetterInfo.class, cri);
			//分类ID
			onese.put("categoryId", categoryId);
			//分类名称
			onese.put("categoryName", lgLetterCategory.getCateName());
			onese.put("books", lgLetterInfos);
			result.add(onese);
		}
		return result;
	}

}