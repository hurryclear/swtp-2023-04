<template>
    <v-container fluid="true" class="wrapper">
      <h1>Offene Anträge</h1>
      <FormDisplay/>
    </v-container>
</template>

<script setup>
  import FormDisplay from "@/components/FormDisplay.vue";
</script>

<style scoped>
  .wrapper {
    margin-left: 5%;
  }
</style>