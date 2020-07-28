package top.naccl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.naccl.entity.Blog;
import top.naccl.mapper.BlogMapper;

import java.util.List;

@SpringBootTest
class BlogApiApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	BlogMapper blogMapper;

	@Test
	void test() {
		List<Blog> blogs = blogMapper.getBlogList();
		System.out.println(blogs);
	}

}
