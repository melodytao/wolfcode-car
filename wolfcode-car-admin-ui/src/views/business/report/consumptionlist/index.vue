<template>
    <div>
      <el-main>
        <!-- 查询表单 -->
        <el-row>
          <el-form
            :model="queryParams"
            ref="queryForm"
            :inline="true"
            v-show="showSearch"
          >
            <el-form-item label="到店时间">
              <el-date-picker
                v-model="dateRange"
                style="width: 240px"
                value-format="yyyy-MM-dd HH:mm"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
  
            <el-form-item prop="dateStatus">
              <el-select
                v-model="queryParams.dateStatus"
                placeholder="请选择查询类型"
              >
                <el-option
                  v-for="dict in dict.type.report_date_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="serviceStatus">
              <el-select
                v-model="queryParams.serviceStatus"
                placeholder="请选择查询类型"
              >
                <el-option
                  v-for="dict in dict.type.report_service_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                icon="el-icon-search"
                size="mini"
                @click="handleQuery"
                >搜索</el-button
              >
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
                >重置</el-button
              >
            </el-form-item>
          </el-form>
        </el-row>
        <!-- 数据显示 -->
        <el-row :span="24">
          <el-col :span="12">
            <el-table v-loading="loading" :data="reportList">
              <el-table-column
                label="统计日期"
                prop="statisticalDate"
                align="center"
              />
              <el-table-column
                label="订单数量"
                prop="orderCount"
                align="center"
              />
            </el-table>
          </el-col>
          <el-col :span="12">
            <div ref="bar" class="chart" :style="{ height: '400px', width: '100%' }" />
          </el-col>
        </el-row>
      </el-main>
     <!-- 分页显示 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
  </template>
  <script>
  import { statisticConsumption } from "@/api/business/report.js";
  import echarts from "echarts";
  require("echarts/theme/macarons"); // echarts theme
  import resize from "../mixins/resize";
  const animationDuration = 6000;
  export default {
    mixins: [resize],
    name: "Report",
    dicts: ["report_date_type",'report_service_type'],
    data() {
      return {
        // 遮罩层
        laoding: false,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 日期范围
        dateRange: [],
        // 报表数据
        reportList: [],
        // 报表柱状图数据
        barReportList:{
          statisticalDates:[],
          consumptionList:[]
        },
        // 查询参数
        queryParams: {
          dateStatus: 0,
          serviceStatus: 0,
          pageNum: 1,
          pageSize: 10,
        },
        chart: null,
      };
    },
    created() {
      this.getList();
    },
    beforeDestroy() {
      if (!this.chart) {
        return;
      }
      this.chart.dispose();
      this.chart = null;
    },
    methods: {
      /**查询项目服务结算列表 */
      getList() {
        this.loading = true;
        statisticConsumption(
          this.addDateRange(this.queryParams, this.dateRange)
        ).then((response) => {
          console.log(response);
          this.reportList = response.rows;
          this.total = response.total;
          this.queryParams.dateStatus = this.queryParams.dateStatus.toString();
          this.queryParams.serviceStatus = this.queryParams.serviceStatus.toString();
          this.loading = false;
          //bar数据处理
          this.barReportList.statisticalDates=[];
          this.barReportList.consumptionList=[];
          this.reportList.forEach((item) => {
            this.barReportList.statisticalDates.push(item.statisticalDate);
            let consumption={value:item.orderCount,name:item.statisticalDate};
            this.barReportList.consumptionList.push(consumption);
          })
          //初始化barChart
          this.$nextTick(() => {
            this.initChart();
          });
        });
      },
      initChart() {
        this.chart = echarts.init(this.$refs.bar, "macarons");
        this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: this.barReportList.statisticalDates
        },
        series: [
          {
            name: '消费单统计',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: this.barReportList.consumptionList,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ],
        });
},
      // 表单重置
      reset() {
        this.resetForm("queryForm");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
    },
  };
  </script>
  <style>
  </style>
  
  