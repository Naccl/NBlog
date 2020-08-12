import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'
import Index from "@/views/Index";
import Home from "@/views/home/Home";
import Archives from "@/views/archives/Archives";
import Blog from "@/views/blog/Blog";

Vue.use(VueRouter)

const routes = [
	{
		path: '/',
		component: Index,
		redirect: '/home',
		children: [
			{
				path: '/home',
				component: Home,
				meta: {
					title: '首页'
				}
			},
			{
				path: '/archives',
				component: Archives,
				meta: {
					title: '归档'
				}
			},
			{
				path: '/blog',
				component: Blog,
				meta: {
					title: '博客'
				}
			}
		]
	}
]

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

//挂载路由守卫
router.beforeEach((to, from, next) => {
	if (to.meta.title) {
		if (store.state.webTitleSuffix !== '') {
			document.title = to.meta.title + store.state.webTitleSuffix
		} else {
			document.title = to.meta.title
		}
	}
	next()
})

export default router
