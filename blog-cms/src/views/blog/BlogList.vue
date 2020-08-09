<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="博客管理"/>

		<el-card>
			<!--搜索-->
			<el-row>
				<el-col :span="8">
					<el-input placeholder="请输入标题" v-model="queryInfo.title" :clearable="true" @clear="search" style="min-width: 500px" @keyup.native.enter="search">
						<el-select v-model="queryInfo.CategoryId" slot="prepend" placeholder="请选择分类" :clearable="true" @change="search" style="width: 160px">
							<el-option :label="item.name" :value="item.id" v-for="item in categoryList" :key="item.id"></el-option>
						</el-select>
						<el-button slot="append" icon="el-icon-search" @click="search"></el-button>
					</el-input>
				</el-col>
			</el-row>

			<el-table :data="blogList" border stripe>
				<el-table-column label="序号" type="index" width="50"></el-table-column>
				<el-table-column label="标题" prop="title"></el-table-column>
				<el-table-column label="分类" prop="category.name" width="150"></el-table-column>
				<el-table-column label="置顶" width="80">
					<template v-slot="scope">
						<el-switch v-model="scope.row.top" @change="blogTopChanged(scope.row)"></el-switch>
					</template>
				</el-table-column>
				<el-table-column label="推荐" width="80">
					<template v-slot="scope">
						<el-switch v-model="scope.row.recommend" @change="blogRecommendChanged(scope.row)"></el-switch>
					</template>
				</el-table-column>
				<el-table-column label="发布状态" width="80">
					<template v-slot="scope">
						<el-switch v-model="scope.row.published" @change="blogPublishedChanged(scope.row)"></el-switch>
					</template>
				</el-table-column>
				<el-table-column label="创建时间" width="170">
					<template v-slot="scope">{{ scope.row.createTime | dateFormat}}</template>
				</el-table-column>
				<el-table-column label="最近更新" width="170">
					<template v-slot="scope">{{ scope.row.updateTime | dateFormat}}</template>
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template v-slot="scope">
						<el-button type="primary" icon="el-icon-edit" size="mini" @click="goBlogEditPage(scope.row.id)">编辑</el-button>
						<el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @onConfirm="deleteBlogById(scope.row.id)">
							<el-button size="mini" type="danger" icon="el-icon-delete" slot="reference">删除</el-button>
						</el-popconfirm>
					</template>
				</el-table-column>
			</el-table>

			<!--分页-->
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pageNum"
			               :page-sizes="[5, 10, 15, 20]" :page-size="queryInfo.pageSize" :total="total"
			               layout="total, sizes, prev, pager, next, jumper" background>
			</el-pagination>
		</el-card>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getDataByQuery, deleteBlogById, updateTop, updateRecommend, updatePublished} from '@/network/blog'

	export default {
		name: "BlogList",
		components: {
			Breadcrumb
		},
		data() {
			return {
				queryInfo: {
					title: '',
					CategoryId: null,
					pageNum: 1,
					pageSize: 10
				},
				blogList: [],
				categoryList: [],
				total: 0,
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getDataByQuery(this.queryInfo)
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg);
						this.blogList = res.data.blogs.list
						this.categoryList = res.data.categories
						this.total = res.data.blogs.total
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			search() {
				this.queryInfo.pageNum = 1
				this.queryInfo.pageSize = 10
				this.getData()
			},
			//切换博客置顶状态
			blogTopChanged(row) {
				updateTop(row.id, row.top)
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg);
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			//切换博客推荐状态
			blogRecommendChanged(row) {
				updateRecommend(row.id, row.recommend)
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg);
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			//切换博客发布状态
			blogPublishedChanged(row) {
				updatePublished(row.id, row.published)
				.then(res => {
					if (res.code === 200) {
						this.msgSuccess(res.msg);
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			//监听 pageSize 改变事件
			handleSizeChange(newSize) {
				this.queryInfo.pageSize = newSize
				this.getData()
			},
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				this.queryInfo.pageNum = newPage
				this.getData()
			},
			goBlogEditPage(id) {
				this.$router.push(`/blogs/edit/${id}`)
			},
			deleteBlogById(id) {
				deleteBlogById(id).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg)
						this.getData()
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("操作失败")
				})
			}
		}
	}
</script>

<style scoped>
	.el-button + span {
		margin-left: 10px;
	}
</style>