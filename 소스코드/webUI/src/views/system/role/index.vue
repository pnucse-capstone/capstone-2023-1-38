<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="역할명" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="역할명을 입력하세요"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="권한ID" prop="roleKey">
        <el-input
          v-model="queryParams.roleKey"
          placeholder="권한 문자열을 입력하세요"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="상태" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="역할 상태"
          clearable
          size="small"
          style="width: 240px"
        >
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
          v-hasPermi="['system:role:add']"
        >추가</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:role:edit']"
        >수정</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:role:remove']"
        >삭제</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="역할명" prop="roleName" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="관한ID" prop="roleKey" :show-overflow-tooltip="true" width="150" />

      <el-table-column label="상태" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
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
            v-hasPermi="['system:role:edit']"
          >수정</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleDataScope(scope.row)"
            v-hasPermi="['system:role:edit']"
          >권한 부여</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:role:remove']"
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
        <el-form-item label="역할명" prop="roleName">
          <el-input v-model="form.roleName" placeholder="역할명을 입력하세요" />
        </el-form-item>
        <el-form-item label="관한ID" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="관한 문자열을 입력하세요" />
        </el-form-item>

        <el-form-item label="상태">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="메뉴 권한">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">펼치기/접기</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">모두 선택/선택 안 함</el-checkbox>
          <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">상위/하위 메뉴 연동</el-checkbox>
          <el-tree
            class="tree-border"
            :data="menuOptions"
            show-checkbox
            ref="menu"
            node-key="id"
            :check-strictly="!form.menuCheckStrictly"
            empty-text="로딩 중"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
        <el-form-item label="비고">
          <el-input v-model="form.remark" type="textarea" placeholder="내용을 입력하세요"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">확 인</el-button>
        <el-button @click="cancel">최 소</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色数据权限对话框 -->
    <el-dialog :title="title" :visible.sync="openDataScope" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="역할명">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item label="관한 문자열">
          <el-input v-model="form.roleKey" :disabled="true" />
        </el-form-item>
        <el-form-item label="관한 범위 ">
          <el-select v-model="form.dataScope">
            <el-option
              v-for="item in dataScopeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="데이터 권한" v-show="form.dataScope == 2">
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">펼치기/접기</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">모두 선택/선택 안 함</el-checkbox>
          <el-checkbox v-model="form.deptCheckStrictly" @change="handleCheckedTreeConnect($event, 'dept')">상위/하위 메뉴 연동</el-checkbox>
          <el-tree
            class="tree-border"
            :data="deptOptions"
            show-checkbox
            default-expand-all
            ref="dept"
            node-key="id"
            :check-strictly="!form.deptCheckStrictly"
            empty-text="로딩 중"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">확 인</el-button>
        <el-button @click="cancelDataScope">최 소</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRole, getRole, delRole, addRole, updateRole, exportRole, dataScope, changeRoleStatus } from "@/get_post/system/role";
import { treeselect as menuTreeselect, roleMenuTreeselect } from "@/get_post/system/menu";
import { treeselect as deptTreeselect, roleDeptTreeselect } from "@/get_post/system/dept";

