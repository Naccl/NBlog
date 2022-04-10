package top.naccl.util.markdown.ext.cover.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: HTML节点渲染
 * @Author: Naccl
 * @Date: 2020-05-13
 */
public class CoverHtmlNodeRenderer extends AbstractCoverNodeRenderer {
    private final HtmlNodeRendererContext context;
    private final HtmlWriter html;

    public CoverHtmlNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public void render(Node node) {
        Map<String, String> attributes = new HashMap<>(2);
        attributes.put("class", "m-text-cover");
        html.tag("span", context.extendAttributes(node, "span", attributes));
        renderChildren(node);
        html.tag("/span");
    }

    private void renderChildren(Node parent) {
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            context.render(node);
            node = next;
        }
    }
}
