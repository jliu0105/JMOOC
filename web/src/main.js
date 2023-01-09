import Vue from 'vue'
import App from './app.vue'
import router from './router'
import axios from 'axios'
import filter from "./filter/filter";

Vue.config.productionTip = false;
Vue.prototype.$ajax = axios;

//EventBus
Vue.prototype.$event = new Vue();

Object.keys(filter).forEach(key => {
  Vue.filter(key, filter[key])
});

// Solve the problem that the corresponding sessionId is inconsistent for each ajax request
axios.defaults.withCredentials = true;

/**
 * axios blocker
 */
axios.interceptors.request.use(function (config) {
  console.log("request：", config);
  return config;
}, error => {});
axios.interceptors.response.use(function (response) {
  console.log("return result：", response);
  return response;
}, error => {});

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
