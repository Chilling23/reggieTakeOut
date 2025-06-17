
const getSetmealPage = (params) => {
  return $axios({
    url: '/setmeal/page',
    method: 'get',
    params
  })
}


const deleteSetmeal = (ids) => {
  return $axios({
    url: '/setmeal',
    method: 'delete',
    params: { ids }
  })
}


const editSetmeal = (params) => {
  return $axios({
    url: '/setmeal',
    method: 'put',
    data: { ...params }
  })
}


const addSetmeal = (params) => {
  return $axios({
    url: '/setmeal',
    method: 'post',
    data: { ...params }
  })
}

// 查询详情接口
const querySetmealById = (id) => {
  return $axios({
    url: `/setmeal/${id}`,
    method: 'get'
  })
}

// 批量起售禁售
const setmealStatusByStatus = (params) => {
  return $axios({
    url: `/setmeal/status/${params.status}`,
    method: 'post',
    params: { ids: params.ids }
  })
}
