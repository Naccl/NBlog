import axios from '@/plugins/axios'

export function getLoginLogList(queryInfo) {
	return axios({
		url: 'loginLogs',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteLoginLogById(id) {
	return axios({
		url: 'loginLog',
		method: 'DELETE',
		params: {
			id
		}
	})
}