<script>
import moduleJSON  from '../assets/module_liste.json'
export default {
  data: () => ({
    formIsEmpty: "true",
    moduleNamesList: [],
    module: {
      name: '',
      comment: '',
      description: null,
      module2bCredited: null
    }
  }),
  mounted() {
    this.extractModuleNames();
  },
  methods: {
    onFormFilled() {
      // Emit an event when the form is filled
      this.$emit('formFilled');
    },
    extractModuleNames() {
      this.moduleNamesList = moduleJSON.courses[0].modules.map(module => module.name);
    },
    checkFormFilled() {
      // Check conditions to determine if all required fields are filled
      const isFilled =
          this.module.name.trim() !== '' &&
          this.module.description !== null &&
          this.module.module2bCredited !== null;

      if (isFilled) {
        this.onFormFilled(); // Call onFormFilled when all required fields are filled
      }
    },
    removeModule() {
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
      <template v-slot:default="{ expanded }">
        <v-row no-gutters>
          <v-col>
            Modul
          </v-col>
          <v-col
              class="text-grey"
          >
            <v-fade-transition leave-absolute>
                <span
                    v-if="expanded"
                    key="0"
                >
                  Geben Sie Angaben über das anzurechnende Modul an!
                </span>
              <span
                  v-else
                  key="1"
              >
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
          label="Modulname"
          variant="outlined"
      />
      <v-file-input
          v-model="module.description"
          class="userInput"
          accept=".pdf"
          label="Modulbeschreibung"
          variant="outlined"
          hide-details
          prepend-icon=""
      />
      <v-select
          v-model="module.module2bCredited"
          class="userInput"
          label="Anzurechnendes Modul"
          variant="outlined"
          hide-details
          :items=moduleNamesList
      />
      <v-text-field
          v-model="module.comment"
          class="userInput"
          hide-details
          label="Bemerkung"
          variant="outlined"
      />
      <v-btn @click="removeModule">
        Entferne Modul
      </v-btn>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<style scoped>
</style>