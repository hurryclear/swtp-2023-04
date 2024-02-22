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

export default {
  name: 'LogoutButton',
  methods: {
    async logout() {
      try {
        await this.$store.dispatch('logout');

        // Redirect to the login page or home
        this.$router.push('/login'); // Change to the appropriate route

      } catch (error) {
        console.error('Logout failed:', error);
      }
    },
  }
}
</script>