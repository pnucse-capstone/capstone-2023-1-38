import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/vuex'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';
// Create Axios instance
const service = axios.create({
  // Axios request configuration includes baseURL option, representing the common part of the request URL
  // baseURL: process.env.VUE_APP_BASE_API,
  baseURL: 'http://localhost:8080',
  timeout: 10000
});
// Request interceptor
service.interceptors.request.use(config => {
  // Check if token needs to be set
  const isToken = (config.headers || {}).isToken === false;
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken(); // Attach custom token to each request
  }
  return config;
}, error => {
  console.log(error);
  return Promise.reject(error);
});

// Response interceptor
service.interceptors.response.use(res => {
    // Use default success status if not set
    const code = res.data.code || 200;
    // Get error message
    const msg = errorCode[code] || res.data.msg || errorCode['default'];
    if (code === 401) {
      MessageBox.confirm('Login status has expired. You can choose to stay on this page or log in again.', 'System Prompt', {
          confirmButtonText: 'Log in again',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }
      ).then(() => {
        store.dispatch('LogOut').then(() => {
          location.href = '/index';
        });
      });
    } else if (code === 500) {
      Message({
        message: msg,
        type: 'error'
      });
      return Promise.reject(new Error(msg));
    } else if (code !== 200) {
      Notification.error({
        title: msg
      });
      return Promise.reject('error');
    } else {
      return res.data;
    }
  },
  error => {
    console.log('err' + error);
    let { message } = error;
    if (message == "Network Error") {
      message = "Backend interface connection exception";
    }
    else if (message.includes("timeout")) {
      message = "System interface request timeout";
    }
    else if (message.includes("Request failed with status code")) {
      message = "System interface exception: " + message.substr(message.length - 3);
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
)


export default service
