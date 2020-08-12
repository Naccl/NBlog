import axios from '@/plugins/axios'

export function getBlogById(id) {
	return axios({
		url: 'blog',
		method: 'GET',
		params: {
			id
		}
	})
}