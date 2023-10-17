import { constantRoutes } from '@/router'
import { getRouters } from '@/get_post/menu'
import Layout from '@/layout/index'

const permission = {
  state: {
    routes: [],
    addRoutes: []
  },
  mutations: {

    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    }
  },
  actions: {
    // 라우트 생성
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        // 백엔드에서 라우트 데이터 요청
        getRouters().then(res => {
          // 받은 라우트를 필터링하고 처리
          const accessedRoutes = filterAsyncRouter(res.data)
          accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTES', accessedRoutes)
          resolve(accessedRoutes)
        })
      })
    }
  }
}


function filterAsyncRouter(asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.component === 'Layout') {
        route.component = Layout
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
}

export const loadView = (view) => {
  return (resolve) =>  require([`@/views/${view}`], resolve)
}

export default permission
