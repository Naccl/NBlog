import axios from '@/util/request'

export function getDashboard() {
	return axios({
		url: 'dashboard',
		method: 'GET'
	})
}