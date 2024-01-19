<template>
  <!--TODO: i18n-->
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>Antrag</u>
      </v-card-title>
      <v-spacer></v-spacer>
        <v-btn @click="openComparisonMenu">Mit anderen Anträgen vergleichen</v-btn>
        <v-btn class="button-top"  icon="mdi-close" @click="closeEditMenu" variant="text"></v-btn>
    </div>
    <v-card-item>
      <u>Universität</u>: {{ form.universityData.universityName }}
    </v-card-item>
    <v-card-item>
      <u>Bisheriger Studiengang</u>: {{ form.universityData.studyProgram }}
    </v-card-item>
    <v-card-item>
      <u>Land</u>: {{ form.universityData.country }}
    </v-card-item>
    <v-card-title>
      Module:
    </v-card-title>
    <div v-for="moduleData in form.moduleFormsData" v-bind:key="moduleData.key" >
      <v-card-subtitle>
        Modul {{ moduleData.key + 1 }}
      </v-card-subtitle>
      <v-card-item>
        <u>Modulname</u>: {{ moduleData.name }}
        <v-card-item>
          Gewünschte Anrechnung:
          <v-card-text v-for="modules in moduleData.module2bCredited" v-bind:key="modules">
            {{ modules}}
          </v-card-text>
        </v-card-item>
      </v-card-item>
      <v-card-item>
        <u>Kommentar zu diesem Modul</u>: {{ moduleData.comment }}
      </v-card-item>
    </div>
    <v-text-field class="text-field" label="Begründung" v-model="reason"/>
    <v-card-actions>
        <v-btn color="blue" class="button-bottom" @click="sendToPruefungsausschuss">
          An Prüfungsausschuss senden
        </v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
export default {
  props: {
    form: JSON
  },

  data() {
    return {
      reason: '',
    }
  },

  methods: {
    closeEditMenu() {
      this.$emit("close-edit-menu");
    },
    sendToPruefungsausschuss() {
      // Dispatch action to accept the form
      this.$store.dispatch('changeFormStatus', {
        formId: this.form.id,
        newStatus: 'in progress',
        comment: this.reason
      });
      this.closeEditMenu();
    },
    openComparisonMenu() {
      this.$emit("open-comparison");
    }
  },
}
</script>

<style scoped>
  .text-field {
    margin: 2%;
  }

  .button-bottom {
    margin-left: 1%;
    margin-bottom: 1%;
  }

  .button-top {
    margin-right: 1%;
  }

  .card-header {
    display: flex;
    align-items: center;
  }
</style>