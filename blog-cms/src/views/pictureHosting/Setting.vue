<template>
	<div>
		<el-alert title="图床配置及用法请查看：https://github.com/Naccl/PictureHosting" type="warning" show-icon v-if="hintShow"></el-alert>
		<el-card>
			<div slot="header">
				<span>GitHub配置</span>
			</div>
			<el-row>
				<el-col>
					<el-input placeholder="请输入token进行初始化" v-model="githubToken" :clearable="true" @keyup.native.enter="searchGithubUser" style="min-width: 500px">
						<el-button slot="append" icon="el-icon-search" @click="searchGithubUser">查询</el-button>
					</el-input>
				</el-col>
			</el-row>
			<el-row>
				<el-col>
					<span class="middle">当前用户：</span>
					<el-avatar :size="50" :src="githubUserInfo.avatar_url">User</el-avatar>
					<span class="middle">{{ githubUserInfo.login }}</span>
				</el-col>
			</el-row>
			<el-row>
				<el-col>
					<el-button type="primary" size="medium" icon="el-icon-check" :disabled="!isGithubSave" @click="saveGithub(true)">保存配置</el-button>
					<el-button type="info" size="medium" icon="el-icon-close" @click="saveGithub(false)">清除配置</el-button>
				</el-col>
			</el-row>
		</el-card>

		<el-card>
			<div slot="header">
				<span>又拍云存储配置</span>
			</div>
			<el-form :model="upyunConfig" label-width="100px">
				<el-form-item label="操作员名称">
					<el-input v-model="upyunConfig.username"></el-input>
				</el-form-item>
				<el-form-item label="操作员密码">
					<el-input v-model="upyunConfig.password"></el-input>
				</el-form-item>
				<el-form-item label="存储空间名">
					<el-input v-model="upyunConfig.bucketName"></el-input>
				</el-form-item>
				<el-form-item label="CDN访问域名">
					<el-input v-model="upyunConfig.domain"></el-input>
				</el-form-item>
				<el-button type="primary" size="medium" icon="el-icon-check" :disabled="!isUpyunSave" @click="saveUpyun(true)">保存配置</el-button>
				<el-button type="info" size="medium" icon="el-icon-close" @click="saveUpyun(false)">清除配置</el-button>
			</el-form>
		</el-card>

		<el-card>
			<div slot="header">
				<span>腾讯云存储配置</span>
			</div>
			<el-form :model="txyunConfig" label-width="100px">
				<el-form-item label="secret-id">
					<el-input v-model="txyunConfig.secretId"></el-input>
				</el-form-item>
				<el-form-item label="secret-key">
					<el-input v-model="txyunConfig.secretKey"></el-input>
				</el-form-item>
				<el-form-item label="存储空间名">
					<el-input v-model="txyunConfig.bucketName"></el-input>
				</el-form-item>
				<el-form-item label="地域">
					<el-input v-model="txyunConfig.region"></el-input>
				</el-form-item>
				<el-form-item label="CDN访问域名">
					<el-input v-model="txyunConfig.domain"></el-input>
				</el-form-item>
				<el-button type="primary" size="medium" icon="el-icon-check" :disabled="!isTxyunSave" @click="saveTxyun(true)">保存配置</el-button>
				<el-button type="info" size="medium" icon="el-icon-close" @click="saveTxyun(false)">清除配置</el-button>
			</el-form>
		</el-card>
	</div>
</template>

<script>
import {getUserInfo} from "@/api/github";

export default {
	name: "Setting",
	data() {
		return {
			githubToken: '',
			githubUserInfo: {
				login: '未配置'
			},
			isGithubSave: false,
			hintShow: false,
			upyunConfig: {
				username: '',
				password: '',
				bucketName: '',
				domain: ''
			},
			txyunConfig: {
				secretId: '',
				secretKey: '',
				bucketName: '',
				region: '',
				domain: ''
			},
		}
	},
	computed: {
		isUpyunSave() {
			return this.upyunConfig.username && this.upyunConfig.password && this.upyunConfig.bucketName && this.upyunConfig.domain
		},
		isTxyunSave() {
			return this.txyunConfig.secretId && this.txyunConfig.secretKey && this.txyunConfig.bucketName && this.txyunConfig.region && this.txyunConfig.domain
		}
	},
	created() {
		this.githubToken = localStorage.getItem("githubToken")
		const githubUserInfo = localStorage.getItem('githubUserInfo')
		if (this.githubToken && githubUserInfo) {
			this.githubUserInfo = JSON.parse(githubUserInfo)
			this.isGithubSave = true
		} else {
			this.githubUserInfo = {login: '未配置'}
		}

		const upyunConfig = localStorage.getItem('upyunConfig')
		if (upyunConfig) {
			this.upyunConfig = JSON.parse(upyunConfig)
		}

		const txyunConfig = localStorage.getItem('txyunConfig')
		if (txyunConfig) {
			this.txyunConfig = JSON.parse(txyunConfig)
		}

		const userJson = window.localStorage.getItem('user') || '{}'
		const user = JSON.parse(userJson)
		if (userJson !== '{}' && user.role !== 'ROLE_admin') {
			//对于访客模式，增加个提示
			this.hintShow = true
		}
	}
	,
	methods: {
		// 获取用户信息
		searchGithubUser() {
			getUserInfo(this.githubToken).then(res => {
				this.githubUserInfo = res
				this.isGithubSave = true
			})
		}
		,
		saveGithub(save) {
			if (save) {
				localStorage.setItem('githubToken', this.githubToken)
				localStorage.setItem('githubUserInfo', JSON.stringify(this.githubUserInfo))
				this.msgSuccess('保存成功')
			} else {
				localStorage.removeItem('githubToken')
				localStorage.removeItem('githubUserInfo')
				this.msgSuccess('清除成功')
			}
		}
		,
		saveUpyun(save) {
			if (save) {
				localStorage.setItem('upyunToken', btoa(`${this.upyunConfig.username}:${this.upyunConfig.password}`))
				localStorage.setItem('upyunConfig', JSON.stringify(this.upyunConfig))
				this.msgSuccess('保存成功')
			} else {
				localStorage.removeItem('upyunConfig')
				this.msgSuccess('清除成功')
			}
		}
		,
		saveTxyun(save) {
			if (save) {
				localStorage.setItem('txyunConfig', JSON.stringify(this.txyunConfig))
				this.msgSuccess('保存成功')
			} else {
				localStorage.removeItem('txyunConfig')
				this.msgSuccess('清除成功')
			}
		}
	}
	,
}
</script>

<style scoped>
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

.el-card {
	width: 50%;
}

.el-card + .el-card {
	margin-top: 20px;
}
</style>
