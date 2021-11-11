<template>
	<div>
		<el-alert title="图床配置及用法请查看：https://github.com/Naccl/PictureHosting" type="warning" show-icon v-if="hintShow"></el-alert>
		<el-row>
			<el-col :span="8">
				<el-input placeholder="请输入token进行初始化" v-model="token" :clearable="true" @keyup.native.enter="searchUser" style="min-width: 500px">
					<el-button slot="append" icon="el-icon-search" @click="searchUser">查询</el-button>
				</el-input>
			</el-col>
		</el-row>

		<el-row>
			<el-col :span="8">
				<span class="middle">当前用户：</span>
				<el-avatar :size="50" :src="userInfo.avatar_url">User</el-avatar>
				<span class="middle">{{ userInfo.login }}</span>
			</el-col>
		</el-row>

		<el-row>
			<el-col :span="8">
				<el-button type="primary" size="medium" icon="el-icon-check" :disabled="isSave" @click="saveUser(true)">保存配置</el-button>
				<el-button type="info" size="medium" icon="el-icon-close" @click="saveUser(false)">清除配置</el-button>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import {getUserInfo} from "@/api/github";

	export default {
		name: "Setting",
		data() {
			return {
				token: '',
				userInfo: {
					login: '未配置'
				},
				isSave: true,
				hintShow: false
			}
		},
		created() {
			this.token = localStorage.getItem("githubToken")
			const userInfo = localStorage.getItem('githubUserInfo')
			if (this.token && userInfo) {
				this.userInfo = JSON.parse(userInfo)
				this.isSave = false
			} else {
				this.userInfo = {login: '未配置'}
			}

			const userJson = window.localStorage.getItem('user') || '{}'
			const user = JSON.parse(userJson)
			if (userJson !== '{}' && user.role !== 'ROLE_admin') {
				//对于访客模式，增加个提示
				this.hintShow = true
			}
		},
		methods: {
			// 获取用户信息
			searchUser() {
				getUserInfo(this.token).then(res => {
					this.userInfo = res
					this.isSave = false
				})
			},
			saveUser(save) {
				if (save) {
					localStorage.setItem('githubToken', this.token)
					localStorage.setItem('githubUserInfo', JSON.stringify(this.userInfo))
					this.msgSuccess('保存成功')
				} else {
					localStorage.removeItem('githubToken')
					localStorage.removeItem('githubUserInfo')
					this.msgSuccess('清除成功')
				}
			},
		},
	}
</script>

<style>
	.el-alert + .el-row, .el-row + .el-row {
		margin-top: 20px;
	}

	.el-avatar {
		vertical-align: middle;
		margin-right: 15px;
	}

	.middle {
		vertical-align: middle;
	}
</style>