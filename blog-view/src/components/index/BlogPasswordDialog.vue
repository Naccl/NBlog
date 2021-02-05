<template>
	<!--私密文章密码对话框-->
	<el-dialog title="请输入受保护文章密码" width="30%" :visible.sync="blogPasswordDialogVisible"
	           :lock-scroll="false" :before-close="blogPasswordDialogClosed">
		<!--内容主体-->
		<el-form :model="blogPasswordForm" :rules="formRules" ref="formRef" label-width="80px">
			<el-form-item label="密码" prop="password">
				<el-input v-model="blogPasswordForm.password"></el-input>
			</el-form-item>
		</el-form>
		<!--底部-->
		<span slot="footer">
			<el-button @click="blogPasswordDialogClosed">取 消</el-button>
			<el-button type="primary" @click="submitBlogPassword">确 定</el-button>
		</span>
	</el-dialog>
</template>

<script>
	import {mapState} from "vuex";
	import {SET_BLOG_PASSWORD_DIALOG_VISIBLE} from "../../store/mutations-types";
	import {checkBlogPassword} from "@/api/blog";

	export default {
		name: "BlogPasswordDialog",
		computed: {
			...mapState(['blogPasswordDialogVisible', 'blogPasswordForm'])
		},
		data() {
			return {
				formRules: {
					password: [{required: true, message: '请输入密码', trigger: 'change'}]
				}
			}
		},
		methods: {
			blogPasswordDialogClosed() {
				this.$refs.formRef.resetFields()
				this.$store.commit(SET_BLOG_PASSWORD_DIALOG_VISIBLE, false)
			},
			submitBlogPassword() {
				this.$refs.formRef.validate(valid => {
					if (valid) {
						checkBlogPassword(this.blogPasswordForm).then(res => {
							if (res.code === 200) {
								this.msgSuccess(res.msg)
								window.localStorage.setItem(`blog${this.blogPasswordForm.blogId}`, res.data)
								this.$router.push(`/blog/${this.blogPasswordForm.blogId}`)
								this.blogPasswordDialogClosed()
							} else {
								this.msgError(res.msg)
							}
						}).catch(() => {
							this.msgError("请求失败")
						})
					}
				})
			}
		}
	}
</script>

<style scoped>

</style>