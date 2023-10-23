import request from '@/utils/request'

// 부서 ID에 해당하는 작물 목록을 조회하는 함수
export function queryCropsList(deptId) {
  return request({
    url: '/traces/product/queryCropsList/'+deptId,
    method: 'get',
  })
}

/**
 * 작업 추가
 */
export function addTask(data) {
  return request({
    url: '/traces/product/addTask',
    method: 'post',
    data:data
  })
}

// 기계 상태를 업데이트하는 함수
export function updateMachingStatus(data) {
  return request({
    url: '/traces/product/updateMachingStatus',
    method: 'post',
    data:data
  })
}

// 작물 ID에 해당하는 작업을 조회하는 함수
export function queryTaskByCropsId(cropsId) {
  return request({
    url: '/traces/product/queryTaskByCropsId/'+cropsId,
    method: 'get',
  })
}

// 작물의 상태를 '작성 완료'로 업데이트하는 함수
export function updateProductWriteStatus(cropsId) {
  return request({
    url: '/traces/product/updateProductWriteStatus/'+cropsId,
    method: 'get',
  })
}

// 작업 목록을 조회하는 함수
export function queryTaskList() {
  return request({
    url: '/traces/product/queryTaskList',
    method: 'get',
  })
}

// 작물을 공장에서 출고하는 함수
export function productOutFactory(cropsId) {
  return request({
    url: '/traces/product/productOutFactory/'+cropsId,
    method: 'get',
  })
}
