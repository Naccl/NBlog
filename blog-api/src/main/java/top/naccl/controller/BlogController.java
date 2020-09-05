package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.dto.BlogPassword;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.RandomBlog;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 博客相关
 * @Author: Naccl
 * @Date: 2020-08-12
 */
@RestController
@RequestMapping
public class BlogController {
	@Autowired
	BlogService blogService;

	/**
	 * 按置顶、创建时间排序 分页查询博客简要信息列表
	 *
	 * @param pageNum 页码
	 * @return
	 */
	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "1") Integer pageNum) {
		int pageSize = 5;//每页显示5条
		String orderBy = "is_top desc, create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<BlogInfo> pageInfo = new PageInfo<>(blogService.getBlogInfoListByIsPublished());
		PageResult<BlogInfo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		return Result.ok("请求成功", pageResult);
	}

	/**
	 * 按id获取公开博客详情
	 *
	 * @param id      博客id
	 * @param request 用于获取密码保护文章的访问Token
	 * @return
	 */
	@GetMapping("/blog")
	public Result getBlog(@RequestParam Long id, HttpServletRequest request) {
		BlogDetail blog = blogService.getBlogByIdAndIsPublished(id);
		//对密码保护的文章校验Token
		if (!"".equals(blog.getPassword())) {
			String jwtToken = request.getHeader("Authorization");
			if (jwtToken != null && !"".equals(jwtToken) && !"null".equals(jwtToken)) {
				try {
					//获取Token中博客id
					String tokenBlogIdString = JwtUtils.validateToken(jwtToken);
					Long tokenBlogId = Long.parseLong(tokenBlogIdString);
					//博客id不匹配，验证不通过，可能博客id改变或客户端传递了其它密码保护文章的Token
					if (tokenBlogId != id) {
						return Result.create(403, "Token不匹配，请重新验证密码！");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return Result.create(403, "Token已失效，请重新验证密码！");
				}
			} else {
				return Result.create(403, "此文章受密码保护，请验证密码！");
			}
			blog.setPassword("");
		}
		blogService.updateViews(id);
		return Result.ok("获取成功", blog);
	}

	/**
	 * 校验受保护文章密码是否正确，正确则返回jwt
	 *
	 * @param blogPassword 博客id、密码
	 * @return
	 */
	@PostMapping("/checkBlogPassword")
	public Result checkBlogPassword(@RequestBody BlogPassword blogPassword) {
		String password = blogService.getBlogPassword(blogPassword.getBlogId());
		if (password.equals(blogPassword.getPassword())) {
			//生成有效时间一个月的Token
			String jwt = JwtUtils.generateToken(blogPassword.getBlogId().toString(), 1000 * 3600 * 24 * 30L);
			return Result.ok("密码正确", jwt);
		} else {
			return Result.create(403, "密码错误");
		}
	}

	/**
	 * 获取随机公开文章
	 *
	 * @return
	 */
	@GetMapping("/randomBlogs")
	public Result randomBlogs() {
		int limitNum = 5;
		List<RandomBlog> randomBlogs = blogService.getRandomBlogListByLimitNumAndIsPublished(limitNum);
		return Result.ok("获取成功", randomBlogs);
	}
}
