
<template>
  <div>
    <LoginComponent @loginSubmitted="handleLogin"/>
  </div>
</template>

<script>
import LoginComponent from '@/components/LoginComponent.vue';

export default {
  name: 'LoginView',
  components: {
    LoginComponent
  },
  methods: {
    handleLogin(credentials) {
      console.log('Login submitted', credentials);
    }
  }
};
</script>



