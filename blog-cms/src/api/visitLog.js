import axios from '@/plugins/axios'

export function getVisitLogList(queryInfo) {
	return axios({
		url: 'visitLogs',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteVisitLogById(id) {
	return axios({
		url: 'visitLog',
		method: 'DELETE',
		params: {
			id
		}
	})
}