<template>
  <div>
    <!-- Expansion panels for displaying ModuleForms -->
    <v-expansion-panels>
      <!-- Use v-for to iterate through moduleForms array and display ModuleForm components -->
      <ModuleForm
          v-for="(form, index) in moduleForms"
          :key="index"
          @formFilled="handleFormFilled"
          @removeModule="removeModuleForm(index)"
      />
      <!-- Button to add a new ModuleForm -->
      <v-btn
          class="userInput"
          @click="addModuleForm"
          :disabled="!previousFormFilled"
      >
        {{ $t("moduleForm.addModule") }}
      </v-btn>
    </v-expansion-panels>
  </div>
</template>

<script>
import ModuleForm from "@/components/ModuleForm.vue";
import { defineComponent } from "vue";

export default defineComponent({
  components: { ModuleForm },
  data() {
    return {
      moduleForms: [ModuleForm], // Array to store instances of ModuleForm
      previousFormFilled: false,
    };
  },
  methods: {
    addModuleForm() {
      // Add a new instance of ModuleForm to the moduleForms array
      this.moduleForms.push(ModuleForm);
      this.previousFormFilled = false;
    },
    handleFormFilled() {
      // Enable the button when the current form is filled
      this.previousFormFilled = true;
    },
    removeModuleForm(index) {
      // Check if there is more than one module form before removing
      if (this.moduleForms.length > 1) {
        console.log('Deleting module at Index', index);
        this.moduleForms.splice(index, 1);
      } else {
        console.log('Not deleting the last module.');
      }
    },
  },
});
</script>

<style scoped>
/* Apply styles specifically to the scoped component */
.v-btn {
  width: 100%;
}
</style>
