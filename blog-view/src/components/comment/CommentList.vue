<template>
	<div class="ui bottom teal attached segment threaded comments">
		<Comment :count="count" :comments="comments" :parentCommentId="parentCommentId" :setParentCommentId="setParentCommentId"/>
		<Pagination :page="page" :blogId="blogId" :totalPage="totalPage" :getCommentList="getCommentList" :setParentCommentId="setParentCommentId"/>
	</div>
</template>

<script>
	import Comment from "./Comment";
	import Pagination from "./Pagination";
	import {getCommentListByQuery} from "@/network/comment";

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
		data() {
			return {
				count: 0,
				comments: [],
				totalPage: 0,
				parentCommentId: -1
			}
		},
		methods: {
			getCommentList(query) {
				getCommentListByQuery(query).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.count = res.data.count
						this.comments = res.data.comments.list
						this.totalPage = res.data.comments.totalPage
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			setParentCommentId(id) {
				this.parentCommentId = id
			}
		}
	}
</script>

<style scoped>

</style>