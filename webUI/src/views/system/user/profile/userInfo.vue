<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="사용자ID" prop="nickName">
      <el-input v-model="user.nickName" />
    </el-form-item>
    <el-form-item label="전화번호" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="전화번호" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="성별">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">남</el-radio>
        <el-radio label="1">여</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">저정</el-button>
      <el-button type="danger" size="mini" @click="close">취소</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/get_post/system/user";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "Username cannot be empty", trigger: "blur" }
        ],
        email: [
          { required: true, message: "Email address cannot be empty", trigger: "blur" },
          {
            type: "email",
            message: "'Please input the correct email address",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          { required: true, message: "Phone number cannot be empty", trigger: "blur" },
          {
            pattern: /^01[0-9]{8,9}$/,
            message: "Please enter the correct phone number",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            if (response.code === 200) {
              this.msgSuccess("수정 성공");
            }
          });
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
