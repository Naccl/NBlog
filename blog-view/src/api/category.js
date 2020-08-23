import axios from '@/plugins/axios'

export function getCategoryList() {
	return axios({
		url: 'categories',
		method: 'GET'
	})
}

export function getBlogListByCategoryName(categoryName, pageNum) {
	return axios({
		url: 'category',
		method: 'GET',
		params: {
			categoryName,
			pageNum
		}
	})
}