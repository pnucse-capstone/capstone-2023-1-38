<template>
  <!-- Bread 컴포넌트를 표시하는 템플릿-->
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <!-- levelList 배열의 각 항목을 순회하여 Bread 항목을 렌더링 -->
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <!-- 현재 항목이거나 더 이상 항목이 없는 경우 텍스트로 표시 -->
        <span v-if="item.redirect==='noRedirect'||index==levelList.length-1" class="no-redirect">{{ item.meta.title }}</span>
        <!-- 클릭 가능한 링크로 표시 -->
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'

export default {
  data() {
    return {
      levelList: null
    }
  },
  watch: {
    // $route 객체를 감시하여 경로가 변경되었을 때 호출되는 watch 속성
    $route(route) {
      // 리다이렉트 페이지로 이동한 경우 이동 경로를 업데이트하지 않음
      if (route.path.startsWith('/redirect/')) {
        return
      }
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      const first = matched[0]

      if (!this.isDashboard(first)) {
        matched = [{ path: '/index', meta: { title: '홈페이지' }}].concat(matched)
      }

      this.levelList = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
    },
    // 대시보드 페이지인지 확인하는 유틸리티 메서드
    isDashboard(route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return name.trim() === '홈페이지'
    },
    // 경로를 컴파일하여 동적 경로 매개변수를 적용하는 메서드
    pathCompile(path) {
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    // 이동 경로 항목을 클릭했을 때 처리되는 이벤트 핸들러
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        // 리다이렉트가 지정된 경우 해당 페이지로 이동
        this.$router.push(redirect)
        return
      }
      // 동적 경로 매개변수를 적용한 경로로 이동
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
