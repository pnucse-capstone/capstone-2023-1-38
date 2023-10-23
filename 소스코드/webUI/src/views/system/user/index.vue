
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="부서명을 입력하세요"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="사용자ID" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="사용자ID를 입력하세요"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="전화번호" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="전화번호를 입력하세요"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="상태" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="사용자 상태"
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
<!--          <el-form-item label="创建时间">-->
<!--            <el-date-picker-->
<!--              v-model="dateRange"-->
<!--              size="small"-->
<!--              style="width: 240px"-->
<!--              value-format="yyyy-MM-dd"-->
<!--              type="daterange"-->
<!--              range-separator="-"-->
<!--              start-placeholder="开始日期"-->
<!--              end-placeholder="结束日期"-->
<!--            ></el-date-picker>-->
<!--          </el-form-item>-->
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
              v-hasPermi="['system:user:add']"
            >추가</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:user:edit']"
            >수정</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:user:remove']"
            >삭제</el-button>
          </el-col>
          <!--          <el-col :span="1.5">-->
          <!--            <el-button-->
          <!--              type="info"-->
          <!--              icon="el-icon-upload2"-->
          <!--              size="mini"-->
          <!--              @click="handleImport"-->
          <!--              v-hasPermi="['system:user:import']"-->
          <!--            >导入</el-button>-->
          <!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button-->
<!--              type="warning"-->
<!--              icon="el-icon-download"-->
<!--              size="mini"-->
<!--              @click="handleExport"-->
<!--              v-hasPermi="['system:user:export']"-->
<!--            >导出</el-button>-->
<!--          </el-col>-->
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
<!--          <el-table-column label="用户编号" align="center" prop="userId" />-->
          <el-table-column label="사용자ID" align="center" prop="userName" :show-overflow-tooltip="true" />
          <el-table-column label="사용자 한글명" align="center" prop="nickName" :show-overflow-tooltip="true" />
          <el-table-column label="부서" align="center" prop="dept.deptName" :show-overflow-tooltip="true" />
          <el-table-column label="전화번호" align="center" prop="phonenumber" width="120" />
          <el-table-column label="상태" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="등록일자" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="조작"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']"
              >수정</el-button>
              <el-button
                v-if="scope.row.userId !== 1"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:user:remove']"
              >삭제</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-key"
                @click="handleResetPwd(scope.row)"
                v-hasPermi="['system:user:resetPwd']"
              >비밀번호 변경</el-button>
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
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="한글명" prop="nickName">
              <el-input v-model="form.nickName" placeholder="사용자 한글명을 입력하세요" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="소속부서" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="소속부서를 선택하세요" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="전화번호" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="전화번호를 입력하세요" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="이메일" prop="email">
              <el-input v-model="form.email" placeholder="이메일을 입력하세요" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="사용자ID" prop="userName">
              <el-input v-model="form.userName" placeholder="사용자ID를 입력하세요" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="비밀번호" prop="password">
              <el-input v-model="form.password" placeholder="사용자 비밀번호를 입력하세요" type="password" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="성별">
              <el-select v-model="form.sex" placeholder="성별을 선택하세요" >
                <el-option
                  v-for="dict in sexOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="상태">
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
        <el-row>
          <el-col :span="12">
            <el-form-item label="직위">
              <el-select v-model="form.postIds" multiple placeholder="직위을 선택하세요" >
                <el-option
                  v-for="item in postOptions"
                  :key="item.postId"
                  :label="item.postName"
                  :value="item.postId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="역할">
              <el-select v-model="form.roleIds" multiple placeholder="역할을 선택하세요">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="비고">
              <el-input v-model="form.remark" type="textarea" placeholder="내용을 입력하세요"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">확 인</el-button>
        <el-button @click="cancel">최 소</el-button>
      </div>
    </el-dialog>

