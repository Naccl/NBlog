<template>
	<div>
		<!--面包屑导航-->
		<Breadcrumb parentTitle="博客管理"/>

		<el-card>
			<el-form :model="form" :rules="formRules" ref="formRef" label-position="top">
				<el-form-item prop="title">
					<el-input v-model="form.title" placeholder="请输入标题" style="min-width: 500px">
						<el-select v-model="form.flag" placeholder="请选择类型" slot="prepend" :allow-create="true" :filterable="true" style="width: 160px">
							<el-option :label="item" :value="item" v-for="(item,index) in flagList" :key="index"></el-option>
						</el-select>
					</el-input>
				</el-form-item>

				<el-form-item label="文章描述" prop="description">
					<div id="vditor-description"></div>
				</el-form-item>

				<el-form-item label="文章正文" prop="content">
					<div id="vditor-content"></div>
				</el-form-item>

				<el-form-item label="分类" prop="cate">
					<el-select v-model="form.cate" placeholder="请选择分类（输入可添加新分类）" :allow-create="true" :filterable="true" style="width: 50%;">
						<el-option :label="item.name" :value="item.id" v-for="item in categoryList" :key="item.id"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item label="标签" prop="tagList">
					<el-select v-model="form.tagList" placeholder="请选择标签（输入可添加新标签）" :allow-create="true" :filterable="true" :multiple="true" style="width: 50%;">
						<el-option :label="item.name" :value="item.id" v-for="item in tagList" :key="item.id"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item label="字数" prop="words">
					<el-input v-model="form.words" placeholder="请输入文章字数" type="number" style="width: 50%;"></el-input>
				</el-form-item>

				<el-form-item label="阅读时长(分钟)" prop="readTime">
					<el-input v-model="form.readTime" placeholder="请输入阅读时长（可选）默认 Math.round(字数 / 200)" type="number" style="width: 50%;"></el-input>
				</el-form-item>

				<el-form-item label="浏览次数" prop="views">
					<el-input v-model="form.views" placeholder="请输入文章字数（可选）" type="number" style="width: 50%;"></el-input>
				</el-form-item>

				<el-form-item>
					<el-switch v-model="form.shareStatement" active-text="版权声明"></el-switch>
				</el-form-item>

				<el-form-item>
					<el-switch v-model="form.appreciation" active-text="赞赏"></el-switch>
				</el-form-item>

				<el-form-item>
					<el-switch v-model="form.recommend" active-text="推荐"></el-switch>
				</el-form-item>

				<el-form-item>
					<el-switch v-model="form.commentEnabled" active-text="评论"></el-switch>
				</el-form-item>

				<el-form-item style="text-align: right;">
					<el-button type="info" @click="submit(false)">保存草稿</el-button>
					<el-button type="primary" @click="submit(true)">发布文章</el-button>
				</el-form-item>
			</el-form>
		</el-card>
	</div>
</template>

<script>
	import Breadcrumb from "@/components/Breadcrumb";
	import {getCategoryAndTag, saveBlog, getBlogById, updateBlog} from '@/network/blog'
	import Vditor from 'vditor'

	export default {
		name: "WriteBlog",
		components: {
			Breadcrumb
		},
		data() {
			return {
				ready: false,
				descriptionVditor: null,
				contentVditor: null,
				flagList: ['原创', '转载', '翻译'],
				categoryList: [],
				tagList: [],
				form: {
					title: '',
					flag: null,
					description: '',
					content: '',
					cate: null,
					tagList: [],
					words: null,
					readTime: null,
					views: 0,
					shareStatement: false,
					appreciation: false,
					recommend: false,
					commentEnabled: false,
					published: false
				},
				formRules: {
					title: [{required: true, message: '请输入标题', trigger: 'change'}],
					cate: [{required: true, message: '请选择分类', trigger: 'change'}],
					tagList: [{required: true, message: '请选择标签', trigger: 'change'}],
					words: [{required: true, message: '请输入文章字数', trigger: 'change'}],
				},
			}
		},
		watch: {
			'form.words'(newValue) {
				this.form.readTime = newValue ? Math.round(newValue / 200) : null
			},
		},
		created() {
			this.getData()
		},
		mounted() {
			this.initDescriptionVditor()
			this.initContentVditor()
		},
		methods: {
			//初始化md编辑器
			initDescriptionVditor() {
				const options = {
					height: 320,
					mode: 'ir',//即时渲染
					outline: true,//大纲
					cache: {//不缓存到localStorage
						enable: false,
					},
					resize: {//可调整高度
						enable: true
					},
					after: () => {
						if (!this.ready) {
							this.ready = true //两个编辑器都加载完成后执行，避免重复请求
						} else if (this.$route.params.id) {
							this.getBlog(this.$route.params.id)
						}
					}
				}
				this.descriptionVditor = new Vditor('vditor-description', options)
			},
			initContentVditor() {
				const options = {
					height: 640,
					mode: 'ir',//即时渲染
					outline: true,//大纲
					cache: {//不缓存到localStorage
						enable: false,
					},
					resize: {//可调整高度
						enable: true
					},
					after: () => {
						if (!this.ready) {
							this.ready = true
						} else if (this.$route.params.id) {
							this.getBlog(this.$route.params.id)
						}
					}
				}
				this.contentVditor = new Vditor('vditor-content', options)
			},
			getBlog(id) {
				getBlogById(id)
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.computeCategoryAndTag(res.data)
						this.msgSuccess(res.msg);
						this.form = res.data
						this.descriptionVditor.setValue(this.form.description)
						this.contentVditor.setValue(this.form.content)
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError('请求失败')
				})
			},
			computeCategoryAndTag(blog) {
				blog.cate = blog.category.id
				blog.tagList = []
				blog.tags.forEach(item => {
					blog.tagList.push(item.id)
				})
			},
			getData() {
				getCategoryAndTag()
				.then(res => {
					console.log(res)
					if (res.code === 200) {
						this.msgSuccess(res.msg);
						this.categoryList = res.data.categories
						this.tagList = res.data.tags
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError('请求失败')
				})
			},
			submit(published) {
				this.$refs.formRef.validate(valid => {
					if (valid) {
						this.form.description = this.descriptionVditor.getValue()
						this.form.content = this.contentVditor.getValue()
						this.form.published = published
						console.log(this.form)
						if (this.$route.params.id) {
							this.form.category = null
							this.form.tags = null
							updateBlog(this.form)
							.then(res => {
								console.log(res)
								if (res.code === 200) {
									this.msgSuccess(res.msg)
									this.$router.push('/blogs')
								} else {
									this.msgError(res.msg)
								}
							}).catch(() => {
								this.msgError('请求失败')
							})
						} else {
							saveBlog(this.form)
							.then(res => {
								console.log(res)
								if (res.code === 200) {
									this.msgSuccess(res.msg)
									this.$router.push('/blogs')
								} else {
									this.msgError(res.msg)
								}
							}).catch(() => {
								this.msgError('请求失败')
							})
						}
					} else {
						return this.msgError('请填写必要的表单项')
					}
				})
			}
		}
	}
</script>

<style scoped>
	@import "~vditor/dist/index.css";
</style>