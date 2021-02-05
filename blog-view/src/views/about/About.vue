<template>
	<div>
		<div class="ui top attached segment m-padded-lr-big">
			<h2 class="m-text-500" style="text-align: center">{{ about.title }}</h2>
			<meting-js server="netease" type="song" :id="about.musicId" theme="#25CCF7" v-if="about.musicId!==''"></meting-js>
			<div class="typo content m-margin-top-large" v-viewer v-html="about.content"></div>
		</div>
		<!--评论-->
		<div class="ui bottom teal attached segment threaded comments">
			<CommentList :page="1" :blogId="null" v-if="about.commentEnabled==='true'"/>
			<h3 class="ui header" v-else>评论已关闭</h3>
		</div>
	</div>
</template>

<script>
	import {getAbout} from "@/api/about";
	import CommentList from "@/components/comment/CommentList";

	export default {
		name: "About",
		components: {CommentList},
		data() {
			return {
				about: {
					title: '',
					musicId: '',
					content: '',
					commentEnabled: 'false'
				}
			}
		},
		created() {
			this.getData()
		},
		methods: {
			getData() {
				getAbout().then(res => {
					if (res.code === 200) {
						this.about = res.data
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