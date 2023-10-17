import request from '@/utils/request'

// 사전 유형 목록 조회
export function listType(query) {
  return request({
    url: '/system/dict/type/list',
    method: 'get',
    params: query
  })
}

// 사전 유형 상세 조회
export function getType(dictId) {
  return request({
    url: '/system/dict/type/' + dictId,
    method: 'get'
  })
}

// 사전 유형 추가
export function addType(data) {
  return request({
    url: '/system/dict/type',
    method: 'post',
    data: data
  })
}

// 사전 유형 수정
export function updateType(data) {
  return request({
    url: '/system/dict/type',
    method: 'put',
    data: data
  })
}

// 사전 유형 삭제
export function delType(dictId) {
  return request({
    url: '/system/dict/type/' + dictId,
    method: 'delete'
  })
}

// 매개변수 캐시 지우기
export function clearCache() {
  return request({
    url: '/system/dict/type/clearCache',
    method: 'delete'
  })
}

// 사전 유형 내보내기
export function exportType(query) {
  return request({
    url: '/system/dict/type/export',
    method: 'get',
    params: query
  })
}

// 사전 선택 상자 목록 가져오기
export function optionselect() {
  return request({
    url: '/system/dict/type/optionselect',
    method: 'get'
  })
}
