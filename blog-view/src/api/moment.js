import axios from '@/plugins/axios'

export function getMomentListByPageNum(pageNum) {
	return axios({
		url: 'moments',
		method: 'GET',
		params: {
			pageNum
		}
	})
}

export function likeMoment(id) {
	return axios({
		url: 'moment/like',
		method: 'PUT',
		params: {
			id
		}
	})
}