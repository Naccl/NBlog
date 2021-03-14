<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="页面管理"/>

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
				<div id="vditor"></div>
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
	import Vditor from "vditor";

	export default {
		name: "About",
		components: {Breadcrumb},
		data() {
			return {
				vditor: null,
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
		mounted() {
			this.initVditor()
		},
		methods: {
			//初始化md编辑器
			initVditor() {
				const options = {
					height: 640,
					mode: 'sv',//分屏渲染
					outline: true,//大纲
					cache: {//不缓存到localStorage
						enable: false,
					},
					resize: {//可调整高度
						enable: true
					},
					after: () => {
						this.getData()
					}
				}
				this.vditor = new Vditor('vditor', options)
			},
			getData() {
				getAbout().then(res => {
					if (res.code === 200) {
						this.form.title = res.data.title
						this.form.musicId = res.data.musicId
						this.form.content = res.data.content
						this.form.commentEnabled = res.data.commentEnabled === 'true' ? true : false
						this.vditor.setValue(this.form.content)
						this.msgSuccess(res.msg)
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
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
						const form = {
							title: this.form.title,
							musicId: this.form.musicId,
							content: this.vditor.getValue(),
							commentEnabled: this.form.commentEnabled
						}
						updateAbout(form).then(res => {
							if (res.code === 200) {
								this.msgSuccess(res.msg)
							} else {
								this.msgError(res.msg)
							}
						}).catch(() => {
							this.msgError("请求失败")
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