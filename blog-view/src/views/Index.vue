<template>
	<div class="site">
		<!--顶部导航-->
		<Nav :blogName="siteInfo.blogName"/>

		<div class="main">
			<div class="m-padded-tb-big">
				<div class="ui container">
					<div class="ui stackable grid">
						<!--左侧-->
						<div class="three wide column m-mobile-hide">
							<Introduction class="m-position-sticky" :class="{'m-display-none':focusMode}"/>
						</div>
						<!--中间-->
						<div class="ten wide column">
							<router-view/>
						</div>
						<!--右侧-->
						<div class="three wide column m-mobile-hide">
							<RandomBlog :class="{'m-display-none':focusMode}"/>
							<Tags :class="{'m-display-none':focusMode}"/>
							<!--只在文章页面显示目录-->
							<Tocbot v-if="$route.name==='blog'"/>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!--私密文章密码对话框-->
		<BlogPasswordDialog/>

		<!--APlayer-->
		<div class="m-mobile-hide">
			<MyAPlayer/>
		</div>
		<!--回到顶部-->
		<el-backtop style="box-shadow: none;background: none;">
			<img src="/img/paper-plane.png" style="width: 40px;height: 40px;">
		</el-backtop>
		<!--底部footer-->
		<Footer :siteInfo="siteInfo" :badges="badges" :newBlogList="newBlogList" :hitokoto="hitokoto"/>
	</div>
</template>

<script>
	import {getHitokoto, getSite} from '@/api/index'
	import Nav from "@/components/index/Nav";
	import Footer from "@/components/index/Footer";
	import Introduction from "@/components/sidebar/Introduction";
	import Tags from "@/components/sidebar/Tags";
	import RandomBlog from "@/components/sidebar/RandomBlog";
	import MyAPlayer from "@/components/sidebar/MyAPlayer";
	import Tocbot from "@/components/sidebar/Tocbot";
	import BlogPasswordDialog from "@/components/index/BlogPasswordDialog";
	import {mapState} from 'vuex'

	export default {
		name: "Index",
		components: {BlogPasswordDialog, Tocbot, MyAPlayer, RandomBlog, Tags, Nav, Footer, Introduction},
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
		computed: {
			...mapState(['focusMode'])
		},
		watch: {
			//路由改变时，页面滚动至顶部
			'$route.fullPath'() {
				this.scrollToTop()
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
						this.$store.dispatch('saveSiteInfo', this.siteInfo)
						this.$store.dispatch('saveIntroduction', res.data.introduction)
						document.title = this.$route.meta.title + this.siteInfo.webTitleSuffix
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

	.m-display-none {
		display: none !important;
	}
</style>