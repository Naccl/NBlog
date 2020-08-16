<template>
	<!--评论列表-->
	<div>
		<h3 class="ui dividing header">Comments | {{ count }} 条评论</h3>
		<h3 class="ui header" v-if="count===0">快来抢沙发！</h3>
		<div class="comment" v-for="comment in comments" :key="comment.id">
			<span class="anchor" :id="'comment-'+comment.id"></span>
			<a class="ui circular image avatar">
				<img :src="'/img/comment-avatar/'+comment.avatar">
			</a>
			<div class="content">
				<a class="author">
					<span>{{ comment.nickname }}</span>
					<div class="ui basic orange left pointing label" v-if="comment.adminComment">博主</div>
				</a>
				<div class="metadata">
					<strong class="date">{{ comment.createTime | dateFormat('YYYY-MM-DD HH:mm')}}</strong>
				</div>
				<div class="text">{{ comment.content }}</div>
				<div class="actions">
					<a class="reply" data-commentid="1" data-commentnickname="Matt" attr="data-commentid=${comment.id},data-commentnickname=${comment.nickname}">回复</a>
				</div>
			</div>
			<div class="comments" v-if="comment.replyComments.length>0">
				<div class="comment" v-for="reply in comment.replyComments" :key="reply.id">
					<span class="anchor" :id="'comment-'+reply.id"></span>
					<a class="ui circular image avatar">
						<img :src="'/img/comment-avatar/'+reply.avatar">
					</a>
					<div class="content">
						<a class="author">
							<span>{{ reply.nickname }}</span>
							<div class="ui basic orange left pointing label" v-if="reply.adminComment">博主</div>
						</a>
						<div class="metadata">
							<strong class="date">{{ reply.createTime | dateFormat('YYYY-MM-DD HH:mm')}}</strong>
						</div>
						<div class="text"><a style="font-weight: 500" href="" @click.prevent="toComment('comment-'+reply.parentCommentId)">@{{ reply.parentCommentNickname }}</a>{{ reply.content }}</div>
						<div class="actions">
							<a class="reply" data-commentid="1" data-commentnickname="Matt" attr="data-commentid=${reply1.id},data-commentnickname=${reply1.nickname}">回复</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "Comment",
		props: {
			count: {
				type: Number,
				required: true
			},
			comments: {
				type: Array,
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

	.anchor {
		display: block;
		height: 52px;
		margin-top: -52px;
		visibility: hidden;
	}

	.ui.comments .comment .avatar {
		width: 40px !important;
		margin: 0;
	}

	.ui.comments .comment .text a {
		margin-right: 5px;
	}

	.author .label {
		padding: 4px 6px;
		font-weight: 500;
	}
</style>