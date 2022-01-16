<template>
	<!--评论列表-->
	<div>
		<CommentForm v-if="parentCommentId===-1"/>
		<h3 class="ui dividing header">Comments | 共 {{ allComment }} 条评论<span v-if="closeComment!==0">（{{ closeComment }} 条评论被隐藏）</span></h3>
		<h3 class="ui header" v-if="allComment===0">快来抢沙发！</h3>
		<div class="comment" v-for="comment in comments" :key="comment.id">
			<span class="anchor" :id="`comment-${comment.id}`"></span>
			<a class="ui circular image avatar">
				<img :src="comment.avatar">
			</a>
			<div class="content">
				<a class="nickname" :href="comment.website!=''&&comment.website!=null?comment.website:null" target="_blank" rel="external nofollow noopener">{{ comment.nickname }}</a>
				<div class="ui black left pointing label" v-if="comment.adminComment">{{ $store.state.siteInfo.commentAdminFlag }}</div>
				<div class="metadata">
					<strong class="date">{{ comment.createTime | dateFormat('YYYY-MM-DD HH:mm') }}</strong>
				</div>
				<el-button size="mini" type="primary" @click="setReply(comment.id)">回复</el-button>
				<div class="text" v-html="comment.content"></div>
			</div>
			<div class="comments" v-if="comment.replyComments.length>0">
				<div class="comment" v-for="reply in comment.replyComments" :key="reply.id">
					<span class="anchor" :id="`comment-${reply.id}`"></span>
					<a class="ui circular image avatar">
						<img :src="reply.avatar">
					</a>
					<div class="content">
						<a class="nickname" :href="reply.website!=''&&reply.website!=null?reply.website:null" target="_blank" rel="external nofollow noopener">{{ reply.nickname }}</a>
						<div class="ui black left pointing label" v-if="reply.adminComment">{{ $store.state.siteInfo.commentAdminFlag }}</div>
						<div class="metadata">
							<strong class="date">{{ reply.createTime | dateFormat('YYYY-MM-DD HH:mm') }}</strong>
						</div>
            <el-button size="mini" type="primary" @click="setReply(reply.id)">回复</el-button>
						<div class="text">
							<a :href="`#comment-${reply.parentCommentId}`">@{{ reply.parentCommentNickname }}</a>
							<div v-html="reply.content"></div>
						</div>
					</div>
					<CommentForm v-if="parentCommentId===reply.id"/>
				</div>
			</div>
			<div class="border"></div>
			<CommentForm v-if="parentCommentId===comment.id"/>
		</div>
	</div>
</template>

<script>
	import {mapState} from 'vuex'
	import CommentForm from "./CommentForm";
	import {SET_PARENT_COMMENT_ID} from "@/store/mutations-types";

	export default {
		name: "Comment",
		components: {CommentForm},
		computed: {
			...mapState(['allComment', 'closeComment', 'comments', 'parentCommentId'])
		},
		methods: {
			setReply(id) {
				this.$store.commit(SET_PARENT_COMMENT_ID, id)
			}
		}
	}
</script>

<style scoped>
	.comments + .border {
		position: absolute;
		left: 34px;
		top: 47px;
		bottom: 0;
		border-style: solid;
		border-width: 0 0 0 1px;
		border-color: #e0e0e0;
	}

	.ui.threaded.comments .comment .comments {
		box-shadow: none;
		margin-top: -2em;
	}

	.comment {
		padding-right: 1em !important;
		padding-left: 1em !important;
	}

	.nickname {
		font-weight: bolder;
		color: #000;
	}

	.comment .el-button {
		margin-left: 5px;
		padding: 4px 5px;
	}

	.comment > .anchor {
		position: absolute;
		left: 0;
		top: -48px;
	}

	.comments .comment:first-child {
		margin-top: 0 !important;
	}

	.comment .comments .comment {
		box-shadow: 0 0 5px rgb(0, 0, 0, 0.1);
		border-radius: 5px;
		margin-top: 12px;
		padding-top: 10px !important;
		padding-bottom: 10px !important;
	}

	.comment .comments .comment > .anchor {
		top: -55px;
	}

	.ui.comments .comment .avatar {
		width: 40px !important;
		margin: 0;
	}

	.ui.comments .comment .text {
		white-space: pre-wrap !important;
		line-height: 1.5;
	}

	.ui.comments .comment .text a {
		cursor: pointer;
		margin-right: 8px;
		font-weight: bolder;
		color: rgba(0, 0, 0, .87);
	}

	.ui.comments .comment .text div {
		display: inline;
	}

	.label {
		cursor: default;
		padding: 4px 6px !important;
		font-weight: 500 !important;
	}

	.comment .form {
		margin-top: 20px;
	}
</style>