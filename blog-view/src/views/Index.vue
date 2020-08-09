<template>
	<div class="site">
		<!--顶部导航-->
		<Nav :blogName="siteInfo.blogName"/>

		<div class="main">
			<router-view/>
		</div>

		<!--底部footer-->
		<Footer :siteInfo="siteInfo" :badges="badges" :newBlogList="newBlogList" :hitokoto="hitokoto"/>
	</div>
</template>

<script>
	import Nav from "@/components/index/Nav";
	import Footer from "@/components/index/Footer";
	import {getHitokoto, getSite} from '@/network/index'
	import store from "../store";

	export default {
		name: "Index",
		components: {Nav, Footer},
		data() {
			return {
				siteInfo: {
					blogName: ''
				},
				badges: [],
				newBlogList: [],
				hitokoto: {},
			}
		},
		created() {
			this.getSite()
			this.getHitokoto()
		},
		methods: {
			getSite() {
				getSite().then(res => {
					console.log(res)
					if (res.code === 200) {
						this.siteInfo = res.data.siteInfo
						this.badges = res.data.badges
						this.newBlogList = res.data.newBlogList
						this.$store.dispatch('saveIntroduction',res.data.introduction)
						this.$store.dispatch('saveWebTitleSuffix', this.siteInfo.webTitleSuffix)
						document.title = this.$route.meta.title + ' | ' + store.state.webTitleSuffix
					}
				})
			},
			//获取一言
			getHitokoto() {
				getHitokoto().then(res => {
					this.hitokoto = res
				})
			}
		}
	}
</script>

<style scoped>
	.site {
		display: flex;
		min-height: 100vh; /* 没有元素时，也把页面撑开至100% */
		flex-direction: column;
	}

	.main {
		opacity: 0.9;
		flex: 1;
	}
</style>