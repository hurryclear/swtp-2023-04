<template>
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>{{ $t('studentAffairsOfficeView.application') }}</u>
      </v-card-title>
      <v-spacer/>
      <v-btn-toggle
          class="button-top"
          v-model="isEdited"
          mandatory
          shaped
          variant="outlined">
        <v-btn :value="false">
          {{ $t('studentAffairsOfficeView.original') }}
        </v-btn>
        <v-btn :value="true">
          {{ $t('studentAffairsOfficeView.edited') }}
        </v-btn>
      </v-btn-toggle>
      <v-btn
          class="button-top"
          variant="tonal"
          @click="this.$emit('open',{component:'ComparisonMenu',form:{}});"
      >{{ $t('studentAffairsOfficeView.compareWithOtherApplications') }}
      </v-btn>
      <v-btn
          class="button-top"
          variant="tonal"
          icon="mdi-close"
          @click="this.$emit('close')"
      />
    </div>

    <v-divider/>
    <v-text-field class="text-field" :label="$t('studentAffairsOfficeView.reason')" v-model="formalReject"/>
    <p v-if="showCommentWarning" style="color:red; margin: 1%">{{ $t('studentAffairsOfficeView.enterReason') }}</p>
    <v-btn
        @click="formallyReject"
        class="button-top"
        color="red"
        prepend-icon="mdi-hand-back-left"
    >{{ $t('studentAffairsOfficeView.formallyRejectApplication') }}</v-btn>
    <v-divider/>

    <v-card-text>
      {{ $t('studentAffairsOfficeView.previousUniversity') }}: {{ applicationVersion.applicationData.university }}
    </v-card-text>
    <v-card-text>
      {{ $t('studentAffairsOfficeView.previousCourse') }}: {{ applicationVersion.applicationData.oldCourseOfStudy }}
    </v-card-text>
    <v-card-text>
      {{ $t('studentAffairsOfficeView.currentCourse') }}: {{ applicationVersion.applicationData.newCourseOfStudy }}
    </v-card-text>
    <v-card-title>
      {{ $t('studentAffairsOfficeView.modules') }}:
    </v-card-title>
    <div v-for="(moduleData, i) in applicationVersion.moduleFormsData" v-bind:key="moduleData.frontend_key">
      <v-card-subtitle>
        <br>
        {{ $t('studentAffairsOfficeView.mapping') }} {{ i + 1 }}
      </v-card-subtitle>
      <div v-for="(studentModule, j) in moduleData.modulesStudent" v-bind:key="studentModule.frontend_key">
        <v-card-text><u>{{ $t('studentAffairsOfficeView.module') }} {{ j + 1 }}</u></v-card-text>
        <v-card-text>{{ $t('studentAffairsOfficeView.name') }}:</v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.title"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].title"
        />
        <v-card-text>{{ $t('studentAffairsOfficeView.moduleNumber') }}:</v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.number"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].number"
        />
        <v-card-text>{{ $t('studentAffairsOfficeView.credits') }}:</v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.credits"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].credits"
        />
        <v-card-text>{{ $t('studentAffairsOfficeView.studentComment') }}: {{ studentModule.commentStudent }}</v-card-text>
        <v-card-text>{{ $t('studentAffairsOfficeView.officeComment') }}: </v-card-text>
        <v-text-field
            v-if="editedForm"
            :disabled="!isEdited"
            :label="studentModule.commentEmployee"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].commentEmployee"
        />
        <v-btn style="margin: 1%" @click="downloadPdf(studentModule.path, studentModule.title)">{{ $t('studentAffairsOfficeView.downloadDescription') }}</v-btn>
        <v-text-field
            v-if="editedForm"
            :label="$t('studentAffairsOfficeView.formalReject')"
            v-model="editedForm.edited.moduleFormsData[i].modulesStudent[j].reason"/>
        <v-row style="margin: 1%">
          <v-btn
              @click="this.editedForm.edited.moduleFormsData[i].modulesStudent[j].approval = 'formally rejected'"
              :disabled="!isEdited"
              class="button-top"
              prepend-icon="mdi-hand-back-left"
              variant="flat"
              color="red"
          >{{ $t('studentAffairsOfficeView.formallyReject') }}</v-btn>
          <v-btn
              @click="this.editedForm.edited.moduleFormsData[i].modulesStudent[j].approval = 'edited'"
              v-if="editedForm && editedForm.edited.moduleFormsData[i].modulesStudent[j].approval === 'formally rejected'"
              class="button-top"
              prepend-icon="mdi-keyboard-backspace"
              variant="flat"
              color="yellow"
          >{{ $t('studentAffairsOfficeView.undoRejection') }}</v-btn>
        </v-row>
      </div>
      <v-card-text>
        <u>{{ $t('studentAffairsOfficeView.creditFor') }}:</u>
        <br>
        <div v-for="(module, k) in moduleData.modules2bCredited" v-bind:key="module">
          <v-autocomplete
              v-if="editedForm"
              class="text-field"
              :items="this.majorModules"
              item-title="name"
              item-value="id"
              :label="$t('studentAffairsOfficeView.findModule')(applicationVersion.moduleFormsData[i].modules2bCredited[k])"
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
        {{ $t('studentAffairsOfficeView.sendToExaminationCommittee') }}
      </v-btn>
      <v-btn
          color="green"
          variant="flat"
          class="button-bottom"
          prepend-icon="mdi-content-save"
          :loading="loadingSaveButton"
          @click="saveEditedForm(false)"
      >
        {{ $t('studentAffairsOfficeView.save') }}
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
        await StudentAffairsOfficeService.formallyRejectForm(this.editedForm);
        this.$emit("close");
      } catch (error) {
        console.error(error.message);
      }
    },

    async saveEditedForm(readyForApproval) {
      try {
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
