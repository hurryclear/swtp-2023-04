<template>
    <v-container fluid class="wrapper">
      <v-row>
        <LogoutButton style="margin-left: 80%"/>
      </v-row>
      <v-row>
        <v-col class="col-left">
          <FormDisplay
              class="form-display"
              v-if="FDisDisplayed"
              @open-edit-menu="openEditMenu"
          />
          <ComparisonMenu
              class="comparison"
              v-if="CMisDisplayed"
              @close-comparison="closeComparisonMenu"
              @open-view-application="openViewApplication"
          />
          <ViewApplication
              class="view-application"
              v-if="VAisDisplayed"
              :form="VAformContent"
              @close-view-application="closeViewApplication"
          />
        </v-col>
        <v-col>
          <EditMenu
              class="edit-menu"
              v-if="EMisDisplayed"
              :form="EMformContent"
              @close-edit-menu="closeEditMenu"
              @open-comparison="openComparison"
              @close-edit-menu-by-saving="closeEditMenuBySaving"
          />
        </v-col>
      </v-row>
    </v-container>
</template>

<script>
  import EditMenu from "@/components/EditMenu.vue";
  import FormDisplay from "@/components/FormDisplay.vue";
  import LogoutButton from "@/components/LogoutButton.vue";
  import ComparisonMenu from "@/components/ComparisonMenu.vue";
  import ViewApplication from "@/components/ViewApplication.vue";
  export default {
    components: {ComparisonMenu, EditMenu, FormDisplay, LogoutButton, ViewApplication},
    data() {
      return {
        VAisDisplayed: false,
        EMisDisplayed: false,
        FDisDisplayed: true,
        CMisDisplayed: false,
        EMformContent: {},
        VAformContent: {}
      }
    },
    methods: {
      openEditMenu(form) {
        this.FDisDisplayed = false;
        this.EMisDisplayed = true;
        this.EMformContent = form;
      },

      closeEditMenu() {
        this.EMisDisplayed = false;
        this.FDisDisplayed = true;
        this.EMformContent = {};
      },

      closeEditMenuBySaving() {
        this.EMisDisplayed = false;
        this.FDisDisplayed = true;
        this.EMformContent = {};
      },

      openComparison() {
        this.FDisDisplayed = false;
        this.CMisDisplayed = true;
      },

      closeComparisonMenu() {
        this.CMisDisplayed = false;
        this.FDisDisplayed = true;
      },

      openViewApplication(form) {
        this.VAisDisplayed = true;
        this.CMisDisplayed = false;
        this.VAformContent = form;
      },

      closeViewApplication() {
        this.VAisDisplayed = false;
        this.CMisDisplayed = true;
        this.VAformContent = {};

      }
    }
  }
</script>

<style scoped>
  .comparison {

  }

  .col-left {
    margin-left: 5%;
  }

  .edit-menu {

  }
</style>