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
		url: 'blog',
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

export function updateRecommend(id, recommend) {
	return axios({
		url: 'blog/recommend',
		method: 'PUT',
		params: {
			id,
			recommend
		}
	})
}

export function updatePublished(id, published) {
	return axios({
		url: 'blog/published',
		method: 'PUT',
		params: {
			id,
			published
		}
	})
}