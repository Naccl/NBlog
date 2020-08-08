import axios from '@/plugins/axios'

export function getBlogList(pageNum) {
	return axios({
		url: 'blogs',
		method: 'GET',
		params: {
			pageNum
		}
	})
}