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
        Mapping {{ index + 1}}
      </v-card-subtitle>
      <v-card-text><u>Modulnamen:</u></v-card-text>
      <div v-for="(studentModules) in moduleData.modulesStudent" v-bind:key="studentModules.frontend_key">
        <v-autocomplete class="text-field" :items="getMajorModulesName()" :label="studentModules.title" :disabled="!isEdited"></v-autocomplete>
      </div>
      <v-card-text>
        Anrechnen für:
        <br>
        <div v-for="(module) in moduleData.modules2bCredited" v-bind:key="module">
          <v-autocomplete class="text-field" :items="getMajorModulesName()" :label="findModule(module)" :disabled="!isEdited"></v-autocomplete>
        </div>
      </v-card-text>
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
          @click="sendToPruefungsausschuss"
      >
        An Prüfungsausschuss senden
      </v-btn>
      <v-btn
          color="green"
          variant="flat"
          class="button-bottom"
          prepend-icon="mdi-content-save"
          :loading="loadingSaveButton"
          @click="saveEditedForm"
      >
        Speichern
      </v-btn>
      <p v-if="showFormWarning" style="color:red">Es dürfen keine leeren Anrechnungen gespeichert werden!</p>
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
    this.getModules()
  },

  data() {
    return {
      reason: '',
      showFormWarning: false,
      showCommentWarning: false,
      loadingSaveButton: false,
      loadingSendButton: false,
      isEdited: false,
      editedForm: null,
      editedModules: "",
      majorModules: [
        {
          "number": "10-201-2005-2",
          "name": "Programmierparadigmen",
          "id": 2
        },
        {
          "number": "10-201-2001-1",
          "name": "Algorithmen und Datenstrukturen 1",
          "id": 3
        }
      ]
    }
  },

  computed: {
    applicationVersion() {
      return this.isEdited ? this.form.edited : this.form.original
    }
  },

  methods: {
    closeEditMenu() {
      this.$emit("close-edit-menu");
    },

    sendToPruefungsausschuss() {
      // Dispatch action to accept the form
      console.log(this.isEdited === 1)
    },

    openComparisonMenu() {
      this.$emit("open-comparison");
    },

    async getModules() {
      await axios.get(`/api/unidata/getModules?majorName=${this.form.original.applicationData.newCourseOfStudy}`).then(
          res => this.modules = res.data.modules
      ).catch(err => {
        console.log(err);
      });
    },

    findModule(module) {
      const foundModule = this.majorModules.find(item => item.id === module);
      return foundModule ? foundModule.name : "Module not found";
    },

    getMajorModulesName() {
      return this.majorModules.map(module => module.name)
    },

    async saveEditedForm() {
      //Check if any of the fields are empty
      for(let i = 0; i < this.editedForm.moduleFormsData.length; i++) {
        for(let j = 0; j < this.editedForm.moduleFormsData[i].module2bCredited.length; j++) {
          if(this.editedModules[i][j] === undefined) {
            this.showFormWarning = true;
            return;
          }
        }
      }
      this.showFormWarning = false;
      //Replace module2bCredited[j] in moduleFormsData[i] with module that was changed by student affairs office
      //i - index for mapping of modules
      //j - index for module in the mapping
      for(let i = 0; i < this.editedForm.moduleFormsData.length; i++) {
        for(let j = 0; j < this.editedForm.moduleFormsData[i].module2bCredited.length; j++) {
          this.editedForm.moduleFormsData[i].module2bCredited[j] = this.editedModules[i][j];
        }
      }

      if(this.reason === "") {
        this.showCommentWarning = true;
        return;
      }
      this.showCommentWarning = false;
      this.editedForm.comment = this.reason;

      this.loadingSaveButton = true;
      await axios.put("/api/application/saveEdited"
      ).then

      this.loadingSaveButton = false;
    }
  }
}
</script>

<style scoped>
.text-field {
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