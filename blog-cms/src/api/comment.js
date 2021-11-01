import axios from '@/util/request'

export function getCommentListByQuery(queryInfo) {
	return axios({
		url: 'comments',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function getBlogList() {
	return axios({
		url: 'blogIdAndTitle',
		method: 'GET'
	})
}

export function updatePublished(id, published) {
	return axios({
		url: 'comment/published',
		method: 'PUT',
		params: {
			id,
			published
		}
	})
}

export function updateNotice(id, notice) {
	return axios({
		url: 'comment/notice',
		method: 'PUT',
		params: {
			id,
			notice
		}
	})
}

export function deleteCommentById(id) {
	return axios({
		url: 'comment',
		method: 'DELETE',
		params: {
			id
		}
	})
}

export function editComment(form) {
	return axios({
		url: 'comment',
		method: 'PUT',
		data: {
			...form
		}
	})
}