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
	SET_BLOG_PASSWORD_FORM
} from "./mutations-types";

import {getCommentListByQuery, submitComment} from "@/api/comment";
import {Message, Notification} from "element-ui";
import router from "../router";

export default {
	saveSiteInfo({commit}, siteInfo) {
		commit(SAVE_SITE_INFO, {siteInfo})
	},
	saveIntroduction({commit}, introduction) {
		commit(SAVE_INTRODUCTION, {introduction})
	},
	getCommentList({commit, rootState}) {
		let token = window.localStorage.getItem(`blog${rootState.commentQuery.blogId}`)
		token = token ? token : ''
		getCommentListByQuery(token, rootState.commentQuery).then(res => {
			console.log(res)
			if (res.code === 200) {
				commit(SAVE_COMMENT_RESULT, res.data)
			}
		}).catch(() => {
			Message.error("请求失败")
		})
	},
	setCommentQueryPage({commit}, page) {
		commit(SET_COMMENT_QUERY_PAGE, {page})
	},
	setCommentQueryBlogId({commit}, blogId) {
		commit(SET_COMMENT_QUERY_BLOG_ID, {blogId})
	},
	setCommentQueryPageNum({commit}, pageNum) {
		commit(SET_COMMENT_QUERY_PAGE_NUM, {pageNum})
	},
	setParentCommentId({commit}, parentCommentId) {
		commit(SET_PARENT_COMMENT_ID, {parentCommentId})
	},
	setCommentFormEmpty({commit}) {
		commit(SET_COMMENT_FORM_EMPTY)
	},
	submitCommentForm({rootState, dispatch}, token = '') {
		let form = {...rootState.commentForm}
		form.page = rootState.commentQuery.page
		form.blogId = rootState.commentQuery.blogId
		form.parentCommentId = rootState.parentCommentId
		submitComment(token, form).then(res => {
			console.log(res)
			if (res.code === 200) {
				Notification({
					title: res.msg,
					type: 'success'
				})
				dispatch('setParentCommentId', -1)
				dispatch('setCommentFormEmpty')
				dispatch('getCommentList')
			} else {
				Notification({
					title: '评论失败',
					message: res.msg,
					type: 'error'
				})
			}
		}).catch(() => {
			Notification({
				title: '评论失败',
				message: '异常错误',
				type: 'error'
			})
		})
	},
	setIsBlogRenderComplete({commit}, ok) {
		commit(SET_IS_BLOG_RENDER_COMPLETE, {ok})
	},
	goBlogPage({commit}, blog) {
		if (blog.privacy) {
			const token = window.localStorage.getItem(`blog${blog.id}`)
			if (token) {
				return router.push(`/blog/${blog.id}`)
			}
			commit(SET_BLOG_PASSWORD_FORM, {blogId: blog.id, password: ''})
			commit(SET_BLOG_PASSWORD_DIALOG_VISIBLE, true)
		} else {
			router.push(`/blog/${blog.id}`)
		}
	},
}