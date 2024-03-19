<template>
  <v-container fluid class="wrapper">
    <v-row v-if="currentComponent === 'ViewApplication'">
      <v-col cols="12" md="6">
        <!-- Dynamic component rendering based on currentComponent -->
        <component
            :is="currentComponent"
            :form="comparisonForm"
            @open="openComponent"
            @close="closeComponent(false)"
        />
      </v-col>
      <v-col cols="12" md="6">
        <!-- EditMenuCommittee component -->
        <EditMenuCommittee
            class="edit-menu"
            v-if="Object.keys(currentForm).length !== 0"
            :form="currentForm"
            @close="closeComponent(true)"
            @open="openComponent"
            @save="currentForm={}"
        />
      </v-col>
    </v-row>
    <div v-else>
      <v-col>
        <!-- Dynamic component rendering based on currentComponent -->
        <component
            :is="currentComponent"
            :form="comparisonForm"
            @open="openComponent"
            @close="closeComponent(false)"
        />
      </v-col>
      <v-col>
        <!-- EditMenu component -->
        <EditMenuCommittee
            class="edit-menu"
            v-if="Object.keys(currentForm).length !== 0"
            :form="currentForm"
            @close="closeComponent(true)"
            @open="openComponent"
            @save="currentForm={}"
        />
      </v-col>
    </div>
  </v-container>
</template>

<script>
import EditMenuCommittee from "@/components/EditMenuCommittee.vue";
import FormDisplay from "@/components/FormDisplay.vue";
import ComparisonMenu from "@/components/ComparisonMenu.vue";
import ViewApplication from "@/components/ViewApplication.vue";
import SplitComponent from "@/components/SplitComponent.vue";
import MergeComponent from "@/components/MergeComponent.vue";

export default {
  components: { ComparisonMenu, EditMenuCommittee, FormDisplay, ViewApplication, SplitComponent, MergeComponent },
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
      if (component !== 'EditMenuCommittee') {
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
  }
}
</script>
