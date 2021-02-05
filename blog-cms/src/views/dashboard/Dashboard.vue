<template>
	<div>
		<el-row class="panel-group" :gutter="50">
			<el-col :span="6">
				<el-card class="card-panel" :body-style="cardBodyStyle">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="#icon-pv" class-name="card-panel-icon"/>
					</div>
					<div class="card-panel-description">
						<div class="card-panel-text">今日PV</div>
						<span class="card-panel-num">{{ pv }}</span>
					</div>
				</el-card>
			</el-col>

			<el-col :span="6">
				<el-card class="card-panel" :body-style="cardBodyStyle">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="#icon-yonghu" class-name="card-panel-icon"/>
					</div>
					<div class="card-panel-description">
						<div class="card-panel-text">今日UV</div>
						<span class="card-panel-num">{{ uv }}</span>
					</div>
				</el-card>
			</el-col>

			<el-col :span="6">
				<el-card class="card-panel" :body-style="cardBodyStyle">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="#icon-article" class-name="card-panel-icon"/>
					</div>
					<div class="card-panel-description">
						<div class="card-panel-text">文章数</div>
						<span class="card-panel-num">{{ blogCount }}</span>
					</div>
				</el-card>
			</el-col>

			<el-col :span="6">
				<el-card class="card-panel" :body-style="cardBodyStyle">
					<div class="card-panel-icon-wrapper">
						<SvgIcon icon-class="#icon-pinglun-blue" class-name="card-panel-icon"/>
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
					<div id="categoryEcharts" style="height:500px;"></div>
				</el-card>
			</el-col>
			<el-col :span="8">
				<el-card>
					<div id="tagEcharts" style="height:500px;"></div>
				</el-card>
			</el-col>
			<el-col :span="8">
				<el-card>
					<div id="mapEcharts" style="height:500px;"></div>
				</el-card>
			</el-col>
		</el-row>

		<el-card class="panel-group">
			<div id="visitorEcharts" style="height:500px;"></div>
		</el-card>
	</div>
</template>

