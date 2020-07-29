<template>
	<div>
		<!--面包屑导航-->
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
			<el-breadcrumb-item>博客管理</el-breadcrumb-item>
			<el-breadcrumb-item>博客列表</el-breadcrumb-item>
		</el-breadcrumb>

		<el-card>
			<!--搜索-->
			<el-row>
				<el-col :span="8">
					<el-input placeholder="请输入标题" v-model="queryInfo.query" :clearable="true" @clear="getBlogList">
						<el-select class="select" v-model="queryInfo.typeId" slot="prepend" placeholder="请选择分类" :clearable="true" style="width: 160px">
							<el-option label="分类1" value="1"></el-option>
							<el-option label="分类2" value="2"></el-option>
							<el-option label="分类3" value="3"></el-option>
							<el-option label="分类4" value="4"></el-option>
						</el-select>
						<el-button slot="append" icon="el-icon-search" @click="getBlogList"></el-button>
					</el-input>
				</el-col>
			</el-row>

			<el-table :data="blogList" border stripe>
				<el-table-column label="序号" type="index" width="50"></el-table-column>
				<el-table-column label="标题" prop="title"></el-table-column>
				<el-table-column label="分类" prop="category.name" width="150"></el-table-column>
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
						<el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @onConfirm="removeBlogById(scope.row.id)">
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
	import {blogs, deleteBlogById} from '@/network/blog'

	export default {
		name: "BlogList",
		data() {
			return {
				queryInfo: {
					query: '',
					typeId: null,
					pageNum: 1,
					pageSize: 10
				},
				blogList: [],
				total: 0,
			}
		},
		created() {
			this.getBlogList()
		},
		methods: {
			getBlogList() {
				blogs(this.queryInfo.query, this.queryInfo.typeId, this.queryInfo.pageNum, this.queryInfo.pageSize)
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg);
						this.blogList = res.data.blogs.list
						this.total = res.data.blogs.total
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			//切换博客推荐状态
			blogRecommendChanged() {

			},
			//切换博客发布状态
			blogPublishedChanged() {

			},
			//监听 pageSize 改变事件
			handleSizeChange(newSize) {
				this.queryInfo.pageSize = newSize
				this.getBlogList()
			},
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				this.queryInfo.pageNum = newPage
				this.getBlogList()
			},
			goBlogEditPage(id) {
				this.$router.push(`/blogs/edit/${id}`)
			},
			removeBlogById(id) {
				deleteBlogById(id).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg)
						this.getBlogList()
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
	.el-button {
		margin-right: 10px;
	}
</style>