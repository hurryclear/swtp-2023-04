<script>
export default {
  methods: {
    navigateTo(page) {
      if (page === 'home'){
        this.$router.push('/')
      }
    }
  }
}
</script>

<template>
  <v-btn @click="navigateTo('home')">Home</v-btn>
</template>

<style scoped>

</style>