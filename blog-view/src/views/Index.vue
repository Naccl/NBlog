<template>
	<div class="site">
		<!--顶部导航-->
		<Nav :blogName="blogName"/>

		<div class="main">

		</div>

		<!--底部footer-->
		<Footer :footer="footer"/>
	</div>
</template>

<script>
	import Nav from "../components/index/Nav";
	import Footer from "../components/index/Footer";
	import {getHitokoto} from '@/network/index'

	export default {
		name: "Home",
		components: {Nav, Footer},
		data() {
			return {
				blogName: 'Naccl\'s Blog',
				footer: {
					mobileQRSrc: require('@/assets/img/logo.png'),
					newBlogList: [
						{
							href: '/',
							title: '最新博客文章1'
						},
						{
							href: '/',
							title: '最新博客文章2'
						},
						{
							href: '/',
							title: '最新博客文章3'
						},
					],
					hitokoto: {},
					copyright: {
						time: 'Copyright © 2019 - 2020',
						title: 'NACCL\'S BLOG',
						beianId: '闽ICP备20005222号'
					},
					badges: [
						{
							href: 'https://spring.io/projects/spring-boot/',
							title: '由 Spring Boot 强力驱动',
							subject: 'Powered',
							value: 'Spring Boot',
							color: 'blue',
						},
						{
							href: 'https://cn.vuejs.org/',
							title: 'Vue.js 客户端渲染',
							subject: 'SPA',
							value: 'Vue.js',
							color: 'brightgreen',
						},
						{
							href: 'https://semantic-ui.com/',
							title: 'UI 框架 Semantic-UI',
							subject: 'UI',
							value: 'Semantic-UI',
							color: 'semantic-ui',
						},
						{
							href: 'https://www.aliyun.com/',
							title: '阿里云提供服务器及域名相关服务',
							subject: 'VPS & DNS',
							value: 'Aliyun',
							color: 'blueviolet',
						},
						{
							href: 'https://www.jsdelivr.com/',
							title: 'jsDelivr 提供 CDN 加速服务',
							subject: 'CDN',
							value: 'jsDelivr',
							color: 'orange',
						},
						{
							href: 'https://github.com/',
							title: 'GitHub 提供图床',
							subject: 'OSS',
							value: 'GitHub',
							color: 'github',
						},
						{
							href: 'https://creativecommons.org/licenses/by/4.0/',
							title: '本站点采用 CC BY 4.0 国际许可协议进行许可',
							subject: 'CC',
							value: 'BY 4.0',
							color: 'lightgray',
						}
					]
				}
			}
		},
		created() {
			this.getHitokoto()
		},
		methods: {
			//获取一言
			getHitokoto() {
				getHitokoto().then(res => {
					this.footer.hitokoto = res
				})
			}
		}
	}
</script>

<style scoped>
	.site {
		display: flex;
		min-height: 100vh;/* 没有元素时，也把页面撑开至100% */
		flex-direction: column;
	}

	.main {
		opacity: 0.9;
		flex: 1;
	}
</style>