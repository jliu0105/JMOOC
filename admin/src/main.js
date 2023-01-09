import Vue from 'vue'
import App from './app.vue'
import router from './router'
import axios from 'axios'
import filter from './filter/filter'

Vue.config.productionTip = false;
Vue.prototype.$ajax = axios;

// Solve the problem that the corresponding sessionId is inconsistent for each ajax request
axios.defaults.withCredentials = true;

axios.interceptors.request.use(function (config) {
  console.log("request：", config);
  let token = Tool.getLoginUser().token;
  if (Tool.isNotEmpty(token)) {
    config.headers.token = token;
    console.log("request headers add token:", token);
  }
  return config;
}, error => {});
axios.interceptors.response.use(function (response) {
  console.log("return result：", response);
  return response;
}, error => {
  console.log("return error's response：", error.response);
  let response = error.response;
  const status = response.status;
  if (status === 401) {
    console.log("not login, jump to log in page");
    Tool.setLoginUser(null);
    router.push('/login');
  }
  if (status === 417) {
    // Judging that the status code is 401, jump to login
    console.log("TODO add edit feature");
    return {
      data: {
        success: false,
        message: "TODO add edit feature"
      }
    };
  }
  return {
    data: {
      success: false,
      message: "please log in again"
    }
  };
});

Object.keys(filter).forEach(key => {
  Vue.filter(key, filter[key])
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(function (item) {
    return item.meta.loginRequire
  })) {
    let loginUser = Tool.getLoginUser();
    if (Tool.isEmpty(loginUser)) {
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');

console.log("environment：", process.env.NODE_ENV);
