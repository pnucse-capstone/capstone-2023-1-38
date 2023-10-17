
<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">농산물 근원탐구 시스템관리</h3>
      <!-- 제목 -->
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="아이디">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <!-- 사용자명 입력란 -->
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="비밀번호"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <!-- 비밀번호 입력란 -->
      <el-form-item prop="code">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="인증 번호"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <!-- 인증 번호 입력란과 인증 번호 이미지 -->
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">비밀번호 저장</el-checkbox>
      <!-- 비밀번호 저장 체크박스 -->
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">로그인</span>
          <span v-else>로그인 중...</span>
        </el-button>
      </el-form-item>
      <!-- 로그인 버튼 -->
      <el-form-item style="width:100%;">
        <el-button
          size="medium"
          type="success"
          style="width:100%;"
          @click.native.prevent="roTrace"
        >
          <span >소비자 조회</span>
        </el-button>
      </el-form-item>
      <!-- "소비자 조회" 버튼 -->
    </el-form>


    <el-dialog
      title="농산물 근원탐구및 시스템관리 플랫폼 "
      :visible.sync="drawer"
      width="100%">
      <el-divider content-position="center">아래에 조회번호를 입력해 주세요</el-divider>
      <div>
        <Trace></Trace>
      </div>

    </el-dialog>
    <!-- 팝업 창, "농산물 근원탐구및 시스템관리 플랫폼" 제목과 Trace 컴포넌트를 표시하는 용도 -->
  </div>
</template>





<script>
import { getCodeImg } from "@/get_post/login"; // 인증 코드와 관련된 API 가져오기
import Cookies from "js-cookie"; // 브라우저 쿠키 조작을 위한 Cookies 라이브러리 가져오기
import { encrypt, decrypt } from '@/utils/jsencrypt' // 암호화 및 복호화 함수 가져오기
import Trace from './height.vue' // Trace 컴포넌트 가져오기(소비자 조회 페이지, 아직 작성되지 않다)
export default {
  name: "Login", // 컴포넌트 이름
  data() {
    return {
      codeUrl: "", // 인증 코드 이미지 URL
      cookiePassword: "", // 쿠키에 저장된 비밀번호
      loginForm: { // 로그인 폼 데이터
        username: "admin", // 기본 사용자 이름
        password: "admin123", // 기본 비밀번호
        rememberMe: false, // 비밀번호 기억 여부
        code: "", // 인증 코드
        uuid: "" // 인증 코드 UUID
      },
      loginRules: { // 로그인 폼 유효성 검사 규칙
        username: [
          { required: true, trigger: "blur", message: "User name cannot be empty" }
        ],
        password: [
          { required: true, trigger: "blur", message: "Password cannot be empty" }
        ],
        code: [{ required: true, trigger: "change", message: "The verification code cannot be empty" }]
      },
      loading: false, // 로그인 버튼의 로딩 상태
      redirect: undefined, // 리디렉션 경로
      drawer:false, // 팝업창 표시 여부
    };
  },
  components:{
    Trace // Trace 컴포넌트 등록
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect; // 라우트 변경 감지하여 리디렉션 경로 가져오기
      },
      immediate: true // 즉시 watch 콜백 실행
    }
  },
  created() {
    this.getCode(); // 페이지가 생성될 때 인증 코드 가져오기
    this.getCookie(); // 페이지가 생성될 때 쿠키에서 사용자 이름과 비밀번호 가져오기
  },
  methods: {
    roTrace(){ // "소비자 조회" 버튼 클릭 이벤트 처리 함수
      this.drawer = true; // 팝업창 표시
    },

    getCode() { // 인증 코드 가져오기 함수
      getCodeImg().then(res => { // API를 호출하여 인증 코드 이미지 가져오기
        this.codeUrl = "data:image/gif;base64," + res.img; // 가져온 인증 코드 이미지 URL을 codeUrl에 할당
        this.loginForm.uuid = res.uuid; // 가져온 인증 코드 UUID를 loginForm의 uuid 필드에 할당
      });
    },
    getCookie() { // 쿠키 가져오기 함수
      const username = Cookies.get("username"); // 쿠키에서 사용자 이름 가져오기
      const password = Cookies.get("password"); // 쿠키에서 비밀번호 가져오기
      const rememberMe = Cookies.get('rememberMe'); // 쿠키에서 비밀번호 기억 상태 가져오기
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username, // 쿠키에 사용자 이름이 없으면 기본 사용자 이름 사용
        password: password === undefined ? this.loginForm.password : decrypt(password), // 쿠키에 비밀번호가 없으면 기본 비밀번호 사용 및 복호화
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe) // 쿠키에 비밀번호 기억 상태가 없으면 기본값은 false
      };
    },
    handleLogin() {//로그인 처리를 담당하는 함수
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store
            .dispatch("Login", this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || "/" });
            })
            .catch(() => {
              this.loading = false;
              this.getCode();
            });
        }
      });
    }

  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/image/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>
