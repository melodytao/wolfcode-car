import request from '@/utils/request'
export function listStatementItem(params){
    return request({
        url: '/business/statementItem',
        method: 'get',
        params: params
    })
}
export function addStatementItem(data){
    return request({
        url: '/business/statementItem',
        method: 'post',
        data: data
    })
}