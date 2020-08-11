<template>
	<div class="ui fixed inverted stackable menu">
		<div class="ui container">
			<a href="#">
				<h3 class="ui header item m-blue">{{ blogName }}</h3>
			</a>
			<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="idea icon"></i>分类</a>
			<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="tags icon"></i>标签</a>
			<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="clone icon"></i>归档</a>
			<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="info icon"></i>关于我</a>
			<div class="right item ui inverted transparent icon input" :class="{'m-mobile-hide': mobileHide}">
				<input type="text" placeholder="Search...">
				<i class="search icon"></i>
			</div>
			<a class="ui menu black icon button m-right-top m-mobile-show" @click="toggle">
				<i class="sidebar icon"></i>
			</a>
		</div>
	</div>
</template>

<script>
	export default {
		name: "Nav",
		props: {
			blogName: {
				type: String,
				required: true
			}
		},
		data() {
			return {
				mobileHide: true
			}
		},
		methods: {
			toggle() {
				this.mobileHide = !this.mobileHide
			}
		}
	}
</script>

<style scoped>
	.ui.fixed.menu .container {
		width: 1400px !important;
		margin-left: auto !important;
		margin-right: auto !important;
	}
</style>