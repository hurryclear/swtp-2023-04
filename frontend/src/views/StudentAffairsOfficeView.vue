<template>
  <v-container fluid class="wrapper">
    <v-row>
      <v-col>
        <!-- Dynamic component rendering based on currentComponent -->
        <component
            :is="currentComponent"
            :form="comparisonForm"
            @open="openComponent"
            @close="closeComponent"
        />
      </v-col>
      <v-col>
        <!-- EditMenu component -->
        <EditMenu
            class="edit-menu"
            v-if="Object.keys(currentForm).length !== 0"
            :form="currentForm"
            @close="currentForm={}"
            @open="openComponent"
            @save="currentForm={}"
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

export default {
  components: { ComparisonMenu, EditMenu, FormDisplay, ViewApplication },
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
        this.currentForm = form;
      }
    },
    /**
     * Closes the currently open component and resets form data.
     */
    closeComponent() {
      this.currentComponent = 'FormDisplay';
      this.comparisonForm = {};
    },
  }
}
</script>
