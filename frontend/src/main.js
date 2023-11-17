import { createApp } from 'vue'
import App from './App.vue'
import "@/assets/main.css"

// i18n
const deLocale = require('@/locale/de.json');
const enLocale = require('@/locale/en.json');
const frLocale = require('@/locale/fr.json');
const esLocale = require('@/locale/es.json');
const zhLocale = require('@/locale/zh.json');
const itLocale = require('@/locale/it.json');
const ruLocale = require('@/locale/ru.json');
const jaLocale = require('@/locale/ja.json');
const koLocale = require('@/locale/ko.json');
const ptLocale = require('@/locale/pt.json');

import { createI18n } from "vue-i18n";
console.log(navigator.language)
const i18n = createI18n({
    locale: navigator.language.split('-')[0], // preferred locale
    messages: {
        de: deLocale,
        en: enLocale,
        fr: frLocale,
        es: esLocale,
        zh: zhLocale,
        it: itLocale,
        ru: ruLocale,
        ja: jaLocale,
        ko: koLocale,
        pt: ptLocale,
    },
});

export default i18n;


// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
const vuetify = createVuetify({
    components,
    directives,
})

const app = createApp(App)
app.use(vuetify)
app.use(i18n)
app.mount('#app')
