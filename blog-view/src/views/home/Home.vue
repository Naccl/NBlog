<template>
	<div>
		<BlogList :getBlogList="getBlogList" :blogList="blogList" :totalPage="totalPage"/>
	</div>
</template>

<script>
	import BlogList from "@/components/blog/BlogList";
	import {getBlogList} from "@/api/home";
	import {SET_IS_BLOG_TO_HOME} from "../../store/mutations-types";

	export default {
		name: "Home",
		components: {BlogList},
		data() {
			return {
				blogList: [],
				totalPage: 0,
				getBlogListFinish: false
			}
		},
		beforeRouteEnter(to, from, next) {
			next(vm => {
				if (from.name !== 'blog') {
					//其它页面跳转到首页时，重新请求数据
					//设置一个flag，让首页的分页组件指向正确的页码
					vm.$store.commit(SET_IS_BLOG_TO_HOME, false)
					vm.getBlogList()
				} else {
					//如果文章页面是起始访问页，首页将是第一次进入，即缓存不存在，要请求数据
					if (!vm.getBlogListFinish) {
						vm.getBlogList()
					}
					//从文章页面跳转到首页时，使用首页缓存
					vm.$store.commit(SET_IS_BLOG_TO_HOME, true)
				}
			})
		},
		methods: {
			getBlogList(pageNum) {
				getBlogList(pageNum).then(res => {
					if (res.code === 200) {
						this.blogList = res.data.list
						this.totalPage = res.data.totalPage
						this.$nextTick(() => {
							Prism.highlightAll()
						})
						this.getBlogListFinish = true
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