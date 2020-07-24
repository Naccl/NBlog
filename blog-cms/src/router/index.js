import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login";

Vue.use(VueRouter)

const routes = [
	{
		path: '/',
		redirect: '/login',
	},
	{
		path: '/login',
		component: Login,
		meta: {
			title: '后台管理登录'
		}
	},
]

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

//挂载路由守卫
router.beforeEach((to, from, next) => {
	if (to.path !== '/login'){
		//获取token
		// const tokenStr = window.sessionStorage.getItem('token')
		// if (!tokenStr) return next("/login")
	}
	if (to.meta.title){
		document.title = to.meta.title
	}
	next()
})

export default router
