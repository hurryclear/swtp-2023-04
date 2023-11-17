<script>
import moduleJSON from '../assets/module_liste.json';

export default {
  computed:{
    moduleNameLabel(){return this.$t("moduleForm.moduleNameLabel")},
    moduleDescriptionLabel(){return this.$t("moduleForm.moduleDescriptionLabel")},
    moduleCreditedLabel(){return this.$t("moduleForm.moduleCreditedLabel")},
    commentLabel(){return this.$t("moduleForm.commentLabel")}

  },
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
    // Call the method to extract module names from the JSON file on component mount
    this.extractModuleNames();
  },
  methods: {
    onFormFilled() {
      // Emit an event when the form is filled
      this.$emit('formFilled');
    },
    extractModuleNames() {
      // Extract module names from the imported JSON data
      this.moduleNamesList = moduleJSON.courses[0].modules.map(module => module.name);
    },
    checkFormFilled() {
      // Check conditions to determine if all required fields are filled
      const isFilled =
          this.module.name.trim() !== '' &&
          this.module.description !== null &&
          this.module.module2bCredited !== null;

      if (isFilled) {
        // Call onFormFilled when all required fields are filled
        this.onFormFilled();
      }
    },
    removeModule() {
      // Emit an event to remove the module
      this.$emit('removeModule');
    }
  },
  watch: {
    'module.name': 'checkFormFilled', // Watch for changes in module.name
    'module.description': 'checkFormFilled', // Watch for changes in module.description
    'module.module2bCredited': 'checkFormFilled', // Watch for changes in module.module2bCredited
  },
}
</script>

<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <!-- Display the module name in the title -->
      <template v-slot:default="{ expanded }">
        <v-row no-gutters>
          <v-col>
            {{ $t("moduleForm.module") }}
          </v-col>
          <v-col class="text-grey">
            <!-- Use fade transition to show/hide content based on expansion state -->
            <v-fade-transition leave-absolute>
              <span v-if="expanded" key="0">
                {{$t("moduleForm.moduleDescription")}}
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
      <!-- Input fields for module details -->
      <v-text-field
          class="userInput"
          v-model="module.name"
          hide-details
          :label="moduleNameLabel"
          variant="outlined"
      />
      <v-file-input
          v-model="module.description"
          class="userInput"
          accept=".pdf"
          :label="moduleDescriptionLabel"
          variant="outlined"
          hide-details
          prepend-icon=""
      />
      <v-select
          v-model="module.module2bCredited"
          class="userInput"
          :label="moduleCreditedLabel"
          variant="outlined"
          hide-details
          :items="moduleNamesList"
      />
      <v-text-field
          v-model="module.comment"
          class="userInput"
          hide-details
          :label="commentLabel"
          variant="outlined"
      />
      <!-- Button to remove the module -->
      <v-btn @click="removeModule">
        {{ $t("moduleForm.removeModule") }}
      </v-btn>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<style scoped>
/* Add scoped styles if needed */
</style>
