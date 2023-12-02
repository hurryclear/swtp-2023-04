import {createRouter, createWebHistory} from "vue-router";
import Home from '@/views/HomeView.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component:Home
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/LoginView.vue')
    },
    {
        path: '/application-form',
        name: 'Application Form',
        component: () => import('@/views/ApplicationForm.vue')
    },
    {
        path: '/review-application',
        name: 'Review Application',
        component: () => import('@/views/ReviewView.vue')
    },
    {
        path: '/study-office',
        name: 'Studienbüro',
        component: () => import('@/views/StudyOfficeView.vue')
    }
]
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router