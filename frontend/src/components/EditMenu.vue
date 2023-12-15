<template>
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>{{ $t("formDisplay.application") }}</u>
      </v-card-title>
      <v-spacer></v-spacer>
        <v-btn class="button-top"  icon="mdi-close" @click="closeEditMenu" variant="text"></v-btn>
    </div>
    <v-card-item>
      <u>{{ $t("formDisplay.university") }}</u>: {{ form.universityData.universityName }}
    </v-card-item>
    <v-card-item>
      <u>{{$t("applicationForm.previousStudyProgram")}}</u>: {{ form.universityData.studyProgram }}
    </v-card-item>
    <v-card-item>
      <u>{{ $t("applicationForm.countryLabel") }}</u>: {{ form.universityData.country }}
    </v-card-item>
    <v-card-title>
      <u>{{ $t("editMenu.modules") }}</u>:
    </v-card-title>
    <div v-for="moduleData in form.moduleFormsData" v-bind:key="moduleData.key" >
      <v-card-subtitle>
        {{ $t("applicationForm.module") }} {{ moduleData.key + 1 }}
      </v-card-subtitle>
      <v-card-item v-for="modules in moduleData.module2bCredited" v-bind:key="modules">
        <u>{{ $t("applicationForm.moduleNameLabel")}}</u>: {{ moduleData.name }} <br>
        {{ $t("applicationForm.moduleCreditedLabel")}}: {{ modules }}
      </v-card-item>
      <v-card-item>
        <u>{{ $t("applicationForm.commentLabel")}}</u>: {{ moduleData.comment }}
      </v-card-item>
    </div>
    <v-text-field class="text-field" :label="$t('editMenu.reason')" v-model="reason"/>
    <v-card-actions>
        <v-btn color="blue" class="button-bottom" @click="sendToPruefungsausschuss">
          {{ $t("editMenu.sendToExaminingCommitteeChair") }}
        </v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
export default {
  props: {
    form: Object
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
        newStatus: 'in Bearbeitung',
        comment: this.reason
      });
      this.closeEditMenu();
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