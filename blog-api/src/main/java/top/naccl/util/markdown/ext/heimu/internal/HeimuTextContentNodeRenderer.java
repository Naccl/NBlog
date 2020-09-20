package top.naccl.util.markdown.ext.heimu.internal;

import org.commonmark.node.Node;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;

/**
 * @Description: 文本节点渲染
 * @Author: Naccl
 * @Date: 2020-05-13
 */
public class HeimuTextContentNodeRenderer extends AbstractHeimuNodeRenderer {
    private final TextContentNodeRendererContext context;
    private final TextContentWriter textContent;

    public HeimuTextContentNodeRenderer(TextContentNodeRendererContext context) {
        this.context = context;
        this.textContent = context.getWriter();
    }

    @Override
    public void render(Node node) {
        textContent.write('/');
        renderChildren(node);
        textContent.write('/');
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
