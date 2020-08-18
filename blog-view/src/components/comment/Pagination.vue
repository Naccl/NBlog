<template>
	<!--评论分页-->
	<el-pagination @current-change="handleCurrentChange" :current-page="commentQuery.pageNum" :page-count="commentTotalPage"
	               layout="prev, pager, next" background hide-on-single-page class="pagination">
	</el-pagination>
</template>

<script>
	import {mapState} from 'vuex'

	export default {
		name: "Pagination",
		computed: {
			...mapState(['commentQuery', 'commentTotalPage'])
		},
		methods: {
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				this.$store.dispatch('setCommentQueryPageNum', newPage)
				this.$store.dispatch('setParentCommentId', -1)
				this.$store.dispatch('getCommentList')
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