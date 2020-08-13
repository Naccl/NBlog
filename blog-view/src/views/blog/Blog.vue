<template>
	<div>
		<div class="ui padded segment m-padded-tb-large m-margin-bottom-big">
			<a class="ui large red right corner label" v-if="blog.top">
				<i class="arrow alternate circle up icon"></i>
			</a>
			<div class="ui middle aligned mobile reversed stackable">
				<div class="ui grid m-margin-lr">
					<!--标题-->
					<div class="row m-padded-tb-small">
						<h2 class="ui header m-center m-scaleup">
							<a href="#" class="m-black">{{ blog.title }}</a>
						</h2>
					</div>
					<!--文章简要信息-->
					<div class="row m-padded-tb-small">
						<div class="ui horizontal link list m-center">
							<div class="item m-datetime">
								<i class="small calendar icon"></i><span>{{ blog.createTime | dateFormat('YYYY-MM-DD')}}</span>
							</div>
							<div class="item m-views">
								<i class="small eye icon"></i><span>{{ blog.views }}</span>
							</div>
							<div class="item m-common-black">
								<i class="small pencil alternate icon"></i><span>字数≈{{ blog.words }}字</span>
							</div>
							<div class="item m-common-black">
								<i class="small clock icon"></i><span>阅读时长≈{{ blog.readTime }}分</span>
							</div>
						</div>
					</div>
					<!--分类-->
					<a :href="blog.category.id" class="ui orange large ribbon label" v-if="blog.category">
						<i class="small folder open icon"></i><span class="m-text-500">{{ blog.category.name }}</span>
					</a>
					<!--文章Markdown正文-->
					<div class="typo m-padded-tb-small m-markdown" v-html="blog.content"></div>
					<!--横线-->
					<div class="ui section divider m-margin-lr-no"></div>
					<!--标签-->
					<div class="row m-padded-tb-no">
						<div class="column m-padding-left-no">
							<a :href="tag.id" class="ui tag label m-text-500 m-margin-small" :class="tag.color" v-for="tag in blog.tags" :key="tag.id">{{ tag.name }}</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {getBlogById} from "@/network/blog";

	export default {
		name: "Blog",
		data() {
			return {
				blog: {}
			}
		},
		created() {
			this.getBlog()
		},
		methods: {
			getBlog() {
				getBlogById(30).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.blog = res.data
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
	.ui.section.divider {
		flex-grow: 1 !important;
		margin-top: 1rem !important;
		margin-bottom: 1rem !important;
	}
</style>