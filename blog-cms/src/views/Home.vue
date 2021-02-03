<template>
	<el-container class="home-container">
		<!--头部-->
		<el-header>
			<div class="title">
				<img src="~assets/img/logo.png" alt="" height="60">
				<span>NBlog 博客后台管理</span>
			</div>
			<el-dropdown trigger="click" class="user" v-if="user" @command="logout">
				<div class="el-dropdown-link">
					<el-avatar shape="circle" :size="45" fit="contain" :src="user.avatar"></el-avatar>
				</div>
				<el-dropdown-menu slot="dropdown">
					<el-dropdown-item icon="ali-iconfont icon-logout">退出</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</el-header>
		<!--页面主体-->
		<el-container>
			<!--侧边栏-->
			<el-aside :width="isCollapse? '64px' : '190px'">
				<div class="toggle-button" @click="isCollapse=!isCollapse"><i :class="isCollapse?'el-icon-s-unfold':'el-icon-s-fold'"></i></div>
				<!--菜单-->
				<el-menu background-color="#333744" text-color="#fff" active-text-color="#409eff" :default-openeds="defaultOpeneds"
				         :unique-opened="false" :collapse="isCollapse" :collapse-transition="false"
				         :router="true" :default-active="$store.state.activePath">
					<el-menu-item index="/dashboard">
						<i class="iconfont ali-iconfont icon-dashboard"></i>
						<span>仪表盘</span>
					</el-menu-item>
					<!-- 一级菜单 -->
					<el-submenu :index="item.id + ''" v-for="item in menuList" :key="item.id">
						<!-- 一级菜单的模板区域 -->
						<template slot="title">
							<i class="iconfont" :class="iconsObj[item.id]"></i>
							<span>{{ item.title }}</span>
						</template>
						<!-- 二级菜单 -->
						<el-menu-item :index="subItem.path" v-for="subItem in item.children" :key="subItem.id">
							<template slot="title">
								<i :class="iconsObj[subItem.id]"></i>
								<span>{{ subItem.title }}</span>
							</template>
						</el-menu-item>
					</el-submenu>
				</el-menu>
			</el-aside>
			<!--右侧内容主体-->
			<el-main :class="isCollapse?'m-el-main-width-64':'m-el-main-width-190'">
				<!--加 key 让组件被重用时 重新执行生命周期 否则在编辑文章页面路由到写文章页面时 数据被重用-->
				<router-view :key="$route.fullPath"/>
			</el-main>
		</el-container>
	</el-container>
</template>

<script>
	export default {
		name: "Home",
		data() {
			return {
				menuList: [
					{
						id: 1,
						title: '博客管理',
						children: [
							{
								id: 11,
								title: '写文章',
								path: '/blogs/write'
							},
							{
								id: 12,
								title: '写动态',
								path: '/moments/write'
							},
							{
								id: 13,
								title: '文章管理',
								path: '/blogs'
							},
							{
								id: 14,
								title: '动态管理',
								path: '/moments'
							},
							{
								id: 15,
								title: '分类管理',
								path: '/categories'
							},
							{
								id: 16,
								title: '标签管理',
								path: '/tags'
							},
							{
								id: 17,
								title: '评论管理',
								path: '/comments'
							}
						]
					},
					{
						id: 2,
						title: '页面管理',
						children: [
							{
								id: 21,
								title: '站点设置',
								path: '/siteSettings'
							},
							{
								id: 22,
								title: '友链管理',
								path: '/friends'
							},
							{
								id: 23,
								title: '关于我',
								path: '/about'
							}
						]
					},
					{
						id: 3,
						title: '系统管理',
						children: [
							{
								id: 31,
								title: '定时任务',
								path: '/jobs'
							},
						]
					},
					{
						id: 4,
						title: '日志管理',
						children: [
							{
								id: 41,
								title: '任务日志',
								path: '/jobs/logs'
							},
							{
								id: 42,
								title: '登录日志',
								path: '/loginLog'
							},
							{
								id: 43,
								title: '操作日志',
								path: '/operationLog'
							},
							{
								id: 44,
								title: '异常日志',
								path: '/exceptionLog'
							},
							{
								id: 45,
								title: '访问日志',
								path: '/visitLog'
							}
						]
					},
					{
						id: 5,
						title: '数据统计',
						children: [
							{
								id: 51,
								title: '访客统计',
								path: '/visitor'
							},
							{
								id: 52,
								title: '受访页面',
								path: '/visitPage'
							}
						]
					},
				],
				iconsObj: {
					'1': 'el-icon-menu',
					'2': 'el-icon-document-copy',
					'3': 'el-icon-s-tools',
					'4': 'el-icon-document',
					'5': 'el-icon-s-data',
					'11': 'el-icon-edit',
					'12': 'el-icon-edit',
					'13': 'el-icon-s-order',
					'14': 'el-icon-chat-dot-round',
					'15': 'el-icon-s-opportunity',
					'16': 'submenu ali-iconfont icon-biaoqian',
					'17': 'el-icon-s-comment',
					'21': 'submenu ali-iconfont icon-bianjizhandian',
					'22': 'submenu ali-iconfont icon-friend',
					'23': 'el-icon-tickets',
					'31': 'el-icon-alarm-clock',
					'41': 'el-icon-alarm-clock',
					'42': 'el-icon-finished',
					'43': 'el-icon-document-checked',
					'44': 'el-icon-document-delete',
					'45': 'el-icon-data-line',
					'51': 'el-icon-s-marketing',
					'52': 'el-icon-view',
				},
				//是否折叠
				isCollapse: false,
				//默认打开的菜单
				defaultOpeneds: ['1', '2', '3', '4', '5'],
				user: null,
			}
		},
		created() {
			this.getUserInfo()
		},
		methods: {
			getUserInfo() {
				this.user = JSON.parse(window.localStorage.getItem('user') || null)
				if (!this.user) {
					this.$router.push('/login')
				}
			},
			logout() {
				window.localStorage.clear()
				this.$router.push('/login')
				this.msgSuccess('退出成功')
			}
		}
	}
