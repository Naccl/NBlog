<template>
	<div class="site">
		<!--顶部导航-->
		<Nav :blogName="siteInfo.blogName"/>

		<div class="main">
			<div class="m-padded-tb-big">
				<div class="ui container">
					<div class="ui stackable grid">
						<!--左侧-->
						<div class="three wide column fixed m-mobile-hide">
							<div class="m-position-sticky">
								<Introduction/>
							</div>
						</div>
						<!--中间-->
						<div class="ten wide column">
							<router-view/>
						</div>
						<!--右侧-->
						<div class="three wide column m-mobile-hide">
							<Introduction/>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!--底部footer-->
		<Footer :siteInfo="siteInfo" :badges="badges" :newBlogList="newBlogList" :hitokoto="hitokoto"/>
	</div>
</template>

<script>
	import Nav from "@/components/index/Nav";
	import Footer from "@/components/index/Footer";
	import Introduction from "@/components/sidebar/Introduction";
	import {getHitokoto, getSite} from '@/network/index'

	export default {
		name: "Index",
		components: {Nav, Footer, Introduction},
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
						this.$store.dispatch('saveIntroduction', res.data.introduction)
						this.$store.dispatch('saveWebTitleSuffix', this.siteInfo.webTitleSuffix)
						document.title = this.$route.meta.title + ' | ' + this.$store.state.webTitleSuffix
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
		margin-top: 40px;
		flex: 1;
	}

	.main .ui.container {
		width: 1400px !important;
		margin-left: auto !important;
		margin-right: auto !important;
	}

	.ui.grid .three.column {
		padding: 0;
	}

	.ui.grid .ten.column {
		padding-top: 0;
	}

	.m-position-sticky {
		position: sticky !important;
		top: 68px;
	}
</style>