<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="110px">
    <el-form-item label="예전 비밀번호" prop="oldPassword">
      <el-input v-model="user.oldPassword" placeholder="예전 비밀번호를 입력하세요" type="password" />
    </el-form-item>
    <el-form-item label="새 비밀번호" prop="newPassword">
      <el-input v-model="user.newPassword" placeholder="새 비밀번호를 입력하세요" type="password" />
    </el-form-item>
    <el-form-item label="비밀번호 확인" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" placeholder="비밀번호 확인 부탁드립니다" type="password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">저장</el-button>
      <el-button type="danger" size="mini" @click="close">취소</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserPwd } from "@/get_post/system/user";

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.newPassword !== value) {
        callback(new Error("두 번 입력한 비밀번호가 일치하지 않습니다"));
      } else {
        callback();
      }
    };
    return {
      test: "1test",
      user: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [
          { required: true, message: "Old password cannot be empty", trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "New password cannot be empty", trigger: "blur" },
          { min: 6, max: 20, message: "6 to 20 characters in length", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "New password cannot be empty", trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserPwd(this.user.oldPassword, this.user.newPassword).then(
            response => {
              if (response.code === 200) {
                this.msgSuccess("수정성공");
              }
            }
          );
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>
