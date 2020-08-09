<template>
	<div class="m-padded-tb-big">
		<div class="ui container">
			<div class="ui stackable grid">
				<!--左边博客列表-->
				<div class="twelve wide column">
					<BlogList :getBlogList="getBlogList" :blogList="blogList" :totalPage="totalPage"/>
				</div>
				<!--侧边栏-->
				<div class="four wide column">
					<!--个人简介-->
					<Introduction/>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import BlogList from "@/components/blogList/BlogList";
	import Introduction from "@/components/sidebar/Introduction";
	import {getBlogList} from "@/network/home";

	export default {
		name: "Home",
		components: {Introduction, BlogList},
		data() {
			return {
				blogList: [],
				totalPage: 0
			}
		},
		created() {
			this.getBlogList()
		},
		methods: {
			getBlogList(pageNum) {
				getBlogList(pageNum).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.blogList = res.data.list
						this.totalPage = res.data.totalPage
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

</style>