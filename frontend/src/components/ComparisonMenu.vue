<template>
  <div>
    <v-btn @click="loadItems">{{$t("studentAffairsOfficeView.search")}}</v-btn>
    <v-row>
      <v-text-field :label="$t('studentAffairsOfficeView.courseOfStudy')" v-model="courseOfStudy"/>
      <v-text-field :label="$t('studentAffairsOfficeView.applicationID')" v-model="id"/>
      <v-text-field :label="$t('studentAffairsOfficeView.previousUniversity')" v-model="previousUniversity"/>
      <v-text-field :label="$t('studentAffairsOfficeView.previousModule')" v-model="previousModule"/>
      <v-text-field :label="$t('studentAffairsOfficeView.dateOfSubmission')" v-model="dateOfSubmission" type="date"/>
    </v-row>

    <v-data-table-server
        v-model:items-per-page="itemsPerPage"
        :items-per-page-options="itemsPerPageOptions"
        v-model:sort-by="sortBy"
        :headers="translatedHeaders"
        :items="formattedForms"
        :loading="loading"
        :items-length="totalItems"
        v-model:page="page"
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
      items: [],
      loading: false,
      itemsPerPage: 5,
      itemsPerPageOptions: [
        {value: 5, title: "5"},
        {value: 10, title: "10"},
        {value: 25, title: "25"},
        {value: -1, title: "$vuetify.dataFooter.itemsPerPageAll"}
      ],
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
      const queryParams = {
        uniMajor: this.courseOfStudy,
        applicationID: this.id,
        universityName: this.previousUniversity,
        module: this.previousModule,
        dateOfSubmission: this.formatDateForQueryString(this.dateOfSubmission.toString()),
        pageNumber: this.page - 1,
        pageSize: this.itemsPerPage
      };

      // Check if sortBy exists and append to queryParams
      if (this.sortBy.length) {
        queryParams.sortBy = this.sortBy[0].key;
        queryParams.sortDirection = this.sortBy[0].order.toUpperCase();
      }

      // Construct query string
      return Object.entries(queryParams)
          .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
          .join("&");
    },

    formatDateForQueryString(dateString) {
      const [year, month, day] = dateString.split("-");
      return `${day}.${month}.${year}`;
    },

    formatDateForTable(inputDate) {
      const date = new Date(inputDate);
      const options = {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      };
      return date.toLocaleDateString(this.$i18n.locale, options);
    },

    async loadItems() {
      this.loading = true;
      try {
        const response = await StudentAffairsOfficeService.searchApplication(this.buildQueryString());
        this.items = response.content;
        this.totalItems = response.totalItems;
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
  },

  computed: {
    // Computed property for translated headers
    translatedHeaders() {
      return [
        { title: this.$t("studentAffairsOfficeView.ID"), key: "applicationID" },
        { title: this.$t("studentAffairsOfficeView.university"), key: "universityName" },
        { title: this.$t("studentAffairsOfficeView.dateOfSubmission"), key: "dateOfSubmission" },
        { title: this.$t("studentAffairsOfficeView.dateLastEdited"), key: "dateLastEdited" },
        { title: this.$t("studentAffairsOfficeView.status"), key: "status" },
        { title: this.$t("studentAffairsOfficeView.view"), value: "actions", sortable: false }
      ];
    },
    formattedForms() {
      return this.items.map(form => ({
        ...form,
        dateOfSubmission: this.formatDateForTable(form.dateOfSubmission),
        dateLastEdited: this.formatDateForTable(form.dateLastEdited)
      }));
    }
  },
}
</script>

<style scoped>
.v-text-field {
  margin: 1%;
}
</style>
