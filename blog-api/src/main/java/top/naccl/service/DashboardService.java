package top.naccl.service;

import java.util.List;
import java.util.Map;

public interface DashboardService {
	int getBlogCount();

	int getCommentCount();

	Map<String, List> getCategoryBlogCountList();
}
