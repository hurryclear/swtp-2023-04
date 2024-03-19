<template>
  <v-card>
    <div class="card-header">
      <v-card-title>
        <u>{{ $t('studentAffairsOfficeView.application') }}</u>
      </v-card-title>
      <v-spacer/>
      <v-btn-toggle
          class="ma-2"
          v-model="showEdited"
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
      <!-- Button for SplitComponent-->
      <v-btn
          class="ma-2"
          icon="mdi-call-split"
          @click="this.$emit('open', { component: 'SplitComponent', form: this.formCopy })"
      />
      <!-- Button for MergeComponent-->
      <v-btn
          class="ma-2"
          icon="mdi-call-merge"
          @click="this.$emit('open', { component: 'MergeComponent', form: this.formCopy })"
      />
      <v-btn
          class="ma-2"
          icon="mdi-file-compare"
          @click="this.$emit('open',{component:'ComparisonMenu',formCopy:{}});"
      /><!--{{ $t('studentAffairsOfficeView.compareWithOtherApplications') }}
      </v-btn>-->
      <v-btn
          class="ma-2"
          icon="mdi-close"
          @click="this.$emit('close')"
      />
    </div>

    <v-divider/>
    <v-text-field
        class="text-field"
        :label="$t('studentAffairsOfficeView.reason')"
        v-model="formalRejectionReason"
        :disabled="!showEdited"
    />
    <v-btn
        @click="formallyReject"
        class="ma-2"
        color="red"
        prepend-icon="mdi-hand-back-left"
    >{{ $t('studentAffairsOfficeView.formallyRejectApplication') }}
    </v-btn>
    <v-divider/>

    <v-tabs v-if="formCopy" v-model="selectedTabIndex">
      <v-tab
          v-for="(moduleMapping,i) in (showEdited ? formCopy.edited.moduleFormsData : formCopy.original.moduleFormsData)"
          :key="i"
          :value="i"
      >
        {{ $t('studentAffairsOfficeView.mapping') }} {{ i + 1 }}
      </v-tab>
    </v-tabs>
    <div class="pa-2" v-if="formCopy">
      <v-window v-model="selectedTabIndex">
        <v-window-item
            v-for="(moduleMapping,i) in showEdited ? formCopy.edited.moduleFormsData : formCopy.original.moduleFormsData"
            :key="i"
            :value="i"
        >
          <v-card-text>

            <v-text-field
                disabled
                v-model="formCopy.original.applicationData.university"
                :label="$t('studentAffairsOfficeView.previousUniversity')"
                variant="outlined"
            />
            <v-text-field
                disabled
                v-model="formCopy.original.applicationData.oldCourseOfStudy"
                :label="$t('studentAffairsOfficeView.previousCourse')"
                variant="outlined"
            />
            <v-text-field
                disabled
                v-model="formCopy.original.applicationData.newCourseOfStudy"
                :label="$t('studentAffairsOfficeView.currentCourse')"
                variant="outlined"
            />
          </v-card-text>

          <!--Modules-->
          <v-card-title>
            {{ $t('studentAffairsOfficeView.modules') }}:
          </v-card-title>
          <div v-for="(module, j) in moduleMapping.modulesStudent" :key="module.frontend_key">
            <v-card-subtitle>
              {{ $t('studentAffairsOfficeView.module') }} {{ j + 1 }}
            </v-card-subtitle>
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('applicationFormView.moduleFormList.moduleMapping.moduleNameLabel')"
                v-model="module.title"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('applicationFormView.moduleFormList.moduleMapping.moduleId')"
                v-model="module.number"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('applicationFormView.universityForm.nameLabel')"
                v-model="module.university"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('applicationFormView.courseOfStudy.courseOfStudy')"
                v-model="module.major"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('applicationFormView.moduleFormList.moduleMapping.creditLabel')"
                v-model="module.credits"
            />
            <v-text-field
                disabled
                variant="outlined"
                :label="$t('studentAffairsOfficeView.studentComment')"
                v-model="module.commentStudent"
            >
            </v-text-field>
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('studentAffairsOfficeView.officeComment')"
                v-model="module.commentEmployee"
            />
            <v-btn class="ma-2" @click="downloadPdf(module.path, module.title)">
              {{ $t('studentAffairsOfficeView.downloadDescription') }}
            </v-btn>
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('studentAffairsOfficeView.formalReject')"
                v-model="module.reason"/>
            <v-row>
              <v-btn
                  @click="module.approval = 'formally rejected'"
                  :disabled="!showEdited"
                  class="ma-2"
                  prepend-icon="mdi-hand-back-left"
                  variant="elevated"
                  color="red"
              >{{ $t('studentAffairsOfficeView.formalReject') }}
              </v-btn>
              <v-btn
                  @click="module.approval = 'edited'"
                  v-if="formCopy && module.approval === 'formally rejected'"
                  class="ma-2"
                  prepend-icon="mdi-keyboard-backspace"
                  variant="elevated"
                  color="yellow"
              >{{ $t('studentAffairsOfficeView.undoRejection') }}
              </v-btn>
            </v-row>
            <v-divider/>
          </div>
          <v-card-text>
            <v-select
                multiple
                variant="outlined"
                class="text-field"
                :items="majorModules"
                item-title="name"
                item-value="id"
                :label="$t('studentAffairsOfficeView.creditFor')"
                :disabled="!showEdited"
                v-model="moduleMapping.modules2bCredited"
            />
          </v-card-text>
          <v-divider/>
        </v-window-item>
      </v-window>
      <v-divider/>
      <v-card-actions>
        <v-btn
            color="blue"
            variant="flat"
            class="ma-2"
            prepend-icon="mdi-arrow-u-left-top"
            :loading="loadingSendButton"
            @click="sendToExaminingCommitteeChair()"
        >
          {{ $t('studentAffairsOfficeView.sendToExaminationCommittee') }}
        </v-btn>
        <v-btn
            color="green"
            variant="flat"
            class="ma-2"
            prepend-icon="mdi-content-save"
            :loading="loadingSaveButton"
            @click="saveEditedForm()"
        >
          {{ $t('studentAffairsOfficeView.save') }}
        </v-btn>
      </v-card-actions>
    </div>
  </v-card>
