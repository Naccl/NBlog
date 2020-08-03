import axios from '@/plugins/axios'

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