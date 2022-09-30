import request from '@/utils/request'
// 店铺收入
export function statisticShopIncome(params) {
  return request({
    url: '/report/list',
    method: 'get',
    params
  })
}
//消费单
export function statisticConsumption(params) {
  return request({
    url: '/report/consumption',
    method: 'get',
    params
  })
}

//手机号码，消费金额
export function statisticCustomer(params) {
  return request({
    url: '/report/customer',
    method: 'get',
    params
  })
}