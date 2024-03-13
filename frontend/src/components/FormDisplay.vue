<template>
  <h1>{{$t("studentAffairsOfficeView.openApplications")}}</h1>

  <v-card>
    <template v-slot:text>
      <v-text-field
          v-model="search"
          label="Search"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          hide-details
          single-line
      ></v-text-field>
    </template>
  <v-data-table :headers="headers" :items="forms" :search="search" item-key="id">
    <template v-slot:[`item.actions`]="{ item }">
      <v-btn
          @click="openEditMenu(item)"
          :disabled="item.edited.applicationData.status === 'editing in progress'"
          icon="mdi-pencil"
      ></v-btn>
    </template>
  </v-data-table>
  </v-card>
</template>

<script>
import axios from "@/plugins/axios";
export default {
    data() {
      return {
        search: "",
        forms: [] ,
        //TODO i18n
        headers: [
          { title: "ID", key: "edited.applicationData.applicationID" },
          { title: "Universität" , key: "edited.applicationData.university" },
          { title: "Anzahl von Mappings", key: "edited.moduleFormsData.length" },
          { title: "Vorheriger Studiengang", key: "edited.applicationData.oldCourseOfStudy" },
          { title: "Status", key: "edited.applicationData.status" },
          { title: "Edit", value: "actions", sortable: false }
        ]
      }
    },

  created() {
      this.getForms();
  },

  methods: {
      openEditMenu(form) {
        this.$emit('open-edit-menu', form);
      },

      async getForms() {
        //TODO richtigen endpoint einsetzen
        await axios.get("/api/application/overviewOffice?status=OPEN")
            .then(response => response.data = this.forms)
            .catch(err => console.error("Error retrieving open forms: ", err));
      }
    }
  }
</script>

<style scoped>

  td {
    padding: 1rem;
    width: 20%;
  }

</style>