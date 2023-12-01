import "@/assets/main.css"

// Router
import router from "@/router";

// i18n
import i18n from "@/plugins/i18n";

// Vuetify
import vuetify from "@/plugins/vuetify";

// Vue App
import { createApp } from 'vue'
import App from './App.vue'

// TODO: Axios
//import axiosInstance from "@/plugins/axios";
//app.config.globalProperties.$axios = axiosInstance;

const app = createApp(App)
app.use(vuetify)
app.use(i18n)
app.use(router)
app.mount('#app')
