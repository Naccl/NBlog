<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="系统管理"/>

		<el-table :data="logList">
			<el-table-column label="序号" type="index" width="50"></el-table-column>
			<el-table-column label="用户名称" prop="username"></el-table-column>
			<el-table-column label="ip" prop="ip"></el-table-column>
			<el-table-column label="ip来源" prop="ipSource"></el-table-column>
			<el-table-column label="操作系统" prop="os"></el-table-column>
			<el-table-column label="浏览器" prop="browser"></el-table-column>
			<el-table-column label="登录状态">
				<template v-slot="scope">
					<el-tag v-if="scope.row.status" size="small" effect="dark">成功</el-tag>
					<el-tag v-else size="small" effect="dark" type="danger">失败</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="描述" prop="description"></el-table-column>
			<el-table-column label="操作时间" width="170">
				<template v-slot="scope">{{ scope.row.createTime | dateFormat }}</template>
			</el-table-column>
			<el-table-column label="操作" width="120">
				<template v-slot="scope">
					<el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @onConfirm="deleteLogById(scope.row.id)">
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
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getLoginLogList, deleteLoginLogById} from "@/api/loginLog";

	export default {
		name: "LoginLog",
		components: {Breadcrumb},
		data() {
			return {
				queryInfo: {
					pageNum: 1,
					pageSize: 10
				},
				logList: [],
				total: 0,
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getLoginLogList(this.queryInfo).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg)
						this.logList = res.data.list
						this.total = res.data.total
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			handleSizeChange(newSize) {
				this.queryInfo.pageSize = newSize
				this.getData()
			},
			handleCurrentChange(newPage) {
				this.queryInfo.pageNum = newPage
				this.getData()
			},
			deleteLogById(id) {
				deleteLoginLogById(id).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg)
						this.getData()
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
		}
	}
</script>

<style>

</style>