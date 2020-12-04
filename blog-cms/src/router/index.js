import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login";
import Home from "@/views/Home";
import Dashboard from "@/views/dashboard/Dashboard";
import WriteBlog from "@/views/blog/blog/WriteBlog";
import BlogList from "@/views/blog/blog/BlogList";
import CategoryList from "@/views/blog/category/CategoryList";
import TagList from "@/views/blog/tag/TagList";
import CommentList from "@/views/blog/comment/CommentList";
import MomentList from "@/views/blog/moment/MomentList";
import WriteMoment from "@/views/blog/moment/WriteMoment";
import SiteSetting from "@/views/page/site/SiteSetting";
import FriendList from "@/views/page/friend/FriendList";
import About from "@/views/page/about/About";
import JobList from "@/views/system/schedule/JobList";
import JobLogList from "@/views/system/schedule/JobLogList";
import OperationLog from "@/views/system/operationLog/OperationLog";
import LoginLog from "@/views/system/loginLog/LoginLog";
import ExceptionLog from "@/views/system/ExceptionLog/ExceptionLog";
import VisitLog from "@/views/system/visitLog/VisitLog";

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
				path: '/moments/write',
				component: WriteMoment,
				meta: {
					title: '写动态'
				}
			},
			{
				path: '/moments/edit/:id',
				component: WriteMoment,
				meta: {
					title: '编辑动态'
				}
			},
			{
				path: '/blogs',
				component: BlogList,
				meta: {
					title: '文章管理'
				}
			},
			{
				path: '/moments',
				component: MomentList,
				meta: {
					title: '动态管理'
				}
			},
			{
				path: '/categories',
				component: CategoryList,
				meta: {
					title: '分类管理'
				}
			},
			{
				path: '/tags',
				component: TagList,
				meta: {
					title: '标签管理'
				}
			},
			{
				path: '/comments',
				component: CommentList,
				meta: {
					title: '评论管理'
				}
			},
			{
				path: '/siteSettings',
				component: SiteSetting,
				meta: {
					title: '站点管理'
				}
			},
			{
				path: '/about',
				component: About,
				meta: {
					title: '关于我'
				}
			},
			{
				path: '/friends',
				component: FriendList,
				meta: {
					title: '友链管理'
				}
			},
			{
				path: '/jobs',
				component: JobList,
				meta: {
					title: '定时任务'
				}
			},
			{
				path: '/jobs/logs',
				component: JobLogList,
				meta: {
					title: '任务日志'
				}
			},
			{
				path: '/operationLog',
				component: OperationLog,
				meta: {
					title: '操作日志'
				}
			},
			{
				path: '/loginLog',
				component: LoginLog,
				meta: {
					title: '登录日志'
				}
			},
			{
				path: '/exceptionLog',
				component: ExceptionLog,
				meta: {
					title: '异常日志'
				}
			},
			{
				path: '/visitLog',
				component: VisitLog,
				meta: {
					title: '访问日志'
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
	if (to.path !== '/login') {
		//获取token
		const tokenStr = window.sessionStorage.getItem('token')
		if (!tokenStr) return next("/login")
	}
	if (to.meta.title) {
		document.title = to.meta.title + ' - Naccl\'s Blog'
	}
	router.app.$options.store.dispatch('saveNavState', to.path)
	next()
})

export default router
