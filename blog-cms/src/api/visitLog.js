import axios from '@/util/request'

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

export function exportVisitLog(queryInfo) {
	return axios({
		url: 'visitLog/export',
		method: 'GET',
		params: {
			...queryInfo
		},
		responseType: 'blob'
	})
}