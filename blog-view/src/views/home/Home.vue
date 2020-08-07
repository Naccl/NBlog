<template>
	<div class="m-padded-tb-big">
		<div class="ui container">
			<div class="ui stackable grid">
				<!--左边博客列表-->
				<div class="twelve wide column">
					<BlogList/>
				</div>

				<!--侧边栏-->
<!--		<div class="four wide column"></div>-->

			</div>
		</div>
	</div>
</template>

<script>
	import BlogList from "./BlogList";
	export default {
		name: "Home",
		components: {BlogList}
	}
</script>

<style scoped>

</style>