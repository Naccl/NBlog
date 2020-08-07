<template>
	<nav class="ui inverted attached segment m-padded-tb-mini m-shadow-small">
		<div class="ui container">
			<div class="ui inverted secondary stackable menu">
				<h2 class="ui header item m-blue">{{ blogName }}</h2>
				<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="home icon"></i>首页</a>
				<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="idea icon"></i>分类</a>
				<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="tags icon"></i>标签</a>
				<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="clone icon"></i>归档</a>
				<a href="#" class="item" :class="{'m-mobile-hide': mobileHide}"><i class="info icon"></i>关于我</a>
				<div class="right item ui inverted transparent icon input" :class="{'m-mobile-hide': mobileHide}">
					<input type="text" placeholder="Search...">
					<i class="search icon"></i>
				</div>
			</div>
		</div>
		<a class="ui menu black icon button m-right-top m-mobile-show" @click="toggle">
			<i class="sidebar icon"></i>
		</a>
	</nav>
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
			toggle(){
				this.mobileHide = !this.mobileHide
			}
		}
	}
</script>

<style scoped>

</style>