<!--    &lt;!&ndash; 用户导入对话框 &ndash;&gt;-->
<!--    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>-->
<!--      <el-upload-->
<!--        ref="upload"-->
<!--        :limit="1"-->
<!--        accept=".xlsx, .xls"-->
<!--        :headers="upload.headers"-->
<!--        :action="upload.url + '?updateSupport=' + upload.updateSupport"-->
<!--        :disabled="upload.isUploading"-->
<!--        :on-progress="handleFileUploadProgress"-->
<!--        :on-success="handleFileSuccess"-->
<!--        :auto-upload="false"-->
<!--        drag-->
<!--      >-->
<!--        <i class="el-icon-upload"></i>-->
<!--        <div class="el-upload__text">-->
<!--          将文件拖到此处，或-->
<!--          <em>点击上传</em>-->
<!--        </div>-->
<!--        <div class="el-upload__tip" slot="tip">-->
<!--          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据-->
<!--          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>-->
<!--        </div>-->
<!--        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>-->
<!--      </el-upload>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitFileForm">确 定</el-button>-->
<!--        <el-button @click="upload.open = false">取 消</el-button>-->
<!--      </div>-->
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, exportUser, resetUserPwd, changeUserStatus, importTemplate } from "@/get_post/system/user";
import { getToken } from "@/utils/auth";
import { treeselect } from "@/get_post/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "User",
  components: { Treeselect },
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      // roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // // 用户导入参数
      // upload: {
      //   // 是否显示弹出层（用户导入）
      //   open: false,
      //   // 弹出层标题（用户导入）
      //   title: "",
      //   // 是否禁用上传
      //   isUploading: false,
      //   // 是否更新已经存在的用户数据
      //   updateSupport: 0,
      //   // 设置上传的请求头部
      //   headers: { Authorization: "Bearer " + getToken() },
      //   // 上传的地址

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "User ID cannot be empty", trigger: "blur" }
        ],
        nickName: [
          { required: true, message: "User Korean name cannot be empty", trigger: "blur" }
        ],
        deptId: [
          { required: true, message: "Department cannot be empty", trigger: "blur" }
        ],
        password: [
          { required: true, message: "User password cannot be empty", trigger: "blur" }
        ],
        email: [
          { required: true, message: "Email address cannot be empty", trigger: "blur" },
          {
            type: "email",
            message: "정확한 이메일 주소를 입력하세요",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          { required: true, message: "Phone number can not be empty", trigger: "blur" },
          {
            pattern: /^01[0-9]{8,9}$/,
            message: "정확한 핸드폰 번호를 입력하세요",
            trigger: "blur"
          }
        ]
      }
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_user_sex").then(response => {
      this.sexOptions = response.data;
    });
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** user list */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data;
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
    handleStatusChange(row) {
      let text = row.status === "0" ? "open" : "close";
      this.$confirm('Are you sure you want to "' + text  + '"this user?', "경고", {
        confirmButtonText: "확인",
        cancelButtonText: "취소",
        type: "warning"
      }).then(function() {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.msgSuccess(text + "성공");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
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
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: []
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      getUser().then(response => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "사용자 추가";
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.form.postIds = response.postIds;
        this.form.roleIds = response.roleIds;
        this.open = true;
        this.title = "사용자 수정";
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('사용자"' + row.userName + '"의 비밀번호를 입력하십시오', "제시", {
        confirmButtonText: "확인",
        cancelButtonText: "취소"
      }).then(({ value }) => {
        resetUserPwd(row.userId, value).then(response => {
          if (response.code === 200) {
            this.msgSuccess("수정 완료. 새 비밀번호는：" + value);
          }
        });
      }).catch(() => {});
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("수정 성공");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addUser(this.form).then(response => {
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$confirm('이 사용자를 삭제하시겠습니까?', "경고", {
        confirmButtonText: "확인",
        cancelButtonText: "취소",
        type: "warning"
      }).then(function() {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("삭제 성공");
      }).catch(function() {});
    },
    /** 导出按钮操作 */
    // handleExport() {
    //   const queryParams = this.queryParams;
    //   this.$confirm('是否确认导出所有用户数据项?', "警告", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning"
    //   }).then(function() {
    //     return exportUser(queryParams);
    //   }).then(response => {
    //     this.download(response.msg);
    //   }).catch(function() {});
    // },
    /** 导入按钮操作 */
  //   handleImport() {
  //     this.upload.title = "用户导入";
  //     this.upload.open = true;
  //   },
  //   /** 下载模板操作 */
  //   importTemplate() {
  //     importTemplate().then(response => {
  //       this.download(response.msg);
  //     });
  //   },
  //   // 文件上传中处理
  //   handleFileUploadProgress(event, file, fileList) {
  //     this.upload.isUploading = true;
  //   },
  //   // 文件上传成功处理
  //   handleFileSuccess(response, file, fileList) {
  //     this.upload.open = false;
  //     this.upload.isUploading = false;
  //     this.$refs.upload.clearFiles();
  //     this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
  //     this.getList();
  //   },
  //   // 提交上传文件
  //   submitFileForm() {
  //     this.$refs.upload.submit();
  //   }
  }
};
</script>
