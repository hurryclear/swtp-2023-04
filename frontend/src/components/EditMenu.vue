<template>
  <!--TODO: i18n-->
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>Antrag</u>
      </v-card-title>
      <v-spacer/>
      <v-btn-toggle class="button-top" v-model="isEdited" mandatory shaped variant="outlined">
        <v-btn :value="false">
          Original
        </v-btn>
        <v-btn :value="true">
          Bearbeitet
        </v-btn>
      </v-btn-toggle>
      <v-btn class="button-top" variant="tonal" @click="openComparisonMenu">Mit anderen Anträgen vergleichen</v-btn>
      <v-btn class="button-top" variant="tonal" icon="mdi-close" @click="closeEditMenu"></v-btn>
    </div>
    <v-card-text>
      Vorherige Universität: {{ applicationVersion.applicationData.university }}
    </v-card-text>
    <v-card-text>
      Vorheriger Studiengang: {{ applicationVersion.applicationData.oldCourseOfStudy }}
    </v-card-text>
    <v-card-text>
      Jetziger Studiengang: {{ applicationVersion.applicationData.newCourseOfStudy }}
    </v-card-text>
    <v-card-title>
      Module:
    </v-card-title>
    <div v-for="(moduleData, index) in applicationVersion.moduleFormsData" v-bind:key="moduleData.frontend_key">
      <v-card-subtitle>
        <br>
        Mapping {{ index + 1 }}
      </v-card-subtitle>
      <div v-for="(studentModule, index2) in moduleData.modulesStudent" v-bind:key="studentModule.frontend_key">
        <v-card-text><u>Modul {{ index2 + 1 }}</u></v-card-text>
        <v-card-text>Name:</v-card-text>
        <v-text-field
            :disabled="!isEdited"
            :label="studentModule.title"
            v-model="editedForm.edited.moduleFormsData[index].modulesStudent[index2].title"
        ></v-text-field>
        <v-card-text>Modulnummer:</v-card-text>
        <v-text-field
            :disabled="!isEdited"
            :label="studentModule.number"
            v-model="editedForm.edited.moduleFormsData[index].modulesStudent[index2].number"
        ></v-text-field>
        <v-card-text>Leistungspunkte:</v-card-text>
        <v-text-field
            :disabled="!isEdited"
            :label="studentModule.credits"
            v-model="editedForm.edited.moduleFormsData[index].modulesStudent[index2].credits"
        ></v-text-field>
        <v-card-text>Studentenkommentar: {{ studentModule.commentStudent }}</v-card-text>
        <v-card-text>Studienbürokommentar: </v-card-text>
        <v-text-field
            :disabled="!isEdited"
            :label="studentModule.commentEmployee"
            v-model="editedForm.edited.moduleFormsData[index].modulesStudent[index2].commentEmployee"
        ></v-text-field>
        <v-btn style="margin: 1%" @click="downloadPdf(studentModule.path, studentModule.title)">Beschreibung herunterladen</v-btn>
        <v-text-field label="Formale Ablehnung" v-model="editedForm.edited.moduleFormsData[index].modulesStudent[index2].reason"></v-text-field>
        <v-btn
            @click="setFormalReject(index, index2)"
            :disabled="!isEdited"
            class="button-top"
            prepend-icon="mdi-hand-back-left"
            variant="flat"
            color="red">Formal Ablehnen</v-btn>
      </div>
      <v-card-text>
        <u>Anrechnen für:</u>
        <br>
        <div v-for="(module, index3) in moduleData.modules2bCredited" v-bind:key="module">
          <v-autocomplete
              class="text-field"
              :items="getMajorModulesName()"
              :label="findModule(applicationVersion.moduleFormsData[index].modules2bCredited[index3])"
              :disabled="!isEdited"
              v-model="editedForm.edited.moduleFormsData[index].modules2bCredited[index3]"
          ></v-autocomplete>
        </div>
      </v-card-text>
      <v-divider/>
    </div>
    <v-divider/>
    <v-text-field class="text-field" label="Begründung" v-model="reason"/>
    <v-card-actions>
      <v-btn
          color="blue"
          variant="flat"
          class="button-bottom"
          prepend-icon="mdi-arrow-u-left-top"
          :loading="loadingSendButton"
          @click="sendToPruefungsausschuss(true)"
      >
        An Prüfungsausschuss senden
      </v-btn>
      <v-btn
          color="green"
          variant="flat"
          class="button-bottom"
          prepend-icon="mdi-content-save"
          :loading="loadingSaveButton"
          @click="saveEditedForm(false)"
      >
        Speichern
      </v-btn>
      <p v-if="showCommentWarning" style="color:red">Bitte geben sie eine Begründung an!</p>
    </v-card-actions>
  </v-card>
