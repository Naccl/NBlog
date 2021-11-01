import axios from '@/util/request'

export function getAbout() {
	return axios({
		url: 'about',
		method: 'GET'
	})
}

export function updateAbout(form) {
	return axios({
		url: 'about',
		method: 'PUT',
		data: {
			...form
		}
	})
}