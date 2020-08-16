<template>
	<!--评论列表-->
	<div>
		<SubmitComment v-if="parentCommentId===-1" :parentCommentId="parentCommentId" :setParentCommentId="setParentCommentId"/>
		<h3 class="ui dividing header">Comments | {{ count }} 条评论</h3>
		<h3 class="ui header" v-if="count===0">快来抢沙发！</h3>
		<div class="comment" v-for="comment in comments" :key="comment.id">
			<span class="anchor" :id="'comment-'+comment.id"></span>
			<a class="ui circular image avatar">
				<img :src="'/img/comment-avatar/'+comment.avatar">
			</a>
			<div class="content">
				<a class="nickname" :href="comment.website" target="_blank" rel="external nofollow noopener">{{ comment.nickname }}</a>
				<div class="ui black left pointing label" v-if="comment.adminComment">博主</div>
				<div class="metadata">
					<strong class="date">{{ comment.createTime | dateFormat('YYYY-MM-DD HH:mm')}}</strong>
				</div>
				<el-button size="mini" type="primary" @click="setParentCommentId(comment.id)">回复</el-button>
				<div class="text">{{ comment.content }}</div>
			</div>
			<div class="comments" v-if="comment.replyComments.length>0">
				<div class="comment" v-for="reply in comment.replyComments" :key="reply.id">
					<span class="anchor" :id="'comment-'+reply.id"></span>
					<a class="ui circular image avatar">
						<img :src="'/img/comment-avatar/'+reply.avatar">
					</a>
					<div class="content">
						<a class="nickname" :href="reply.website" target="_blank" rel="external nofollow noopener">{{ reply.nickname }}</a>
						<div class="ui black left pointing label" v-if="reply.adminComment">博主</div>
						<div class="metadata">
							<strong class="date">{{ reply.createTime | dateFormat('YYYY-MM-DD HH:mm')}}</strong>
						</div>
						<div class="text"><strong @click="toComment('comment-'+reply.parentCommentId)">@{{ reply.parentCommentNickname }}</strong>{{ reply.content }}</div>
						<div class="actions">
							<el-button size="mini" type="primary" @click="setParentCommentId(reply.id)">回复</el-button>
						</div>
					</div>
					<SubmitComment v-if="parentCommentId===reply.id" :parentCommentId="parentCommentId" :setParentCommentId="setParentCommentId"/>
				</div>
			</div>
			<SubmitComment v-if="parentCommentId===comment.id" :parentCommentId="parentCommentId" :setParentCommentId="setParentCommentId"/>
		</div>
	</div>
</template>

<script>
	import SubmitComment from "./SubmitComment";

	export default {
		name: "Comment",
		components: {SubmitComment},
		props: {
			count: {
				type: Number,
				required: true
			},
			comments: {
				type: Array,
				required: true
			},
			parentCommentId: {
				type: Number,
				required: true
			},
			setParentCommentId: {
				type: Function,
				required: true
			}
		},
		methods: {
			toComment(id) {
				document.getElementById(id).scrollIntoView()
			}
		}
	}
</script>

<style scoped>
	.comment {
		padding-right: 2em !important;
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

	.anchor {
		display: block;
		height: 55px;
		margin-top: -55px;
		visibility: hidden;
	}

	.ui.comments .comment .avatar {
		width: 40px !important;
		margin: 0;
	}

	.ui.comments .comment .text {
		margin-top: 7px;
		line-height: 1.5;
	}

	.ui.comments .comment .text strong {
		cursor: pointer;
		margin-right: 8px;
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