import "@/assets/main.css";

// Router
import router from "@/router";

// i18n
import i18n from "@/plugins/i18n";

// Vuetify
import vuetify from "@/plugins/vuetify";

// Vuex Store
import store from '@/store'; // Import the store

// Vue App
import { createApp } from 'vue';
import App from './App.vue';

const app = createApp(App);

app.use(vuetify);
app.use(i18n);
app.use(router);
app.use(store); // Use the store here

app.mount('#app');
