<template>
	<div>
		<el-row class="panel-group" :gutter="50">
			<el-col :span="6">
				<el-card class="card-panel" body-style="padding: 0">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="pv" class-name="card-panel-icon"/>
					</div>
					<div class="card-panel-description">
						<div class="card-panel-text">今日PV</div>
						<span class="card-panel-num">{{ pv }}</span>
					</div>
				</el-card>
			</el-col>

			<el-col :span="6">
				<el-card class="card-panel" body-style="padding: 0">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="yonghu" class-name="card-panel-icon"/>
					</div>
					<div class="card-panel-description">
						<div class="card-panel-text">今日UV</div>
						<span class="card-panel-num">{{ uv }}</span>
					</div>
				</el-card>
			</el-col>

			<el-col :span="6">
				<el-card class="card-panel" body-style="padding: 0">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="article" class-name="card-panel-icon"/>
					</div>
					<div class="card-panel-description">
						<div class="card-panel-text">文章数</div>
						<span class="card-panel-num">{{ blogCount }}</span>
					</div>
				</el-card>
			</el-col>

			<el-col :span="6">
				<el-card class="card-panel" body-style="padding: 0">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="pinglun-blue" class-name="card-panel-icon"/>
					</div>
					<div class="card-panel-description">
						<div class="card-panel-text">评论数</div>
						<span class="card-panel-num">{{ commentCount }}</span>
					</div>
				</el-card>
			</el-col>
		</el-row>

		<el-row class="panel-group" :gutter="20">
			<el-col :span="8">
				<el-card>
					<div ref="categoryEcharts" style="height:500px;"></div>
				</el-card>
			</el-col>
			<el-col :span="8">
				<el-card>
					<div ref="tagEcharts" style="height:500px;"></div>
				</el-card>
			</el-col>
			<el-col :span="8">
				<el-card>
					<div ref="mapEcharts" style="height:500px;"></div>
				</el-card>
			</el-col>
		</el-row>

		<el-card class="panel-group">
			<div ref="visitRecordEcharts" style="height:500px;"></div>
		</el-card>
	</div>
</template>

