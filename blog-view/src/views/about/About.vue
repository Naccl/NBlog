<template>
	<div>
		<div class="ui top attached segment m-padded-lr-big">
			<h2 class="m-text-500" style="text-align: center">{{ title }}</h2>
			<meting-js server="netease" type="song" :id="musicId" theme="#25CCF7" v-if="musicId!==''"></meting-js>
			<div class="typo content m-margin-top-large" v-html="content"></div>
		</div>
	</div>
</template>

<script>
	import {getAbout} from "@/api/about";

	export default {
		name: "About",
		data() {
			return {
				title: '',
				musicId: '',
				content: ''
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getAbout().then(res => {
					console.log(res)
					if (res.code === 200) {
						this.title = res.data.title
						this.musicId = res.data.musicId
						this.content = res.data.content
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

<style>
	.content ul li {
		letter-spacing: 1px !important;
	}
</style>