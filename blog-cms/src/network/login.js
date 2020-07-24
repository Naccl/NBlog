import axios from '@/plugins/axios'

export function login(username, password) {
	return axios({
		url: 'login',
		method: 'POST',
		data: {
			username,
			password
		}
	})
}
