import axios from '@/plugins/axios'

export function getTagList() {
	return axios({
		url: 'tags',
		method: 'GET'
	})
}

export function getBlogListByTagId(tagId, pageNum) {
	return axios({
		url: 'tag',
		method: 'GET',
		params: {
			tagId,
			pageNum
		}
	})
}