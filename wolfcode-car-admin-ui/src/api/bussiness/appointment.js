import request from '@/utils/request'

// 查询养修信息预约列表
export function listAppointment(query) {
  return request({
    url: '/business/appointment/list',
    method: 'get',
    params: query
  })
}

// 查询养修信息预约详细
export function getAppointment(id) {
  return request({
    url: '/business/appointment/' + id,
    method: 'get'
  })
}

// 新增养修信息预约
export function addAppointment(data) {
  return request({
    url: '/business/appointment',
    method: 'post',
    data: data
  })
}

// 修改养修信息预约
export function updateAppointment(data) {
  return request({
    url: '/business/appointment',
    method: 'put',
    data: data
  })
}

// 删除养修信息预约
export function delAppointment(id) {
  return request({
    url: '/business/appointment/' + id,
    method: 'delete'
  })
}
// 用户到店状态修改操作
export function arrival(id){
  return request({
    url: '/business/appointment/arrival/' + id,
    method: 'put'
  })
}
// 取消预约
export function cancelAppointment(id){
  return request({
    url:'/business/appointment/cancel/' + id,
    method:'put'
  })
}
