import axios from '@/plugins/axios'

export function blogs(query, typeId, pageNum, pageSize) {
	return axios({
		url: 'blogs',
		method: 'GET',
		params: {
			query,
			typeId,
			pageNum,
			pageSize
		}
	})
}

export function deleteBlogById(id) {
	return axios({
		url: 'blogs',
		method: 'DELETE',
		params: {
			id
		}
	})
}
