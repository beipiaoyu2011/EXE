import Vue from 'vue';
import VueResource from 'vue-resource';
import App from './page/login.vue';
import dataService from './dataService';

Vue.use(VueResource);

window.dataService = dataService;
Vue.prototype.dataService = dataService;

new Vue({
    el: '#app',
    template: '<App />',
    components: { App }
})