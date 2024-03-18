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
      <v-btn
          class="ma-2"
          @click="this.$emit('open',{component:'ComparisonMenu',formCopy:{}});"
      >{{ $t('studentAffairsOfficeView.compareWithOtherApplications') }}
      </v-btn>
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
        :disabled="!formalRejectionReason"
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
            <u>{{ $t('studentAffairsOfficeView.previousUniversity') }}:</u> {{
              formCopy.original.applicationData.university
            }}
            <br/>
            <u>{{ $t('studentAffairsOfficeView.previousCourse') }}:</u> {{
              formCopy.original.applicationData.oldCourseOfStudy
            }}
            <br/>
            <u>{{ $t('studentAffairsOfficeView.currentCourse') }}:</u> {{
              formCopy.original.applicationData.newCourseOfStudy
            }}
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
                :label="$t('studentAffairsOfficeView.name')"
                v-model="module.title"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('studentAffairsOfficeView.moduleNumber')"
                v-model="module.number"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('studentAffairsOfficeView.credits')"
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
            @click="sendToExaminingCommitteeChair(true)"
        >
          {{ $t('studentAffairsOfficeView.sendToExaminationCommittee') }}
        </v-btn>
        <v-btn
            color="green"
            variant="flat"
            class="ma-2"
            prepend-icon="mdi-content-save"
            :loading="loadingSaveButton"
            @click="saveEditedForm(false)"
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
      majorModules: [],
      selectedTabIndex: 0,
      formCopy: null
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
        await StudentAffairsOfficeService.sendFormToApproval(this.formCopy);
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

    async saveEditedForm(readyForApproval) {
      try {
        this.formCopy.edited.applicationData.dateLastEdited = new Date().toISOString();
        await StudentAffairsOfficeService.saveEditedForm(this.formCopy);
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

    /**
     * Merges two module mappings by combining their module data and credited module IDs.
     * @param {number} moduleMappingIndex1 - The index of the first module mapping.
     * @param {number} moduleMappingIndex2 - The index of the second module mapping.
     */
    mergeModuleMappings(moduleMappingIndex1, moduleMappingIndex2) {
      const mapping1 = this.formCopy.edited.moduleFormsData[moduleMappingIndex1];
      const mapping2 = this.formCopy.edited.moduleFormsData[moduleMappingIndex2];

      // Create a set to track unique module numbers
      const uniqueNumbers = new Set();

      // Filter out duplicates from mapping2
      const uniqueModules2 = mapping2.modulesStudent.filter(module => {
        if (uniqueNumbers.has(module.number)) {
          return false; // duplicate, exclude
        }
        uniqueNumbers.add(module.number);
        return true; // not a duplicate, include
      });

      // Append unique modules from mapping2 to mapping1
      mapping1.modulesStudent.push(...uniqueModules2);

      // Merge modules2bCredited arrays
      mapping1.modules2bCredited.push(...mapping2.modules2bCredited);

      // Remove mapping2
      this.formCopy.edited.moduleFormsData.splice(moduleMappingIndex2, 1);
    },

    /**
     * Splits a module mapping by moving selected modules and credited IDs to a new mapping.
     * @param {number} moduleMappingIndex - The index of the module mapping to split.
     * @param {number[]} moduleIndices - Indices of modules to move to the new mapping.
     * @param {number[]} modules2BCIds - IDs of credited modules to move to the new mapping.
     */
    splitModuleMapping(moduleMappingIndex, moduleIndices, modules2BCIds) {
      const originalModuleMapping = this.formCopy.edited.moduleFormsData[moduleMappingIndex];
      const newModuleMapping = {
        frontend_key: originalModuleMapping.frontend_key + 1,
        backend_block_id: originalModuleMapping.backend_block_id,
        modulesStudent: [],
        modules2bCredited: []
      };

      // Iterate over moduleIndices and add modules to the new ModuleMapping
      moduleIndices.forEach(moduleIndex => {
        const moduleToAdd = originalModuleMapping.modulesStudent[moduleIndex];
        // Check if moduleToAdd is not a duplicate in newModuleMapping
        if (!newModuleMapping.modulesStudent.some(module => module.number === moduleToAdd.number)) {
          newModuleMapping.modulesStudent.push(moduleToAdd);
        }
      });

      // Iterate over modules2BCIds and add IDs to the new ModuleMapping
      modules2BCIds.forEach(id => {
        // Check if id is not a duplicate in newModuleMapping
        if (!newModuleMapping.modules2bCredited.includes(id)) {
          newModuleMapping.modules2bCredited.push(id);
        }
      });

      // Remove the modules and IDs from the original ModuleMapping
      moduleIndices.sort((a, b) => b - a); // Sort moduleIndices in descending order
      moduleIndices.forEach(moduleIndex => {
        originalModuleMapping.modulesStudent.splice(moduleIndex, 1);
      });

      modules2BCIds.forEach(id => {
        const index = originalModuleMapping.modules2bCredited.indexOf(id);
        if (index !== -1) {
          originalModuleMapping.modules2bCredited.splice(index, 1);
        }
      });

      // Add the new ModuleMapping to the array
      this.formCopy.edited.moduleFormsData.splice(moduleMappingIndex + 1, 0, newModuleMapping);
    },



  }
}
</script>

<style scoped>
.v-text-field {
  margin: 1%;
}

.card-header {
  display: flex;
  align-items: center;
}

</style>
