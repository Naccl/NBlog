export const letsLazyload = (content) => {
    return content.replace(/(<img )([^>]*)(src=")([^"]*")([^>]*)(\>)/g, (match, p1, p2, p3, p4, p5, p6) => p1 + p2 + 'data-src="' + p4 + p5 + p6)
}
