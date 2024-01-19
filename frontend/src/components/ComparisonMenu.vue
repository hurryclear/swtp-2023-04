<template>
  <!--TODO: i18n-->
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>Anträge</u>
      </v-card-title>
      <v-spacer></v-spacer>
      <v-btn class="button-top"  icon="mdi-close" @click="closeComparison" variant="text"></v-btn>
    </div>
    <div v-for="form in forms" v-bind:key="form">
      <v-card-subtitle>Antrag</v-card-subtitle>
      <v-card-item>
        Universität: {{ form.universityData.universityName }}
      </v-card-item>
      <v-card-item>
        Bisheriger Studiengang: {{ form.universityData.studyProgram }}
      </v-card-item>
      <v-card-item>
        Land: {{ form.universityData.country }}
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
      </div>
      <v-card-item>
        Status: {{ form.status }}
      </v-card-item>
      <v-card-item>Begründung: {{ form.comment }}</v-card-item>
      <br>
      <v-divider/>
    </div>
  </v-card>
</template>

<script>
  import form1 from "@/assets/form-2023-12-15T10_34_51.207Z.json"
  import form2 from "@/assets/form-2017-10-21T22_11_56.973Z.json"
  import form3 from "@/assets/form-2023-12-13T6_45_30.512.json"
  import form4 from "@/assets/form4.json"
  export default {
    data() {
      return {
        forms: [form1, form2, form3, form4]
      }
    },
    methods: {
      closeComparison() {
        this.$emit("close-comparison");
      }
    }
  }
</script>

<style scoped>
  .card-header {
    display: flex;
    align-items: center;
  }
</style>