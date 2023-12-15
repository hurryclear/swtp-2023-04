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
        path: '/study-office',
        name: 'Studienbüro',
        component: () => import('@/views/StudyOfficeView.vue'),
        meta: { requiresAuth: true, role: 'studentAffairsOffice'}
    },
    {
      path:'/pruefungsausschuss',
      name:'PrüfungsAusschuss',
      component: () => import('@/views/PruefungsAusschuss.vue'),
      meta: { requiresAuth: true, role: 'examiningCommitteeChair'}
    }
    
]
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
  console.log('Navigating to:', to.path);
  console.log('Current user authenticated status:', store.state.isAuthenticated);
  console.log('Current user role:', store.state.userRole);

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.state.isAuthenticated) {
      console.log('User not authenticated. Redirecting to Login.');
      next({ name: 'Login' });
    } else {
      const routeRequiresRole = to.meta.role;
      const userRole = store.state.userRole;

      console.log('Route requires role:', routeRequiresRole);

      if (routeRequiresRole && userRole !== routeRequiresRole) {
        console.log(`User role ${userRole} does not match required role ${routeRequiresRole}. Redirecting.`);
        next({ name: 'Login' }); // Redirect to a safe route
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