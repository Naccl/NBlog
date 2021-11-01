import axios from '@/util/request'

export function getExceptionLogList(queryInfo) {
	return axios({
		url: 'exceptionLogs',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteExceptionLogById(id) {
	return axios({
		url: 'exceptionLog',
		method: 'DELETE',
		params: {
			id
		}
	})
}