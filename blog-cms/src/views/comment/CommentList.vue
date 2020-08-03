<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="评论管理"/>

		<el-card>
			<!--搜索-->
			<el-row>
				<el-col :span="6">
					<el-select v-model="pageId" placeholder="请选择页面" :filterable="true" :clearable="true" @change="search">
						<el-option :label="item.title" :value="item.id" v-for="item in blogList" :key="item.id"></el-option>
					</el-select>
				</el-col>
			</el-row>

			<el-table :data="commentList" row-key="id" :tree-props="{children: 'replyComments'}" border stripe>
				<el-table-column label="序号" type="index" width="50"></el-table-column>
				<el-table-column label="昵称" prop="nickname">
					<template v-slot="scope">
						{{ scope.row.nickname }}
						<el-tag v-if="scope.row.adminComment" size="mini" effect="dark" style="margin-left: 5px">我</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="头像" width="80">
					<template v-slot="scope">
						<el-avatar shape="square" :size="60" fit="contain" :src="require('@/assets/img/comment-avatar/'+scope.row.avatar)"></el-avatar>
					</template>
				</el-table-column>
				<el-table-column label="邮箱" prop="email"></el-table-column>
				<el-table-column label="ip" prop="ip" width="130"></el-table-column>
				<el-table-column label="评论内容" prop="content"></el-table-column>
				<el-table-column label="所在页面">
					<template v-slot="scope">
						<el-link type="success" href="" target="_blank" v-if="scope.row.page==0">{{ scope.row.blog.title }}</el-link>
						<el-link type="success" href="" target="_blank" v-else-if="scope.row.page==1">关于我</el-link>
					</template>
				</el-table-column>
				<el-table-column label="发表时间" width="170">
					<template v-slot="scope">{{ scope.row.createTime | dateFormat}}</template>
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
						<el-button type="primary" icon="el-icon-edit" size="mini" @click="">编辑</el-button>
						<el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteCommentById(scope.row.id)">删除</el-button>
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
	import {getCommentListByQuery, getBlogList, updatePublished, updateNotice, deleteCommentById} from '@/network/comment'

	export default {
		name: "CommentList",
		components: {
			Breadcrumb
		},
		data() {
			return {
				pageId: null,
				queryInfo: {
					page: 0,
					blogId: null,
					pageNum: 1,
					pageSize: 10
				},
				total: 0,
				commentList: [],
				blogList: []
			}
		},
		created() {
			this.getCommentList()
			this.getBlogList()
		},
		methods: {
			getCommentList() {
				getCommentListByQuery(this.queryInfo)
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg);
						this.commentList = res.data.list
						this.total = res.data.total
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			getBlogList() {
				getBlogList().then(res => {
					console.log(res)
					if (res.code === 200) {
						this.blogList = res.data
						this.blogList.unshift({id: -1, title: '关于我'})
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			search() {
				if (this.pageId === -1) {
					this.queryInfo.page = 1
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
			//切换评论公开状态
			commentPublishedChanged(row) {
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
			//切换评论邮件提醒状态
			commentNoticeChanged(row) {
				updateNotice(row.id, row.notice)
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
			deleteCommentById(id) {
				this.$confirm('此操作将永久删除该评论<strong style="color: red">及其所有子评论</strong>，是否删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning',
					dangerouslyUseHTMLString: true
				}).then(() => {
					deleteCommentById(id).then(res => {
						console.log(res)
						if (res.code === 200) {
							this.msgSuccess(res.msg)
							this.getCommentList()
						} else {
							this.msgError(res.msg)
						}
					}).catch(() => {
						this.msgError("请求失败")
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			}
		}
	}
</script>

<style scoped>

</style>