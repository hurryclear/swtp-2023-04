<template>
  <ul v-for="form in forms" v-bind:key="form" class="list">
    <v-row>
      Studiengang: {{ form.universityData.studyProgram }}
      <br>
      Anzahl der Module: {{ form.moduleFormsData.length }}
      <v-btn class="edit-button">
        Bearbeiten
      </v-btn>
    </v-row>
  </ul>
</template>

<script setup>
import test1 from '@/assets/test_Form.json'
import test2 from '@/assets/test_Form2.json'

const forms = {test1, test2}
</script>

<style scoped>
.list {
  padding: 2rem;
}

.edit-button {
  margin-left: 5%;
  align-self: center;
}
</style>