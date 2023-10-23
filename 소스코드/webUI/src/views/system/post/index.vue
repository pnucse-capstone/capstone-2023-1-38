<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="직위ID" prop="postCode">
        <el-input
          v-model="queryParams.postCode"
          placeholder="직위ID 입력하세요"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="한글명" prop="postName">
        <el-input
          v-model="queryParams.postName"
          placeholder="직위 한글명을 입력하세요"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="상태" prop="status">
        <el-select v-model="queryParams.status" placeholder="직위 상태" clearable size="small">
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
          v-hasPermi="['system:post:add']"
        >추가</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:post:edit']"
        >수정</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:post:remove']"
        >삭제</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column label="직위ID" align="center" prop="postCode" />

      <el-table-column label="한글명" align="center" prop="postName" />
      <el-table-column label="상태" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="등록일자" align="center" prop="createTime" width="180">
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
            v-hasPermi="['system:post:edit']"
          >수정</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:menu:add']"
          >추가</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:post:remove']"
          >삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="한글명" prop="postName">
          <el-input v-model="form.postName" placeholder="직위 한글명을 입력하세요" />
        </el-form-item>
        <el-form-item label="직위ID" prop="postCode">
          <el-input v-model="form.postCode" placeholder="직위ID 입력하세요" />
        </el-form-item>

        <el-form-item label="상태" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="비고" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="내용을 입력하세요" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">확 인</el-button>
        <el-button @click="cancel">최 소</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPost, getPost, delPost, addPost, updatePost, exportPost } from "@/post_get/system/post";

export default {
  name: "Post",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      showSearch: true,
      total: 0,
      title: "",
      open: false,
      statusOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        postCode: undefined,
        postName: undefined,
        status: undefined
      },
      form: {},
      rules: {
        postName: [
          { required: true, message: "Position name cannot be empty", trigger: "blur" }
        ],
        postCode: [
          { required: true, message: "Position id cannot be empty", trigger: "blur" }
        ],
        postSort: [
          { required: true, message: "Position sorting cannot be empty", trigger: "blur" }
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
      listPost(this.queryParams).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: "0",
        remark: undefined
      };
      this.resetForm("form");
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.postId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "직위 추가";
    },

    handleUpdate(row) {
      this.reset();
      const postId = row.postId || this.ids
      getPost(postId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "직위 수정";
      });
    },

    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId != undefined) {
            updatePost(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("수정 성공");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addPost(this.form).then(response => {
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
      const postIds = row.postId || this.ids;
      this.$confirm("이 직위를 삭제하시겠습니까?", "경고", {
          confirmButtonText: "확인",
          cancelButtonText: "취소",
          type: "warning"
        }).then(function() {
          return delPost(postIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("삭제 성공");
        }).catch(function() {});
    }

  }
};
</script>
