import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'
import Index from "@/views/Index";
import Home from "@/views/home/Home";
import Archives from "@/views/archives/Archives";
import Blog from "@/views/blog/Blog";
import Tag from "@/views/tag/Tag";
import Category from "@/views/category/Category";

Vue.use(VueRouter)

const routes = [
	{
		path: '/',
		component: Index,
		redirect: '/home',
		children: [
			{
				path: '/home',
				name: 'home',
				component: Home,
				meta: {
					title: '首页'
				}
			},
			{
				path: '/archives',
				name: 'archives',
				component: Archives,
				meta: {
					title: '归档'
				}
			},
			{
				path: '/blog/:id',
				name: 'blog',
				component: Blog,
				meta: {
					title: '博客'
				}
			},
			{
				path: '/tag/:id',
				name: 'tag',
				component: Tag,
				meta: {
					title: '标签'
				}
			},
			{
				path: '/category/:id',
				name: 'category',
				component: Category,
				meta: {
					title: '分类'
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
		if (store.state.siteInfo.webTitleSuffix) {
			document.title = to.meta.title + store.state.siteInfo.webTitleSuffix
		} else {
			document.title = to.meta.title
		}
	}
	next()
})

export default router
