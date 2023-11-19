<script>

export default {
  methods: {
    navigateTo(page) {
      // Implement navigation logic based on the clicked button
      switch (page) {
        case 'main':
          this.$router.push('/'); // Adjust the route as needed
          break;
        case 'applicationForm':
          this.$router.push('/application-form'); // Adjust the route as needed
          break;
          case 'reviewApplication':
          this.$router.push('/review-application'); // Adjust the route as needed
          break;
        case 'login':
          this.$router.push('/login'); // Adjust the route as needed
          break;
      }
    },
  },
};
</script>

<template>
  <v-container>
    <!-- Navigation Menu -->
    <v-app-bar app>
      <v-btn @click="navigateTo('main')">Main Page</v-btn>
      <v-btn @click="navigateTo('applicationForm')">Apply Now</v-btn>
      <v-btn @click="navigateTo('reviewApplication')">Review Application</v-btn>
      <v-btn @click="navigateTo('login')">University Login</v-btn>
      <!-- Add more buttons for different user roles and actions -->
    </v-app-bar>

    <!-- Your Main Page Content Goes Here -->

  </v-container>
</template>

<style scoped>

</style>