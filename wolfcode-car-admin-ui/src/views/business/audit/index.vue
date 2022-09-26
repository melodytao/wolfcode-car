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
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.package_audit_status"
            :value="scope.row.status"
          ></dict-tag>
        </template>
      </el-table-column>
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
            @click="handleUpdate(scope.row)"
            v-hasPermi="['audit:audit:edit']"
            >审批历史</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-info"
            @click="handleDelete(scope.row)"
            v-hasPermi="['audit:audit:remove']"
            >进度查看</el-button
          >

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['audit:audit:remove']"
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

    <!-- 添加或修改套餐审核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="服务单项id" prop="serviceItemId">
          <el-input
            v-model="form.serviceItemId"
            placeholder="请输入服务单项id"
          />
        </el-form-item>
        <el-form-item label="${comment}" prop="serviceItemName">
          <el-input
            v-model="form.serviceItemName"
            placeholder="请输入${comment}"
          />
        </el-form-item>
        <el-form-item label="服务单项备注" prop="serviceItemInfo">
          <el-input
            v-model="form.serviceItemInfo"
            placeholder="请输入服务单项备注"
          />
        </el-form-item>
        <el-form-item label="服务单项审核价格" prop="serviceItemPrice">
          <el-input
            v-model="form.serviceItemPrice"
            placeholder="请输入服务单项审核价格"
          />
        </el-form-item>
        <el-form-item label="流程实例id" prop="instanceId">
          <el-input v-model="form.instanceId" placeholder="请输入流程实例id" />
        </el-form-item>
        <el-form-item label="创建者" prop="creatorId">
          <el-input v-model="form.creatorId" placeholder="请输入创建者" />
        </el-form-item>
        <el-form-item label="备注" prop="info">
          <el-input v-model="form.info" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
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
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询套餐审核列表 */
    getList() {
      this.loading = true;
      listAudit(this.addDateRange(this.queryParams,this.dateRange)).then((response) => {
        this.auditList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "audit/audit/export",
        {
          ...this.queryParams,
        },
        `audit_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
  