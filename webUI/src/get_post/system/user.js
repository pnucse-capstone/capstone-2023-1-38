import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 사용자 목록 조회
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}

// 사용자 상세 조회
export function getUser(userId) {
  return request({
    url: '/system/user/' + praseStrEmpty(userId),
    method: 'get'
  })
}

// 사용자 추가
export function addUser(data) {
  return request({
    url: '/system/user',
    method: 'post',
    data: data
  })
}

// 사용자 수정
export function updateUser(data) {
  return request({
    url: '/system/user',
    method: 'put',
    data: data
  })
}

// 사용자 삭제
export function delUser(userId) {
  return request({
    url: '/system/user/' + userId,
    method: 'delete'
  })
}

// 사용자 내보내기
export function exportUser(query) {
  return request({
    url: '/system/user/export',
    method: 'get',
    params: query
  })
}

// 사용자 비밀번호 초기화
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 사용자 상태 변경
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 사용자 개인 정보 조회
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 사용자 개인 정보 수정
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 사용자 비밀번호 수정
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    params: data
  })
}

// 사용자 아바타 업로드
export function uploadAvatar(data) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data: data
  })
}

// 사용자 가져오기 템플릿 다운로드
export function importTemplate() {
  return request({
    url: '/system/user/importTemplate',
    method: 'get'
  })
}
