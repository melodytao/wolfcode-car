<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";

const animationDuration = 6000;
import { statisticShopIncome } from "@/api/business/report.js";
export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "400px",
    },
  },
  data() {
    return {
      chart: null,
      barReportList: {
        statisticalDates: [],
        statisticalAmounts: [],
      },
      // 查询参数
      queryParams: {
        dateStatus: 0,
        pageNum: 1,
        pageSize: 10,
      },
    };
  },
  mounted() {
    this.getReportList();
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    getReportList() {
      statisticShopIncome(
        this.addDateRange(this.queryParams, this.dateRange)
      ).then((response) => {
        this.reportList = response.rows;
        this.total = response.total;
        this.queryParams.dateStatus = this.queryParams.dateStatus.toString();
        this.barReportList.statisticalDates = [];
        this.barReportList.statisticalAmounts = [];
        //bar
        this.reportList.forEach((item) => {
          this.barReportList.statisticalDates.push(item.statisticalDate);
          this.barReportList.statisticalAmounts.push(item.statisticalAmount);
        });
        this.$nextTick(() => {
          this.initChart();
        });
      });
    },
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");
      this.chart.setOption({
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        grid: {
          top: 10,
          left: "2%",
          right: "2%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: this.barReportList.statisticalDates,
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            axisTick: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: "店铺收入",
            type: "bar",
            stack: "vistors",
            barWidth: "30%",
            data: this.barReportList.statisticalAmounts,
            animationDuration,
          },
        ],
      });
    },
  },
};
</script>
