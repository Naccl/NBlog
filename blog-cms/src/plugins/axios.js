import axios from "axios";
import NProgress from 'nprogress'

const request = axios.create({
	baseURL: 'http://localhost:8090/admin/',
	timeout: 10000,
})

// 请求拦截
request.interceptors.request.use(
	config => {
		NProgress.start()
		const token = window.sessionStorage.getItem('token')
		if (token) {
			config.headers.Authorization = token
		}
		return config
	}
)

// 响应拦截
request.interceptors.response.use(
	config => {
		NProgress.done()
		return config.data
	}
)

export default request