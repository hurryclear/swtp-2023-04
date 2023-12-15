import "@/assets/main.css";

// Router
import router from "@/router";

// i18n
import i18n from "@/plugins/i18n";

// Vuetify
import vuetify from "@/plugins/vuetify";

// Vuex Store
import store from '@/store';

//TODO: AXIOS

import VueAxios from 'vue-axios';
import axios from '@/plugins/axios';

// Vue App
import { createApp } from 'vue';
import App from './App.vue';

const app = createApp(App);

app.use(VueAxios, axios);
app.use(vuetify);
app.use(i18n);
app.use(router);
app.use(store); 

app.mount('#app');
