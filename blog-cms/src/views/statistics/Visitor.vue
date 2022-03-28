<template>
	<div>
		<!--搜索-->
		<el-form inline>
			<el-form-item label="最后访问时间">
				<DateTimeRangePicker :date="queryInfo.date" :setDate="setDate"/>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" size="small" icon="el-icon-search" @click="search">搜索</el-button>
			</el-form-item>
		</el-form>

		<el-table :data="visitorList">
			<el-table-column label="序号" type="index" width="50"></el-table-column>
			<el-table-column label="访客标识" prop="uuid" show-overflow-tooltip></el-table-column>
			<el-table-column label="IP" prop="ip" show-overflow-tooltip></el-table-column>
			<el-table-column label="IP来源" prop="ipSource" show-overflow-tooltip></el-table-column>
			<el-table-column label="操作系统" prop="os" show-overflow-tooltip></el-table-column>
			<el-table-column label="浏览器" prop="browser" show-overflow-tooltip></el-table-column>
			<el-table-column label="首次访问" width="170">
				<template v-slot="scope">{{ scope.row.createTime | dateFormat }}</template>
			</el-table-column>
			<el-table-column width="170">
				<template slot="header" slot-scope="scope">
					最后访问
					<el-tooltip effect="dark" content="每日凌晨自动更新" placement="top"><i class="el-icon-question"></i></el-tooltip>
				</template>
				<template v-slot="scope">{{ scope.row.lastTime | dateFormat }}</template>
			</el-table-column>
			<el-table-column prop="pv" width="70">
				<template slot="header" slot-scope="scope">
					PV
					<el-tooltip effect="dark" content="访客总浏览量，每日凌晨自动更新" placement="top"><i class="el-icon-question"></i></el-tooltip>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="200">
				<template v-slot="scope">
					<el-button type="warning" icon="el-icon-view" size="mini" @click="showLog(scope.row.uuid)">查看记录</el-button>
					<el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @onConfirm="deleteVisitorById(scope.row)">
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
	import {getVisitorList, deleteVisitor} from "@/api/visitor";
	import DateTimeRangePicker from "@/components/DateTimeRangePicker";

	export default {
		name: "Visitor",
		components: {DateTimeRangePicker, Breadcrumb},
		data() {
			return {
				queryInfo: {
					date: [],
					pageNum: 1,
					pageSize: 10
				},
				visitorList: [],
				total: 0,
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				let query = {...this.queryInfo}
				if (query.date && query.date.length === 2) {
					query.date = query.date[0] + ',' + query.date[1]
				}
				getVisitorList(query).then(res => {
					this.visitorList = res.data.list
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
			deleteVisitorById(visitor) {
				deleteVisitor(visitor.id, visitor.uuid).then(res => {
					this.msgSuccess(res.msg)
					this.getData()
				})
			},
			showLog(uuid) {
				this.$router.push({
					path: '/log/visit',
					query: {
						uuid
					}
				})
			},
			search() {
				this.queryInfo.pageNum = 1
				this.queryInfo.pageSize = 10
				this.getData()
			},
			setDate(value) {
				this.queryInfo.date = value
			},
		}
	}
</script>

<style scoped>
	.el-button + span {
		margin-left: 10px;
	}

	.el-form--inline .el-form-item {
		margin-bottom: 0;
	}
</style>