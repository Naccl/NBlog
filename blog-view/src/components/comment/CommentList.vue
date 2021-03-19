<template>
	<div>
		<Comment/>
		<Pagination/>
	</div>
</template>

<script>
	import Comment from "./Comment";
	import Pagination from "./Pagination";
	import {SET_COMMENT_QUERY_PAGE, SET_COMMENT_QUERY_BLOG_ID, SET_COMMENT_QUERY_PAGE_NUM, SET_PARENT_COMMENT_ID} from "@/store/mutations-types";

	export default {
		name: "CommentList",
		components: {Comment, Pagination},
		props: {
			page: {
				type: Number,
				required: true
			},
			blogId: {
				type: Number,
				required: false
			}
		},
		created() {
			this.init()
		},
		watch: {
			//在博客文章路由到其它含有评论的页面时，要重新获取评论
			'$route.path'() {
				this.init()
			}
		},
		methods: {
			init() {
				//重置评论表单位置
				this.$store.commit(SET_PARENT_COMMENT_ID, -1)
				this.$store.commit(SET_COMMENT_QUERY_PAGE, this.page)
				this.$store.commit(SET_COMMENT_QUERY_BLOG_ID, this.blogId)
				this.$store.commit(SET_COMMENT_QUERY_PAGE_NUM, 1)
				this.$store.dispatch('getCommentList')
			}
		}
	}
</script>

<style scoped>

</style>