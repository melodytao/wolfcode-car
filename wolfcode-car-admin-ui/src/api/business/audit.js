import request from '@/utils/request'

// 查询套餐审核列表
export function listAudit(query) {
  return request({
    url: '/business/audit/list',
    method: 'get',
    params: query
  })
}

// 查询套餐审核详细
export function getAudit(id) {
  return request({
    url: '/business/audit/' + id,
    method: 'get'
  })
}

// 新增套餐审核
export function addAudit(data) {
  return request({
    url: '/business/audit',
    method: 'post',
    data: data
  })
}

// 修改套餐审核
export function updateAudit(data) {
  return request({
    url: '/business/audit',
    method: 'put',
    data: data
  })
}

// 删除套餐审核
export function delAudit(id) {
  return request({
    url: '/business/audit/' + id,
    method: 'delete'
  })
}
