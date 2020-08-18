import axios from '@/plugins/axios'

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