package top.naccl.util.markdown;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import top.naccl.util.markdown.ext.heimu.HeimuExtension;
import top.naccl.util.markdown.ext.cover.CoverExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: Markdown转换
 * @Author: Naccl
 * @Date: 2020-04-29
 */
public class MarkdownUtils {
	/**
	 * markdown格式转换成HTML格式
	 */
	public static String markdownToHtml(String markdown) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		return renderer.render(document);
	}

	/**
	 * 增加扩展
	 */
	public static String markdownToHtmlExtensions(String markdown) {
		//为h标签生成id 供tocbot目录生成
		Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
		//转换table的HTML
		List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
		//任务列表
		Set<Extension> taskListExtension = Collections.singleton(TaskListItemsExtension.create());
		//删除线
		Set<Extension> delExtension = Collections.singleton(StrikethroughExtension.create());
		//黑幕
		Set<Extension> heimuExtension = Collections.singleton(HeimuExtension.create());
		//遮盖层
		Set<Extension> coverExtension = Collections.singleton(CoverExtension.create());
		Parser parser = Parser.builder()
				.extensions(tableExtension)
				.extensions(taskListExtension)
				.extensions(delExtension)
				.extensions(heimuExtension)
				.extensions(coverExtension)
				.build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer = HtmlRenderer.builder()
				.extensions(headingAnchorExtensions)
				.extensions(tableExtension)
				.extensions(taskListExtension)
				.extensions(delExtension)
				.extensions(heimuExtension)
				.extensions(coverExtension)
				.attributeProviderFactory(new AttributeProviderFactory() {
					public AttributeProvider create(AttributeProviderContext context) {
						return new CustomAttributeProvider();
					}
				})
				.build();
		return renderer.render(document);
	}

	/**
	 * 处理标签的属性
	 */
	static class CustomAttributeProvider implements AttributeProvider {
		@Override
		public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
			//改变a标签的target属性为_blank
			if (node instanceof Link) {
				attributes.put("target", "_blank");
				attributes.put("rel", "external nofollow noopener");
			}
			if (node instanceof TableBlock) {
				attributes.put("class", "ui celled table");//针对 semantic-ui 的class属性
			}
		}
	}


	public static void main(String[] args) {
		System.out.println(markdownToHtmlExtensions(""));
	}
}
