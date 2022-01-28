<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="12">
				<el-card>
					<div slot="header">
						<span>基础设置</span>
					</div>
					<el-form label-position="right" label-width="100px">
						<el-form-item :label="item.nameZh" v-for="item in typeMap.type1" :key="item.id">
							<el-input v-model="item.value" size="mini"></el-input>
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
						<el-form-item :label="item.nameZh" v-for="item in typeMap.type2" :key="item.id">
							<div v-if="item.nameEn=='favorite'">
								<el-col :span="20">
									<el-input v-model="item.value" size="mini"></el-input>
								</el-col>
								<el-col :span="4">
									<el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteFavorite(item)">删除</el-button>
								</el-col>
							</div>
							<div v-else>
								<el-input v-model="item.value" size="mini"></el-input>
							</div>
						</el-form-item>
						<el-button type="primary" size="mini" icon="el-icon-plus" @click="addFavorite">添加自定义</el-button>
					</el-form>
				</el-card>
			</el-col>
		</el-row>

		<el-row style="margin-top: 20px">
			<el-card>
				<div slot="header">
					<span>页脚徽标</span>
				</div>
				<el-form :inline="true" v-for="badge in typeMap.type3" :key="badge.id">
					<el-form-item label="title">
						<el-input v-model="badge.value.title" size="mini"></el-input>
					</el-form-item>
					<el-form-item label="url">
						<el-input v-model="badge.value.url" size="mini"></el-input>
					</el-form-item>
					<el-form-item label="subject">
						<el-input v-model="badge.value.subject" size="mini"></el-input>
					</el-form-item>
					<el-form-item label="value">
						<el-input v-model="badge.value.value" size="mini"></el-input>
					</el-form-item>
					<el-form-item label="color">
						<el-input v-model="badge.value.color" size="mini"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteBadge(badge)">删除</el-button>
					</el-form-item>
				</el-form>
				<el-button type="primary" size="mini" icon="el-icon-plus" @click="addBadge">添加 badge</el-button>
			</el-card>
		</el-row>

		<div style="text-align: right;margin-top: 30px">
			<el-button type="primary" icon="el-icon-check" @click="submit">保存</el-button>
		</div>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getSiteSettingData, update} from "@/api/siteSetting";
	import _ from 'lodash'

	export default {
		name: "SiteSetting",
		components: {Breadcrumb},
		data() {
			return {
				deleteIds: [],
				typeMap: {},
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getSiteSettingData().then(res => {
					this.typeMap = res.data
					res.data.type3.forEach(item => {
						item.value = JSON.parse(item.value)
					})
				})
			},
			addFavorite() {
				this.typeMap.type2.push({
					key: Date.now(),
					nameEn: "favorite",
					nameZh: "自定义",
					type: 2,
					value: "{\"title\":\"\",\"content\":\"\"}"
				})
			},
			addBadge() {
				this.typeMap.type3.push({
					key: Date.now(),
					nameEn: "badge",
					nameZh: "徽标",
					type: 3,
					value: {
						color: "",
						subject: "",
						title: "",
						url: "",
						value: ""
					}
				})
			},
			deleteFavorite(favorite) {
				let arr = this.typeMap.type2
				if (favorite.id) {
					this.deleteIds.push(favorite.id)
					arr.forEach((item, index) => {
						if (item.id === favorite.id) {
							arr.splice(index, 1)
							return
						}
					})
				} else {
					arr.forEach((item, index) => {
						if (item.key === favorite.key) {
							arr.splice(index, 1)
							return
						}
					})
				}
			},
			deleteBadge(badge) {
				let arr = this.typeMap.type3
				if (badge.id) {
					this.deleteIds.push(badge.id)
					arr.forEach((item, index) => {
						if (item.id === badge.id) {
							arr.splice(index, 1)
							return
						}
					})
				} else {
					arr.forEach((item, index) => {
						if (item.key === badge.key) {
							arr.splice(index, 1)
							return
						}
					})
				}
			},
			submit() {
				const result = _.cloneDeep(this.typeMap)
				result.type3.forEach(item => {
					item.value = JSON.stringify(item.value)
				})
				let updateArr = []
				updateArr.push(...result.type1)
				updateArr.push(...result.type2)
				updateArr.push(...result.type3)
				update(updateArr, this.deleteIds).then(res => {
					this.deleteIds = []
					this.getData()
					this.msgSuccess(res.msg)
				})
			}
		}
	}
</script>

<style scoped>

</style>