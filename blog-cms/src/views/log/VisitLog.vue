<template>
	<div>
		<!--搜索-->
		<el-form inline>
			<el-form-item label="访客标识">
				<el-input v-model="queryInfo.uuid" :clearable="true" size="small" @keyup.native.enter="search"
				          placeholder="请输入访客标识码" style="min-width: 300px">
				</el-input>
			</el-form-item>
			<el-form-item label="访问时间">
				<DateTimeRangePicker :date="queryInfo.date" :setDate="setDate"/>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" size="small" icon="el-icon-search" @click="search">搜索</el-button>
			</el-form-item>
		</el-form>

		<el-table :data="logList">
			<el-table-column type="expand">
				<template v-slot="props">
					<el-form label-position="left" class="table-expand">
						<el-form-item label="访客标识">
							<span>{{ props.row.uuid }}</span>
						</el-form-item>
						<el-form-item label="请求方式">
							<span>{{ props.row.method }}</span>
						</el-form-item>
						<el-form-item label="请求接口">
							<span>{{ props.row.uri }}</span>
						</el-form-item>
						<el-form-item label="请求参数">
							<span>{{ props.row.param }}</span>
						</el-form-item>
						<el-form-item label="备注">
							<span>{{ props.row.remark }}</span>
						</el-form-item>
					</el-form>
				</template>
			</el-table-column>
			<el-table-column label="序号" type="index" width="50"></el-table-column>
			<el-table-column label="访客标识" width="100" show-overflow-tooltip>
				<template v-slot="scope">
					<el-link type="primary" href="" :underline="false" @click.prevent="showThis(scope.row.uuid)">{{ scope.row.uuid }}</el-link>
				</template>
			</el-table-column>
			<el-table-column label="访问行为" prop="behavior"></el-table-column>
			<el-table-column label="访问内容" prop="content" show-overflow-tooltip></el-table-column>
			<el-table-column label="ip" prop="ip"></el-table-column>
			<el-table-column label="ip来源" prop="ipSource" show-overflow-tooltip></el-table-column>
			<el-table-column label="操作系统" prop="os" show-overflow-tooltip></el-table-column>
			<el-table-column label="浏览器" prop="browser" show-overflow-tooltip></el-table-column>
			<el-table-column label="访问时间" width="170">
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
		               :page-sizes="[10, 20, 30, 50]" :page-size="queryInfo.pageSize" :total="total"
		               layout="total, sizes, prev, pager, next, jumper" background>
		</el-pagination>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getVisitLogList, deleteVisitLogById} from "@/api/visitLog";
	import DateTimeRangePicker from "@/components/DateTimeRangePicker";

	export default {
		name: "VisitLog",
		components: {DateTimeRangePicker, Breadcrumb},
		data() {
			return {
				queryInfo: {
					uuid: '',
					date: [],
					pageNum: 1,
					pageSize: 10
				},
				logList: [],
				total: 0,
			}
		},
		created() {
			if (this.$route.query.uuid) {
				this.queryInfo.uuid = this.$route.query.uuid
			}
			this.getData()
		},
		methods: {
			getData() {
				let query = {...this.queryInfo}
				if (query.date && query.date.length === 2) {
					query.date = query.date[0] + ',' + query.date[1]
				}
				getVisitLogList(query).then(res => {
					this.logList = res.data.list
					this.total = res.data.total
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
				deleteVisitLogById(id).then(res => {
					this.msgSuccess(res.msg)
					this.getData()
				})
			},
			search() {
				this.queryInfo.pageNum = 1
				this.queryInfo.pageSize = 10
				this.getData()
			},
			showThis(uuid) {
				this.queryInfo.uuid = uuid
				this.search()
			},
			setDate(value) {
				this.queryInfo.date = value
			},
		}
	}
</script>

<style scoped>
	.el-form--inline .el-form-item {
		margin-bottom: 0;
	}
</style>