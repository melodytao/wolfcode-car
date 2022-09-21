import request from '@/utils/request'

// 查询服务项列表
export function listItem(query) {
  return request({
    url: '/business/serviceitem/list',
    method: 'get',
    params: query
  })
}

// 查询服务项详细
export function getItem(id) {
  return request({
    url: '/business/serviceitem/' + id,
    method: 'get'
  })
}

// 新增服务项
export function addItem(data) {
  return request({
    url: '/business/serviceitem',
    method: 'post',
    data: data
  })
}

// 修改服务项
export function updateItem(data) {
  return request({
    url: '/business/serviceitem',
    method: 'put',
    data: data
  })
}

// 删除服务项
export function delItem(id) {
  return request({
    url: '/business/serviceitem/' + id,
    method: 'delete'
  })
}
//养修服务单项上架
export function serviceItemSaleOn(params) {
  return request({
    url: '/business/serviceitem/saleOn/' + params.id,
    method: 'put'
  })
}

//养修服务单项下架
export function serviceItemSaleOff(params) {
  return request({
    url: '/business/serviceitem/saleOff/' + params.id,
    method: 'put'
  })
}
