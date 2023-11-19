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
          v-model="module.name"
          hide-details
          :label="$t('moduleForm.moduleNameLabel')"
          variant="outlined"
      />
      <v-file-input
          v-model="module.description"
          class="userInput"
          accept=".pdf"
          :label="$t('moduleForm.moduleDescriptionLabel')"
          variant="outlined"
          hide-details
          prepend-icon=""
      />
      <v-select
          v-model="module.module2bCredited"
          class="userInput"
          :label="$t('moduleForm.moduleCreditedLabel')"
          variant="outlined"
          hide-details
          :items="moduleNamesList"
      />
      <v-text-field
          v-model="module.comment"
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
  data() {
    return {
      formIsEmpty: true,
      moduleNamesList: [],
      module: {
        name: '',
        comment: '',
        description: null,
        module2bCredited: null
      }
    };
  },
  mounted() {
    this.extractModuleNames();
  },
  methods: {
    onFormFilled() {
      this.$emit('formFilled');
    },
    extractModuleNames() {
      this.moduleNamesList = moduleJSON.courses[0].modules.map(module => module.name);
    },
    checkFormFilled() {
      const isFilled =
          this.module.name.trim()!== '' &&
          this.module.description!== null &&
          this.module.module2bCredited!== null;

      if (isFilled) {
        this.onFormFilled();
      }
    },
    removeModule() {
      this.$emit('removeModule');
    }
  },
  watch: {
    'module.name': 'checkFormFilled',
    'module.description': 'checkFormFilled',
    'module.module2bCredited': 'checkFormFilled',
  },
};
</script>

<style scoped>
/* Add scoped styles if needed */
</style>
