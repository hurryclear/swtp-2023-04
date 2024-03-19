<template>
  <v-container fluid class="wrapper">
    <v-row>
      <v-col cols="6">
        <!-- Dynamic component rendering based on currentComponent -->
        <component
            :is="currentComponent"
            :form="comparisonForm"
            @open="openComponent"
            @close="closeComponent(false)"
            ref="childComponent"
        />
      </v-col>
      <v-col cols="6">
        <!-- EditMenu component -->
        <EditMenu
            class="edit-menu"
            v-if="Object.keys(currentForm).length !== 0"
            :form="currentForm"
            @close="closeComponent(true)"
            @open="openComponent"
            @save="save"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import EditMenu from "@/components/EditMenu.vue";
import FormDisplay from "@/components/FormDisplay.vue";
import ComparisonMenu from "@/components/ComparisonMenu.vue";
import ViewApplication from "@/components/ViewApplication.vue";
import SplitComponent from "@/components/SplitComponent.vue";
import MergeComponent from "@/components/MergeComponent.vue";

export default {
  components: { ComparisonMenu, EditMenu, FormDisplay, ViewApplication, SplitComponent, MergeComponent }, // Add SplitComponent and MergeComponent
  data() {
    return {
      currentComponent: 'FormDisplay', // Default component to display
      comparisonForm: {}, // Form data for comparison
      currentForm: {}, // Current form data for editing
    }
  },
  methods: {
    /**
     * Opens a specific component with associated form data.
     * @param {object} options - Object containing component and form data.
     * @param {string} options.component - Name of the component to open.
     * @param {object} options.form - Form data to pass to the component.
     */
    openComponent({component, form}) {
      if (component !== 'EditMenu') {
        this.currentComponent = component;
        this.comparisonForm = form;
      } else {
        this.currentComponent = 'FormDisplay'
        this.currentForm = form;
      }
    },
    /**
     * Closes the currently open component and resets form data.
     */
    closeComponent(isEditMenu) {
      // Reset the current form data and switch to the default component
      if (isEditMenu) {
        this.currentForm = {};
      } else {
        this.comparisonForm = {};
      }
      this.currentComponent = 'FormDisplay';
    },
    save(){
      this.currentForm = {};
      this.$refs.childComponent.updateForm();
    }
  }
}
</script>
