<template>
	<div>
		<!--content-->
		<BlogItem :blogList="blogList"/>
		<!--分页-->
		<Pagination :getBlogList="getBlogList" :totalPage="totalPage"/>
	</div>
</template>

<script>
	import BlogItem from "@/components/blogList/BlogItem";
	import Pagination from "@/components/blogList/Pagination";

	export default {
		name: "BlogList",
		components: {Pagination, BlogItem},
		props: {
			getBlogList: {
				type: Function,
				required: true
			},
			blogList: {
				type: Array,
				required: true
			},
			totalPage: {
				type: Number,
				required: true
			}
		}
	}
</script>

<style scoped>

</style>