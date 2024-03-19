import {createRouter, createWebHistory} from "vue-router";
import Home from '@/views/HomeView.vue';
import i18n from '@/plugins/i18n';
import store from '@/store/index'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/LoginView.vue')
    },
    {
        path: '/update-module-data',
        name: 'Update Module Data',
        component: () => import('@/views/UpdateModuleDataView.vue'),
        meta: { requiresAuth: true, roles: ['ROLE_COMMITTEE', 'ROLE_OFFICE'] }
    },
    {
        path: '/application-form',
        name: 'Application Form',
        component: () => import('@/views/ApplicationFormView.vue')
    },
    {
        path: '/review-application',
        name: 'Review Application',
        component: () => import('@/views/ReviewView.vue')
    },
    {
        path: '/student-affairs-office',
        name: 'Student Affairs Office',
        component: () => import('@/views/StudentAffairsOfficeView.vue'),
        meta: { requiresAuth: true, roles: ['ROLE_OFFICE'] }
    },
    {
        path: '/examining-committee-chair',
        name: 'Examining Committee Chair',
        component: () => import('@/views/ExaminingCommitteeChairView.vue'),
        meta: { requiresAuth: true, roles: ['ROLE_COMMITTEE'] }
    }
];
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    document.title = i18n.global.t(`routes.${to.path}`);
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!store.state.authentication.isAuthenticated) {
            next({name: 'Login'});
        } else {
            const routeRequiredRoles = to.meta.roles; // Get array of roles required for this route
            const userRole = store.state.authentication.userRole;
            if (routeRequiredRoles && !routeRequiredRoles.includes(userRole)) {
                next({name: 'Login'}); // Redirect to a safe route
            } else {
                next();
            }
        }
    } else {
        next();
    }
});


export default router