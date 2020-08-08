<template>
	<div class="ui bottom" style="text-align:center">
		<el-pagination @current-change="handleCurrentChange" :current-page="pageNum" :page-count="totalPage"
		               layout="prev, pager, next" background hide-on-single-page>
		</el-pagination>
	</div>
</template>

<script>
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
		data() {
			return {
				pageNum: 1
			}
		},
		methods: {
			//监听页码改变的事件
			handleCurrentChange(newPage) {
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