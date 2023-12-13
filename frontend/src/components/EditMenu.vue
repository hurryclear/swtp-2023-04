<template>
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>Antrag</u>
      </v-card-title>
      <v-spacer></v-spacer>
        <v-btn class="button-top" @click="closeEditMenu">
          Schließen
        </v-btn>
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
      <u>Module</u>:
    </v-card-title>
    <div v-for="moduleData in form.moduleFormsData" v-bind:key="moduleData.key" >
      <v-card-subtitle>
        Modul {{ moduleData.key + 1 }}
      </v-card-subtitle>
      <v-card-item v-for="modules in moduleData.module2bCredited" v-bind:key="modules">
        <u>Modulname</u>: {{ moduleData.name }} <br>
        Gewünschte Anrechnung: {{ modules }}
      </v-card-item>
      <v-card-item>
        <u>Kommentar zu diesem Modul</u>: {{ moduleData.comment }}
      </v-card-item>
    </div>
    <v-text-field class="text-field" label="Begründung" v-model="begruendung"/>
    <v-card-actions>
        <v-btn class="button-bottom" @click="accept">
          Annehmen
        </v-btn>
        <v-btn class="button-bottom" @click="decline">
          Ablehnen
        </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  props: {
    display: Boolean,
    form: JSON
  },
  data() {
    return {
      begruendung: null
    }
  },
  methods: {
    closeEditMenu() {
      this.$emit("close-edit-menu");
    },
    accept() {
      console.log(this.form);
      console.log(this.begruendung);
      console.log("STATUS: ACCEPTED");
      this.$emit("close-edit-menu");
    },
    decline() {
      console.log(this.form);
      console.log(this.begruendung);
      console.log("STATUS: DECLINED");
      this.$emit("close-edit-menu");
    },
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