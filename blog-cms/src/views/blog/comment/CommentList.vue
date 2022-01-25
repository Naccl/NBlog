<template>
	<div>
		<!--搜索-->
		<el-row>
			<el-col :span="6">
				<el-select v-model="pageId" placeholder="请选择页面" :filterable="true" :clearable="true" @change="search" size="small" style="min-width: 400px">
					<el-option :label="item.title" :value="item.id" v-for="item in blogList" :key="item.id"></el-option>
				</el-select>
			</el-col>
		</el-row>

		<el-table :data="commentList" row-key="id" :tree-props="{children: 'replyComments'}">
			<el-table-column label="序号" type="index" width="50"></el-table-column>
			<el-table-column label="昵称" prop="nickname">
				<template v-slot="scope">
					{{ scope.row.nickname }}
					<el-tag v-if="scope.row.adminComment" size="mini" effect="dark" style="margin-left: 5px">我</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="头像" width="80">
				<template v-slot="scope">
					<el-avatar shape="square" :size="60" fit="contain" :src="scope.row.avatar"></el-avatar>
				</template>
			</el-table-column>
			<el-table-column label="邮箱" prop="email" show-overflow-tooltip></el-table-column>
			<el-table-column label="网站" prop="website" show-overflow-tooltip></el-table-column>
			<el-table-column label="ip" prop="ip" width="130"></el-table-column>
			<el-table-column label="评论内容" prop="content" show-overflow-tooltip></el-table-column>
			<el-table-column label="QQ" prop="qq" width="115"></el-table-column>
			<el-table-column label="所在页面" show-overflow-tooltip>
				<template v-slot="scope">
					<el-link type="success" :href="`/blog/${scope.row.blog.id}`" target="_blank" v-if="scope.row.page===0">{{ scope.row.blog.title }}</el-link>
					<el-link type="success" :href="'/about'" target="_blank" v-else-if="scope.row.page===1">关于我</el-link>
					<el-link type="success" :href="'/friends'" target="_blank" v-else-if="scope.row.page===2">友人帐</el-link>
				</template>
			</el-table-column>
			<el-table-column label="发表时间" width="170">
				<template v-slot="scope">{{ scope.row.createTime | dateFormat }}</template>
			</el-table-column>
			<el-table-column label="是否公开" width="80">
				<template v-slot="scope">
					<el-switch v-model="scope.row.published" @change="commentPublishedChanged(scope.row)"></el-switch>
				</template>
			</el-table-column>
			<el-table-column label="邮件提醒" width="80">
				<template v-slot="scope">
					<el-switch v-model="scope.row.notice" @change="commentNoticeChanged(scope.row)"></el-switch>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="200">
				<template v-slot="scope">
					<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row)">编辑</el-button>
					<el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteCommentById(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--分页-->
		<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryInfo.pageNum"
		               :page-sizes="[10, 20, 30, 50]" :page-size="queryInfo.pageSize" :total="total"
		               layout="total, sizes, prev, pager, next, jumper" background>
		</el-pagination>

		<!--编辑评论对话框-->
		<el-dialog title="编辑评论" width="50%" :visible.sync="editDialogVisible" :close-on-click-modal="false" @close="editDialogClosed">
			<!--内容主体-->
			<el-form :model="editForm" :rules="formRules" ref="editFormRef" label-width="80px">
				<el-form-item label="昵称" prop="nickname">
					<el-input v-model="editForm.nickname"></el-input>
				</el-form-item>
				<el-form-item label="头像" prop="avatar">
					<el-input v-model="editForm.avatar"></el-input>
				</el-form-item>
				<el-form-item label="邮箱" prop="email">
					<el-input v-model="editForm.email"></el-input>
				</el-form-item>
				<el-form-item label="网站" prop="website">
					<el-input v-model="editForm.website"></el-input>
				</el-form-item>
				<el-form-item label="ip" prop="ip">
					<el-input v-model="editForm.ip"></el-input>
				</el-form-item>
				<el-form-item label="评论内容" prop="content">
					<el-input v-model="editForm.content" type="textarea" maxlength="250" :rows="5" show-word-limit></el-input>
				</el-form-item>
			</el-form>
			<!--底部-->
			<span slot="footer">
				<el-button @click="editDialogVisible=false">取 消</el-button>
				<el-button type="primary" @click="editComment">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getCommentListByQuery, getBlogList, updatePublished, updateNotice, deleteCommentById, editComment} from '@/api/comment'
	import {checkEmail} from "@/util/reg";

	export default {
		name: "CommentList",
		components: {
			Breadcrumb
		},
		data() {
			return {
				pageId: null,
				queryInfo: {
					page: null,
					blogId: null,
					pageNum: 1,
					pageSize: 10
				},
				total: 0,
				commentList: [],
				blogList: [],
				editDialogVisible: false,
				editForm: {
					id: null,
					nickname: '',
					avatar: '',
					email: '',
					website: null,
					ip: '',
					content: ''
				},
				formRules: {
					nickname: [{required: true, message: '请输入评论昵称', trigger: 'blur'}],
					avatar: [{required: true, message: '请输入评论头像', trigger: 'blur'}],
					email: [
						{required: true, message: '请输入评论邮箱', trigger: 'blur'},
						{validator: checkEmail, trigger: 'blur'}
					],
					ip: [
						{required: true, message: '请输入评论ip', trigger: 'blur'},
						// {validator: checkIpv4, trigger: 'blur'}
					],
					content: [
						{required: true, message: '请输入评论内容', trigger: 'blur'},
						{max: 255, message: '评论内容不可多于255个字符', trigger: 'blur'}
					],
				}
			}
		},
		created() {
			this.getCommentList()
			this.getBlogList()
		},
		methods: {
			getCommentList() {
				getCommentListByQuery(this.queryInfo).then(res => {
					this.commentList = res.data.list
					this.total = res.data.total
				})
			},
			getBlogList() {
				getBlogList().then(res => {
					this.blogList = res.data
					this.blogList.unshift({id: -2, title: '友人帐'})
					this.blogList.unshift({id: -1, title: '关于我'})
				})
			},
			search() {
				if (this.pageId === '') {
					this.queryInfo.page = null
					this.queryInfo.blogId = null
				} else if (this.pageId === -1) {
					this.queryInfo.page = 1
					this.queryInfo.blogId = null
				} else if (this.pageId === -2) {
					this.queryInfo.page = 2
					this.queryInfo.blogId = null
				} else {
					this.queryInfo.page = 0
					this.queryInfo.blogId = this.pageId
				}
				this.queryInfo.pageNum = 1
				this.queryInfo.pageSize = 10
				this.getCommentList()
			},
			//监听 pageSize 改变事件
			handleSizeChange(newSize) {
				this.queryInfo.pageSize = newSize
				this.getCommentList()
			},
			//监听页码改变事件
			handleCurrentChange(newPage) {
				this.queryInfo.pageNum = newPage
				this.getCommentList()
			},
			//切换评论公开状态（如果切换成隐藏，则该评论的所有子评论都修改为同样的隐藏状态）
			commentPublishedChanged(row) {
				if (row.published) {
					updatePublished(row.id, row.published).then(res => {
						this.msgSuccess(res.msg)
					})
				} else {
					//切换成隐藏状态
					let replyCommentList = []
					replyCommentList.push(row)
					this.getAllReplyCommentList(row, replyCommentList)

					updatePublished(row.id, row.published).then(res => {
						this.msgSuccess(res.msg)
						replyCommentList.forEach(comment => {
							comment.published = row.published
						})
					})
				}
			},
			//递归展开所有子评论
			getAllReplyCommentList(comment, replyCommentList) {
				comment.replyComments.forEach(replyComment => {
					replyCommentList.push(replyComment)
					this.getAllReplyCommentList(replyComment, replyCommentList)
				})
			},
			//切换评论邮件提醒状态
			commentNoticeChanged(row) {
				updateNotice(row.id, row.notice).then(res => {
					this.msgSuccess(res.msg);
				})
			},
			deleteCommentById(id) {
				this.$confirm('此操作将永久删除该评论<strong style="color: red">及其所有子评论</strong>，是否删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning',
					dangerouslyUseHTMLString: true
				}).then(() => {
					deleteCommentById(id).then(res => {
						this.msgSuccess(res.msg)
						this.getCommentList()
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			showEditDialog(row) {
				this.editForm = {...row}
				this.editDialogVisible = true
			},
			editDialogClosed() {
				this.editForm = {}
				this.$refs.editFormRef.resetFields()
			},
			editComment() {
				this.$refs.editFormRef.validate(valid => {
					if (valid) {
						const form = {
							id: this.editForm.id,
							nickname: this.editForm.nickname,
							avatar: this.editForm.avatar,
							email: this.editForm.email,
							website: this.editForm.website,
							ip: this.editForm.ip,
							content: this.editForm.content,
						}
						editComment(form).then(res => {
							this.msgSuccess(res.msg)
							this.editDialogVisible = false
							this.getCommentList()
						})
					}
				})
			}
		}
	}
</script>

<style scoped>

</style>