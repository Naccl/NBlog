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
					<sui-dropdown-item @click="categoryRoute(category.name)" v-for="(category,index) in categoryList" :key="index">{{ category.name }}</sui-dropdown-item>
				</sui-dropdown-menu>
			</sui-dropdown>
			<router-link to="/archives" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='archives'}">
				<i class="clone icon"></i>归档
			</router-link>
			<router-link to="/moments" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='moments'}">
				<i class="comment alternate outline icon"></i>动态
			</router-link>
			<router-link to="/friends" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='friends'}">
				<i class="users icon"></i>友人帐
			</router-link>
			<router-link to="/about" class="item" :class="{'m-mobile-hide': mobileHide,'active':$route.name==='about'}">
				<i class="info icon"></i>关于我
			</router-link>
			<el-autocomplete v-model="queryString" :fetch-suggestions="querySearchAsync" placeholder="Search..."
			                 class="right item m-search" :class="{'m-mobile-hide': mobileHide}"
			                 popper-class="m-search-item" @select="handleSelect">
				<i class="search icon el-input__icon" slot="suffix"></i>
				<template slot-scope="{ item }">
					<div class="title">{{ item.title }}</div>
					<span class="content">{{ item.content }}</span>
				</template>
			</el-autocomplete>
			<button class="ui menu black icon button m-right-top m-mobile-show" @click="toggle">
				<i class="sidebar icon"></i>
			</button>
		</div>
	</div>
</template>

<script>
	import {getCategoryList} from "@/api/category";
	import {getSearchBlogList} from "@/api/blog";

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
				categoryList: [],
				queryString: '',
				queryResult: [],
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
			categoryRoute(name) {
				this.$router.push(`/category/${name}`)
			},
			querySearchAsync(queryString, callback) {
				if (queryString == null
					|| queryString.trim() === ''
					|| queryString.indexOf('%') !== -1
					|| queryString.indexOf('_') !== -1
					|| queryString.indexOf('[') !== -1
					|| queryString.indexOf('#') !== -1
					|| queryString.indexOf('*') !== -1) {
					return
				}
				getSearchBlogList(queryString).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.queryResult = res.data
						if (this.queryResult.length === 0) {
							this.queryResult.push({title: '无相关搜索结果'})
						}
						callback(this.queryResult)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			handleSelect(item) {
				if (item.id) {
					this.$router.push(`/blog/${item.id}`)
				}
			}
		}
	}
</script>

<style>
	.ui.fixed.menu .container {
		width: 1400px !important;
		margin-left: auto !important;
		margin-right: auto !important;
	}

	#m-dropdown {
		background: #1b1c1d !important;
	}

	#m-dropdown .item {
		color: rgba(255, 255, 255, .9) !important;
	}

	#m-dropdown .item:hover {
		background: rgba(255, 255, 255, .08) !important;
	}

	.m-search {
		min-width: 220px;
		padding: 0 !important;
	}

	.m-search input {
		color: rgba(255, 255, 255, .9);;
		border: 0px !important;
		background-color: inherit;
		padding: .67857143em 2.1em .67857143em 1em;
	}

	.m-search i {
		color: rgba(255, 255, 255, .9) !important;
	}

	.m-search-item {
		min-width: 350px !important;
	}

	.m-search-item li {
		line-height: normal !important;
		padding: 8px 10px !important;
	}

	.m-search-item li .title {
		text-overflow: ellipsis;
		overflow: hidden;
		color: rgba(0, 0, 0, .87);
	}

	.m-search-item li .content {
		text-overflow: ellipsis;
		font-size: 12px;
		color: rgba(0, 0, 0, .70);
	}
</style>