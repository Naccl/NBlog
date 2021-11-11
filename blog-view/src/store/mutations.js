import {
	SAVE_SITE_INFO,
	SAVE_INTRODUCTION,
	SAVE_COMMENT_RESULT,
	SET_COMMENT_QUERY_PAGE_NUM,
	SET_PARENT_COMMENT_ID,
	RESET_COMMENT_FORM,
	RESTORE_COMMENT_FORM,
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
	[SAVE_SITE_INFO](state, siteInfo) {
		state.siteInfo = siteInfo
	},
	[SAVE_INTRODUCTION](state, introduction) {
		state.introduction = introduction
	},
	[SAVE_COMMENT_RESULT](state, data) {
		state.allComment = data.allComment
		state.closeComment = data.closeComment
		state.commentTotalPage = data.comments.totalPage
		state.comments = data.comments.list
	},
	[SET_COMMENT_QUERY_PAGE](state, page) {
		state.commentQuery.page = page
	},
	[SET_COMMENT_QUERY_BLOG_ID](state, blogId) {
		state.commentQuery.blogId = blogId
	},
	[SET_COMMENT_QUERY_PAGE_NUM](state, pageNum) {
		state.commentQuery.pageNum = pageNum
	},
	[SET_PARENT_COMMENT_ID](state, parentCommentId) {
		state.parentCommentId = parentCommentId
	},
	[RESET_COMMENT_FORM](state) {
		const commentForm = {
			nickname: state.commentForm.nickname,
			email: state.commentForm.email,
			website: state.commentForm.website
		}
		//保存访客信息，下次评论时自动填充表单
		window.localStorage.setItem('commentForm', JSON.stringify(commentForm))
		state.commentForm.content = ''
		state.commentForm.notice = true
	},
	[RESTORE_COMMENT_FORM](state) {
		const lastForm = JSON.parse(window.localStorage.getItem('commentForm'))
		if (lastForm) {
			state.commentForm.nickname = lastForm.nickname
			state.commentForm.email = lastForm.email
			state.commentForm.website = lastForm.website
		}
	},
	[SET_IS_BLOG_RENDER_COMPLETE](state, ok) {
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