import axios from '@/plugins/axios'

export function getMomentListByQuery(queryInfo) {
	return axios({
		url: 'moments',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function updatePublished(id, published) {
	return axios({
		url: 'moment/published',
		method: 'PUT',
		params: {
			id,
			published
		}
	})
}