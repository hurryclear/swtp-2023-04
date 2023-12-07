<template>
    <v-container fluid="true" class="wrapper">
      <h1>Offene Anträge</h1>
      <FormDisplay/>
    </v-container>
    <div>
      <LogoutButton />
    </div>
</template>

<script setup>
  import FormDisplay from "@/components/FormDisplay.vue";

  import LogoutButton from '@/components/LogoutButton.vue'; 

</script>

<style scoped>
  .wrapper {
    margin-left: 5%;
  }
</style>