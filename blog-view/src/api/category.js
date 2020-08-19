import axios from '@/plugins/axios'

export function getCategoryList() {
	return axios({
		url: 'categories',
		method: 'GET'
	})
}

export function getBlogListByCategoryId(categoryId, pageNum) {
	return axios({
		url: 'category',
		method: 'GET',
		params: {
			categoryId,
			pageNum
		}
	})
}