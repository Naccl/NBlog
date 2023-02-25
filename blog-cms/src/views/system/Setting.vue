<template>
	<div>
		<el-tabs type="border-card">
			<el-tab-pane label="博客配置">
				<el-form label-position="right" label-width="130px">
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type1" :key="item.id">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>
				</el-form>
			</el-tab-pane>

			<el-tab-pane label="鉴权配置">
				<el-form label-position="right" label-width="130px">
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type2" :key="item.id">
						<el-input v-model="tokenExpireTime" size="mini" v-if="item.nameEn === 'token_expire_time'" @blur="tokenExpireTimeChange" style="width: 100px">
							<i slot="suffix" style="font-style: normal; margin-right: 10px">天</i>
						</el-input>
						<el-input v-model="item.value" size="mini" v-else></el-input>
					</el-form-item>
				</el-form>
			</el-tab-pane>

			<el-tab-pane label="评论配置">
				<el-form label-position="right" label-width="130px">
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type3" :key="item.id">
						<el-switch v-model="item.value" size="mini" v-if="item.nameEn === 'comment_default_open'"></el-switch>
						<el-select v-model="item.value" size="mini" v-if="item.nameEn === 'comment_notify_channel'" @change="commentNotifyChannelChange">
							<el-option label="无" value="none"></el-option>
							<el-option label="邮箱" value="mail"></el-option>
							<el-option label="Telegram" value="tg"></el-option>
						</el-select>
					</el-form-item>

					<el-form-item :label="item.nameZh" v-for="item in typeMap.type4" :key="item.id" v-if="commentNotifyChannel === 'mail'">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>

					<el-form-item :label="item.nameZh" v-for="item in typeMap.type5" :key="item.id" v-if="commentNotifyChannel === 'tg' && ['tg_bot_api', 'tg_bot_token', 'tg_bot_chat_id'].indexOf(item.nameEn) > -1">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>

					<el-form-item :label="item.nameZh" v-for="item in typeMap.type5" :key="item.id" v-if="commentNotifyChannel === 'tg' && item.nameEn === 'tg_bot_use_proxy'">
						<el-switch v-model="item.value" size="mini" @change="tgBotUseProxyChange"></el-switch>
					</el-form-item>
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type5" :key="item.id"
					              v-if="commentNotifyChannel === 'tg' && tgBotUseProxy && ['tg_bot_proxy_server_host', 'tg_bot_proxy_server_port', 'tg_bot_proxy_server_timeout'].indexOf(item.nameEn) > -1">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>

					<el-form-item :label="item.nameZh" v-for="item in typeMap.type5" :key="item.id" v-if="commentNotifyChannel === 'tg' && item.nameEn === 'tg_bot_use_reverse_proxy'">
						<el-switch v-model="item.value" size="mini" @change="tgBotUseReverseProxyChange"></el-switch>
					</el-form-item>
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type5" :key="item.id" v-if="commentNotifyChannel === 'tg' && tgBotUseReverseProxy && item.nameEn === 'tg_bot_reverse_proxy_url'">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>

				</el-form>
			</el-tab-pane>

			<el-tab-pane label="图片存储">
				<el-form label-position="right" label-width="130px">
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type6" :key="item.id">
						<el-select v-model="item.value" size="mini" v-if="item.nameEn === 'upload_channel'" @change="uploadChannelChange">
							<el-option label="本地" value="local"></el-option>
							<el-option label="GitHub" value="github"></el-option>
							<el-option label="又拍云" value="upyun"></el-option>
						</el-select>
					</el-form-item>

					<el-form-item :label="item.nameZh" v-for="item in typeMap.type7" :key="item.id" v-if="uploadChannel === 'local'">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type8" :key="item.id" v-if="uploadChannel === 'github'">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>
					<el-form-item :label="item.nameZh" v-for="item in typeMap.type9" :key="item.id" v-if="uploadChannel === 'upyun'">
						<el-input v-model="item.value" size="mini"></el-input>
					</el-form-item>
				</el-form>
			</el-tab-pane>
		</el-tabs>

		<div style="text-align: left;margin-top: 20px">
			<el-button type="primary" icon="el-icon-check" @click="submit">全部保存</el-button>
		</div>
	</div>
</template>

<script>
import {getSystemSettingData, update} from "@/api/systemSetting";

export default {
	name: "Setting",
	data() {
		return {
			typeMap: {
				type1: [],
				type2: [],
				type3: [],
				type4: [],
				type5: [],
				type6: [],
				type7: [],
				type8: [],
				type9: [],
			},
			tokenExpireTime: null,
			commentNotifyChannel: 'none',
			tgBotUseProxy: false,
			tgBotUseReverseProxy: false,
			uploadChannel: 'local',
		}
	},
	created() {
		this.getData()
	},
	methods: {
		getData() {
			getSystemSettingData().then(res => {
				this.typeMap = res.data
				this.tokenExpireTime = this.findItemByNameEn(2, 'token_expire_time').value / 86400000
				this.initSwitch()
			})
		},
		initSwitch() {
			let commentDefaultOpen = this.findItemByNameEn(3, 'comment_default_open')
			commentDefaultOpen.value = commentDefaultOpen.value === 'true'

			let tgBotUseProxy = this.findItemByNameEn(5, 'tg_bot_use_proxy')
			this.tgBotUseProxy = tgBotUseProxy.value = tgBotUseProxy.value === 'true'

			let tgBotUseReverseProxy = this.findItemByNameEn(5, 'tg_bot_use_reverse_proxy')
			this.tgBotUseReverseProxy = tgBotUseReverseProxy.value = tgBotUseReverseProxy.value === 'true'

			this.commentNotifyChannel = this.findItemByNameEn(3, 'comment_notify_channel').value
			this.uploadChannel = this.findItemByNameEn(6, 'upload_channel').value
		},
		findItemByNameEn(type, nameEn) {
			for (const item of this.typeMap[`type${type}`]) {
				if (item.nameEn === nameEn) {
					return item
				}
			}
		},
		tokenExpireTimeChange() {
			let tokenExpireTime = this.findItemByNameEn(2, 'token_expire_time')
			if (this.tokenExpireTime <= 0 || this.tokenExpireTime > 365) {
				this.tokenExpireTime = 1
				tokenExpireTime.value = 86400000
			} else {
				tokenExpireTime.value = this.tokenExpireTime * 86400000
			}
		},
		commentNotifyChannelChange(newVal) {
			this.commentNotifyChannel = newVal
		},
		tgBotUseProxyChange(newVal) {
			this.tgBotUseProxy = newVal
		},
		tgBotUseReverseProxyChange(newVal) {
			this.tgBotUseReverseProxy = newVal
		},
		uploadChannelChange(newVal) {
			this.uploadChannel = newVal
		},
		submit() {
			let data = []
			for (const key in this.typeMap) {
				if (this.typeMap.hasOwnProperty(key)) {
					const element = this.typeMap[key]
					data = data.concat(element)
				}
			}
			update(data).then(res => {
				this.msgSuccess(res.msg)
			})
		},
	}
}
</script>

<style scoped>

</style>
