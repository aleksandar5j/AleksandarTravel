function get(from, params, callback) {
  if (callback == null && params instanceof Function) {
    callback = params;
    params = null;
  }   
  axios.get(from, {params})
  .then(function (response) {
      callback.call(this, response.data, response);
  })
  .catch(function (e) {
      console.log(e);
  })
}

function post(from, params, callback) {
  if (callback == null && params instanceof Function) {
    callback = params;
    params = null;
  }  
  axios.post(from, params)
  .then(function (response) {
      callback.call(this, response.data, response);
  })
  .catch(function (e) {
      console.log(e);
  })
}

function postMP(url, data, callback) {
  if (callback == null && data instanceof Function) {
    callback = data;
    data = null;
  }
  if (!(data instanceof FormData)) {
    let f = new FormData();
    Object.keys(data).forEach((key) => {
        f.append(key, data[key])
    })
    data = f;
  }
  axios.post(url, data, {headers: {
    'Content-Type': 'multipart/form-data'}
  })
  .then(function (response) {
      callback.call(this, response.data, response);
  })
  .catch(function (e) {
      console.log(e)
  })
}