</template>

<script>
import StudentAffairsOfficeService from "@/services/StudentAffairsOfficeService";

export default {
  props: {
    form: JSON
  },

  async created() {
    await this.getModules();
    this.formCopy = structuredClone(this.form);
  },

  data() {
    return {
      formalRejectionReason: '',
      showCommentWarning: false,
      loadingSaveButton: false,
      loadingSendButton: false,
      showEdited: false,
      selectedTabIndex: 0,
      formCopy: null
    }
  },

  methods: {
    async getModules() {
      try {
        this.$store.state.studentAffairsOffice.majorModules= await StudentAffairsOfficeService.getAllModules(this.form.original.applicationData.newCourseOfStudy);
      } catch (error) {
        console.error(error.message);
      }
    },

    async sendToExaminingCommitteeChair() {
      try {
        this.formCopy.edited.applicationData.dateLastEdited = new Date().toISOString();
        await StudentAffairsOfficeService.sendFormToApproval(this.formCopy);
        this.$emit("save");
      } catch (error) {
        console.error(error.message);
      }
    },

    async saveEditedForm() {
      try {
        this.formCopy.edited.applicationData.dateLastEdited = new Date().toISOString();
        await StudentAffairsOfficeService.saveEditedForm(this.formCopy);
        this.$emit("save");
      } catch (error) {
        console.error(error.message);
      }
    },

    async formallyReject() {
      try {
        if (this.formalRejectionReason === "") {
          this.showCommentWarning = true;
          return;
        }
        this.showCommentWarning = false;
        this.formCopy.edited.applicationData.formalReject = this.formalRejectionReason;
        await StudentAffairsOfficeService.formallyRejectForm(this.formCopy);
        this.$emit("close");
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
  },
  watch:{
    form: {
      handler(newVal){
        this.formCopy = structuredClone(newVal);
      },
      deep: true
    }
  },
  computed: {
    majorModules() {
      return this.$store.state.studentAffairsOffice.majorModules;
    },
  }
}
</script>

<style scoped>
.v-text-field {
  margin: 1%;
}

.v-row {
  margin: 1%;
}

.card-header {
  display: flex;
  align-items: center;
}

</style>
