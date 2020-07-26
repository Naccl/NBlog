package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.util.Result;
import top.naccl.service.UserServiceImpl;

/**
 * @Description: 用户登录控制器
 * @Author: Naccl
 * @Date: 2020-07-19
 */
@RestController
@RequestMapping("/admin")
public class LoginController {
	@Autowired
	UserServiceImpl userService;

	@GetMapping
	public Result index() {
		Result result = Result.ok("成功",Result.ok("22",Result.ok("22",null)));
		return result;
	}
}