</script>

<style scoped>
	.el-aside {
		-ms-overflow-style: none; /* IE10 */
		scrollbar-width: none; /* Firefox */
	}

	.el-aside::-webkit-scrollbar {
		display: none;
	}

	.el-main::-webkit-scrollbar {
		width: 8px;
		height: 5px;
	}

	.el-main::-webkit-scrollbar-thumb {
		-webkit-box-shadow: inset 0 0 6px #48dbfb;
		box-shadow: inset 0 0 6px #48dbfb;
		background-color: #48dbfb;
	}

	.el-main::-webkit-scrollbar-track {
		-webkit-box-shadow: inset 0 0 6px transparent;
		box-shadow: inset 0 0 6px transparent;
		background-color: transparent;
	}

	.el-main::-webkit-scrollbar-track-piece {
		background-color: transparent;
	}

	.home-container {
		height: 100%;
	}

	.el-header {
		background-color: #373D41;
		display: flex;
		justify-content: space-between;
		padding-left: 10px;
		align-items: center;
		color: #ffffff;
		font-size: 22px;
		user-select: none;
	}

	.el-header div {
		display: flex;
		align-items: center;
	}

	.el-header .title span {
		margin-left: 15px;
	}

	.el-dropdown-link {
		outline-style: none !important;
		outline-color: unset !important;
		height: 100%;
		cursor: pointer;
	}

	.el-dropdown-menu {
		margin: 7px 0 0 0 !important;
		padding: 0 !important;
		border: 0 !important;
	}

	.el-aside {
		background-color: #333744;
		position: absolute;
		top: 60px;
		bottom: 0;
		user-select: none;
	}

	.el-aside .el-menu {
		border-right: none;
	}

	.el-submenu .el-menu-item {
		min-width: inherit;
	}

	.el-main {
		background-color: #ffffff;
		position: absolute;
		top: 60px;
		bottom: 0;
		right: 0;
		overflow-y: auto;
		overflow-x: hidden;
	}

	.m-el-main-width-190 {
		width: calc(100% - 190px);
	}

	.m-el-main-width-64 {
		width: calc(100% - 64px);
	}

	.iconfont {
		margin-right: 20px;
		font-size: 20px;
	}

	.submenu.ali-iconfont {
		vertical-align: middle;
		margin-right: 5px;
		width: 24px;
		text-align: center;
		display: inline-block;
	}

	.toggle-button {
		background-color: #4a5064;
		font-size: 20px;
		line-height: 40px;
		color: #ffffff;
		text-align: center;
		cursor: pointer;
	}
</style>