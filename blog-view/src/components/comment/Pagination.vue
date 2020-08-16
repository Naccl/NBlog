<template>
	<!--评论分页-->
	<el-pagination @current-change="handleCurrentChange" :current-page="pageNum" :page-count="totalPage"
	               layout="prev, pager, next" background hide-on-single-page class="pagination">
	</el-pagination>
</template>

<script>
	export default {
		name: "Pagination",
		props: {
			page: {
				type: Number,
				required: true
			},
			blogId: {
				type: Number,
				required: true
			},
			getCommentList: {
				type: Function,
				required: true
			},
			totalPage: {
				type: Number,
				required: true
			},
			setParentCommentId: {
				type: Function,
				required: true
			}
		},
		computed: {
			query() {
				return {
					page: this.page,
					blogId: this.blogId,
					pageNum: this.pageNum,
					pageSize: 1
				}
			}
		},
		watch: {
			//在博客文章路由到其它博客文章时，要重新获取评论
			'$route.fullPath'() {
				this.pageNum = 1
				this.getCommentList(this.query)
			}
		},
		data() {
			return {
				pageNum: 1
			}
		},
		created() {
			this.getCommentList(this.query)
		},
		methods: {
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				this.pageNum = newPage
				this.setParentCommentId(-1)
				this.getCommentList(this.query)
			},
		}
	}
</script>

<style scoped>
	.pagination {
		margin-top: 2em;
		text-align: center;
	}
</style>