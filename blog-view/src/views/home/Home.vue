<template>
	<div class="m-padded-tb-big">
		<div class="ui container">
			<div class="ui stackable grid">
				<!--左边博客列表-->
				<div class="twelve wide column">
					<BlogList/>
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
	import BlogList from "./BlogList";
	import Introduction from "@/components/sidebar/Introduction";

	export default {
		name: "Home",
		components: {Introduction, BlogList},
	}
</script>

<style scoped>

</style>