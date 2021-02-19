<template>
	<div class="ui bottom" style="text-align:center">
		<el-pagination @current-change="handleCurrentChange" :current-page="pageNum" :page-count="totalPage"
		               layout="prev, pager, next" background hide-on-single-page>
		</el-pagination>
	</div>
</template>

<script>
	import {mapState} from 'vuex'

	export default {
		name: "Pagination",
		props: {
			getBlogList: {
				type: Function,
				required: true
			},
			totalPage: {
				type: Number,
				required: true
			}
		},
		//目前只有首页被缓存，所以这个钩子只会被首页调用
		activated() {
			this.$nextTick(() => {
				if (!this.isBlogToHome) {
					//从其它页面路由到首页时，让首页的分页组件页码重置到第一页
					this.pageNum = 1
				}
			})
		},
		computed: {
			...mapState(['isBlogToHome', 'clientSize'])
		},
		data() {
			return {
				pageNum: 1
			}
		},
		methods: {
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				//如果是首页，则滚动至Header下方
				if (this.$route.name === 'home') {
					window.scrollTo({top: this.clientSize.clientHeight, behavior: 'smooth'})
				} else {
					//其它页面（分类和标签页）滚动至顶部
					this.scrollToTop()
				}
				this.pageNum = newPage
				this.getBlogList(newPage)
			},
		}
	}
</script>

<style>
	.el-pagination.is-background .btn-next, .el-pagination.is-background .btn-prev, .el-pagination.is-background .el-pager li {
		background-color: #ffffff !important;
	}

	.el-pagination.is-background .el-pager li:not(.disabled).active {
		background-color: #409EFF !important;
	}
</style>