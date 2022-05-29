import axios from 'axios'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {Message} from 'element-ui'

const request = axios.create({
	baseURL: 'https://v0.api.upyun.com',
	//对于较大的文件，可能需要更多的超时时间
	timeout: 30000,
})

// request interceptor
request.interceptors.request.use(config => {
		NProgress.start()
		config.headers['Accept'] = 'application/json'
		const token = localStorage.getItem('upyunToken')
		if (token) {
			config.headers['Authorization'] = `Basic ${token}`
		}
		return config
	},
	error => {
		console.info(error)
		return Promise.reject(error)
	}
)

// response interceptor
request.interceptors.response.use(response => {
		NProgress.done()
		return response.data
	},
	error => {
		console.info(error)
		Message.error(error.message)
		return Promise.reject(error)
	}
)

// 获取存储空间下指定目录的文件列表
export function getBucketContents(bucket, path) {
	path = path.startsWith('/') ? path : `/${path}`
	return request({
		url: `/${bucket}${path}`,
		method: 'GET',
	})
}

// 删除文件
export function delFile(bucket, path) {
	return request({
		url: `/${bucket}${path}`,
		method: 'DELETE'
	})
}

// 上传文件至仓库指定目录下
export function upload(bucket, path, fileName, data) {
	path = path.startsWith('/') ? path : `/${path}`
	path = path.endsWith('/') ? path : `${path}/`
	return request({
		url: `/${bucket}${path}${fileName}`,
		method: 'PUT',
		data
	})
}