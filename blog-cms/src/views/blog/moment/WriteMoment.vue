<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="博客管理"/>

		<el-form :model="form" label-position="top">
			<el-form-item label="动态内容" prop="content">
				<div id="vditor"></div>
			</el-form-item>

			<el-form-item label="点赞数" prop="likes" style="width: 50%">
				<el-input v-model="form.likes" type="number" placeholder="可选，默认为 0"></el-input>
			</el-form-item>

			<el-form-item label="创建时间" prop="createTime">
				<el-date-picker v-model="form.createTime" type="datetime" placeholder="可选，默认此刻" :editable="false"></el-date-picker>
			</el-form-item>

			<el-form-item style="text-align: right;">
				<el-button type="info" @click="submit(false)">保存草稿</el-button>
				<el-button type="primary" @click="submit(true)">发布动态</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getMomentById, saveMoment, updateMoment} from "@/api/moment";
	import Vditor from 'vditor'

	export default {
		name: "WriteMoment",
		components: {Breadcrumb},
		data() {
			return {
				vditor: null,
				form: {
					content: '',
					createTime: null,
					likes: 0,
					published: false
				},
			}
		},
		mounted() {
			this.initVditor()
		},
		methods: {
			initVditor() {
				const options = {
					height: 320,
					mode: 'sv',//分屏渲染
					outline: false,//大纲
					cache: {//不缓存到localStorage
						enable: false,
					},
					resize: {//可调整高度
						enable: true
					},
					after: () => {
						if (this.$route.params.id) {
							this.getMoment(this.$route.params.id)
						}
					}
				}
				this.vditor = new Vditor('vditor', options)
			},
			getMoment(id) {
				getMomentById(id).then(res => {
					if (res.code === 200) {
						this.form = res.data
						this.vditor.setValue(this.form.content)
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			submit(published) {
				this.form.content = this.vditor.getValue()
				this.form.published = published
				if (this.$route.params.id) {
					updateMoment(this.form).then(res => {
						if (res.code === 200) {
							this.msgSuccess(res.msg)
							this.$router.push('/moments')
						} else {
							this.msgError(res.msg)
						}
					}).catch(() => {
						this.msgError('请求失败')
					})
				} else {
					saveMoment(this.form).then(res => {
						if (res.code === 200) {
							this.msgSuccess(res.msg)
							this.$router.push('/moments')
						} else {
							this.msgError(res.msg)
						}
					}).catch(() => {
						this.msgError('请求失败')
					})
				}
			}
		}
	}
</script>

<style scoped>

</style>