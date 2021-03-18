<template>
	<!--评论分页-->
	<el-pagination @current-change="handleCurrentChange" :current-page="commentQuery.pageNum" :page-count="commentTotalPage"
	               layout="prev, pager, next" background hide-on-single-page class="pagination">
	</el-pagination>
</template>

<script>
	import {mapState} from 'vuex'
	import {SET_COMMENT_QUERY_PAGE_NUM, SET_PARENT_COMMENT_ID} from "@/store/mutations-types";

	export default {
		name: "Pagination",
		computed: {
			...mapState(['commentQuery', 'commentTotalPage'])
		},
		methods: {
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				this.$store.commit(SET_COMMENT_QUERY_PAGE_NUM, newPage)
				this.$store.commit(SET_PARENT_COMMENT_ID, -1)
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