<template>
  <div>
    <v-card>
      <div class="card-header">
        <v-card-title>
          <u>Antrag</u>
        </v-card-title>
        <v-spacer/>
        <v-btn class="button-top" variant="tonal" icon="mdi-close" @click="this.$emit('close');"/>
      </div>
      <v-card-text>
        Vorherige Universität: {{ copy.edited.applicationData.university }}
      </v-card-text>
      <v-card-text>
        Vorheriger Studiengang: {{ copy.edited.applicationData.oldCourseOfStudy }}
      </v-card-text>
      <v-card-text>
        Jetziger Studiengang: {{ copy.edited.applicationData.newCourseOfStudy }}
      </v-card-text>
      <v-card-title>
        Module:
      </v-card-title>
      <div v-for="(moduleData, index) in copy.edited.moduleFormsData" v-bind:key="moduleData.frontend_key">
        <v-card-subtitle>
          <br>
          Mapping {{ index + 1 }}
        </v-card-subtitle>
        <div v-for="(studentModule, index2) in moduleData.modulesStudent" v-bind:key="studentModule.frontend_key">
          <v-card-text><u>Modul {{ index2 + 1 }}</u></v-card-text>
          <v-card-text>Name: {{ studentModule.title }}</v-card-text>
          <v-card-text>Modulnummer: {{ studentModule.number }}</v-card-text>
          <v-card-text>Leistungspunkte: {{ studentModule.credits }}</v-card-text>
          <v-card-text>Studentenkommentar: {{ studentModule.commentStudent }}</v-card-text>
          <v-card-text>Studienbürokommentar: {{ studentModule.commentEmployee }}</v-card-text>
          <v-btn style="margin: 1%" @click="downloadPdf(studentModule.path)">Beschreibung herunterladen</v-btn>
          <v-card-text>Entscheidung: {{ studentModule.reason }}</v-card-text>
        </div>
        <v-card-text>
          <u>Anrechnen für:</u>
          <br>
          <div v-for="module in moduleData.modules2bCredited" v-bind:key="module">
            <v-card-text>{{ module }}</v-card-text>
          </div>
        </v-card-text>
        <v-divider/>
      </div>
      <v-divider/>
      <v-card-text>Entscheidung: {{ copy.edited.formalReject}}</v-card-text>
    </v-card>
  </div>
</template>

<script>
import StudentAffairsOfficeService from "@/services/StudentAffairsOfficeService";

export default {
  props: {
    form: JSON
  },

  data() {
    return {
      copy: {},
      majorModules: []
    }
  },

  methods: {

    findModule(module) {
      const foundModule = this.majorModules.find(item => item.id === module);
      return foundModule ? foundModule.name : "Module not found";
    },

    replaceIdWithName() {
      for (let i = 0; i < this.copy.edited.moduleFormsData.length; i++) {
        for (let j = 0; j < this.copy.edited.moduleFormsData[i].modules2bCredited.length; j++) {
          //Replace module ID in moduleFormsData[i], modules2bCredited[j] with their names
          this.copy.edited.moduleFormsData[i].modules2bCredited[j] = this.findModule(this.copy.edited.moduleFormsData[i].modules2bCredited[j]);
        }
      }
    },

    createCopy() {
      this.copy = structuredClone(this.form);
    },

    async getModules() {
      try {
        this.majorModules = await StudentAffairsOfficeService.getModules(this.copy.edited.applicationData.newCourseOfStudy);
      } catch (error) {
        console.error('Error fetching modules:', error);
      }
    },

    async downloadPdf(pdfPath) {
      try {
        const pdfBlob = await StudentAffairsOfficeService.getModulePDF(pdfPath);
        const url = window.URL.createObjectURL(new Blob([pdfBlob]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'file.pdf');
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error('Error downloading PDF:', error);
      }
    }

  },

  async created() {
    this.createCopy();
    await this.getModules();
    this.replaceIdWithName();
  }
}
</script>

<style scoped>
.button-top {
  margin: 1%;
}

.card-header {
  display: flex;
  align-items: center;
}
</style>