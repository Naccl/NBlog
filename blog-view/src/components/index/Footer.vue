<template>
	<footer class="ui inverted vertical segment m-padded-tb-large m-opacity">
		<div class="ui center aligned container">
			<div class="ui inverted divided stackable grid">

				<div class="three wide column">
					<div class="ui link list">
						<h4 class="ui inverted header m-text-thin m-text-spaced">手机看本站</h4>
						<div class="item">
							<img :src="footer.mobileQRSrc" class="ui rounded image" alt="" style="width: 100px">
						</div>
					</div>
				</div>

				<div class="six wide column">
					<h4 class="ui inverted header m-text-thin m-text-spaced">最新博客</h4>
					<div class="ui inverted link list">
						<a :href="item.href" target="_blank" v-for="(item,index) in footer.newBlogList" :key="index" class="item m-text-thin m-padded-tb-small">{{ item.title }}</a>
					</div>
				</div>

				<div class="seven wide column">
					<p id="hitokotoText" class="m-text-thin m-text-spaced m-opacity-mini">{{ footer.hitokoto.hitokoto }}</p>
					<p id="hitokotoFrom" class="m-text-thin m-text-spaced m-opacity-mini" style="float: right" v-text="footer.hitokoto.from?'——《'+footer.hitokoto.from+'》':''"></p>
				</div>
			</div>

			<div class="ui inverted section divider"></div>

			<p class="m-text-thin m-text-spaced m-opacity-tiny">
				<span style="margin-right: 10px">{{ footer.copyright.time }}</span>
				<a href="/" target="_blank" style="color:#ffe500">{{ footer.copyright.title }}</a>
				<span style="margin: 0 15px">|</span>
				<img src="~assets/img/beian.png" alt="" class="beian">
				<a rel="external nofollow noopener" href="http://www.beian.miit.gov.cn/" target="_blank" style="color:#ffe500">{{ footer.copyright.beianId }}</a>
			</p>

			<div class="github-badge" v-for="(item,index) in footer.badges" :key="index">
				<a rel="external nofollow" :href="item.href" target="_blank" :title="item.title">
					<span class="badge-subject">{{ item.subject }}</span>
					<span class="badge-value" :class="'bg-'+item.color">{{ item.value }}</span>
				</a>
			</div>

		</div>
	</footer>
</template>

<script>
	export default {
		name: "Footer",
		props: {
			footer: {
				type: Object,
				required: true
			}
		}
	}
</script>

<style scoped>
	@import "../../assets/css/badge.css";

	.github-badge a {
		color: #fff;
	}

	.beian {
		width: 17px;
		height: 17px;
		margin-bottom: -4px;
		margin-right: 5px;
	}
</style>