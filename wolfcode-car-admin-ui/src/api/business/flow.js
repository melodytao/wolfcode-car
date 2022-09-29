import request from '@/utils/request'

// 查询流程定义明细列表
export function listInfo(query) {
  return request({
    url: '/business/flow/list',
    method: 'get',
    params: query
  })
}
// 审批流程文件提交
export function deployBpmnInfo(data){
  return request(
    {
      url:"/business/flow",
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      data:data
    }
  )
}
// 撤销部署
export function removeBpmnInfo(id){
  return request({
    url:'/business/flow/'+id,
    method: 'delete'
  })
}
// 查看流程文件和图片
export function getBpmnInfoFile(params){
  return request({
    url:`/business/flow/${params.type}/${params.id}`,
    method:'get'
  })
}
