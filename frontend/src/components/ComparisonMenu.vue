<template>
  <div>
    <v-btn @click="loadItems">Suchen</v-btn>
    <v-row>
      <v-text-field label="Studiengang" v-model="courseOfStudy"/>
      <v-text-field label="Antragsnummer" v-model="id"/>
      <v-text-field label="Vorherige Universität" v-model="previousUniversity"/>
      <v-text-field label="Vorheriges Modul" v-model="previousModule"/>
      <v-text-field label="Antragsdatum" v-model="dateOfSubmission" type="date"/>
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
import StudentAffairsOfficeService from "@/services/StudentAffairsOfficeService";

export default {
  data() {
    return {
      headers: [
        { title: "Antragsnummer", key: "applicationID" },
        { title: "Vorherige Universität", key: "universityName" },
        { title: "Antragsdatum", key: "dateOfSubmission" },
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
      let queryString = ""
      const appendQueryParam = (param, value) => {
        queryString += (queryString === "" ? "" : "&") + param + "=" + value;
      };

      if (this.courseOfStudy) appendQueryParam("uniMajor", this.courseOfStudy);
      if (this.id) appendQueryParam("applicationID", this.id);
      if (this.previousUniversity) appendQueryParam("universityName", this.previousUniversity);
      if (this.previousModule) appendQueryParam("module", this.previousModule);
      if (this.dateOfSubmission) appendQueryParam("dateOfSubmission", this.formatDate(this.dateOfSubmission.toString()));

      appendQueryParam("pageNumber", this.page - 1);

      if (this.sortBy.length) {
        appendQueryParam("sortBy", this.sortBy[0].key);
        appendQueryParam("sortDirection", this.sortBy[0].order? "DESC" : "ASC");
      }
      return queryString
    },

    formatDate(dateString) {
      const [year, month, day] = dateString.split("-");
      return `${day}.${month}.${year}`;
    },

    async loadItems() {
      this.loading = true;
      try {
        const response = await StudentAffairsOfficeService.searchApplication(this.buildQueryString());
        this.items = response.content;
        this.totalItems = response.totalElements;
      } catch (error) {
        console.error("Error retrieving filtered/sorted applications: ", error);
      }
      this.queryString = "";
      this.loading = false;
    },

    async viewApplication(item) {
      try {
        const form = await StudentAffairsOfficeService.getApplication(item.applicationID);
        this.$emit("open", {component: 'ViewApplication', form});
      } catch (error) {
        console.error("Error retrieving form: ", error);
      }
    }
  }
}
</script>

<style scoped>
.v-text-field {
  margin: 1%;
}
</style>