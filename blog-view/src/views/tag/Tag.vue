<template>
	<div>
		<BlogList :getBlogList="getBlogList" :blogList="blogList" :totalPage="totalPage"/>
	</div>
</template>

<script>
	import BlogList from "@/components/blog/BlogList";
	import {getBlogListByTagId} from "@/network/tag";

	export default {
		name: "Tag",
		components: {BlogList},
		data() {
			return {
				blogList: [],
				totalPage: 0
			}
		},
		watch: {
			//在当前组件内路由到其它标签页时，要重新获取博客列表
			'$route.fullPath'() {
				this.getBlogList()
			}
		},
		created() {
			this.getBlogList()
		},
		computed: {
			tagId() {
				return parseInt(this.$route.params.id)
			}
		},
		methods: {
			getBlogList(pageNum) {
				getBlogListByTagId(this.tagId, pageNum).then(res => {
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