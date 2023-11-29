<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <template v-slot:default="{ expanded }">
        <v-row no-gutters>
          <v-col>
            {{ $t("applicationForm.module") }}
          </v-col>
          <v-col class="text-grey">
            <v-fade-transition leave-absolute>
              <span v-if="expanded" key="0">
                {{ $t("applicationForm.moduleDescription") }}
              </span>
              <span v-else key="1">
                {{ module.name }}
              </span>
            </v-fade-transition>
          </v-col>
        </v-row>
      </template>
    </v-expansion-panel-title>
    <v-expansion-panel-text>
      <v-text-field
          class="userInput"
          v-model="formData.name"
          hide-details
          :label="$t('applicationForm.moduleNameLabel')"
          variant="outlined"
      />
      <v-file-input
          v-model="formData.description"
          class="userInput"
          accept=".pdf"
          show-size
          :label="$t('applicationForm.moduleDescriptionLabel')"
          variant="outlined"
          hide-details
          prepend-icon=""
          @change="handleFileChange"
      />
      <v-select
          v-model="formData.module2bCredited"
          class="userInput"
          :label="$t('applicationForm.moduleCreditedLabel')"
          variant="outlined"
          hide-details
          multiple
          :items="moduleNamesList"
      />
      <v-text-field
          v-model="formData.comment"
          class="userInput"
          hide-details
          :label="$t('applicationForm.commentLabel')"
          variant="outlined"
      />
      <v-btn
          @click="removeModule"
          variant="outlined"
      >
        {{ $t("applicationForm.removeModule") }}
      </v-btn>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<script>
import moduleJSON from '../assets/module_liste.json';

export default {
  props: {
    module: Object, // Add a prop to receive module data
  },
  data() {
    return {
      isFilled: false,
      moduleNamesList: [],
      formData: {...this.module},
      selectedFile: null,
    };
  },
  mounted() {
    this.extractModuleNames();
  },
  methods: {
    extractModuleNames() {
      this.moduleNamesList = moduleJSON.courses[0].modules.map(module => module.name);
    },
    checkFormFilled() {
      this.formData.isFilled =
          this.formData.name.trim()!== '' &&
          this.formData.description!== null &&
          this.formData.module2bCredited!== null;
    },
    removeModule() {
      this.$emit('removeModule');
    },
    handleFileChange(event) {
      this.selectedFile = event.target.files[0];
    },
    // Watch changes in module data and emit an event to the parent
    watchModuleData() {
      this.checkFormFilled();
      this.$emit('updateModuleData', this.formData, this.selectedFile);
    },
  },
  watch: {
    'formData.name': 'watchModuleData',
    'formData.description': 'watchModuleData',
    'formData.module2bCredited': 'watchModuleData',
    'formData.comment': 'watchModuleData',
  },
};
</script>

<style scoped>
/* Add scoped styles if needed */
</style>
