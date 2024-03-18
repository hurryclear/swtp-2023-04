<!-- ModuleFormList.vue -->
<template>
  <div>
    <v-expansion-panels>
      <ModuleForm
          v-for="(moduleMapping, index) in moduleMappings"
          :moduleMappingIndex="index"
          :key="moduleMapping.meta.key"
          :moduleKey="moduleMapping.meta.key"
      />
      <v-btn
          class="userInput"
          @click="addModuleForm"
          :disabled="!this.moduleMappingsFilled"
      >
        {{ $t("applicationFormView.moduleFormList.addMapping") }}
      </v-btn>
    </v-expansion-panels>
  </div>
</template>

<script>
import ModuleForm from "@/components/ModuleForm.vue";

/**
 * Vue component representing a list of module forms.
 * This component manages the display and addition of module forms.
 * @component ModuleFormList
 */
export default {
  // Components
  components: { ModuleForm },

  // Computed properties
  computed: {
    /**
     * Retrieves the list of module mappings from the store.
     * @returns {Array} List of module mappings.
     */
    moduleMappings() {
      return this.$store.getters.moduleMappings;
    },

    /**
     * Checks if all module mappings are filled.
     * @returns {boolean} Whether all module mappings are filled or not.
     */
    moduleMappingsFilled() {
      return this.$store.getters.moduleMappingsFilled;
    }
  },

  // Methods
  methods: {
    /**
     * Adds a new module form.
     */
    addModuleForm() {
      const key = this.moduleMappings[this.moduleMappings.length - 1].meta.key + 1
      this.$store.commit('addModuleMappingForm', key);
    }
  },
};

</script>

<style scoped>
.v-btn {
  width: 100%;
}
</style>
