import request from '@/utils/request'

// 운전자의 운송 목록을 가져오는 함수
export function listTransport(driverId) {
  return request({
    url: '/traces/driver/listTransport/'+driverId,
    method: 'get',
  })
}

// 데이터를 데이터베이스에 저장 및 업데이트하는 함수
export function saveAndUpdateTransportInfoToDb(data) {
  return request({
    url: '/traces/driver/saveAndUpdateTransportInfoToDb',
    method: 'post',
    data:data
  })
}

// 운송 상태를 업데이트하는 함수
export function updateTransportStatus(cropsId) {
  return request({
    url: '/traces/driver/updateTransportStatus/'+cropsId,
    method: 'get',
  })
}
