<template>
	<div>
		<Comment/>
		<Pagination/>
	</div>
</template>

<script>
	import Comment from "./Comment";
	import Pagination from "./Pagination";

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
				required: true
			}
		},
		created() {
			this.init()
		},
		watch: {
			//在博客文章路由到其它含有评论的页面时，要重新获取评论
			'$route.fullPath'() {
				this.init()
			}
		},
		methods:{
			init(){
				this.$store.dispatch('setCommentQueryPage', this.page)
				this.$store.dispatch('setCommentQueryBlogId', this.blogId)
				this.$store.dispatch('setCommentQueryPageNum', 1)
				this.$store.dispatch('getCommentList')
			}
		}
	}
</script>

<style scoped>

</style>