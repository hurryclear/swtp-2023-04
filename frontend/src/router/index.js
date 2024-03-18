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
    console.log('Navigating to:', to.path, "\nCurrent user authenticated status:", store.state.authentication.isAuthenticated, "\nCurrent user role:", store.state.authentication.userRole);
    document.title = i18n.global.t(`routes.${to.path}`);//TODO:i18n
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!store.state.authentication.isAuthenticated) {
            console.log('User not authenticated. Redirecting to Login.');
            next({name: 'Login'});
        } else {
            const routeRequiredRoles = to.meta.roles; // Get array of roles required for this route
            const userRole = store.state.authentication.userRole;

            console.log('Route requires roles:', routeRequiredRoles);

            if (routeRequiredRoles && !routeRequiredRoles.includes(userRole)) {
                console.log(`User role ${userRole} does not match required roles ${routeRequiredRoles}. Redirecting.`);
                next({name: 'Login'}); // Redirect to a safe route
            } else {
                console.log('User role matches or no specific role required. Proceeding to route.');
                next();
            }
        }
    } else {
        console.log('No authentication required for this route. Proceeding to route.');
        next();
    }
});


export default router