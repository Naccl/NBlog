package top.naccl.controller.admin;

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
import top.naccl.annotation.OperationLogger;
import top.naccl.entity.Friend;
import top.naccl.model.vo.Result;
import top.naccl.service.FriendService;

import java.util.Map;

/**
 * @Description: 友链页面后台管理
 * @Author: Naccl
 * @Date: 2020-09-08
 */
@RestController
@RequestMapping("/admin")
public class FriendAdminController {
	@Autowired
	FriendService friendService;

	/**
	 * 分页获取友链列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页条数
	 * @return
	 */
	@GetMapping("/friends")
	public Result friends(@RequestParam(defaultValue = "1") Integer pageNum,
	                      @RequestParam(defaultValue = "10") Integer pageSize) {
		String orderBy = "create_time asc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<Friend> pageInfo = new PageInfo<>(friendService.getFriendList());
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 更新友链公开状态
	 *
	 * @param id        友链id
	 * @param published 是否公开
	 * @return
	 */
	@OperationLogger("更新友链公开状态")
	@PutMapping("/friend/published")
	public Result updatePublished(@RequestParam Long id, @RequestParam Boolean published) {
		friendService.updateFriendPublishedById(id, published);
		return Result.ok("操作成功");
	}

	/**
	 * 添加友链
	 *
	 * @param friend 友链DTO
	 * @return
	 */
	@OperationLogger("添加友链")
	@PostMapping("/friend")
	public Result saveFriend(@RequestBody Friend friend) {
		friendService.saveFriend(friend);
		return Result.ok("添加成功");
	}

	/**
	 * 更新友链
	 *
	 * @param friend 友链DTO
	 * @return
	 */
	@OperationLogger("更新友链")
	@PutMapping("/friend")
	public Result updateFriend(@RequestBody top.naccl.model.dto.Friend friend) {
		friendService.updateFriend(friend);
		return Result.ok("修改成功");
	}

	/**
	 * 按id删除友链
	 *
	 * @param id
	 * @return
	 */
	@OperationLogger("删除友链")
	@DeleteMapping("/friend")
	public Result deleteFriend(@RequestParam Long id) {
		friendService.deleteFriend(id);
		return Result.ok("删除成功");
	}

	/**
	 * 获取友链页面信息
	 *
	 * @return
	 */
	@GetMapping("/friendInfo")
	public Result friendInfo() {
		return Result.ok("请求成功", friendService.getFriendInfo(false, false));
	}

	/**
	 * 修改友链页面评论开放状态
	 *
	 * @param commentEnabled 是否开放评论
	 * @return
	 */
	@OperationLogger("修改友链页面评论开放状态")
	@PutMapping("/friendInfo/commentEnabled")
	public Result updateFriendInfoCommentEnabled(@RequestParam Boolean commentEnabled) {
		friendService.updateFriendInfoCommentEnabled(commentEnabled);
		return Result.ok("修改成功");
	}

	/**
	 * 修改友链页面content
	 *
	 * @param map 包含content的JSON对象
	 * @return
	 */
	@OperationLogger("修改友链页面信息")
	@PutMapping("/friendInfo/content")
	public Result updateFriendInfoContent(@RequestBody Map map) {
		friendService.updateFriendInfoContent((String) map.get("content"));
		return Result.ok("修改成功");
	}
}
