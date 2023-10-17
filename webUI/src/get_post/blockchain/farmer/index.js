import request from '@/utils/request'

// 작물을 저장하는 함수
export function saveCrops(data) {
  return request({
    url: '/traces/farmer/saveCrops',
    method: 'post',
    data: data,
  })
}

// 사용자명(username)에 해당하는 작물 목록을 가져오는 함수
export function listCrops(username) {
  return request({
    url: '/traces/farmer/getCropsByUsername/'+username,
    method: 'get',
  })
}

/**
 * @param {Object} image
 * 이미지 업로드
 */
export function uplodImagesBase64(image){
  return request({
    url: '/traces/farmer/imageUpload',
    method: 'post',
    data:image
  })
}

// 부서 ID에 해당하는 모든 운전자를 가져오는 함수
export function getAllDriverByDeptId(departId){
  return request({
    url: '/traces/farmer/getAllDriverByDeptId/'+departId,
    method: 'get',
  })
}

// 부서 ID에 해당하는 공장 정보를 가져오는 함수
export function getFactoryByDeptId(deptId){
  return request({
    url: '/traces/farmer/getFactoryByDeptId/'+deptId,
    method: 'get',
  })
}

// 운송을 추가하는 함수
export function addTransport(data){
  return request({
    url: '/traces/farmer/addTransport',
    method: 'post',
    data:data,
  })
}
