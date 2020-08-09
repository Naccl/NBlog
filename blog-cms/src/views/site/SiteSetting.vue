<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="系统管理"/>

		<el-card>
			<el-row :gutter="20">
				<el-col :span="12">
					<el-card>
						<div slot="header">
							<span>基础设置</span>
						</div>
						<el-form label-position="right" label-width="100px">
							<el-form-item :label="item.nameZh" v-for="item in typeMap.type1" :key="item.id">
								<el-input v-model="item.value"></el-input>
							</el-form-item>
						</el-form>
					</el-card>
				</el-col>
				<el-col :span="12">
					<el-card>
						<div slot="header">
							<span>资料卡</span>
						</div>
						<el-form label-position="right" label-width="100px">
							<el-form-item :label="item.nameZh" v-for="item in typeMap.type3" :key="item.id">
								<el-input v-model="item.value"></el-input>
							</el-form-item>
						</el-form>
					</el-card>
				</el-col>
			</el-row>

			<el-row style="margin-top: 20px">
				<el-card>
					<div slot="header">
						<span>页脚徽标</span>
					</div>
					<el-form :inline="true" v-for="badge in typeMap.type2" :key="badge.id">
						<el-form-item label="title">
							<el-input v-model="badge.value.title"></el-input>
						</el-form-item>
						<el-form-item label="url">
							<el-input v-model="badge.value.url"></el-input>
						</el-form-item>
						<el-form-item label="subject">
							<el-input v-model="badge.value.subject"></el-input>
						</el-form-item>
						<el-form-item label="value">
							<el-input v-model="badge.value.value"></el-input>
						</el-form-item>
						<el-form-item label="color">
							<el-input v-model="badge.value.color"></el-input>
						</el-form-item>
						<el-form-item>
							<el-button type="danger" icon="el-icon-delete">删除</el-button>
						</el-form-item>
					</el-form>
					<el-button type="primary" icon="el-icon-plus">添加</el-button>
				</el-card>
			</el-row>

			<div style="text-align: right;margin-top: 30px">
				<el-button type="primary" icon="el-icon-check">保存</el-button>
			</div>
		</el-card>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getSiteSettingList} from "@/network/siteSetting";

	export default {
		name: "SiteSetting",
		components: {Breadcrumb},
		data() {
			return {
				typeMap: {},
			}
		},
		created() {
			this.getList()
		},
		methods: {
			getList() {
				getSiteSettingList().then(res => {
					console.log(res)
					if (res.code === 200) {
						this.typeMap = res.data
						res.data.type2.forEach(item => {
							item.value = eval("(" + item.value + ")")
							// console.log(JSON.parse(JSON.stringify(item.value)))
							// item.value = JSON.parse(JSON.stringify(item.value))
							// console.log(item.value.toJSONString())
							// console.log(JSON.parse(item.value))
						})
						this.msgSuccess(res.msg)
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			}
		}
	}
</script>

<style scoped>

</style>