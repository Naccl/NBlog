import axios from '@/plugins/axios'

export function getHitokoto() {
	return axios({
		url: 'https://v1.hitokoto.cn/?c=a',
		method: 'GET'
	})
}

export function getSite() {
	return axios({
		url: 'site',
		method: 'GET'
	})
}

export function getRandomBlogs() {
	return axios({
		url: 'randomBlogs',
		method: 'GET'
	})
}

export function getTags() {
	return axios({
		url: 'tags',
		method: 'GET'
	})
}