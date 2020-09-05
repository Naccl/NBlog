package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.User;
import top.naccl.model.dto.LoginInfo;
import top.naccl.model.vo.Result;
import top.naccl.service.impl.UserServiceImpl;
import top.naccl.util.JwtUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 前台登录
 * @Author: Naccl
 * @Date: 2020-09-02
 */
@RestController
@RequestMapping
public class LoginController {
	@Autowired
	UserServiceImpl userService;

	/**
	 * 登录成功后，签发博主身份Token
	 *
	 * @param loginInfo
	 * @return
	 */
	@PostMapping("/login")
	public Result login(@RequestBody LoginInfo loginInfo) {
		User user = (User) userService.loadUserByUsername(loginInfo.getUsername());
		user.setPassword(null);
		String jwt = JwtUtils.generateToken("admin:" + user.getUsername());
		Map<String, Object> map = new HashMap<>();
		map.put("user", user);
		map.put("token", jwt);
		return Result.ok("登录成功", map);
	}
}
