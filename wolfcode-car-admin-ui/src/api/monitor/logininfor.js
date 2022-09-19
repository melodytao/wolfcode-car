import request from '@/utils/request'

// 查询系统访问记录列表
export function list(query) {
  return request({
    url: '/monitor/logininfor/list',
    method: 'get',
    params: query
  })
}


// 删除系统访问记录
export function delLogininfor(infoId) {
  return request({
    url: '/monitor/logininfor/' + infoId,
    method: 'delete'
  })
}

// 清空登录日志
export function cleanLogininfor() {
    return request({
      url: '/monitor/logininfor/clean',
      method: 'delete'
    })
}