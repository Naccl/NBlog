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
			...mapState(['isBlogToHome'])
		},
		data() {
			return {
				pageNum: 1
			}
		},
		methods: {
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				this.scrollToTop()
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