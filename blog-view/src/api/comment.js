import axios from '@/plugins/axios'

export function getCommentListByQuery(token, query) {
	return axios({
		url: 'comments',
		method: 'GET',
		headers: {
			Authorization: token,
		},
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