export default {
  name: "Role",
  data() {
    return {

      loading: true,

      ids: [],

      single: true,

      multiple: true,

      showSearch: true,

      total: 0,

      roleList: [],

      title: "",

      open: false,
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,

      dateRange: [],

      statusOptions: [],
      dataScopeOptions: [
        {
          value: "1",
          label: "모든 데이터 권한"
        },
        {
          value: "2",
          label: "권한 스스로 선택"
        },
        {
          value: "3",
          label: "자기 부서의 권한"
        },

      ],

      menuOptions: [],

      deptOptions: [],

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleName: undefined,
        roleKey: undefined,
        status: undefined
      },

      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },

      rules: {
        roleName: [
          { required: true, message: "Role name cannot be empty", trigger: "blur" }
        ],
        roleKey: [
          { required: true, message: "Permission character cannot be empty", trigger: "blur" }
        ],

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
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
          this.roleList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    getMenuTreeselect() {
      menuTreeselect().then(response => {
        this.menuOptions = response.data;
      });
    },

    getDeptTreeselect() {
      deptTreeselect().then(response => {
        this.deptOptions = response.data;
      });
    },

    getMenuAllCheckedKeys() {

      let checkedKeys = this.$refs.menu.getHalfCheckedKeys();
      let halfCheckedKeys = this.$refs.menu.getCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },

    getDeptAllCheckedKeys() {

      let checkedKeys = this.$refs.dept.getHalfCheckedKeys();
      let halfCheckedKeys = this.$refs.dept.getCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },

    getRoleMenuTreeselect(roleId) {
      return roleMenuTreeselect(roleId).then(response => {
        this.menuOptions = response.menus;
        return response;
      });
    },

    getRoleDeptTreeselect(roleId) {
      return roleDeptTreeselect(roleId).then(response => {
        this.deptOptions = response.depts;
        return response;
      });
    },

    handleStatusChange(row) {
      let text = row.status === "0" ? "open" : "close";
      this.$confirm('Are you sure you want to "' + text + '""' + row.roleName + '"this role?', "경고", {
          confirmButtonText: "확인",
          cancelButtonText: "취소",
          type: "warning"
        }).then(function() {
          return changeRoleStatus(row.roleId, row.status);
        }).then(() => {
          this.msgSuccess(text + "성공");
        }).catch(function() {
          row.status = row.status === "0" ? "1" : "0";
        });
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    cancelDataScope() {
      this.openDataScope = false;
      this.reset();
    },

    reset() {
      if (this.$refs.menu != undefined) {
        this.$refs.menu.setCheckedKeys([]);
      }
      this.menuExpand = false,
      this.menuNodeAll = false,
      this.deptExpand = true,
      this.deptNodeAll = false,
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: "0",
        menuIds: [],
        deptIds: [],
        menuCheckStrictly: true,
		deptCheckStrictly: true,
        remark: undefined
      };
      this.resetForm("form");
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },

    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
        }
      } else if (type == 'dept') {
        let treeList = this.deptOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
        }
      }
    },

    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
      } else if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
      }
    },

    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.form.menuCheckStrictly = value ? true: false;
      } else if (type == 'dept') {
        this.form.deptCheckStrictly = value ? true: false;
      }
    },

    handleAdd() {
      this.reset();
      this.getMenuTreeselect();
      this.open = true;
      this.title = "역할 추가";
    },

    handleUpdate(row) {
      this.reset();
      const roleId = row.roleId || this.ids
      const roleMenu = this.getRoleMenuTreeselect(roleId);
      getRole(roleId).then(response => {
        this.form = response.data;
        this.open = true;
        this.$nextTick(() => {
          roleMenu.then(res => {
            this.$refs.menu.setCheckedKeys(res.checkedKeys);
          });
        });
        this.title = "역할 수정";
      });
    },

    handleDataScope(row) {
      this.reset();
      const roleDeptTreeselect = this.getRoleDeptTreeselect(row.roleId);
      getRole(row.roleId).then(response => {
        this.form = response.data;
        this.openDataScope = true;
        this.$nextTick(() => {
          roleDeptTreeselect.then(res => {
            this.$refs.dept.setCheckedKeys(res.checkedKeys);
          });
        });
        this.title = "관한 부여";
      });
    },

    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roleId != undefined) {
            this.form.menuIds = this.getMenuAllCheckedKeys();
            updateRole(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("수정 성공");
                this.open = false;
                this.getList();
              }
            });
          } else {
            this.form.menuIds = this.getMenuAllCheckedKeys();
            addRole(this.form).then(response => {
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

    submitDataScope: function() {
      if (this.form.roleId != undefined) {
        this.form.deptIds = this.getDeptAllCheckedKeys();
        dataScope(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("수정 성공");
            this.openDataScope = false;
            this.getList();
          }
        });
      }
    },

    handleDelete(row) {
      const roleIds = row.roleId || this.ids;
      this.$confirm("이 역할을 삭제하시겠습니까?", "경고", {
          confirmButtonText: "확인",
          cancelButtonText: "취소",
          type: "warning"
        }).then(function() {
          return delRole(roleIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("삭제 성공");
        }).catch(function() {});
    }
  }
};
</script>
