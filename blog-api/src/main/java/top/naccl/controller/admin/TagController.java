package top.naccl.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Tag;
import top.naccl.service.BlogService;
import top.naccl.service.TagService;
import top.naccl.model.vo.Result;
import top.naccl.util.StringUtils;

import java.util.Map;

/**
 * @Description: 博客标签后台管理
 * @Author: Naccl
 * @Date: 2020-08-02
 */
@RestController
@RequestMapping("/admin")
public class TagController {
	@Autowired
	BlogService blogService;
	@Autowired
	TagService tagService;

	/**
	 * 获取博客标签列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/tags")
	public Result tags(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
		try {
			String orderBy = "id desc";
			PageHelper.startPage(pageNum, pageSize, orderBy);
			PageInfo<Tag> pageInfo = new PageInfo<>(tagService.getTagList());
			return Result.ok("请求成功", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 添加新标签
	 *
	 * @param map 包含标签名称、颜色的map => {name="123", color="red"}
	 * @return
	 */
	@PostMapping("/tag")
	public Result saveTag(@RequestBody Map<String, Object> map) {
		return getResult(map, "save");
	}

	/**
	 * 修改标签
	 *
	 * @param map 包含标签id、名称、颜色的map => {id=1, name="123", color="red"}
	 * @return
	 */
	@PutMapping("/tag")
	public Result updateTag(@RequestBody Map<String, Object> map) {
		return getResult(map, "update");
	}

	/**
	 * 执行标签添加或更新操作：校验参数是否合法，标签是否已存在
	 *
	 * @param map  标签map对象
	 * @param type 添加或更新
	 * @return
	 */
	private Result getResult(Map<String, Object> map, String type) {
		try {
			JSONObject tagJsonObject = new JSONObject(map);
			Tag tag = JSONObject.toJavaObject(tagJsonObject, Tag.class);
			if (StringUtils.isEmpty(tag.getName())) {
				return Result.error("参数不能为空");
			}
			//查询标签是否已存在
			Tag tag1 = tagService.getTagByName(tag.getName());
			if (tag1 != null && tag1.getId() != tag.getId()) {
				return Result.error("该标签已存在");
			}
			if ("save".equals(type)) {
				int r = tagService.saveTag(tag);
				if (r == 1) {//添加标签成功
					return Result.ok("添加成功");
				} else {//添加标签失败
					return Result.error("添加失败");
				}
			} else {
				int r = tagService.updateTag(tag);
				if (r == 1) {//更新标签成功
					return Result.ok("更新成功");
				} else {//更新标签失败
					return Result.error("更新失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 按id删除标签
	 *
	 * @param id 标签id
	 * @return
	 */
	@DeleteMapping("/tag")
	public Result delete(@RequestParam Long id) {
		try {
			//删除存在博客关联的标签后，该博客的查询会出异常
			int num = blogService.countBlogByTagId(id);
			if (num != 0) {
				return Result.error("已有博客与此标签关联，不可删除");
			}
			int r = tagService.deleteTagById(id);
			if (r == 1) {//删除标签成功
				return Result.ok("删除成功");
			} else {//删除标签失败
				return Result.error("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
