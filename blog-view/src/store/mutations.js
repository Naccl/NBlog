import {
	SAVE_SITE_INFO,
	SAVE_INTRODUCTION,
	SAVE_COMMENT_RESULT,
	SET_COMMENT_QUERY_PAGE_NUM,
	SET_PARENT_COMMENT_ID,
	SET_COMMENT_FORM_EMPTY,
	SET_COMMENT_QUERY_PAGE,
	SET_COMMENT_QUERY_BLOG_ID,
	SET_IS_BLOG_RENDER_COMPLETE,
	SET_BLOG_PASSWORD_DIALOG_VISIBLE,
	SET_BLOG_PASSWORD_FORM,
	SET_FOCUS_MODE,
	SET_IS_BLOG_TO_HOME,
	SAVE_CLIENT_SIZE,
} from "./mutations-types";

export default {
	[SAVE_SITE_INFO](state, {siteInfo}) {
		state.siteInfo = siteInfo
	},
	[SAVE_INTRODUCTION](state, {introduction}) {
		state.introduction = introduction
	},
	[SAVE_COMMENT_RESULT](state, data) {
		state.commentCount = data.count
		state.commentTotalPage = data.comments.totalPage
		state.comments = data.comments.list
	},
	[SET_COMMENT_QUERY_PAGE](state, {page}) {
		state.commentQuery.page = page
	},
	[SET_COMMENT_QUERY_BLOG_ID](state, {blogId}) {
		state.commentQuery.blogId = blogId
	},
	[SET_COMMENT_QUERY_PAGE_NUM](state, {pageNum}) {
		state.commentQuery.pageNum = pageNum
	},
	[SET_PARENT_COMMENT_ID](state, {parentCommentId}) {
		state.parentCommentId = parentCommentId
	},
	[SET_COMMENT_FORM_EMPTY](state) {
		state.commentForm = {
			content: '',
			nickname: '',
			email: '',
			website: '',
			notice: true
		}
	},
	[SET_IS_BLOG_RENDER_COMPLETE](state, {ok}) {
		state.isBlogRenderComplete = ok
	},
	[SET_BLOG_PASSWORD_DIALOG_VISIBLE](state, visible) {
		state.blogPasswordDialogVisible = visible
	},
	[SET_BLOG_PASSWORD_FORM](state, form) {
		state.blogPasswordForm = form
	},
	[SET_FOCUS_MODE](state, focusMode) {
		state.focusMode = focusMode
	},
	[SET_IS_BLOG_TO_HOME](state, isBlogToHome) {
		state.isBlogToHome = isBlogToHome
	},
	[SAVE_CLIENT_SIZE](state, clientSize) {
		state.clientSize = clientSize
	},
}