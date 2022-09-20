<template>
  <div class="app-container">
    <!-- 搜索条件表单 -->
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="客户姓名:" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户姓名"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话号码:" prop="customerPhone">
        <el-input
          v-model="queryParams.customerPhone"
          placeholder="请输入电话号码"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号码:" prop="licensePlate">
        <el-input
          v-model="queryParams.licensePlate"
          placeholder="请输入车牌号码"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['appointment:appointment:add']"
          >新增</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      ref="tables"
      v-loading="loading"
      :data="appointmentList"
      :default-sort="defaultSort"
      @sort-change="handleSortChange"
    >
      <el-table-column
        label="客户名称"
        align="center"
        prop="customerName"
        width="80"
      />
      <el-table-column
        label="联系方式"
        align="center"
        prop="customerPhone"
        width="110"
      />
      <el-table-column
        label="预约时间"
        align="center"
        prop="appointmentTime"
        width="150"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.appointmentTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="到店时间"
        align="center"
        prop="actualArrivalTime"
        width="150"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actualArrivalTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="车牌号码" align="center" prop="licensePlate" />
      <el-table-column
        label="汽车类型"
        align="center"
        prop="carSeries"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="服务类型" align="center" prop="serviceType">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.cmi_service_type"
            :value="scope.row.serviceType"
          ></dict-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="info" :show-overflow-tooltip="true" />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        sortable="custom"
        :sort-orders="['descending', 'ascending']"
      >
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.cmi_status"
            :value="scope.row.status"
          ></dict-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="260">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['appointment:appointment:edit']"
            v-if="scope.row.status === 0"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleArrive(scope.row)"
            v-hasPermi="['appointment:appointment:arrive']"
            v-if="scope.row.status === 0"
            >到店</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="
              scope.row.status === 1 ||
              scope.row.status === 4 ||
              scope.row.status === 5
            "
            @click="handleStatement(scope.row)"
          >
            结算单
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.status === 0"
            @click="handleCancel(scope.row)"
          >
            取消
          </el-button>
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

    <!-- 添加或修改养修信息预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="联系方式" prop="customerPhone">
          <el-input
            v-model="form.customerPhone"
            placeholder="请输入客户联系方式"
          />
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker
            clearable
            v-model="form.appointmentTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择预约时间"
           
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="车牌号码" prop="licensePlate">
          <el-input v-model="form.licensePlate" placeholder="请输入车牌号码" />
        </el-form-item>
        <el-form-item label="汽车类型" prop="carSeries">
          <el-input v-model="form.carSeries" placeholder="请输入汽车类型" />
        </el-form-item>
        <el-form-item label="服务类型:" prop="serviceType">
          <el-select v-model="form.serviceType" clearable>
            <el-option
              v-for="dict in dict.type.cmi_service_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注信息" prop="info">
          <el-input v-model="form.info" placeholder="请输入备注信息" />
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
import { validatePhone, validateLicensePlate } from "@/utils/validate";
import {
  listAppointment,
  getAppointment,
  delAppointment,
  addAppointment,
  updateAppointment,
  cancelAppointment,
  arrival,
} from "@/api/bussiness/appointment";

export default {
  name: "Appointment",
  dicts: ["cmi_service_type", "cmi_status"],
  data() {
    return {
      // 限制时间选择当日之前时间
      pickerOptions:{
        disabledDate(time){
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
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
      // 养修信息预约表格数据
      appointmentList: [],
      defaultSort: { prop: "status", order: "ascending" },
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerName: null,
        customerPhone: null,
        appointmentTime: null,
        actualArrivalTime: null,
        licensePlate: null,
        carSeries: null,
        serviceType: null,
        info: null,
        status: null,
        orderByColumn: "status",
        isAsc: "ascending",
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        customerName:[
          {required: true, message: "客户名称不能为空",trigger:"blur"}
        ],
        customerPhone:[
          {required: true, message: "联系方式不能为空",trigger:"blur"},
          {validator: validatePhone, trigger: "blur"}
        ],
        appointmentTime:[
        {required: true, message: "预约时间不能为空",trigger:"blur"}
        ],
        licensePlate: [
          { required: true, message: "车牌号码不能为空", trigger: "blur" },
          { validator: validateLicensePlate, trigger: "blur" },
        ],
        carSeries: [
          { required: true, message: "汽车类型不能为空", trigger: "blur" },
        ],
        serviceType: [
          { required: true, message: "服务类型不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询养修信息预约列表 */
    getList() {
      this.loading = true;
      listAppointment(this.queryParams).then((response) => {
        this.appointmentList = response.rows;
        console.log(this.appointmentList);
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
        customerName: null,
        customerPhone: null,
        appointmentTime: null,
        actualArrivalTime: null,
        licensePlate: null,
        carSeries: null,
        serviceType: null,
        createTime: null,
        info: null,
        status: 0,
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
      this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order);
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加养修信息预约";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAppointment(id).then((response) => {
        this.form = response.data;
        // 编辑时候-数字转换具体的文字
        this.form.serviceType = this.form.serviceType.toString();
        this.open = true;
        this.title = "修改养修信息预约";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateAppointment(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppointment(this.form).then((response) => {
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
        .confirm('是否确认删除养修信息预约编号为"' + ids + '"的数据项？')
        .then(function () {
          return delAppointment(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    // 排序触发事件
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /** 到店按钮操作*/
    handleArrive(row) {
      const id = row.id;
      this.$modal
        .confirm("客户是否到店?")
        .then(function () {
          return arrival(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("操作成功");
        })
        .catch(() => {});
    },
    handleStatement(row) {},
    handleCancel(row) {
      const id = row.id;
      this.$modal
        .confirm("用户是否取消预约?")
        .then(function () {
          return cancelAppointment(id);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("操作成功");
        })
        .catch(() => {});
    },
  },
};
</script>
<style lang="scss" scoped>
  .el-date-editor.el-input {
    width: 100% !important;
  }
  
  .el-select {
    width: 100%;
  }
</style>  