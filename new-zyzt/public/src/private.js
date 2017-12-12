import Vue from 'vue';
import VueResource from 'vue-resource';
import App from './page/private.vue';
import router from './router/privateRouter';
import dataService from './dataService';

Vue.use(VueResource);

Vue.prototype.dataService = dataService;

new Vue({
    el: '#app',
    router,
    template: '<App />',
    components: { App }
});