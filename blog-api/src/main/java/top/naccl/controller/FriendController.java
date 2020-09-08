package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.Result;
import top.naccl.service.FriendService;

/**
 * @Description: 友链
 * @Author: Naccl
 * @Date: 2020-09-08
 */
@RestController
public class FriendController {
	@Autowired
	FriendService friendService;

	/**
	 * 获取友链列表
	 *
	 * @return
	 */
	@GetMapping("/friends")
	public Result friends() {
		return Result.ok("获取成功", friendService.getFriendVOList());
	}

	/**
	 * 按昵称增加友链浏览次数
	 *
	 * @param nickname 友链昵称
	 * @return
	 */
	@PostMapping("/friend")
	public Result addViews(@RequestParam String nickname) {
		friendService.updateViewsByNickname(nickname);
		return Result.ok("请求成功");
	}
}
