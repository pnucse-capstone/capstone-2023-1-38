<template>
  <!-- 페이지네이션 컨테이너 分页-->
  <div :class="{'hidden':hidden}" class="pagination-container">
    <!-- Element UI의 페이지네이션 컴포넌트 -->
    <el-pagination
      :background="background"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      :layout="layout"
      :page-sizes="pageSizes"
      :total="total"
      v-bind="$attrs"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { scrollTo } from '@/utils/scroll-to'

export default {
  name: 'Pagination',
  props: {
    // 총 레코드 수
    total: {
      required: true,
      type: Number
    },
    // 현재 페이지 번호
    page: {
      type: Number,
      default: 1
    },
    // 페이지당 표시할 레코드 수
    limit: {
      type: Number,
      default: 20
    },
    // 선택 가능한 페이지당 레코드 수
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 30, 50]
      }
    },
    // 페이지네이션 레이아웃
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    // 배경 색상 표시 여부
    background: {
      type: Boolean,
      default: true
    },
    // 자동으로 맨 위로 스크롤할지 여부
    autoScroll: {
      type: Boolean,
      default: true
    },
    // 페이지네이션 숨김 여부
    hidden: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return this.limit
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    }
  },
  methods: {
    // 페이지당 표시할 레코드 수 변경 처리
    handleSizeChange(val) {
      this.$emit('pagination', { page: this.currentPage, limit: val })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    },
    // 현재 페이지 번호 변경 처리
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
      if (this.autoScroll) {
        scrollTo(0, 800)
      }
    }
  }
}
</script>

<style scoped>
.pagination-container {
  background: #fff;
  padding: 32px 16px;
}
.pagination-container.hidden {
  display: none;
}
</style>
