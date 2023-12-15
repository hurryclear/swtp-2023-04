<template>
  <h1>Offene Anträge</h1>
  <v-table class="table">
    <thead>
    <tr>
      <th style="text-align: left;">{{$t("formDisplay.university")}}</th>
      <th style="text-align: center;">{{ $t("examiningCommitteeChairView.moduleCount") }}</th>
      <th style="text-align: right;">{{$t("applicationForm.previousStudyProgram")}}</th>
    </tr>
    </thead>
    <tr v-for="form in forms" v-bind:key="form" class="table-row">
      <td style="text-align: left;">
        {{ form.universityData.universityName }}
      </td>
      <td style="text-align: center;">
        {{ form.moduleFormsData.length }}
      </td>
      <td style="text-align: right;">
        {{ form.universityData.studyProgram }}
      </td>
      <td>
        <v-btn @click="openEditMenu(form)">
          {{ $t("formDisplay.edit") }}
        </v-btn>
      </td>
    </tr>
  </v-table>
</template>

<script>
  export default {
    computed: {
      // Use Vuex getter to get forms with a specific status
      forms() {
        return this.$store.getters.formsByStatus('Offen');
      }
    },
    methods: {
      openEditMenu(form) {
        this.$emit('open-edit-menu', form);
      }
    }
  }
</script>


<style scoped>
  .table {
    border: 2px solid gray;
    border-radius: 10px;
    width: 80%;
  }

  td {
    padding: 1rem;
    width: 33%;
  }

  .table-row {
    padding-top: 20%;
  }


</style>