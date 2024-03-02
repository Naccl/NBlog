import axios from '@/util/request'

export function changeAccount(account) {
	return axios({
		url: 'account',
		method: 'POST',
		data: {
			...account
		}
	})
}
