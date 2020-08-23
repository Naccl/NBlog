import axios from '@/plugins/axios'

export function getTagList() {
	return axios({
		url: 'tags',
		method: 'GET'
	})
}

export function getBlogListByTagName(tagName, pageNum) {
	return axios({
		url: 'tag',
		method: 'GET',
		params: {
			tagName,
			pageNum
		}
	})
}