<template>
	<div>
		<div class="ui padded attached segment m-padded-tb-large">
			<a class="ui large red right corner label" v-if="blog.top">
				<i class="arrow alternate circle up icon"></i>
			</a>
			<div class="ui middle aligned mobile reversed stackable">
				<div class="ui grid m-margin-lr">
					<!--标题-->
					<div class="row m-padded-tb-small">
						<h2 class="ui header m-center">{{ blog.title }}</h2>
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
					<div class="typo m-padded-tb-small line-numbers match-braces rainbow-braces" v-html="blog.content"></div>
					<!--赞赏-->
					<el-popover placement="top" width="220" trigger="click" style="margin: 2em auto" v-if="blog.appreciation">
						<div class="ui orange basic label" style="width: 100%">
							<div class="image">
								<div style="font-size: 12px;text-align: center;margin-bottom: 5px;">一毛是鼓励</div>
								<img :src="$store.state.siteInfo.reward" alt="" class="ui rounded bordered image" style="width: 100%">
								<div style="font-size: 12px;text-align: center;margin-top: 5px;">一块是真爱</div>
							</div>
						</div>
						<el-button slot="reference" class="ui orange inverted circular button m-text-500">赞赏</el-button>
					</el-popover>
					<!--横线-->
					<el-divider></el-divider>
					<!--标签-->
					<div class="row m-padded-tb-no">
						<div class="column m-padding-left-no">
							<a :href="tag.id" class="ui tag label m-text-500 m-margin-small" :class="tag.color" v-for="tag in blog.tags" :key="tag.id">{{ tag.name }}</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--博客信息-->
		<div class="ui attached positive message">
			<ul class="list">
				<li>作者：{{ $store.state.introduction.name }}<a href="/about">（联系作者）</a></li>
				<li>发表时间：{{ blog.createTime | dateFormat('YYYY-MM-DD HH:mm')}}</li>
				<li>最后修改：{{ blog.updateTime | dateFormat('YYYY-MM-DD HH:mm')}}</li>
				<li>本站点采用<a href="https://creativecommons.org/licenses/by/4.0/"> 知识共享署名 4.0 </a>国际许可协议进行许可。可自由转载、引用，但需署名作者且注明文章出处。</li>
			</ul>
		</div>
		<!--评论-->
		<div>

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
				getBlogById(this.$route.params.id).then(res => {
					console.log(res)
					if (res.code === 200) {
						this.blog = res.data
						//v-html渲染完毕后，渲染代码块样式
						this.$nextTick(() => {
							Prism.highlightAll()
						})
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
	.el-divider {
		margin: 1rem 0 !important;
	}
</style>