<template>
  <v-container fluid :key="updateKey">
    <v-row>
      <v-col v-for="form in inBearbeitungForms" :key="form.timestamp">
        <v-card>
          <v-card-title>{{ form.universityData.universityName }}</v-card-title>
          <v-card-subtitle>{{ form.universityData.studyProgram }}</v-card-subtitle>
          <v-card-text>
            <div>{{$t("examiningCommitteeChairView.moduleCount")}}: {{ form.moduleFormsData.length }}</div>
            <div>{{$t("applicationForm.countryLabel")}}: {{ form.universityData.country }}</div>
          </v-card-text>
          <v-card-text v-if="form.comment">
            {{$t("examiningCommitteeChairView.studentAffairsOfficeComment")}}: {{ form.comment }}
          </v-card-text>
          <v-card-actions>
            <v-btn color="green" @click="acceptForm(form.timestamp)">{{$t("examiningCommitteeChairView.accept")}}</v-btn>
            <v-btn color="red" @click="declineForm(form.timestamp)">{{$t("examiningCommitteeChairView.decline")}}</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  computed: {
    inBearbeitungForms() {
      // Get forms that are 'in bearbeitung'
      return this.$store.getters.formsByStatus('in Bearbeitung');
    }
  },
  methods: {
      // Dispatch action to accept the form
    acceptForm(formTimestamp) {
      this.$store.dispatch('changeFormStatus', {
        formId: formTimestamp,
        newStatus: 'Akzeptiert'
      });
    },
    declineForm(formTimestamp) {
      // Dispatch action to decline the form
      this.$store.dispatch('changeFormStatus', {
        formId: formTimestamp,
        newStatus: 'Abgelehnt'
      });
    },
  }
}
</script>

<style scoped>
</style>