<script>
	import SvgIcon from "@/components/SvgIcon";
	import '@/assets/css/icon/iconfont'
	import echarts from 'echarts'
	import 'echarts/map/js/china'
	import {getDashboard} from "@/api/dashboard";

	export default {
		name: "Dashboard",
		components: {SvgIcon},
		data() {
			return {
				cardBodyStyle: {
					padding: 0
				},
				pv: 0,
				uv: 0,
				blogCount: 0,
				commentCount: 0,
				categoryEcharts: null,
				tagEcharts: null,
				mapEcharts: null,
				visitorEcharts: null,
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
						roam: false,// 一定要关闭拖拽
						zoom: 1.23,
						center: [105, 36], // 调整地图位置
						label: {
							normal: {
								show: false, //关闭省份名展示
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
								borderWidth: 1, //设置外层边框
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
				},
				data: {
					code: "success",
					data: {
						date: ["10-02", "10-03", "10-04", "10-05", "10-06", "10-07", "10-08"],
						uv: [29.0, 39.0, 26.0, 44.0, 42.0, 37.0, 52.0],
						pv: [137.0, 190.0, 128.0, 512.0, 219.0, 205.0, 507.0]
					}
				}
			}
		},
		mounted() {
			this.getData()
			this.initMapEcharts()
			this.initVisitorEcharts()
		},
		methods: {
			getData() {
				getDashboard().then(res => {
					if (res.code === 200) {
						this.pv = res.data.pv
						this.uv = res.data.uv
						this.blogCount = res.data.blogCount
						this.commentCount = res.data.commentCount

						this.categoryOption.legend.data = res.data.category.legend
						this.categoryOption.series[0].data = res.data.category.series
						this.initCategoryEcharts()

						this.tagOption.legend.data = res.data.tag.legend
						this.tagOption.series[0].data = res.data.tag.series
						this.initTagEcharts()
					} else {
						this.msgError(res.msg)
					}
				}).catch(() => {
					this.msgError("请求失败")
				})
			},
			initCategoryEcharts() {
				this.categoryEcharts = echarts.init(document.getElementById('categoryEcharts'), 'light')
				this.categoryEcharts.setOption(this.categoryOption)
			},
			initTagEcharts() {
				this.tagEcharts = echarts.init(document.getElementById('tagEcharts'), 'light')
				this.tagEcharts.setOption(this.tagOption)
			},
			initMapEcharts() {
				this.mapEcharts = echarts.init(document.getElementById('mapEcharts'))
				this.mapEcharts.setOption(this.mapOption)
				let dataValue = this.dealWithData()
				let data1 = dataValue.splice(0, 6)
				let mapOption = {
					series: [
						{
							type: "map",
							map: "china",
							roam: false,
							zoom: 1.23,
							center: [105, 36],
							showLegendSymbol: false, // 存在legend时显示
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
							data: dataValue,
							symbol: "circle",
							symbolSize: 8,
							hoverSymbolSize: 10,
							tooltip: {
								formatter(value) {
									return value.data.name + "<br/>" + "访客数：" + "22";
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
							data: data1,
							symbolSize: 15,
							tooltip: {
								show: false
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
				}
				this.mapEcharts.setOption(mapOption)
			},
			dealWithData() {
				let geoCoordMap = {
					海门: [121.15, 31.89],
					鄂尔多斯: [109.781327, 39.608266],
					招远: [120.38, 37.35],
					舟山: [122.207216, 29.985295],
					齐齐哈尔: [123.97, 47.33],
					盐城: [120.13, 33.38],
					赤峰: [118.87, 42.28],
					青岛: [120.33, 36.07],
					乳山: [121.52, 36.89],
					金昌: [102.188043, 38.520089],
					泉州: [118.58, 24.93],
					莱西: [120.53, 36.86],
					日照: [119.46, 35.42],
					胶南: [119.97, 35.88],
					南通: [121.05, 32.08],
					拉萨: [91.11, 29.97],
					云浮: [112.02, 22.93],
					梅州: [116.1, 24.55],
					文登: [122.05, 37.2],
					上海: [121.48, 31.22],
					攀枝花: [101.718637, 26.582347],
					威海: [122.1, 37.5],
					承德: [117.93, 40.97],
					厦门: [118.1, 24.46],
					汕尾: [115.375279, 22.786211],
					潮州: [116.63, 23.68],
					丹东: [124.37, 40.13],
					太仓: [121.1, 31.45],
					曲靖: [103.79, 25.51],
					烟台: [121.39, 37.52],
					福州: [119.3, 26.08],
					瓦房店: [121.979603, 39.627114],
					即墨: [120.45, 36.38],
					抚顺: [123.97, 41.97],
					玉溪: [102.52, 24.35],
					张家口: [114.87, 40.82],
					阳泉: [113.57, 37.85],
					莱州: [119.942327, 37.177017],
					湖州: [120.1, 30.86],
					汕头: [116.69, 23.39],
					昆山: [120.95, 31.39],
					宁波: [121.56, 29.86],
					湛江: [110.359377, 21.270708],
					揭阳: [116.35, 23.55],
					荣成: [122.41, 37.16],
					连云港: [119.16, 34.59],
					葫芦岛: [120.836932, 40.711052],
					常熟: [120.74, 31.64],
					东莞: [113.75, 23.04],
					河源: [114.68, 23.73],
					淮安: [119.15, 33.5],
					泰州: [119.9, 32.49],
					南宁: [108.33, 22.84],
					营口: [122.18, 40.65],
					惠州: [114.4, 23.09],
					江阴: [120.26, 31.91],
					蓬莱: [120.75, 37.8],
					韶关: [113.62, 24.84],
					嘉峪关: [98.289152, 39.77313],
					广州: [113.23, 23.16],
					延安: [109.47, 36.6],
					太原: [112.53, 37.87],
					清远: [113.01, 23.7],
					中山: [113.38, 22.52],
					昆明: [102.73, 25.04],
					寿光: [118.73, 36.86],
					盘锦: [122.070714, 41.119997],
					长治: [113.08, 36.18],
					溧阳: [119.48, 31.43],
					库尔勒: [86.06, 41.68],
					安阳: [114.35, 36.1],
					开封: [114.35, 34.79],
					济南: [117, 36.65],
					德阳: [104.37, 31.13],
					温州: [120.65, 28.01],
					九江: [115.97, 29.71],
					邯郸: [114.47, 36.6],
					临安: [119.72, 30.23],
					兰州: [103.73, 36.03],
					沧州: [116.83, 38.33],
					临沂: [118.35, 35.05],
					南充: [106.110698, 30.837793],
					天津: [117.2, 39.13],
					富阳: [119.95, 30.07],
					泰安: [117.13, 36.18],
					诸暨: [120.23, 29.71],
					郑州: [113.65, 34.76],
					哈尔滨: [126.63, 45.75],
					聊城: [115.97, 36.45],
					芜湖: [118.38, 31.33],
					唐山: [118.02, 39.63],
					平顶山: [113.29, 33.75],
					邢台: [114.48, 37.05],
					德州: [116.29, 37.45],
					济宁: [116.59, 35.38],
					荆州: [112.239741, 30.335165],
					宜昌: [111.3, 30.7],
					义乌: [120.06, 29.32],
					丽水: [119.92, 28.45],
					洛阳: [112.44, 34.7],
					秦皇岛: [119.57, 39.95],
					株洲: [113.16, 27.83],
					石家庄: [114.48, 38.03],
					莱芜: [117.67, 36.19],
					常德: [111.69, 29.05],
					保定: [115.48, 38.85],
					湘潭: [112.91, 27.87],
					金华: [119.64, 29.12],
					岳阳: [113.09, 29.37],
					长沙: [113, 28.21],
					衢州: [118.88, 28.97],
					廊坊: [116.7, 39.53],
					菏泽: [115.480656, 35.23375],
					合肥: [117.27, 31.86],
					武汉: [114.31, 30.52],
					大庆: [125.03, 46.58]
				};
				let data = [];
				for (let key in geoCoordMap) {
					data.push({name: key, value: geoCoordMap[key]});
				}
				return data;
			},
			initVisitorEcharts() {
				let visitorOption = {
					xAxis: {
						data: ["10-02", "10-03", "10-04", "10-05", "10-06", "10-07", "10-08"],
						boundaryGap: false,
						axisTick: {
							show: false
						}
					},
					grid: {
						left: 10,
						right: 10,
						bottom: 20,
						top: 30,
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
							name: '访问量(PV)', itemStyle: {
								normal: {
									color: '#FF005A',
									lineStyle: {
										color: '#FF005A',
										width: 2
									}
								}
							},
							smooth: true,
							type: 'line',
							data: [137, 190, 128, 512, 219, 205, 110],
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
							data: [29, 39, 26, 44, 42, 37, 13],
							animationDuration: 2800,
							animationEasing: 'quadraticOut'
						}
					]
				}
				this.visitorEcharts = echarts.init(document.getElementById('visitorEcharts'))
				this.visitorEcharts.setOption(visitorOption)
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