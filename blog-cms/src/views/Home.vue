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
			<el-aside :width="isCollapse? '64px' : '200px'">
				<div class="toggle-button" @click="isCollapse=!isCollapse"><i :class="isCollapse?'el-icon-s-unfold':'el-icon-s-fold'"></i></div>
				<!--菜单-->
				<el-menu background-color="#333744" text-color="#fff" active-text-color="#409eff" :default-openeds="defaultOpeneds"
				         :unique-opened="false" :collapse="isCollapse" :collapse-transition="false"
				         :router="true" :default-active="activePath">
					<!-- 一级菜单 -->
					<el-submenu :index="item.id + ''" v-for="item in menuList" :key="item.id">
						<!-- 一级菜单的模板区域 -->
						<template slot="title">
							<i class="iconfont" :class="iconsObj[item.id]"></i>
							<span>{{ item.title }}</span>
						</template>
						<!-- 二级菜单 -->
						<el-menu-item :index="subItem.path" v-for="subItem in item.children" :key="subItem.id" @click="saveNavState(subItem.path)">
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
				<keep-alive>
					<router-view/>
				</keep-alive>
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
								path: '/write'
							},
							{
								id: 12,
								title: '文章列表',
								children: [],
								path: '/blogs'
							},
							{
								id: 13,
								title: '分类列表',
								children: [],
								path: '/types'
							},
							{
								id: 14,
								title: '标签列表',
								children: [],
								path: '/tags'
							}
						]
					},
					{
						id: 2,
						title: '留言管理',
						children: [
							{
								id: 21,
								title: '留言列表',
								children: [],
								path: '/comments'
							},
							{
								id: 22,
								title: '回收站',
								children: [],
								path: '/comments/trashes'
							}
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
							}
						]
					},
					{
						id: 4,
						title: '系统监控',
						children: [
							{
								id: 41,
								title: '日志',
								children: [],
								path: '/log'
							}
						]
					},
				],
				iconsObj: {
					'1': 'el-icon-menu',
					'2': 'el-icon-s-order',
					'3': 'el-icon-s-data',
					'4': 'el-icon-s-tools',
					'11': 'el-icon-edit',
					'12': 'el-icon-s-order',
					'13': 'el-icon-s-opportunity',
					'14': 'el-icon-discount',
					'21': 'el-icon-tickets',
					'22': 'el-icon-delete',
					'31': 'el-icon-s-marketing',
					'32': 'el-icon-view',
					'41': 'el-icon-document',
				},
				//是否折叠
				isCollapse: false,
				//被激活的链接地址
				activePath: '',
				//默认打开的菜单
				defaultOpeneds: ['1','2','3','4']
			}
		},
		created() {
			this.activePath = window.sessionStorage.getItem('activePath')
		},
		methods: {
			logout() {
				window.sessionStorage.clear()
				this.$router.push('/login')
				this.msgSuccess('退出成功')
			},
			//保存链接的激活状态
			saveNavState(activePath) {
				this.activePath = activePath
				window.sessionStorage.setItem('activePath', activePath)
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


	.el-main {
		background-color: #eaedf1;
	}

	.iconfont {
		margin-right: 20px;
		font-size: 20px;
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