<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="메뉴명" prop="menuName">
        <el-input
          v-model="queryParams.menuName"
          placeholder="메뉴명을 입력하세요"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="상태" prop="status">
        <el-select v-model="queryParams.status" placeholder="메뉴상태" clearable size="small">
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

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>


    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="menuName" label="메뉴명" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="icon" label="아이콘" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="순서" width="60"></el-table-column>
      <el-table-column prop="perms" label="권한 문자열" :show-overflow-tooltip="true"></el-table-column>
<!--      权限标识-->
      <el-table-column prop="component" label="경로" :show-overflow-tooltip="true"></el-table-column>
<!--      组件路径-->
      <el-table-column prop="status" label="상태" :formatter="statusFormat" width="80"></el-table-column>
      <el-table-column label="등록일자" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="조작" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:menu:edit']"
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
            v-hasPermi="['system:menu:remove']"
          >삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="메뉴">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="상위메뉴를 선택하세요"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="메뉴 유형" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">상위 메뉴</el-radio>
                <el-radio label="C">하위 메뉴</el-radio>
<!--                <el-radio label="F">按钮</el-radio>-->
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.menuType != 'F'" label="아이콘">
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input slot="reference" v-model="form.icon" placeholder="메뉴 아이콘을 선택하세요" readonly>
                  <svg-icon
                    v-if="form.icon"
                    slot="prefix"
                    :icon-class="form.icon"
                    class="el-input__icon"
                    style="height: 32px;width: 16px;"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="메뉴명" prop="menuName">
              <el-input v-model="form.menuName" placeholder="메뉴명을 입력하세요"" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="순서" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="Router" prop="path">
              <el-input v-model="form.path" placeholder="라우터 입력하세요." />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item label="경로" prop="component">
              <el-input v-model="form.component" placeholder="구성 요소 경로를 입력하세요." />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'M'" label="문자열">
              <el-input v-model="form.perms" placeholder="권한 문자열을 입력하세요." maxlength="40" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="메뉴상태">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">확 인</el-button>
        <el-button @click="cancel">최 소</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMenu, getMenu, delMenu, addMenu, updateMenu } from "@/get_post/system/menu";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IconSelect from "@/components/Icon";

export default {
  name: "Menu",
  components: { Treeselect, IconSelect },
  data() {
    return {
      loading: true,
      showSearch: true,
      menuList: [],
      menuOptions: [],
      title: "",
      open: false,
      visibleOptions: [],
      statusOptions: [],
      queryParams: {
        menuName: undefined,
        visible: undefined
      },

      form: {},
      rules: {
        menuName: [
          { required: true, message: "Menu name cannot be empty", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "Menu sorting cannot be empty", trigger: "blur" }
        ],
        path: [
          { required: true, message: "Routing address cannot be empty", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_show_hide").then(response => {
      this.visibleOptions = response.data;
    });
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {

    selected(name) {
      this.form.icon = name;
    },

    getList() {
      this.loading = true;
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, "menuId");
        this.loading = false;
      });
    },

    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      };
    },

    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = [];
        const menu = { menuId: 0, menuName: '전부 메뉴', children: [] };
        menu.children = this.handleTree(response.data, "menuId");
        this.menuOptions.push(menu);
      });
    },

    visibleFormat(row, column) {
      if (row.menuType == "F") {
        return "";
      }
      return this.selectDictLabel(this.visibleOptions, row.visible);
    },

    statusFormat(row, column) {
      if (row.menuType == "F") {
        return "";
      }
      return this.selectDictLabel(this.statusOptions, row.status);
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    reset() {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: "M",
        orderNum: undefined,
        isFrame: "1",
        isCache: "0",
        visible: "0",
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
      this.getTreeselect();
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "메뉴 추가";
    },

    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getMenu(row.menuId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "메뉴 수정";
      });
    },

    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.menuId != undefined) {
            updateMenu(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("수정 성공");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addMenu(this.form).then(response => {
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
      this.$confirm("이 메뉴를 삭제하시겠습니까?", "경고", {
          confirmButtonText: "확인",
          cancelButtonText: "취소",
          type: "warning"
        }).then(function() {
          return delMenu(row.menuId);
        }).then(() => {
          this.getList();
          this.msgSuccess("삭제 성공");
        }).catch(function() {});
    }
  }
};
</script>
