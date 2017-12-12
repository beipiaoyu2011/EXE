import Vue from 'vue';
import Router from 'vue-router';

import secret from '../components/secret.vue';
Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            name: 'secret',
            component: secret
        }
    ]
});

