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
        forms: [],
        //TODO i18n
        headers: [
          { title: "ID", key: "applicationID" },
          { title: "Universität" , key: "university" },
          { title: "Antragsdatum", key: "dateOfSubmission" },
          { title: "Letzte Bearbeitung", key: "dateLastEdited"},
          { title: "Status", key: "status" },
          { title: "Editieren", value: "actions", sortable: false }
        ]
      }
    },

  async created() {
    await this.getForms();
    for(let i = 0; i < this.forms.length; i++) {
      this.forms[i].dateOfSubmission = this.formatDate(this.forms[i].dateOfSubmission);
      this.forms[i].dateLastEdited = this.formatDate(this.forms[i].dateLastEdited);
    }
  },

  methods: {
    async openEditMenu(item) {
      let form = {};
      await axios.get("/api/application/getApplication?applicationID=" + item.applicationID)
          .then(response => form = response.data)
          .catch(err => console.error("Error retrieving form: ", err));
      this.$emit('open-edit-menu', form);
    },

    async getForms() {
      await axios.get("/api/application/overviewOffice")
          .then(response => this.forms = response.data.content)
          .catch(err => console.error("Error retrieving open forms: ", err));
    },

    formatDate(inputDate) {
      // Parse the input date string
      const date = new Date(inputDate);

      // Extract day, month, and year
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0'); // January is 0
      const year = date.getFullYear();

      return `${day}-${month}-${year}`;
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