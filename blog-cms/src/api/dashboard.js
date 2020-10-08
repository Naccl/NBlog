import axios from '@/plugins/axios'

export function getDashboard() {
	return axios({
		url: 'dashboard',
		method: 'GET'
	})
}