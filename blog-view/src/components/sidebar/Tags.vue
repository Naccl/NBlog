<template>
	<!--标签云-->
	<div class="ui segments m-box">
		<div class="ui secondary segment"><i class="tags icon"></i>标签云</div>
		<div class="ui yellow segment m-padding-small">
			<router-link :to="`/tag/${tag.id}`" class="ui label m-text-500" :class="tag.color" v-for="tag in tagList" :key="tag.id">
				{{ tag.name }}
			</router-link>
		</div>
	</div>
</template>

<script>
	import {getTags} from "@/api/index";

	export default {
		name: "Tags",
		data() {
			return {
				tagList: []
			}
		},
		created() {
			this.getTagList()
		},
		methods: {
			getTagList() {
				getTags().then(res => {
					console.log(res)
					if (res.code === 200) {
						this.tagList = res.data
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
	.secondary.segment {
		padding: 10px;
	}

	.m-padding-small {
		padding: 7px;
	}

	.label {
		margin: 3px !important;
	}
</style>