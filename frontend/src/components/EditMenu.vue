<template>
  <!--TODO: i18n-->
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>Antrag</u>
      </v-card-title>
      <v-spacer/>
      <v-btn-toggle
          class="button-top"
          v-model="isEdited"
          mandatory
          shaped
          variant="outlined">
        <v-btn :value="false">
          Original
        </v-btn>
        <v-btn :value="true">
          Bearbeitet
        </v-btn>
      </v-btn-toggle>
      <v-btn
          class="button-top"
          variant="tonal"
          @click="this.$emit('open',{component:'ComparisonMenu',form:{}});"
      >Mit anderen Anträgen vergleichen
      </v-btn>
      <v-btn
          class="button-top"
          variant="tonal"
          icon="mdi-close"
          @click="this.$emit('close')"
      />
    </div>

    <v-divider/>
    <v-text-field class="text-field" label="Begründung" v-model="formalReject"/>
    <p v-if="showCommentWarning" style="color:red; margin: 1%">Bitte geben sie eine Begründung an!</p>
    <v-btn
        @click="formallyReject"
        class="button-top"
        color="red"
        prepend-icon="mdi-hand-back-left"
    >Ganzen Antrag formal ablehnen</v-btn>
    <v-divider/>

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
    <div v-for="(moduleData, i) in applicationVersion.moduleFormsData" v-bind:key="moduleData.frontend_key">
      <v-card-subtitle>
        <br>
        Mapping {{ i + 1 }}
      </v-card-subtitle>
      <div v-for="(studentModule, j) in moduleData.modulesStudent" v-bind:key="studentModule.frontend_key">
        <v-card-text><u>Modul {{ j + 1 }}</u></v-card-text>
        <v-card-text>Name:</v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.title"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].title"
        />
        <v-card-text>Modulnummer:</v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.number"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].number"
        />
        <v-card-text>Leistungspunkte:</v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.credits"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].credits"
        />
        <v-card-text>Studentenkommentar: {{ studentModule.commentStudent }}</v-card-text>
        <v-card-text>Studienbürokommentar: </v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.commentEmployee"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].commentEmployee"
        />
        <v-btn style="margin: 1%" @click="downloadPdf(studentModule.path, studentModule.title)">Beschreibung herunterladen</v-btn>
        <v-text-field
            v-if="editedForm"
            label="Formale Ablehnung"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].reason"/>
        <v-row style="margin: 1%">
          <v-btn
              @click="this.editedForm.edited.moduleFormsData[i].modulesStudent[j].approval = 'formally rejected'"
              :disabled="!isEdited"
              class="button-top"
              prepend-icon="mdi-hand-back-left"
              variant="flat"
              color="red"
          >Formal Ablehnen</v-btn>
          <v-btn
              @click="this.editedForm.edited.moduleFormsData[i].modulesStudent[j].approval = 'edited'"
              v-if="editedForm && editedForm.edited.moduleFormsData[i].modulesStudent[j].approval === 'formally rejected'"
              class="button-top"
              prepend-icon="mdi-keyboard-backspace"
              variant="flat"
              color="yellow"
          >Ablehnung rückgängig machen</v-btn>
        </v-row>
      </div>
      <v-card-text>
        <u>Anrechnen für:</u>
        <br>
        <div v-for="(module, k) in moduleData.modules2bCredited" v-bind:key="module">
          <v-autocomplete
              v-if="editedForm"
              class="text-field"
              :items="this.majorModules.map(module => module.name)"
              :label="findModule(applicationVersion.moduleFormsData[i].modules2bCredited[k])"
              :disabled="!isEdited"
              v-model="editedForm.edited.moduleFormsData[i].modules2bCredited[k]"
          />
        </div>
      </v-card-text>
      <v-divider/>
    </div>
    <v-divider/>
    <v-card-actions>
      <v-btn
          color="blue"
          variant="flat"
          class="button-bottom"
          prepend-icon="mdi-arrow-u-left-top"
          :loading="loadingSendButton"
          @click="sendToExaminingCommitteeChair(true)"
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
    </v-card-actions>
  </v-card>
</template>


<script>
import StudentAffairsOfficeService from "@/services/StudentAffairsOfficeService";

export default {
  props: {
    form: JSON
  },

  async created() {
    await this.getModules()
    this.editedForm = structuredClone(this.form);
    this.replaceIdWithName();
  },

  data() {
    return {
      formalReject: '',
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
    async getModules() {
      try {
        this.majorModules = await StudentAffairsOfficeService.getAllModules(this.form.original.applicationData.newCourseOfStudy);
      } catch (error) {
        console.error(error.message);
      }
    },

    async sendToExaminingCommitteeChair(readyForApproval) {
      try {
        await this.saveEditedForm(readyForApproval);
        await StudentAffairsOfficeService.sendFormToApproval(this.editedForm);
        this.$emit("save");
      } catch (error) {
        console.error(error.message);
      }
    },

    async formallyReject() {
      try {
        if (this.formalReject === "") {
          this.showCommentWarning = true;
          return;
        }
        this.showCommentWarning = false;
        this.editedForm.edited.applicationData.formalReject = this.formalReject;
        this.replaceNameWithId();
        await StudentAffairsOfficeService.formallyRejectForm(this.editedForm);
        this.$emit("close");
      } catch (error) {
        console.error(error.message);
      }
    },

    async saveEditedForm(readyForApproval) {
      try {
        this.replaceNameWithId();
        this.editedForm.edited.applicationData.dateLastEdited = new Date().toISOString();
        await StudentAffairsOfficeService.saveEditedForm(this.editedForm);
        if (!readyForApproval) {
          this.$emit("save");
        }
      } catch (error) {
        console.error(error.message);
      }
    },

    async downloadPdf(filePath, fileName) {
      try {
        const pdfData = await StudentAffairsOfficeService.getModulePDF(filePath);
        const url = window.URL.createObjectURL(new Blob([pdfData]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName + ".pdf");
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error('Error downloading PDF:', error.message);
      }
    },

    replaceData(dataArray, findMethod, replaceMethod) {
      dataArray.forEach((data) => {
        data.modules2bCredited.forEach((value, index) => {
          const replacement = findMethod(value);
          if (replacement !== undefined) {
            data.modules2bCredited[index] = replaceMethod(replacement);
          }
        });
      });
    },

    replaceIdWithName() {
      this.replaceData(
          this.editedForm.original.moduleFormsData,
          this.findModule,
          (moduleName) => moduleName
      );
    },

    findModule(module) {
      const foundModule = this.majorModules.find(item => item.id === module);
      return foundModule ? foundModule.name : "Module not found";
    },

    replaceNameWithId() {
      this.replaceData(
          this.editedForm.edited.moduleFormsData,
          this.findModuleInverse,
          (moduleId) => moduleId
      );
    },

    findModuleInverse(module) {
      const foundModule = this.majorModules.find(item => item.name === module)
      return foundModule ? foundModule.id : undefined;
    },

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