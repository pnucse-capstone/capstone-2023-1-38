<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="사내 부서명" prop="deptName">
        <el-input
          v-model="queryParams.deptName"
          placeholder="부서명을 입력하세요"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="상태" prop="status">
        <el-select v-model="queryParams.status" placeholder="부서상태" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">검색</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">새로고침</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:dept:add']"
        >추가</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="deptList"
      row-key="deptId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="deptName" label="부서명" width="260"></el-table-column>
      <el-table-column prop="orderNum" label="순서" width="200"></el-table-column>
      <el-table-column prop="status" label="상태" :formatter="statusFormat" width="100"></el-table-column>
      <el-table-column label="등록일자" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="조작" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dept:edit']"
          >수정</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:dept:add']"
          >추가</el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dept:remove']"
          >삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="부서" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="상급 부서를 선택하세요" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="부서명" prop="deptName">
              <el-input v-model="form.deptName" placeholder="부서명을 입력하세요" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="순서" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="담당자" prop="leader">
              <el-input v-model="form.leader" placeholder="담당자를 입력하세요" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="연락처" prop="phone">
              <el-input v-model="form.phone" placeholder="연락처를 입력하세요" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="이메일" prop="email">
              <el-input v-model="form.email" placeholder="이메일 입력하세요" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="부서 상태">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">확 인</el-button>
        <el-button @click="cancel">취 소</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDept, getDept, delDept, addDept, updateDept, listDeptExcludeChild } from "@/get_post/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Dept",
  components: { Treeselect },
  data() {
    return {
      loading: true,

      showSearch: true,

      deptList: [],

      deptOptions: [],

      title: "",

      open: false,

      statusOptions: [],

      queryParams: {
        deptName: undefined,
        status: undefined
      },

      form: {},

      rules: {
        parentId: [
          { required: true, message: "Parent department cannot be empty", trigger: "blur" }
        ],
        deptName: [
          { required: true, message: "Department name cannot be empty", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "Menu order cannot be empty", trigger: "blur" }
        ],
        email: [
          {
            type: "email",
            message: "정확한 이메일 주소를 입력하세요",
            trigger: ["blur", "change"]
          }
        ],
        phone: [
          {
            pattern: /^01[0-9]{8,9}$/,
            message: "정확한 핸드폰 번호를 입력하세요",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {

    getList() {
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data, "deptId");
        this.loading = false;
      });
    },

    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },

    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    reset() {
      this.form = {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0"
      };
      this.resetForm("form");
    },

    handleQuery() {
      this.getList();
    },

    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleAdd(row) {
      this.reset();
      if (row != undefined) {
        this.form.parentId = row.deptId;
      }
      this.open = true;
      this.title = "부서 추가";
      listDept().then(response => {
	        this.deptOptions = this.handleTree(response.data, "deptId");
      });
    },

    handleUpdate(row) {
      this.reset();
      getDept(row.deptId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "부서 수정";
      });
      listDeptExcludeChild(row.deptId).then(response => {
	        this.deptOptions = this.handleTree(response.data, "deptId");
      });
    },

    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deptId != undefined) {
            updateDept(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("수정 성공");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addDept(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("추가 성공");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },

    handleDelete(row) {
      this.$confirm("이 부서를 삭제하시겠습니까?", "경고", {
          confirmButtonText: "확인",
          cancelButtonText: "취소",
          type: "warning"
        }).then(function() {
          return delDept(row.deptId);
        }).then(() => {
          this.getList();
          this.msgSuccess("삭제 성공");
        }).catch(function() {});
    }
  }
};
</script>