</template>


<script>
import axios from "@/plugins/axios";
export default {
  props: {
    form: JSON
  },

  created() {
    this.getModules();
    this.editedForm = structuredClone(this.form);
    this.replaceIdWithName();
  },

  data() {
    return {
      reason: '',
      showCommentWarning: false,
      loadingSaveButton: false,
      loadingSendButton: false,
      isEdited: false,
      editedForm: null,
      majorModules: []
    }
  },

  computed: {
    applicationVersion() {
      return this.isEdited ? this.form.edited : this.form.original;
    }
  },

  methods: {
    closeEditMenu() {
      this.$emit("close-edit-menu");
    },

    closeEditMenuBySaving() {
      this.$emit("close-edit-menu-by-saving");
    },

    async sendToPruefungsausschuss(readyForApproval) {
      this.loadingSendButton = true;
      await this.saveEditedForm(readyForApproval)

      if(this.reason === "") {
        this.showCommentWarning = true;
        return;
      }

      this.showCommentWarning = false;
      this.editedForm.formalReject = this.reason;

      await axios.put("/api/application/readyForApproval", this.editedForm)
          .then(response => console.log(response))
          .catch(err => console.error("Error setting the form status to ready for approval: ", err));
      this.loadingSendButton = false;
      this.closeEditMenuBySaving();
    },

    openComparisonMenu() {
      this.$emit("open-comparison");
    },

    setFormalReject(index, index2) {
      this.editedForm.edited.moduleFormsData[index].modulesStudent[index2].approval = "formally rejected";
    },

    async getModules() {
      await axios.get(`/api/unidata/getModules?majorName=${this.form.original.applicationData.newCourseOfStudy}`).then(
          res => this.majorModules = res.data.modules
      ).catch(err => {
        console.log(err);
      });
    },

    findModule(module) {
      const foundModule = this.majorModules.find(item => item.id === module);
      return foundModule ? foundModule.name : "Module not found";
    },

    findModuleInverse(module) {
      const foundModule = this.majorModules.find(item => item.name === module)
      return foundModule ? foundModule.id : undefined;
    },

    replaceIdWithName() {
      for(let i = 0; i < this.editedForm.original.moduleFormsData.length; i++) {
        for(let j = 0; j < this.editedForm.original.moduleFormsData[i].modules2bCredited.length; j++) {
          //Replace module ID in moduleFormsData[i], modules2bCredited[j] with their names
          this.editedForm.original.moduleFormsData[i].modules2bCredited[j] = this.findModule(this.editedForm.original.moduleFormsData[i].modules2bCredited[j]);
          this.editedForm.edited.moduleFormsData[i].modules2bCredited[j] = this.findModule(this.editedForm.edited.moduleFormsData[i].modules2bCredited[j]);
        }
      }
    },

    replaceNameWithId() {
      for(let i = 0; i < this.editedForm.original.moduleFormsData.length; i++) {
        for(let j = 0; j < this.editedForm.original.moduleFormsData[i].modules2bCredited.length; j++) {
          //Replace module name in moduleFormsData[i], modules2bCredited[j] with their IDs (IMPORTANT FOR SAVING)
          this.editedForm.original.moduleFormsData[i].modules2bCredited[j] = this.findModuleInverse(this.editedForm.original.moduleFormsData[i].modules2bCredited[j]);
          this.editedForm.edited.moduleFormsData[i].modules2bCredited[j] = this.findModuleInverse(this.editedForm.edited.moduleFormsData[i].modules2bCredited[j])
        }
      }
    },

    getMajorModulesName() {
      return this.majorModules.map(module => module.name)
    },

    async saveEditedForm(readyForApproval) {
      this.replaceNameWithId();

      this.loadingSaveButton = true;
      await axios.put("/api/application/saveEdited", this.editedForm)
          .then(
              response => console.log(response)
          )
          .catch(err => console.error("Error saving edited form to database: ", err));

      this.loadingSaveButton = false;
      if(!readyForApproval) {
        this.closeEditMenuBySaving();
      }
    },

    async downloadPdf(filePath, fileName) {
      try {
        const response = await axios.get("/api/application/getModulePDF", {
          params: {
            filePath
          },
          responseType: 'blob' // Ensure response is treated as a blob
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName + ".pdf");
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error('Error downloading PDF:', error);
      }
    }
  }
}
</script>

<style scoped>
.v-text-field {
  margin: 1%;
}

.button-bottom {
  margin: 2%
}

.button-top {
  margin: 1%;
}

.card-header {
  display: flex;
  align-items: center;
}

</style>