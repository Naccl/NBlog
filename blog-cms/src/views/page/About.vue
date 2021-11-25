<template>
	<div>
		<el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
			<el-form-item label="标题" prop="title" style="width: 50%">
				<el-input v-model="form.title" placeholder="请输入标题"></el-input>
			</el-form-item>

			<el-row :gutter="20" style="width: 50%">
				<el-col :span="12">
					<el-form-item label="网易云歌曲ID" prop="musicId">
						<el-input v-model="form.musicId" type="number" placeholder="请输入网易云歌曲ID（可选）"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="评论开关">
						<el-switch v-model="form.commentEnabled" active-text="评论"></el-switch>
					</el-form-item>
				</el-col>
			</el-row>

			<el-form-item label="正文" prop="content">
				<mavon-editor v-model="form.content"/>
			</el-form-item>

			<el-form-item style="text-align: right;">
				<el-button type="primary" icon="el-icon-check" @click="submit">保存</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getAbout, updateAbout} from "@/api/about";

	export default {
		name: "About",
		components: {Breadcrumb},
		data() {
			return {
				form: {
					title: '',
					musicId: null,
					content: '',
					commentEnabled: true
				},
				formRules: {
					title: [{required: true, message: '请输入标题', trigger: 'change'}],
				}
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getAbout().then(res => {
					this.form.title = res.data.title
					this.form.musicId = res.data.musicId
					this.form.content = res.data.content
					this.form.commentEnabled = res.data.commentEnabled === 'true' ? true : false
				})
			},
			submit() {
				this.$refs.formRef.validate(valid => {
					if (valid) {
						//纯数字
						const reg = /^\d{1,}$/
						if (!reg.test(this.form.musicId)) {
							return this.msgError("歌曲ID有误")
						}
						updateAbout(this.form).then(res => {
							this.msgSuccess(res.msg)
						})
					} else {
						return this.msgError('请填写必要的表单')
					}
				})
			}
		}
	}
</script>

<style scoped>

</style>