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

export function submitComment(form) {
	return axios({
		url: 'comment',
		method: 'POST',
		data: {
			...form
		}
	})
}