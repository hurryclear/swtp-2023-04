<template>
  <v-container>
      <!-- Navigation Menu -->
      <v-app-bar app>
        <v-btn @click="navigateTo('main')">{{ $t("navBar.main") }}</v-btn>
        <v-btn @click="navigateTo('applicationForm')">{{$t("navBar.apply")}}</v-btn>
        <v-btn @click="navigateTo('reviewApplication')">{{$t("navBar.review")}}</v-btn>
        <v-btn @click="navigateTo('login')">{{$t("navBar.login")}}</v-btn>
      </v-app-bar>
  </v-container>
</template>

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

<style scoped>

</style>