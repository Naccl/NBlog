import axios from '@/plugins/axios'

export function getArchives() {
	return axios({
		url: 'archives',
		method: 'GET'
	})
}