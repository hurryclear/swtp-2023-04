<template>
    <v-app class="wrapper">
      <h1>Offene Anträge</h1>
      <FormDisplay/>
    </v-app>
</template>

<script setup>
  import FormDisplay from "@/components/FormDisplay.vue";

</script>

<style scoped>
  .wrapper {
    margin-left: 5%;
  }
</style>