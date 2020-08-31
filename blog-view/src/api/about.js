import axios from '@/plugins/axios'

export function getAbout() {
	return axios({
		url: 'about',
		method: 'GET'
	})
}