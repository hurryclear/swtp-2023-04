<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <template v-slot:default="{ expanded }">
        <v-row no-gutters>
          <v-col>
            {{ $t("moduleForm.module") }}
          </v-col>
          <v-col class="text-grey">
            <v-fade-transition leave-absolute>
              <span v-if="expanded" key="0">
                {{ $t("moduleForm.moduleDescription") }}
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
          :label="$t('moduleForm.moduleNameLabel')"
          variant="outlined"
      />
      <v-file-input
          v-model="formData.description"
          class="userInput"
          accept=".pdf"
          show-size
          :label="$t('moduleForm.moduleDescriptionLabel')"
          variant="outlined"
          hide-details
          prepend-icon=""
      />
      <v-select
          v-model="formData.module2bCredited"
          class="userInput"
          :label="$t('moduleForm.moduleCreditedLabel')"
          variant="outlined"
          hide-details
          :items="moduleNamesList"
      />
      <v-text-field
          v-model="formData.comment"
          class="userInput"
          hide-details
          :label="$t('moduleForm.commentLabel')"
          variant="outlined"
      />
      <v-btn
          @click="removeModule"
          variant="outlined"
      >
        {{ $t("moduleForm.removeModule") }}
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
    // Watch changes in module data and emit an event to the parent
    watchModuleData() {
      this.checkFormFilled();
      this.$emit('updateModuleData', this.formData);
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
