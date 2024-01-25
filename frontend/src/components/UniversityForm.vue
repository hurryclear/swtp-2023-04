<template>
  <v-expansion-panels>
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("applicationForm.previousUniversity") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("applicationForm.universityDescription") }}
                </span>
                <span v-else key="1">
                  {{ university.name }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-combobox
            v-model="selectedUniversity"
            :items="universities"
            item-title="name"
            hide-details
            :label="$t('applicationForm.universityNameLabel')"
            variant="outlined"
            class="userInput"
        />
        <v-text-field
            v-model="university.country"
            hide-details
            :label="$t('applicationForm.countryLabel')"
            variant="outlined"
            class="userInput"
        />
        <!--TODO: i18n  -->
        <v-text-field
            v-model="university.website"
            hide-details
            :label="$t('applicationForm.websiteLabel')"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("applicationForm.previousStudyProgram") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("applicationForm.studyProgramDescription") }}
                </span>
                <span v-else key="1">
                  {{ university.courseOfStudy }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-text-field
            v-model="university.courseOfStudy"
            hide-details
            :label="$t('applicationForm.studyProgramLabel')"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
  </v-expansion-panels>
</template>
<script>
import axios from "axios";

export default {
  props: {
    universityData: Object,
  },
  data() {
    return {
      university: {
        name: null,
        country: "",
        website: "",
        courseOfStudy: "",
        ...this.universityData,
      },
      selectedUniversity: null,
      universities: [],
    }
  },
  methods: {
    async fetchUniversities() {
      try {
        const response = await axios.get("http://universities.hipolabs.com/search", {timeout: 10000});
        this.universities = response.data;
        console.log("Universities fetched.")
      } catch (error) {
        console.error('Error fetching universities:', error);
      }
    }
  },
  watch: {
    university: {
      handler(newVal) {
        this.$emit('updateUniversityData', newVal);
      },
      deep: true,
    },
    selectedUniversity: function (newValue) {
      if (typeof newValue === 'string') {
        this.university.name = newValue
      } else {
        if (newValue == null) {
          this.university.name = '';
          return;
        }
        console.log(JSON.stringify(newValue, null, 4))
        this.university.name = newValue.name
        this.university.country = newValue.country
        this.university.website = newValue.web_pages.toString()
      }
    },
  },
  mounted() {
    // Fetch universities data only once when the component is mounted
    this.fetchUniversities();
  },
}
</script>
