<!-- ModuleFormList.vue -->
<template>
  <div>
    <v-expansion-panels>
      <ModuleForm
          v-for="(moduleMapping, index) in moduleMappings"
          :moduleMappingIndex="index"
          :key="moduleMapping.meta.key"
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

export default {
  components: {ModuleForm},
  computed: {
    moduleMappings() {
      return this.$store.getters.moduleMappings;
    },
    moduleMappingsFilled() {
      return this.$store.getters.moduleMappingsFilled;
    }
  },
  methods: {
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
