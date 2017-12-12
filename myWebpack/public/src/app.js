import Vue from 'vue';
import VueResource from 'vue-resource';
import App from './page/app';

Vue.use(VueResource);


new Vue({
    el: '#app',
    template: '<App />',
    components:{
        App
    }
});