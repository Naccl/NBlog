<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="系统管理"/>

		<el-table :data="logList">
			<el-table-column type="expand">
				<template v-slot="props">
					<el-form label-position="left" class="table-expand">
						<el-form-item label="请求接口">
							<span>{{ props.row.uri }}</span>
						</el-form-item>
						<el-form-item label="请求参数">
							<span>{{ props.row.param }}</span>
						</el-form-item>
					</el-form>
				</template>
			</el-table-column>
			<el-table-column label="序号" type="index" width="50"></el-table-column>
			<el-table-column label="请求方式" prop="method" width="80"></el-table-column>
			<el-table-column label="描述" prop="description"></el-table-column>
			<el-table-column label="ip" prop="ip"></el-table-column>
			<el-table-column label="ip来源" prop="ipSource"></el-table-column>
			<el-table-column label="操作系统" prop="os"></el-table-column>
			<el-table-column label="浏览器" prop="browser"></el-table-column>
			<el-table-column label="操作时间" width="170">
				<template v-slot="scope">{{ scope.row.createTime | dateFormat }}</template>
			</el-table-column>
			<el-table-column label="操作" width="200">
				<template v-slot="scope">
					<el-button type="warning" icon="el-icon-edit" size="mini" @click="showDetail(scope.row.error)">查看详情</el-button>
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

		<!-- 异常信息 -->
		<el-dialog title="异常信息" append-to-body top="20px" width="80%" :visible.sync="detailDialogVisible" destroy-on-close>
			<div class="match-braces rainbow-braces">
				<pre>
					<code class="language-java">{{ detail }}</code>
				</pre>
			</div>
			<span slot="footer">
				<el-button @click="detailDialogVisible=false">关 闭</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getExceptionLogList, deleteExceptionLogById} from "@/api/ExceptionLog";

	export default {
		name: "ExceptionLog",
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
				detail: ''
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getExceptionLogList(this.queryInfo).then(res => {
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
				deleteExceptionLogById(id).then(res => {
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
			showDetail(error) {
				this.detail = '\n' + error
				this.detailDialogVisible = true
				this.$nextTick(() => {
					Prism.highlightAll()
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