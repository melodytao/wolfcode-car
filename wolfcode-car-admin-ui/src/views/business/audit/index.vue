<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="queryParams.status" clearable>
          <el-option
            v-for="dict in dict.type.package_audit_status"
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

    <el-table v-loading="loading" :data="auditList">
      <el-table-column label="套餐名称" align="center" prop="serviceItemName" />
      <el-table-column
        label="套餐价格"
        align="center"
        prop="serviceItemPrice"
      />
      <el-table-column
        label="套餐备注"
        align="center"
        prop="serviceItemInfo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      ></el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.package_audit_status"
            :value="scope.row.status"
          ></dict-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="listHistory(scope.row.instanceId)"
            v-hasPermi="['business:carPackageAudit:history']"
            >审批历史</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-info"
            @click="viewProcess(scope.row.id)"
            v-hasPermi="['business:carPackageAudit:processImg']"
            >进度查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="cancelAudit(scope.row.id)"
            v-hasPermi="['business:carPackageAudit:cancel']"
            >撤销</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!--  审批历史对话框  -->
    <el-dialog
      title="审批历史"
      :visible.sync="historyDialog.open"
      width="1200px"
      append-to-body
    >
      <el-table
        ref="tables"
        v-loading="historyDialog.loading"
        :data="historyDialog.list"
      >
        <el-table-column label="任务名称" align="center" prop="taskName"  width="180"/>
        <el-table-column label="开始时间" align="center" prop="startTime" />
        <el-table-column
          label="结束时间"
          align="center"
          prop="endTime"
          width="180"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="耗时"
          align="center"
          prop="durationInMillis"
          width="180"
        >
        </el-table-column>
        <el-table-column
          label="审批意见"
          align="center"
          prop="comment"
          width="180"
        >
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeHistoryDialog">关 闭</el-button>
      </div>
    </el-dialog>
    <!--  流程进度图对话框  -->
    <el-dialog
      title="流程进度图"
      :visible.sync="processDialog.open"
      width="1200px"
      append-to-body
    >
      <div v-html="processDialog.img"></div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeProcessDialog">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
  <script>
import {
  listAudit,
  getAudit,
  delAudit,
  addAudit,
  updateAudit,
  carPackageAuditHistory,
  carPackageAuditProcess,
  cancelCarPackageAudit
} from "@/api/business/audit";

export default {
  name: "Audit",
  dicts: ["package_audit_status"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 套餐审核表格数据
      auditList: [],
      dateRange: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceItemId: null,
        serviceItemName: null,
        serviceItemInfo: null,
        serviceItemPrice: null,
        instanceId: null,
        creatorId: null,
        info: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
       // 审批历史弹窗
      historyDialog: {
        open: false, // 显示状态
        loading: false, // 加载状态
        list: [], // 列表数据
      },
      // 流程进度图弹窗
      processDialog: {
        open: false,
        img: '',
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询套餐审核列表 */
    getList() {
      this.loading = true;
      listAudit(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.auditList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        serviceItemId: null,
        serviceItemName: null,
        serviceItemInfo: null,
        serviceItemPrice: null,
        instanceId: null,
        creatorId: null,
        info: null,
        status: 0,
        createTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加套餐审核";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAudit(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改套餐审核";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateAudit(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAudit(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除套餐审核编号为"' + ids + '"的数据项？')
        .then(function () {
          return delAudit(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  // 审批历史
  listHistory(id) {
      this.historyDialog.open = true;
      this.historyDialog.loading = true;
      // 请求审批历史接口的方法
      carPackageAuditHistory(id).then((res) => {
        this.historyDialog.list = res.rows;
        this.historyDialog.loading = false;
      });
    },
    // 关闭审批历史弹窗
    closeHistoryDialog() {
      this.historyDialog.open = false;
    },

    // 查看进度
    viewProcess(id) {
      this.processDialog.open = true;
      carPackageAuditProcess(id).then((res) => {
        this.processDialog.img = res;
      });
    },
    // 关闭流程进度图弹窗
    closeProcessDialog() {
      this.processDialog.open = false
    },
    // 撤销审批
    cancelAudit(id) {
      this.$confirm("此操作将撤销审核, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        cancelCarPackageAudit(id).then((res) => {
          this.getList();
          this.$modal.msgSuccess("撤销成功");
        }).catch(() => {
          });
      })
    },
  },
};
</script>
  