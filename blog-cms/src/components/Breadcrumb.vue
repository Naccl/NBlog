<template>
	<el-breadcrumb separator-class="el-icon-arrow-right">
		<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
		<el-breadcrumb-item>{{ parentTitle }}</el-breadcrumb-item>
		<el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
	</el-breadcrumb>
</template>

<script>
	export default {
		name: "Breadcrumb",
		props: {
			parentTitle: {
				type: String,
				required: true
			}
		},
	}
</script>

<style scoped>

</style>