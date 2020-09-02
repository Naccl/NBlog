import axios from '@/plugins/axios'

export function getCommentListByQuery(query) {
	return axios({
		url: 'comments',
		method: 'GET',
		params: {
			...query
		}
	})
}

export function submitComment(token, form) {
	return axios({
		url: 'comment',
		method: 'POST',
		headers: {
			Authorization: token,
		},
		data: {
			...form
		}
	})
}