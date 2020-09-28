<template>
	<div>
		<BlogList :getBlogList="getBlogList" :blogList="blogList" :totalPage="totalPage"/>
	</div>
</template>

<script>
	import BlogList from "@/components/blog/BlogList";
	import {getBlogList} from "@/api/home";

	export default {
		name: "Home",
		components: {BlogList},
		data() {
			return {
				blogList: [],
				totalPage: 0
			}
		},
		beforeRouteEnter(to, from, next) {
			//从文章页面跳转到首页时，使用首页缓存
			//其它页面跳转到首页时，重新请求数据
			next(vm => {
				if (from.name !== 'blog') {
					vm.getBlogList()
				}
			})
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
						this.$nextTick(() => {
							Prism.highlightAll()
						})
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