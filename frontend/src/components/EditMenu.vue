<template>
  <!--TODO: i18n-->
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>Antrag</u>
      </v-card-title>
      <v-spacer/>
      <v-btn @click="openComparisonMenu">Mit anderen Anträgen vergleichen</v-btn>
      <v-btn class="button-top"  icon="mdi-close" @click="closeEditMenu" variant="text"></v-btn>
    </div>
    <v-card-text>
      Universität: {{ form.universityData.universityName }}
    </v-card-text>
    <v-card-text>
      Bisheriger Studiengang: {{ form.universityData.studyProgram }}
    </v-card-text>
    <v-card-text>
      Land: {{ form.universityData.country }}
    </v-card-text>
    <v-card-title>
      Module:
    </v-card-title>
    <div v-for="(moduleData, index) in form.moduleFormsData" v-bind:key="moduleData.key">
      <v-card-subtitle>
        Mapping {{ moduleData.key + 1 }}
      </v-card-subtitle>
      <v-card-text>
        <u>Modulnamen</u>: {{ moduleData.name }}
        <br> <br>
        Anrechnen für:
        <div v-for="(modules, index2) in moduleData.module2bCredited" v-bind:key="modules">
          <v-autocomplete :items="this.allModules" :label="modules" v-model="editedModules[index][index2]"></v-autocomplete>
        </div>
      </v-card-text>
      <v-card-text>
        <u>Kommentar zu diesem Modul</u>: {{ moduleData.comment }}
      </v-card-text>
    </div>
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
import module_list from "@/assets/module_liste.json"
import axios from "@/plugins/axios";
export default {
  props: {
    form: JSON
  },

  data() {
    return {
      reason: '',
      showFormWarning: false,
      showCommentWarning: false,
      loadingSaveButton: false,
      loadingSendButton: false,
      editedForm: this.form,
      editedModules: [[]],
      allModules: module_list.courses[0].modules.map(module => module.name)
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
  },
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
  margin-right: 1%;
}

.card-header {
  display: flex;
  align-items: center;
}
</style>