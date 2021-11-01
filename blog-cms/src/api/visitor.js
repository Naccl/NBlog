import axios from '@/util/request'

export function getVisitorList(queryInfo) {
	return axios({
		url: 'visitors',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteVisitor(id, uuid) {
	return axios({
		url: 'visitor',
		method: 'DELETE',
		params: {
			id,
			uuid
		}
	})
}