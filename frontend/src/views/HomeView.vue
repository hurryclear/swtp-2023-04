<template>
  <v-container>
    <!-- Main Navigation Buttons -->
    <v-row>
      <v-col>
        <!-- Student Navigation Button -->
        <v-btn @click="navigateTo('applicationForm')">Apply Now</v-btn>
      </v-col>
      <v-col>
        <!-- University Navigation Button -->
        <v-btn @click="navigateTo('login')">University Login</v-btn>
      </v-col>
    </v-row>

    <!-- Your Main Page Content Goes Here -->

  </v-container>
</template>

<script>
export default {
  methods: {
    navigateTo(page) {
      // Implement navigation logic based on the clicked button
      if (page === 'applicationForm') {
        this.$router.push('/application-form'); // Adjust the route as needed
      } else if (page === 'login') {
        this.$router.push('/login'); // Adjust the route as needed
      }
    },
  },
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
