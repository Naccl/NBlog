import axios from '@/plugins/axios'

export function getDataByQuery(query, CategoryId, pageNum, pageSize) {
	return axios({
		url: 'blogs',
		method: 'GET',
		params: {
			query,
			CategoryId,
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

export function getCategoryAndTag() {
	return axios({
		url: 'categoryAndTag',
		method: 'GET'
	})
}

export function saveBlog(blog) {
	return axios({
		url: 'blog',
		method: 'POST',
		data: {
			blog
		}
	})
}