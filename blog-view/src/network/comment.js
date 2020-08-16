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