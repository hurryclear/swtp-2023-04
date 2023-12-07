import {createRouter, createWebHistory} from "vue-router";
import Home from '@/views/HomeView.vue'
import store from '@/store';

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
        path: '/OpenApplications',
        name: 'Open Applications',
        component: () => import('@/views/OpenApplications.vue'),
        meta: { requiresAuth: true }
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

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
      if (!store.state.isAuthenticated) {
        next({ name: 'Login' });
      } else {
        next();
      }
    } else {
      next();
    }
  });

export default router