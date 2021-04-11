import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'
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
import SiteSetting from "@/views/page/SiteSetting";
import FriendList from "@/views/page/FriendList";
import About from "@/views/page/About";
import JobList from "@/views/system/ScheduleJobList";
import JobLog from "@/views/log/ScheduleJobLog";
import OperationLog from "@/views/log/OperationLog";
import LoginLog from "@/views/log/LoginLog";
import ExceptionLog from "@/views/log/ExceptionLog";
import VisitLog from "@/views/log/VisitLog";
import Visitor from "@/views/statistics/Visitor";
import {SAVE_NAV_STATE} from "@/store/mutations-types";

Vue.use(VueRouter)

const routes = [
	{
		path: '/',
		redirect: '/home',
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
				component: JobLog,
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
			},
			{
				path: '/visitor',
				component: Visitor,
				meta: {
					title: '访客统计'
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
		const tokenStr = window.localStorage.getItem('token')
		if (!tokenStr) return next("/login")
	}
	if (to.meta.title) {
		if (store.state.webTitleSuffix) {
			document.title = to.meta.title + store.state.webTitleSuffix
		} else {
			document.title = to.meta.title
		}
	}
	router.app.$options.store.commit(SAVE_NAV_STATE, to.path)
	next()
})

export default router
