<template>
  <v-table class="table">
    <thead>
    <tr>
      <th style="text-align: left">Universität</th>
      <th style="text-align: center">Anzahl der Module</th>
      <th style="text-align: right;">Bisheriger Studiengang</th>
    </tr>
    </thead>
    <tr v-for="form in forms" v-bind:key="form" class="table-row">
      <td style="text-align: left">
        {{ form.universityData.universityName }}
      </td>
      <td style="text-align: center">
        {{ form.moduleFormsData.length }}
      </td>
      <td style="text-align: right">
        {{ form.universityData.studyProgram }}
      </td>
      <td>
        <v-btn @click="openEditMenu(form)">
          Bearbeiten
        </v-btn>
      </td>
    </tr>
  </v-table>
</template>

<script setup>
  import test1 from '@/assets/test_Form.json'
  import test2 from '@/assets/test_Form2.json'

  const forms = {test1, test2}

  function openEditMenu(form) {
    //edit menu not implemented yet
    console.log(form)
  }
</script>

<style scoped>
  .table {
    table-layout: fixed;
    margin-right: 5rem;
    width: 40%;
    border-collapse: collapse;
  }

  td {
    padding: 1rem;
    width: 33%;
  }

  tr {
    border: solid;
    border-width: 1px 0;
  }

  .table-row {
    padding-top: 20%;
  }
</style>