import axios from '@/plugins/axios'

export function getCategoryList() {
	return axios({
		url: 'categories',
		method: 'GET'
	})
}