import request from '@/utils/request'

// DB 데이터베이스 목록 조회
export function listDbTable(query) {
  return request({
    url: '/tool/gen/db/list',
    method: 'get',
    params: query
  })
}

// 테이블 상세 정보 조회
export function getGenTable(tableId) {
  return request({
    url: '/tool/gen/' + tableId,
    method: 'get'
  })
}

// 데이터베이스 동기화
export function synchDb(tableName) {
  return request({
    url: '/tool/gen/synchDb/' + tableName,
    method: 'get'
  })
}
