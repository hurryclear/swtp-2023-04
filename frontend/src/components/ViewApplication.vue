<template>
  <div>
    <v-card>
      <div class="card-header">
        <v-card-title>
          <u>{{ $t('studentAffairsOfficeView.application') }}</u>
        </v-card-title>
        <v-spacer />
        <v-btn class="button-top" variant="tonal" icon="mdi-close" @click="this.$emit('close');" />
      </div>
      <v-divider />
      <v-tabs v-model="selectedTabIndex">
        <v-tab v-for="(moduleData, index) in copy.edited.moduleFormsData" :key="index">
          {{ $t('studentAffairsOfficeView.mapping') }} {{ index + 1 }}
        </v-tab>
      </v-tabs>
      <v-window v-model="selectedTabIndex">
        <v-window-item v-for="(moduleData, index) in copy.edited.moduleFormsData" :key="index">
          <v-card-text>
            <v-text-field
                disabled
                v-model="copy.edited.applicationData.university"
                :label="$t('studentAffairsOfficeView.previousUniversity')"
                outlined
            />
            <v-text-field
                disabled
                v-model="copy.edited.applicationData.oldCourseOfStudy"
                :label="$t('studentAffairsOfficeView.previousCourse')"
                outlined
            />
            <v-text-field
                disabled
                v-model="copy.edited.applicationData.newCourseOfStudy"
                :label="$t('studentAffairsOfficeView.currentCourse')"
                outlined
            />
          </v-card-text>
          <v-card-title>
            {{ $t('studentAffairsOfficeView.modules') }}:
          </v-card-title>
          <div v-for="(studentModule, index2) in moduleData.modulesStudent" :key="index2">
            <v-card-subtitle>
              {{ $t('studentAffairsOfficeView.module') }} {{ index2 + 1 }}
            </v-card-subtitle>
            <v-text-field
                disabled
                v-model="studentModule.title"
                :label="$t('studentAffairsOfficeView.name')"
                outlined
            />
            <v-text-field
                disabled
                v-model="studentModule.number"
                :label="$t('studentAffairsOfficeView.moduleNumber')"
                outlined
            />
            <v-text-field
                disabled
                v-model="studentModule.credits"
                :label="$t('studentAffairsOfficeView.credits')"
                outlined
            />
            <v-text-field
                disabled
                v-model="studentModule.commentStudent"
                :label="$t('studentAffairsOfficeView.studentComment')"
                outlined
            />
            <v-text-field
                disabled
                v-model="studentModule.commentEmployee"
                :label="$t('studentAffairsOfficeView.officeComment')"
                outlined
            />
            <v-btn style="margin: 1%" @click="downloadPdf(studentModule.path)">
              {{ $t('studentAffairsOfficeView.downloadDescription') }}
            </v-btn>
            <v-text-field
                disabled
                v-model="studentModule.reason"
                :label="$t('studentAffairsOfficeView.decision')"
                outlined
            />
            <v-divider />
          </div>
          <v-card-text>
            <v-select
                multiple
                v-model="moduleData.modules2bCredited"
                :items="majorModules"
                item-title="name"
                item-value="id"
                outlined
                :label="$t('studentAffairsOfficeView.creditFor')"
            />
          </v-card-text>
          <v-divider />
        </v-window-item>
      </v-window>
      <v-divider />
      <v-card-text>
        {{ $t('studentAffairsOfficeView.decision') }}:
        <v-text-field
            disabled
            v-model="copy.edited.formalReject"
            :label="$t('studentAffairsOfficeView.decision')"
            outlined
        />
      </v-card-text>
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
      majorModules: [],
      selectedTabIndex: 0
    };
  },

  methods: {
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
    this.copy = structuredClone(this.form);
    await this.getModules();
  }
};
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
