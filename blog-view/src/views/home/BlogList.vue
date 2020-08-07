<template>
	<div>
		<!--content-->
		<BlogItem :blogList="blogList"/>
		<!--分页-->
		<Pagination :getBlogList="getBlogList"/>
	</div>
</template>

<script>
	import BlogItem from "@/components/BlogItem";
	import Pagination from "@/components/Pagination";

	export default {
		name: "BlogList",
		components: {Pagination, BlogItem},
		methods: {
			getBlogList(val) {
				console.log("getBlogList" + val)
			}
		},
		data() {
			return {
				blogList: [
					{
						id: 1,
						title: '文章的标题可以长一点点点111！',
						date: '2020-08-07',
						category: {
							id: 1,
							name: 'Java',
						},
						views: 1234,
						words: 1234,
						readTime: 1234,
						description: '<p>文章描述</p><p>文章描述文章描述文章描述文章描述</p><p>文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述</p><p>文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述</p><p><img src="img/avatar.jpg" style="max-width: 300px"></p>',
						tags: [
							{
								id: 1,
								name: 'Java',
								color: 'red'
							},
							{
								id: 2,
								name: 'Nginx',
								color: 'blue'
							},
							{
								id: 3,
								name: 'Spring Boot',
								color: 'purple'
							},
							{
								id: 4,
								name: 'Vue',
								color: 'red'
							},
							{
								id: 5,
								name: '卷积神经网络',
								color: 'purple'
							},
						]
					},
					{
						id: 2,
						title: '文章的标题可以长一点点点111！',
						date: '2020-08-07',
						category: {
							id: 1,
							name: 'Java',
						},
						views: 1234,
						words: 1234,
						readTime: 1234,
						description: '<p>文章描述</p><p>文章描述文章描述文章描述文章描述</p><p>文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述</p><p>文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述</p><p><img src="img/avatar.jpg" style="max-width: 300px"></p>',
						tags: [
							{
								id: 1,
								name: 'Java',
								color: 'red'
							},
							{
								id: 2,
								name: 'Nginx',
								color: 'blue'
							},
							{
								id: 3,
								name: 'Spring Boot',
								color: 'purple'
							},
							{
								id: 4,
								name: 'Vue',
								color: 'red'
							},
							{
								id: 5,
								name: '卷积神经网络',
								color: 'purple'
							},
						]
					},
					{
						id: 3,
						title: '文章的标题可以长一点点点111！',
						date: '2020-08-07',
						category: {
							id: 1,
							name: 'Java',
						},
						views: 1234,
						words: 1234,
						readTime: 1234,
						description: '<p>文章描述</p><p>文章描述文章描述文章描述文章描述</p><p>文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述</p><p>文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述文章描述</p><p><img src="img/avatar.jpg" style="max-width: 300px"></p>',
						tags: [
							{
								id: 1,
								name: 'Java',
								color: 'red'
							},
							{
								id: 2,
								name: 'Nginx',
								color: 'blue'
							},
							{
								id: 3,
								name: 'Spring Boot',
								color: 'purple'
							},
							{
								id: 4,
								name: 'Vue',
								color: 'red'
							},
							{
								id: 5,
								name: '卷积神经网络',
								color: 'purple'
							},
						]
					}
				]
			}
		}
	}
</script>

<style scoped>

</style>