<template>
  <v-col>
    <v-row justify="center">
      <v-btn
          color="red"
          varient="outlined"
          @click="logout">
        {{ $t('logoutButton') }}
      </v-btn>
    </v-row>
  </v-col>
</template>

<script>
import axios from "@/plugins/axios";

export default {
  name: 'LogoutButton',
  methods: {
    async logout() {
      try {
        // Send a GET request to /auth/logout
        await axios.get('/api/auth/logout');

        // Logout the user in the store
        this.$store.dispatch('logout');

        // Redirect to the login page or home
        this.$router.push('/login'); // Change to the appropriate route
      } catch (error) {
        console.error('Logout failed:', error);
      }
    },
  }
}
</script>