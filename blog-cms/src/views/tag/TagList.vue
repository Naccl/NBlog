<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="博客管理"/>

		<el-card>
			<!--添加-->
			<el-row :gutter="10">
				<el-col :span="6">
					<el-button type="primary" @click="addDialogVisible=true">添加标签</el-button>
				</el-col>
			</el-row>

			<el-table :data="tagList" border stripe>
				<el-table-column label="序号" type="index" width="50"></el-table-column>
				<el-table-column label="名称" prop="name"></el-table-column>
				<el-table-column label="颜色" prop="color"></el-table-column>
				<el-table-column label="操作">
					<template v-slot="scope">
						<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row)">编辑</el-button>
						<el-popconfirm title="确定删除吗？" icon="el-icon-delete" iconColor="red" @onConfirm="deleteTagById(scope.row.id)">
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

		<!--添加标签对话框-->
		<el-dialog title="添加标签" width="50%" :visible.sync="addDialogVisible" :close-on-click-modal="false" @close="addDialogClosed">
			<!--内容主体-->
			<el-form :model="addForm" :rules="formRules" ref="addFormRef" label-width="80px">
				<el-form-item label="标签名称" prop="name">
					<el-input v-model="addForm.name"></el-input>
				</el-form-item>
				<el-form-item label="标签颜色" prop="color">
					<el-input v-model="addForm.color"></el-input>
				</el-form-item>
			</el-form>
			<!--底部-->
			<span slot="footer">
				<el-button @click="addDialogVisible=false">取 消</el-button>
				<el-button type="primary" @click="addTag">确 定</el-button>
			</span>
		</el-dialog>

		<!--编辑标签对话框-->
		<el-dialog title="编辑标签" width="50%" :visible.sync="editDialogVisible" :close-on-click-modal="false" @close="editDialogClosed">
			<!--内容主体-->
			<el-form :model="editForm" :rules="formRules" ref="editFormRef" label-width="80px">
				<el-form-item label="标签名称" prop="name">
					<el-input v-model="editForm.name"></el-input>
				</el-form-item>
				<el-form-item label="标签颜色" prop="color">
					<el-input v-model="editForm.color"></el-input>
				</el-form-item>
			</el-form>
			<!--底部-->
			<span slot="footer">
				<el-button @click="editDialogVisible=false">取 消</el-button>
				<el-button type="primary" @click="editTag">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getData, addTag, editTag, deleteTagById} from '@/network/tag'

	export default {
		name: "TagList",
		components: {
			Breadcrumb
		},
		data() {
			return {
				queryInfo: {
					pageNum: 1,
					pageSize: 10
				},
				tagList: [],
				total: 0,
				addDialogVisible: false,
				editDialogVisible: false,
				addForm: {
					name: '',
					color: ''
				},
				editForm: {},
				formRules: {
					name: [{required: true, message: '请输入标签名称', trigger: 'blur'}]
				}
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getData(this.queryInfo)
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg);
						this.tagList = res.data.list
						this.total = res.data.total
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
			//监听页码改变事件
			handleCurrentChange(newPage) {
				this.queryInfo.pageNum = newPage
				this.getData()
			},
			addDialogClosed() {
				this.$refs.addFormRef.resetFields()
			},
			editDialogClosed() {
				this.editForm = {}
				this.$refs.editFormRef.resetFields()
			},
			addTag() {
				addTag(this.addForm).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg)
						this.addDialogVisible = false
						this.getData()
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			editTag() {
				editTag(this.editForm).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg)
						this.editDialogVisible = false
						this.getData()
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			showEditDialog(row) {
				this.editForm = {...row}
				this.editDialogVisible = true
			},
			deleteTagById(id) {
				deleteTagById(id).then(res => {
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
			}
		}
	}
</script>

<style scoped>
	.el-button {
		margin-right: 10px;
	}
</style>