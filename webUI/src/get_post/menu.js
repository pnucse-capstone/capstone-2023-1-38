import request from '@/utils/request'

// 라우터 가져오기
export const getRouters = () => {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}
