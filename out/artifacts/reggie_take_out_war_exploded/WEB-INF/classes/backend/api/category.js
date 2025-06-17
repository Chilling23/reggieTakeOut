
const getCategoryPage = (params) => {
  return $axios({
    url: '/category/page',
    method: 'get',
    params
  })
}


const queryCategoryById = (id) => {
  return $axios({
    url: `/category/${id}`,
    method: 'get'
  })
}


const deleCategory = (ids) => {
  return $axios({
    url: '/category',
    method: 'delete',
    params: { ids }
  })
}


const editCategory = (params) => {
  return $axios({
    url: '/category',
    method: 'put',
    data: { ...params }
  })
}

// 新增接口
const addCategory = (params) => {
  return $axios({
    url: '/category',
    method: 'post',
    data: { ...params }
  })
}