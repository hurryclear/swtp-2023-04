<template>
  <v-container fluid :key="updateKey">
    <h1>Prüfungsausschuss: In Bearbeitung</h1>
    <v-row>
      <v-col v-for="form in inBearbeitungForms" :key="form.timestamp">
        <v-card>
          <v-card-title>{{ form.universityData.universityName }}</v-card-title>
          <v-card-subtitle>{{ form.universityData.studyProgram }}</v-card-subtitle>
          <v-card-text>
            <div>Anzahl der Module: {{ form.moduleFormsData.length }}</div>
            <div>Land: {{ form.universityData.country }}</div>
          </v-card-text>
          <v-card-text v-if="form.comment">
            Kommentar vom Studienbüro: {{ form.comment }}
          </v-card-text>
          <v-card-actions>
            <v-btn color="green" @click="acceptForm(form.timestamp)">Annehmen</v-btn>
            <v-btn color="red" @click="declineForm(form.timestamp)">Ablehnen</v-btn>
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
