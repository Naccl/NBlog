<template>
	<el-container class="home-container">
		<!--头部-->
		<el-header>
			<div>
				<img src="~assets/img/logo.png" alt="" height="60">
				<span>博客后台管理系统</span>
			</div>
			<el-button type="info" @click="logout">退出</el-button>
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
			<el-main>
				<!--加 key 让组件被重用时 重新执行生命周期-->
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
								children: [],
								path: '/blogs/write'
							},
							{
								id: 12,
								title: '文章管理',
								children: [],
								path: '/blogs'
							},
							{
								id: 13,
								title: '分类管理',
								children: [],
								path: '/categories'
							},
							{
								id: 14,
								title: '标签管理',
								children: [],
								path: '/tags'
							},
							{
								id: 15,
								title: '评论管理',
								children: [],
								path: '/comments'
							}
						]
					},
					{
						id: 2,
						title: '系统管理',
						children: [
							{
								id: 21,
								title: '菜单管理',
								children: [],
								path: '/menus'
							},
							{
								id: 22,
								title: '站点管理',
								children: [],
								path: '/siteSettings'
							},
						]
					},
					{
						id: 3,
						title: '数据统计',
						children: [
							{
								id: 31,
								title: '访问量',
								children: [],
								path: '/pv'
							},
							{
								id: 32,
								title: '实时访客',
								children: [],
								path: '/latest'
							},
							{
								id: 33,
								title: '日志',
								children: [],
								path: '/log'
							}
						]
					},
				],
				iconsObj: {
					'1': 'el-icon-menu',
					'2': 'el-icon-s-tools',
					'3': 'el-icon-s-data',
					'11': 'el-icon-edit',
					'12': 'el-icon-s-order',
					'13': 'el-icon-s-opportunity',
					'14': 'submenu ali-iconfont icon-biaoqian',
					'15': 'submenu ali-iconfont icon-pinglun',
					'21': 'submenu ali-iconfont icon-caidanguanli',
					'22': 'submenu ali-iconfont icon-bianjizhandian',
					'31': 'el-icon-s-marketing',
					'32': 'el-icon-view',
					'33': 'el-icon-document',
				},
				//是否折叠
				isCollapse: false,
				//默认打开的菜单
				defaultOpeneds: ['1', '2', '3']
			}
		},
		methods: {
			logout() {
				window.sessionStorage.clear()
				this.$router.push('/login')
				this.msgSuccess('退出成功')
			}
		}
	}
</script>

<style scoped>
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
	}

	.el-header div {
		display: flex;
		align-items: center;
	}

	.el-header div span {
		margin-left: 15px;
	}

	.el-aside {
		background-color: #333744;
	}

	.el-aside .el-menu {
		border-right: none;
	}

	.el-submenu .el-menu-item {
		min-width: inherit;
	}

	.el-main {
		background-color: #eaedf1;
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