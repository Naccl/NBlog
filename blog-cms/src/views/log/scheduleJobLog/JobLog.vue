<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="系统管理"/>

		<el-table :data="logList">
			<el-table-column label="序号" type="index" width="50"></el-table-column>
			<el-table-column label="日志id" prop="logId" width="80"></el-table-column>
			<el-table-column label="任务id" prop="jobId" width="80"></el-table-column>
			<el-table-column label="bean" prop="beanName"></el-table-column>
			<el-table-column label="方法名" prop="methodName"></el-table-column>
			<el-table-column label="参数" prop="params"></el-table-column>
			<el-table-column label="结果" width="80">
				<template v-slot="scope">
					<el-tag v-if="scope.row.status" size="small" effect="dark">成功</el-tag>
					<el-tag v-else size="small" effect="dark" type="danger">失败</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="执行耗时" prop="times" width="110">
				<template v-slot="scope">
					<el-tag size="small">{{ scope.row.times }}ms</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="执行时间" width="170">
				<template v-slot="scope">{{ scope.row.createTime | dateFormat }}</template>
			</el-table-column>
			<el-table-column label="操作" width="200">
				<template v-slot="scope">
					<el-button type="warning" icon="el-icon-edit" size="mini" @click="showDetail(scope.row)">查看详情</el-button>
					<el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @onConfirm="deleteJobLogByLogId(scope.row.logId)">
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

		<!-- 任务日志详情 -->
		<el-dialog title="日志详情" width="50%" :visible.sync="detailDialogVisible">
			<el-form :model="detail" ref="detailFormRef" label-width="120px" size="mini">
				<el-row>
					<el-col :span="12">
						<el-form-item label="日志编号：">{{ detail.logId }}</el-form-item>
						<el-form-item label="任务编号：">{{ detail.jobId }}</el-form-item>
						<el-form-item label="执行耗时：">{{ detail.times }} 毫秒</el-form-item>
						<el-form-item label="执行时间：">{{ detail.createTime | dateFormat }}</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="bean：">{{ detail.beanName }}</el-form-item>
						<el-form-item label="方法名：">{{ detail.methodName }}</el-form-item>
						<el-form-item label="参数：">{{ detail.params }}</el-form-item>
						<el-form-item label="任务结果：">
							<el-tag v-if="detail.status" size="mini" effect="dark">成功</el-tag>
							<el-tag v-else size="mini" effect="dark" type="danger">失败</el-tag>
						</el-form-item>
					</el-col>
					<el-form-item label="异常信息：" prop="content">
						<el-input v-model="detail.error" type="textarea" :rows="3"></el-input>
					</el-form-item>
				</el-row>
			</el-form>
			<span slot="footer">
				<el-button @click="detailDialogVisible=false">关 闭</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getJobLogList, deleteJobLogByLogId} from "@/api/schedule";

	export default {
		name: "JobLog",
		components: {Breadcrumb},
		data() {
			return {
				queryInfo: {
					pageNum: 1,
					pageSize: 10
				},
				logList: [],
				total: 0,
				detailDialogVisible: false,
				detail: {}
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getJobLogList(this.queryInfo).then(res => {
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
			showDetail(row) {
				this.detail = {...row}
				this.detailDialogVisible = true
			},
			deleteJobLogByLogId(logId) {
				deleteJobLogByLogId(logId).then(res => {
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

<style scoped>
	.el-button + span {
		margin-left: 10px;
	}
</style>