<template>
	<div>
		<div class="ui padded segment m-padded-tb-large m-margin-bottom-big m-box" v-for="item in blogList" :key="item.id">
			<div class="ui middle aligned mobile reversed stackable">
				<div class="ui grid m-margin-lr">
					<!--标题-->
					<div class="row m-padded-tb-small">
						<h2 class="ui header m-center m-scaleup">
							<a href="#" class="m-black">{{ item.title }}</a>
						</h2>
					</div>
					<!--文章简要信息-->
					<div class="row m-padded-tb-small">
						<div class="ui horizontal link list m-center">
							<div class="item m-datetime">
								<i class="small calendar icon"></i><span>{{ item.date }}</span>
							</div>
							<div class="item m-views">
								<i class="small eye icon"></i><span>{{ item.views }}</span>
							</div>
							<div class="item m-common-black">
								<i class="small pencil alternate icon"></i><span>字数≈{{ item.words }}字</span>
							</div>
							<div class="item m-common-black">
								<i class="small clock icon"></i><span>阅读时长≈{{ item.readTime }}分</span>
							</div>
						</div>
					</div>
					<a :href="item.id" class="ui orange large ribbon label">
						<i class="small folder open icon"></i><span class="m-text-500">{{ item.category.name }}</span>
					</a>
					<!--文章描述-->
					<div class="m-padded-tb-small m-description" v-html="item.description"></div>
					<!--阅读全文按钮-->
					<div class="row m-padded-tb-small">
						<a :href="item.id" class="color-btn">阅读全文</a>
					</div>
					<!--横线-->
					<div class="ui section divider m-margin-lr-no"></div>
					<!--标签-->
					<div class="row m-padded-tb-no">
						<div class="column m-padding-left-no">
							<a :href="tag.id" class="ui tag label m-text-500 m-margin-small" :class="tag.color" v-for="tag in item.tags" :key="tag.id">{{ tag.name }}</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "BlogItem",
		props: {
			blogList: {
				type: Array,
				required: true
			}
		}
	}
</script>

<style scoped>
	.ribbon.label {
		max-height: 30px;
		margin-bottom: 15px !important;
		left: -46px !important;
	}

	.m-datetime {
		color: #00a7e0 !important;
	}

	.m-datetime * {
		vertical-align: middle !important;
	}

	.m-datetime i {
		margin-right: 3px !important;
	}

	.m-views {
		color: #ff3f1f !important;
	}

	.m-views * {
		vertical-align: middle !important;
	}

	.m-views i {
		margin-right: 3px !important;
	}

	.m-common-black {
		color: #000 !important;
	}

	.m-common-black * {
		vertical-align: middle !important;
	}

	.m-common-black i {
		margin-right: 3px !important;
	}
</style>