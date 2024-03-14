<template>
  <div>
    <v-btn @click="loadItems">Suchen</v-btn>
    <v-row>
      <v-text-field label="Studiengang" v-model="courseOfStudy"></v-text-field>
      <v-text-field label="Antragsnummer" v-model="id"></v-text-field>
      <v-text-field label="Vorherige Universität" v-model="previousUniversity"></v-text-field>
      <v-text-field label="Vorheriges Modul" v-model="previousModule"></v-text-field>
      <v-text-field label="Antragsdatum" v-model="dateOfSubmission" type="date"></v-text-field>
    </v-row>

    <v-data-table-server
        v-model:items-per-page="itemsPerPage"
        v-model:sort-by="sortBy"
        :headers="headers"
        :items="items"
        :loading="loading"
        :items-length="totalItems"
        :page="page"
        @update:options="loadItems"
    >
      <template v-slot:[`item.actions`]="{ item }">
        <v-btn
            @click="viewApplication(item)"
            icon="mdi-eye-circle"
        ></v-btn>
      </template>
    </v-data-table-server>
  </div>
</template>

<script>
import axios from "@/plugins/axios";

export default {
  data() {
    return {
      headers: [
        { title: "Antragsnummer", key: "applicationID" },
        { title: "Antragsdatum", key: "dateOfSubmission" },
        { title: "Vorherige Universität", key: "university" },
        { title: "Status", key: "status" },
        { title: "Anschauen", value: "actions", sortable: false }
      ],

      items: [],
      loading: false,
      itemsPerPage: 5,
      totalItems: 0,
      page: 1,
      sortBy: [],
      courseOfStudy: "",
      id: "",
      previousUniversity: "",
      previousModule: "",
      dateOfSubmission: "",
      queryString: ""
    }
  },

  methods: {
    buildQueryString() {
      if(this.courseOfStudy) {
        if(this.queryString === "") {
          this.queryString = this.queryString.concat("uniMajor=" + this.courseOfStudy);
        } else {
          this.queryString = this.queryString.concat("&uniMajor=" + this.courseOfStudy);
        }
      }

      if(this.id) {
        if(this.queryString === "") {
          this.queryString = this.queryString.concat("applicationID=" + this.id);
        } else {
          this.queryString = this.queryString.concat("&applicationID=" + this.id);
        }
      }

      if(this.previousUniversity) {
        if(this.queryString === "") {
          this.queryString = this.queryString.concat("universityName=" + this.previousUniversity);
        } else {
          this.queryString = this.queryString.concat("&universityName=" + this.previousUniversity);
        }
      }

      if(this.previousModule) {
        if(this.queryString === "") {
          this.queryString = this.queryString.concat("module=" + this.previousModule);
        } else {
          this.queryString = this.queryString.concat("&module=" + this.previousModule);
        }
      }

      if(this.dateOfSubmission) {
        if(this.queryString === "") {
          this.queryString = this.queryString.concat("dateOfSubmission=" + this.dateOfSubmission);
        } else {
          this.queryString = this.queryString.concat("&dateOfSubmission=" + this.formatDate(this.dateOfSubmission.toString()));
        }
      }

      if(this.queryString === "") {
        this.queryString = this.queryString.concat("pageNumber=" + (this.page - 1));
      } else {
        this.queryString = this.queryString.concat("&pageNumber=" + (this.page - 1));
      }

      if(this.sortBy.length) {
        if(this.queryString === "") {
          if(this.sortBy[0].key === "university") {
            this.queryString = this.queryString.concat("sortBy=" + this.sortBy[0].key + "Name");
            this.queryString = this.queryString.concat("&sortDirection=" + this.retrieveSortOrder(this.sortBy[0].order));
          } else {
            this.queryString = this.queryString.concat("sortBy=" + this.sortBy[0].key);
            this.queryString = this.queryString.concat("&sortDirection=" + this.retrieveSortOrder(this.sortBy[0].order));
          }
        } else {
          this.queryString = this.queryString.concat("&sortBy=" + this.sortBy[0].key);
          this.queryString = this.queryString.concat("&sortDirection=" + this.retrieveSortOrder(this.sortBy[0].order));
        }
      }
    },

    retrieveSortOrder(isDescending) {
      if (isDescending) {
        return "DESC";
      } else {
        return "ASC";
      }
    },

    formatDate(dateString) {
      let parts = dateString.split("-");
      return parts[2] + '.' + parts[1] + '.' + parts[0];
    },

    async loadItems() {
      this.loading = true;
      this.buildQueryString()
      await axios.get("/api/application/searchApplication?" + this.queryString)
          .then(response => {
            this.items = response.data.content;
            this.totalItems = response.data.totalElements;
          })
          .catch(err => console.error("Error retrieving filtered/sorted applications: ", err));

      this.queryString = "";
      this.loading = false;
    },

    async viewApplication(item) {
      let form;
      await axios.get("/api/application/getApplication?" + item.applicationID)
          .then(response => form = response.data)
          .catch(err => console.error("Error retrieving form: ", err));
      this.$emit("open-view-application", form);
    }
  }
}
</script>

<style scoped>
.v-text-field {
  margin: 1%;
}
</style>