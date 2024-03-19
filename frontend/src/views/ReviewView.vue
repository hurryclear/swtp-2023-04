<template>
  <v-container>
    <ReviewComponent :applicationId="applicationId"/>
  </v-container>
</template>

<script>
import ReviewComponent from "@/components/ReviewComponent.vue"; // Adjust the path as necessary

export default {
  data() {
    return {
      applicationId: null
    };
  },
  mounted() {
    // Accessing application ID from query parameters
    this.applicationId = this.$route.query.applicationId;
  },
  components: {
    ReviewComponent
  }
};
</script>

<style>
  /* Add your styles here */
</style>
