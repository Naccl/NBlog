<template>
	<!--随机文章-->
	<div class="ui segments m-box">
		<div class="ui secondary segment"><i class="bookmark icon"></i>随机文章</div>
		<div class="ui yellow segment">
			<div class="ui divided items">
				<div class="item" v-for="blog in blogList" :key="blog.id">
					<div class="content">
						<router-link :to="`/blog/${blog.id}`" class="header m-text-500">{{ blog.title }}</router-link>
						<div class="meta">
							<router-link :to="`/category/${blog.category.name}`">
								<i class="folder open icon"></i>{{ blog.category.name }}
							</router-link>
						</div>
						<div class="extra">
							<router-link :to="`/tag/${tag.name}`" class="ui label m-text-500" :class="tag.color" v-for="tag in blog.tags" :key="tag.id">{{ tag.name }}</router-link>
							<router-link :to="`/blog/${blog.id}`" class="ui right floated">
								阅读全文<i class="angle double right icon"></i>
							</router-link>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {getRandomBlogs} from "@/api/index";

	export default {
		name: "RandomBlog",
		data() {
			return {
				blogList: []
			}
		},
		created() {
			this.getRandomBlogList()
		},
		methods: {
			getRandomBlogList() {
				getRandomBlogs().then(res => {
					console.log(res)
					if (res.code === 200) {
						this.blogList = res.data
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
	.secondary.segment {
		padding: 10px;
	}

	.header {
		font-size: 16px !important;
	}

	.meta {
		margin: 10px 0 !important;
	}

	.meta a {
		color: #000 !important;
	}
</style>