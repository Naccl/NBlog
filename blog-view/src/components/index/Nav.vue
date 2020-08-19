<template>
	<div class="ui fixed inverted stackable pointing menu">
		<div class="ui container">
			<router-link to="/">
				<h3 class="ui header item m-blue">{{ blogName }}</h3>
			</router-link>
			<router-link to="/home" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='home'}">
				<i class="home icon"></i>首页
			</router-link>
			<sui-dropdown icon="idea icon" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='category'}">
				分类<i class="caret down icon"></i>
				<sui-dropdown-menu id="m-dropdown">
					<sui-dropdown-item @click="categoryRoute(category.id)" v-for="category in categoryList" :key="category.id">{{ category.name }}</sui-dropdown-item>
				</sui-dropdown-menu>
			</sui-dropdown>
			<router-link to="/tag" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='tag'}">
				<i class="tags icon"></i>标签
			</router-link>
			<router-link to="/archives" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='archives'}">
				<i class="clone icon"></i>归档
			</router-link>
			<router-link to="/moments" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='moments'}">
				<i class="comment alternate outline icon"></i>动态
			</router-link>
			<router-link to="/about" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='about'}">
				<i class="info icon"></i>关于我
			</router-link>
			<div class="right item ui inverted transparent icon input" :class="{'m-mobile-hide': mobileHide}">
				<input type="text" placeholder="Search...">
				<i class="search icon"></i>
			</div>
			<button class="ui menu black icon button m-right-top m-mobile-show" @click="toggle">
				<i class="sidebar icon"></i>
			</button>
		</div>
	</div>
</template>

<script>
	import {getCategoryList} from "@/api/category";

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
				mobileHide: true,
				categoryList: []
			}
		},
		created() {
			this.getCategoryList()
		},
		methods: {
			toggle() {
				this.mobileHide = !this.mobileHide
			},
			getCategoryList() {
				getCategoryList().then(res => {
					console.log(res)
					if (res.code === 200) {
						this.categoryList = res.data
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			categoryRoute(id) {
				this.$router.push(`/category/${id}`)
			}
		}
	}
</script>

<style scoped>
	#m-dropdown {
		background: #1b1c1d !important;
	}

	#m-dropdown .item {
		color: rgba(255, 255, 255, .9) !important;
	}

	#m-dropdown .item:hover {
		background: rgba(255, 255, 255, .08) !important;
	}

	.ui.fixed.menu .container {
		width: 1400px !important;
		margin-left: auto !important;
		margin-right: auto !important;
	}
</style>