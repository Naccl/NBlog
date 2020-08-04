import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login";
import Home from "@/views/Home";
import Dashboard from "@/views/Dashboard";
import WriteBlog from "@/views/blog/WriteBlog";
import BlogList from "@/views/blog/BlogList";
import CategoryList from "@/views/category/CategoryList";
import TagList from "@/views/tag/TagList";
import CommentList from "@/views/comment/CommentList";

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
	{
		path: '/home',
		component: Home,
		redirect: '/dashboard',
		children: [
			{
				path: '/dashboard',
				component: Dashboard,
				meta: {
					title: '仪表盘'
				}
			},
			{
				path: '/blogs/write',
				component: WriteBlog,
				meta: {
					title: '写文章'
				}
			},
			{
				path: '/blogs/edit/:id',
				component: WriteBlog,
				meta: {
					title: '编辑文章'
				}
			},
			{
				path: '/blogs',
				component: BlogList,
				meta: {
					title: '文章列表'
				}
			},
			{
				path: '/categories',
				component: CategoryList,
				meta: {
					title: '分类列表'
				}
			},
			{
				path: '/tags',
				component: TagList,
				meta: {
					title: '标签列表'
				}
			},
			{
				path: '/comments',
				component: CommentList,
				meta: {
					title: '评论列表'
				}
			},
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
	if (to.path !== '/login') {
		//获取token
		const tokenStr = window.sessionStorage.getItem('token')
		if (!tokenStr) return next("/login")
	}
	if (to.meta.title) {
		document.title = to.meta.title + ' | Naccl\'Blog'
	}
	router.app.$options.store.dispatch('saveNavState', to.path)
	next()
})

export default router
