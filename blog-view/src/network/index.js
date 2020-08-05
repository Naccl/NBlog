import axios from '@/plugins/axios'

export function getHitokoto() {
	return axios({
		url: 'https://v1.hitokoto.cn/?c=a',
		method: 'GET'
	})
}