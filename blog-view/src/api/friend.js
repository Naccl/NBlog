import axios from '@/plugins/axios'

export function getData() {
	return axios({
		url: 'friends',
		method: 'GET'
	})
}

export function addViewsByNickname(nickname) {
	return axios({
		url: 'friend',
		method: 'POST',
		params: {
			nickname
		}
	})
}