function loginApi(data) {
    return $axios({
      'url': '/user/login',
      'method': 'post',
      data
    })
  }

window.sendMsgApi = function (data) {
    return axios({
        url: '/user/sendMsg',
        method: 'post',
        data
    });
};

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

  