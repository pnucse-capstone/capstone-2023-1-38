import request from '@/utils/request'

// 부서 ID에 해당하는 작물 목록을 가져오는 함수
export function listCrops(deptId) {
  return request({
    url: '/traces/material/listCrops/'+deptId,
    method: 'get',
  })
}

// 작물의 상태를 '입고'에서 '출고'로 변경하는 함수
export function changeInToOut(cropsId) {
  return request({
    url: '/traces/material/changeInToOut/'+cropsId,
    method: 'get',
  })
}

// 운송을 추가하는 함수
export function addTransport(data) {
  return request({
    url: '/traces/material/addTransport',
    method: 'post',
    data:data,
  })
}
