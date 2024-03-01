<template>
    <v-container fluid class="wrapper">
      <v-row>
        <LogoutButton style="margin-left: 80%"/>
      </v-row>
      <v-row>
        <v-col class="col-left">
          <FormDisplay class="form-display" v-if="FDisDisplayed" @open-edit-menu="openEditMenu"/>
          <ComparisonMenu class="comparison" v-if="CMisDisplayed" @close-comparison="closeComparisonMenu"/>
        </v-col>
        <v-col>
          <EditMenu class="edit-menu" v-if="EMisDisplayed" :form="EMformContent" @close-edit-menu="closeEditMenu" @open-comparison="openComparison"/>
        </v-col>
      </v-row>
    </v-container>
</template>

<script>
  import EditMenu from "@/components/EditMenu.vue";
  import FormDisplay from "@/components/FormDisplay.vue";
  import LogoutButton from "@/components/LogoutButton.vue";
  import ComparisonMenu from "@/components/ComparisonMenu.vue";
  export default {
    components: {ComparisonMenu, EditMenu, FormDisplay, LogoutButton},
    data() {
      return {
        EMisDisplayed: false,
        FDisDisplayed: true,
        CMisDisplayed: false,
        EMformContent: {}
      }
    },
    methods: {
      openEditMenu(form){
        this.EMisDisplayed = true;
        this.EMformContent = form;
      },
      closeEditMenu() {
        this.EMisDisplayed = false;
        this.EMformContent = {};
      },
      openComparison() {
        this.FDisDisplayed = false;
        this.CMisDisplayed = true;
      },
      closeComparisonMenu() {
        this.CMisDisplayed = false;
        this.FDisDisplayed = true;
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