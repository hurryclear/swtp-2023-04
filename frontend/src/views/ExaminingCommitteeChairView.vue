<template>
  <v-container fluid class="wrapper">
    <v-row>
      <v-col>
        <component
            :is="currentComponent"
            :form="comparisonForm"
            @open="openComponent"
            @close="closeComponent"
        />
      </v-col>
      <v-col>
        <EditMenuCommittee
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
import EditMenuCommittee from "@/components/EditMenuCommittee.vue";
import FormDisplayCommittee from "@/components/FormDisplayCommittee.vue";
import ComparisonMenuCommittee from "@/components/ComparisonMenuCommittee.vue";
import ViewApplication from "@/components/ViewApplication.vue";

export default {
  components: {ComparisonMenuCommittee, EditMenuCommittee, FormDisplayCommittee, ViewApplication},
  data() {
    return {
      currentComponent: 'FormDisplayCommittee',
      comparisonForm: {},
      currentForm: {},
    }
  },
  methods: {
    openComponent({component, form}) {
      if (component !== 'EditMenuCommittee') {
        this.currentComponent = component;
        this.comparisonForm = form;
      } else {
        this.currentForm = form;
      }
    },
    closeComponent() {
      this.currentComponent = 'FormDisplayCommittee';
      this.comparisonForm = {};
    },
  }
}
</script>