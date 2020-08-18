import {
	SAVE_SITE_INFO,
	SAVE_INTRODUCTION,
	SAVE_COMMENT_RESULT,
	SET_COMMENT_QUERY_PAGE_NUM,
	SET_PARENT_COMMENT_ID,
	SET_COMMENT_FORM_EMPTY,
	SET_COMMENT_QUERY_PAGE,
	SET_COMMENT_QUERY_BLOG_ID
} from "./mutations-types";

import {getCommentListByQuery, submitComment} from "@/network/comment";
import {Message, Notification} from "element-ui";

export default {
	saveSiteInfo({commit}, siteInfo) {
		commit(SAVE_SITE_INFO, {siteInfo})
	},
	saveIntroduction({commit}, introduction) {
		commit(SAVE_INTRODUCTION, {introduction})
	},
	getCommentList({commit, rootState}) {
		getCommentListByQuery(rootState.commentQuery).then(res => {
			console.log(res)
			if (res.code === 200) {
				commit(SAVE_COMMENT_RESULT, res.data)
			} else {
				Message.error(res.msg)
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
	submitCommentForm({rootState, dispatch}) {
		let form = {...rootState.commentForm}
		form.page = rootState.commentQuery.page
		form.blogId = rootState.commentQuery.blogId
		form.parentCommentId = rootState.parentCommentId
		submitComment(form).then(res => {
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
	}
}