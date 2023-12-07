<template>
    <v-col>
        <v-row justify="center">
            <v-btn
                color="red" 
                varient="outlined"
                @click="logout">Logout
            </v-btn>
        </v-row>
    </v-col>
  </template>
  
  <script>
  export default {
    name: 'LogoutButton',
    methods: {
      logout() {
        // Dispatch action to set isAuthenticated to false
        this.$store.dispatch('logoutUser');
  
        // Redirect to login page
        this.$router.push('/login');
      }
    }
  };
  </script>
  
  <style>

  </style>
  