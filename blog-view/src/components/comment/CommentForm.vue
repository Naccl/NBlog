<template>
	<!-- 评论输入表单 -->
	<div class="form">
		<h3>
			发表评论
			<el-button class="m-small" size="mini" type="primary" @click="setParentCommentId(-1)" v-show="parentCommentId!==-1">取消回复</el-button>
		</h3>
		<el-form :inline="true" :model="form" :rules="formRules" ref="formRef" size="small">
			<el-input :class="'textarea'" type="textarea" :rows="5" v-model="form.content" placeholder="评论千万条，友善第一条"
			          maxlength="250" show-word-limit :validate-event="false"></el-input>
			<el-form-item prop="nickname">
				<el-popover ref="nicknamePopover" placement="bottom" trigger="focus"
				            content="输入QQ号将自动拉取昵称和头像">
				</el-popover>
				<el-input v-model="form.nickname" placeholder="昵称（必填）" :validate-event="false" v-popover:nicknamePopover>
					<i slot="prefix" class="el-input__icon el-icon-user"></i>
				</el-input>
			</el-form-item>
			<el-form-item prop="email">
				<el-popover ref="emailPopover" placement="bottom" trigger="focus"
				            content="邮箱将保密，用于接收回复邮件，可随时退订">
				</el-popover>
				<el-input v-model="form.email" placeholder="邮箱（必填）" :validate-event="false" v-popover:emailPopover>
					<i slot="prefix" class="el-input__icon el-icon-message"></i>
				</el-input>
			</el-form-item>
			<el-form-item>
				<el-popover ref="websitePopover" placement="bottom" trigger="focus"
				            content="可以让我参观一下吗😊">
				</el-popover>
				<el-input v-model="form.website" placeholder="网站或博客（可选）" v-popover:websitePopover>
					<i slot="prefix" class="el-input__icon el-icon-map-location"></i>
				</el-input>
			</el-form-item>
			<el-form-item label="订阅回复">
				<el-switch v-model="form.notice"></el-switch>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" size="medium" @click="submitForm">发表评论</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import {checkEmail} from "@/common/reg";

	export default {
		name: "CommentForm",
		props: {
			parentCommentId: {
				type: Number,
				required: true
			},
			setParentCommentId: {
				type: Function,
				required: true
			}
		},
		data() {
			return {
				form: {
					content: '',
					nickname: '',
					email: '',
					website: '',
					notice: true
				},
				formRules: {
					nickname: [{required: true, message: '请输入评论昵称'}],
					email: [
						{required: true, message: '请输入评论邮箱'},
						{validator: checkEmail}
					],
				}
			}
		},
		methods: {
			submitForm() {
				this.$refs.formRef.validate(valid => {
					if (!valid || this.form.content === '' || this.form.content.length > 250) {
						this.$notify({
							title: '评论失败',
							message: '请正确填写',
							type: 'warning'
						});
					}else {
						console.log("ok")
					}
				})
			}
		}
	}
</script>

<style>
	.form h3 {
		margin: 5px;
		font-weight: 500 !important;
	}

	.form .m-small {
		margin-left: 5px;
		padding: 4px 5px;
	}

	.el-form .textarea {
		margin-top: 5px;
		margin-bottom: 15px;
	}

</style>