<script>
	import SvgIcon from "@/components/SvgIcon";
	import echarts from 'echarts'
	import 'echarts/map/js/china'
	import {getDashboard} from "@/api/dashboard";
	//城市经纬度数据来自 https://github.com/Naccl/region2coord
	import geoCoordMap from '@/util/city2coord.json'

	export default {
		name: "Dashboard",
		components: {SvgIcon},
		data() {
			return {
				pv: 0,
				uv: 0,
				blogCount: 0,
				commentCount: 0,
				categoryEcharts: null,
				tagEcharts: null,
				mapEcharts: null,
				visitRecordEcharts: null,
				categoryOption: {
					title: {
						text: '分类下文章数量',
						x: 'center'
					},
					tooltip: {
						trigger: 'item',
						formatter: '{a} <br/>{b} : {c} ({d}%)'
					},
					legend: {
						left: 'center',
						top: 'bottom',
						data: []
					},
					series: [
						{
							name: '文章数量',
							type: 'pie',
							radius: [30, 110],
							roseType: 'area',
							data: []
						}
					]
				},
				tagOption: {
					title: {
						text: '标签下文章数量',
						x: 'center'
					},
					tooltip: {
						trigger: 'item',
						formatter: '{a} <br/>{b} : {c} ({d}%)'
					},
					legend: {
						left: 'center',
						top: 'bottom',
						data: []
					},
					series: [
						{
							name: '文章数量',
							top: '-10%',
							type: 'pie',
							radius: [30, 110],
							roseType: 'area',
							data: []
						}
					]
				},
				//地图效果 reference https://www.jianshu.com/p/028525cbd080
				//reference https://echarts.apache.org/examples/zh/editor.html?c=map-polygon
				mapOption: {
					title: {
						text: '访客地图',
						x: 'center'
					},
					tooltip: {
						show: false
					},
					geo: {
						map: "china",
						roam: false,//关闭拖拽
						zoom: 1.24,
						center: [104.2, 36],//调整地图位置
						label: {
							normal: {
								show: false,//关闭省份名展示
								fontSize: "10",
								color: "rgba(0,0,0,0.7)"
							},
							emphasis: {
								show: false
							}
						},
						itemStyle: {
							normal: {
								areaColor: "#0d0059",
								borderColor: "#389dff",
								borderWidth: 1,//设置外层边框
								shadowBlur: 5,
								shadowOffsetY: 8,
								shadowOffsetX: 0,
								shadowColor: "#01012a"
							},
							emphasis: {
								areaColor: "#184cff",
								shadowOffsetX: 0,
								shadowOffsetY: 0,
								shadowBlur: 5,
								borderWidth: 0,
								shadowColor: "rgba(0, 0, 0, 0.5)"
							}
						}
					},
					series: [
						{
							type: "map",
							map: "china",
							roam: false,
							zoom: 1.24,
							center: [104.2, 36],
							showLegendSymbol: false,
							label: {
								normal: {
									show: false
								},
								emphasis: {
									show: false
								}
							},
							itemStyle: {
								normal: {
									areaColor: "#0d0059",
									borderColor: "#389dff",
									borderWidth: 0.5
								},
								emphasis: {
									areaColor: "#17008d",
									shadowOffsetX: 0,
									shadowOffsetY: 0,
									shadowBlur: 5,
									borderWidth: 0,
									shadowColor: "rgba(0, 0, 0, 0.5)"
								}
							}
						},
						{
							name: "",
							type: "scatter",
							coordinateSystem: "geo",
							data: [],
							symbol: "circle",
							symbolSize: 5,
							hoverSymbolSize: 10,
							tooltip: {
								formatter(value) {
									return value.data.name + "<br/>" + "访客数：" + value.data.uv
								},
								show: true
							},
							encode: {
								value: 2
							},
							label: {
								formatter: "{b}",
								position: "right",
								show: false
							},
							itemStyle: {
								color: "#0efacc"
							},
							emphasis: {
								label: {
									show: false
								}
							}
						},
						{
							name: "Top 5",
							type: "effectScatter",
							coordinateSystem: "geo",
							data: [],
							symbol: "circle",
							symbolSize: 12,
							tooltip: {
								formatter(value) {
									return value.data.name + "<br/>" + "访客数：" + value.data.uv
								},
								show: true
							},
							encode: {
								value: 2
							},
							showEffectOn: "render",
							rippleEffect: {
								brushType: "stroke",
								color: "#0efacc",
								period: 9,
								scale: 5
							},
							hoverAnimation: true,
							label: {
								formatter: "{b}",
								position: "right",
								show: true
							},
							itemStyle: {
								color: "#0efacc",
								shadowBlur: 2,
								shadowColor: "#333"
							},
							zlevel: 1
						}
					]
				},
				visitRecordOption: {
					xAxis: {
						data: [],
						boundaryGap: false,
						axisTick: {
							show: false
						}
					},
					grid: {
						left: 10,
						right: 20,
						top: 30,
						bottom: 0,
						containLabel: true
					},
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'cross'
						},
						padding: [5, 10]
					},
					yAxis: {
						axisTick: {
							show: false
						}
					},
					legend: {
						data: ['访问量(PV)', '独立访客(UV)']
					},
					series: [
						{
							name: '访问量(PV)',
							smooth: true,
							type: 'line',
							itemStyle: {
								normal: {
									color: '#FF005A',
									lineStyle: {
										color: '#FF005A',
										width: 2
									}
								}
							},
							data: [],
							animationDuration: 2800,
							animationEasing: 'cubicInOut'
						},
						{
							name: '独立访客(UV)',
							smooth: true,
							type: 'line',
							itemStyle: {
								normal: {
									color: '#3888fa',
									lineStyle: {
										color: '#3888fa',
										width: 2
									},
									areaStyle: {
										color: '#f3f8ff'
									}
								}
							},
							data: [],
							animationDuration: 2800,
							animationEasing: 'quadraticOut'
						}
					]
				},
			}
		},
		mounted() {
			this.getData()
		},
		methods: {
			getData() {
				getDashboard().then(res => {
					this.pv = res.data.pv
					this.uv = res.data.uv
					this.blogCount = res.data.blogCount
					this.commentCount = res.data.commentCount
					//渲染分类数据
					this.categoryOption.legend.data = res.data.category.legend
					this.categoryOption.series[0].data = res.data.category.series
					this.initCategoryEcharts()
					//渲染标签数据
					this.tagOption.legend.data = res.data.tag.legend
					this.tagOption.series[0].data = res.data.tag.series
					this.initTagEcharts()
					//渲染访客地图数据
					let mapData = this.convertData(res.data.cityVisitor)
					this.mapOption.series[1].data = mapData
					this.mapOption.series[2].data = mapData.splice(0, 5)
					this.initMapEcharts()
					//渲染一周访问量数据
					this.visitRecordOption.xAxis.data = res.data.visitRecord.date
					this.visitRecordOption.series[0].data = res.data.visitRecord.pv
					this.visitRecordOption.series[1].data = res.data.visitRecord.uv
					this.initVisitRecordEcharts()
				})
			},
			initCategoryEcharts() {
				this.categoryEcharts = echarts.init(this.$refs.categoryEcharts, 'light')
				this.categoryEcharts.setOption(this.categoryOption)
			},
			initTagEcharts() {
				this.tagEcharts = echarts.init(this.$refs.tagEcharts, 'light')
				this.tagEcharts.setOption(this.tagOption)
			},
			initMapEcharts() {
				this.mapEcharts = echarts.init(this.$refs.mapEcharts)
				this.mapEcharts.setOption(this.mapOption)
			},
			convertData(data) {
				let res = []
				for (let i = 0; i < data.length; i++) {
					let geoCoord = geoCoordMap[data[i].city]
					if (geoCoord) {
						res.push({
							name: data[i].city,
							value: geoCoord,
							uv: data[i].uv
						})
					}
				}
				return res
			},
			initVisitRecordEcharts() {
				this.visitRecordEcharts = echarts.init(this.$refs.visitRecordEcharts)
				this.visitRecordEcharts.setOption(this.visitRecordOption)
			},
		}
	}
</script>

<style scoped>
	.panel-group {
		margin-bottom: 30px;
	}

	.panel-group .card-panel {
		height: 108px;
		position: relative;
		overflow: hidden;
	}

	.panel-group .card-panel .card-panel-icon-wrapper {
		float: left;
		margin: 14px 0 0 14px;
		padding: 16px;
	}

	.panel-group .card-panel .card-panel-icon {
		float: left;
		font-size: 48px;
	}

	.panel-group .card-panel .card-panel-description {
		float: right;
		font-weight: 700;
		margin: 26px 26px 26px 0;
	}

	.panel-group .card-panel .card-panel-description .card-panel-text {
		color: rgba(0, 0, 0, 0.45);
		font-size: 16px;
		margin-bottom: 12px;
	}

	.panel-group .card-panel .card-panel-description .card-panel-num {
		font-size: 20px;
	}
</style>