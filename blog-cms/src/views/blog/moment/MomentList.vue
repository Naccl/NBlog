<template>
	<div>
		<el-table :data="momentList">
			<el-table-column label="序号" type="index" width="50"></el-table-column>
			<el-table-column label="内容" prop="content" show-overflow-tooltip></el-table-column>
			<el-table-column label="发布状态" width="80">
				<template v-slot="scope">
					<el-switch v-model="scope.row.published" @change="momentPublishedChanged(scope.row)"></el-switch>
				</template>
			</el-table-column>
			<el-table-column label="点赞数" prop="likes" width="80"></el-table-column>
			<el-table-column label="创建时间" width="170">
				<template v-slot="scope">{{ scope.row.createTime | dateFormat }}</template>
			</el-table-column>
			<el-table-column label="操作" width="200">
				<template v-slot="scope">
					<el-button type="primary" icon="el-icon-edit" size="mini" @click="goEditMomentPage(scope.row.id)">编辑</el-button>
					<el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @onConfirm="deleteMomentById(scope.row.id)">
						<el-button size="mini" type="danger" icon="el-icon-delete" slot="reference">删除</el-button>
					</el-popconfirm>
				</template>
			</el-table-column>
		</el-table>

		<!--分页-->
		<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pageNum"
		               :page-sizes="[10, 20, 30, 50]" :page-size="queryInfo.pageSize" :total="total"
		               layout="total, sizes, prev, pager, next, jumper" background>
		</el-pagination>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getMomentListByQuery, updatePublished, deleteMomentById} from "@/api/moment";

	export default {
		name: "MomentList",
		components: {
			Breadcrumb
		},
		data() {
			return {
				queryInfo: {
					pageNum: 1,
					pageSize: 10
				},
				momentList: [],
				total: 0,
			}
		},
		created() {
			this.getMomentList()
		},
		methods: {
			getMomentList() {
				getMomentListByQuery(this.queryInfo).then(res => {
					this.momentList = res.data.list
					this.total = res.data.total
				})
			},
			//监听 pageSize 改变事件
			handleSizeChange(newSize) {
				this.queryInfo.pageSize = newSize
				this.getMomentList()
			},
			//监听页码改变的事件
			handleCurrentChange(newPage) {
				this.queryInfo.pageNum = newPage
				this.getMomentList()
			},
			momentPublishedChanged(row) {
				updatePublished(row.id, row.published).then(res => {
					this.msgSuccess(res.msg)
				})
			},
			goEditMomentPage(id) {
				this.$router.push(`/blog/moment/edit/${id}`)
			},
			deleteMomentById(id) {
				deleteMomentById(id).then(res => {
					this.msgSuccess(res.msg)
					this.getMomentList()
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