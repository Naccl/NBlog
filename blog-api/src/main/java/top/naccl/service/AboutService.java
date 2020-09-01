package top.naccl.service;

import java.util.Map;

public interface AboutService {
	Map<String, String> getAboutInfo();

	Map<String, String> getAboutSetting();

	void updateAbout(String nameEn, String value);

	boolean getAboutCommentEnabled();
}
