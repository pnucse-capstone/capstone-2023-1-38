import request from '@/utils/request'

// 로그인
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    method: 'post',
    data: data
  })
}

// 사용자 상세 정보 가져오기
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 로그아웃
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 인증 코드 이미지 가져오기
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}
