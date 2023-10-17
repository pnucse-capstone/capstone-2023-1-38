import request from '@/utils/request'

// 직무 목록 조회
export function listPost(query) {
  return request({
    url: '/system/post/list',
    method: 'get',
    params: query
  })
}

// 직무 상세 조회
export function getPost(postId) {
  return request({
    url: '/system/post/' + postId,
    method: 'get'
  })
}

// 직무 추가
export function addPost(data) {
  return request({
    url: '/system/post',
    method: 'post',
    data: data
  })
}

// 직무 수정
export function updatePost(data) {
  return request({
    url: '/system/post',
    method: 'put',
    data: data
  })
}

// 직무 삭제
export function delPost(postId) {
  return request({
    url: '/system/post/' + postId,
    method: 'delete'
  })
}

// 직무 내보내기
export function exportPost(query) {
  return request({
    url: '/system/post/export',
    method: 'get',
    params: query
  })
}
