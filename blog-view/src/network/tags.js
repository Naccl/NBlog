import axios from '@/plugins/axios'

export function getTags() {
	return axios({
		url: 'tags',
		method: 'GET'
	})
}