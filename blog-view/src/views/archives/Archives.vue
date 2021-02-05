<template>
	<div>
		<div class="ui top attached segment" style="text-align: center">
			<h2 class="m-text-500">文章归档</h2>
			<p>好! 目前共计 {{ count }} 篇日志。 继续努力。</p>
		</div>
		<div class="ui attached segment">
			<div class="timeline">
				<div :class="colorObj[index%5]" v-for="(value,key,index) in blogMap" :key="index">
					<div class="tl-header">
						<a class="ui large label m-text-500">{{ key }}</a>
					</div>
					<div class="tl-item" v-for="blog in value" :key="blog.id">
						<div class="tl-wrap">
							<span class="tl-date">{{ blog.day }}</span>
							<a href="javascript:;" @click.prevent="toBlog(blog)">
								<div class="ui left pointing label tl-title">{{ blog.title }}</div>
							</a>
						</div>
					</div>
				</div>

				<div class="tl-header">
					<a class="ui black large label m-text-500">Hello World!</a>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {getArchives} from "@/api/archives";

	export default {
		name: "Archives",
		data() {
			return {
				blogMap: {},
				count: 0,
				colorObj: {
					0: 'tl-blue',
					1: 'tl-dark',
					2: 'tl-green',
					3: 'tl-purple',
					4: 'tl-red',
				}
			}
		},
		created() {
			this.getArchives()
		},
		methods: {
			getArchives() {
				getArchives().then(res => {
					if (res.code === 200) {
						this.blogMap = res.data.blogMap
						this.count = res.data.count
					} else {
						this.msgError(res.msg);
					}
				}).catch(() => {
					this.msgError("请求失败");
				})
			},
			toBlog(blog) {
				this.$store.dispatch('goBlogPage', blog)
			}
		}
	}
</script>

<style scoped>
	.timeline {
		margin: 20px 0;
	}

	.tl-header {
		width: 12em;
		text-align: center;
	}

	.tl-date {
		position: relative;
		top: 10px;
		display: block;
		float: left;
		width: 4.5em;
		margin-left: -7.5em;
		text-align: right;
	}

	.tl-wrap {
		padding: 15px 0 15px 20px;
		margin-left: 6em;
		border-style: solid;
		border-width: 0 0 0 4px;
	}

	.tl-wrap:before {
		position: relative;
		top: 15px;
		float: left;
		width: 10px;
		height: 10px;
		margin-left: -27px;
		background: #fff;
		border-color: inherit;
		border-style: solid;
		border-width: 3px;
		border-radius: 50%;
		content: "";
		box-shadow: 0 0 0 4px #fff;
	}

	.tl-wrap:hover:before {
		background: 0 0;
		border-color: #fff;
	}

	.tl-title {
		margin-left: 0 !important;
		letter-spacing: 0.3px !important;
		font-size: 14px !important;
		font-weight: 500 !important;
		padding: 12px 15px !important;
	}

	.tl-blue .tl-header a, .tl-blue .tl-item .tl-title {
		background: #23b7e5 !important;
		color: #fff !important;
	}

	.tl-blue .tl-item .tl-wrap {
		border-color: #23b7e5;
	}

	.tl-dark .tl-header a, .tl-dark .tl-item .tl-title {
		background: #3a3f51 !important;
		color: #fff !important;
	}

	.tl-dark .tl-item .tl-wrap {
		border-color: #3a3f51;
	}

	.tl-green .tl-header a, .tl-green .tl-item .tl-title {
		background: #27c24c !important;
		color: #fff !important;
	}

	.tl-green .tl-item .tl-wrap {
		border-color: #27c24c;
	}

	.tl-purple .tl-header a, .tl-purple .tl-item .tl-title {
		background: #7266ba !important;
		color: #fff !important;
	}

	.tl-purple .tl-item .tl-wrap {
		border-color: #7266ba;
	}

	.tl-red .tl-header a, .tl-red .tl-item .tl-title {
		background: #f05050 !important;
		color: #fff !important;
	}

	.tl-red .tl-item .tl-wrap {
		border-color: #f05050;
	}
</style>