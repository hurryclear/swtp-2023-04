<template>
  <li class="list">
    <v-btn class="edit-button">
      Anträge bearbeiten
    </v-btn>
    Antrag: {{ json.universityData.universityName }}
  </li>
</template>

<script setup>
  import json from '@/assets/test_Form.json'
</script>

<style scoped>
  .list {
    padding: 30px;
  }

  .edit-button {
    margin-right: 0.5rem;
  }
</style>