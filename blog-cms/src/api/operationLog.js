import axios from '@/util/request'

export function getOperationLogList(queryInfo) {
	return axios({
		url: 'operationLogs',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteOperationLogById(id) {
	return axios({
		url: 'operationLog',
		method: 'DELETE',
		params: {
			id
		}
	})
}