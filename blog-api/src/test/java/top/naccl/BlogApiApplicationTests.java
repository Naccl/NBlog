package top.naccl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.naccl.mapper.CommentMapper;

@SpringBootTest
class BlogApiApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	CommentMapper commentMapper;

	@Test
	void test() {
	}

}
