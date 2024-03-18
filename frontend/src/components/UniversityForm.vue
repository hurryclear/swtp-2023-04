<!-- UniversityForm.vue -->
<template>
  <v-expansion-panels>
    <!-- University Panel -->
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("applicationFormView.universityForm.previous") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("applicationFormView.universityForm.description") }}
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
        <!-- University Form Inputs -->
        <v-combobox
            v-model="selectedUniversity"
            :items="universities"
            item-title="name"
            hide-details
            :label="$t('applicationFormView.universityForm.nameLabel')"
            variant="outlined"
            class="userInput"
        />
        <v-text-field
            v-model="university.country"
            hide-details
            :label="$t('applicationFormView.universityForm.countryLabel')"
            variant="outlined"
            class="userInput"
        />
        <v-text-field
            v-model="university.website"
            hide-details
            :label="$t('applicationFormView.universityForm.websiteLabel')"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
    <!-- Course of Study Panel -->
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("applicationFormView.courseOfStudy.courseOfStudy") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("applicationFormView.courseOfStudy.description") }}
                </span>
                <span v-else key="1">
                  {{ courseOfStudy.new }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <!-- Course of Study Form Inputs -->
        <v-text-field
            v-model="courseOfStudy.old"
            hide-details
            :label="$t('applicationFormView.courseOfStudy.previous')"
            variant="outlined"
            class="userInput"
        />
        <v-select
            v-model="courseOfStudy.new"
            hide-details
            :items="studyPlans"
            item-title="name"
            :label="$t('applicationFormView.courseOfStudy.new')"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<script>

/**
 * Vue component representing the university form.
 * This component allows users to input details about their university.
 * @component UniversityForm
 */
export default {
  // Data
  data() {
    return {
      university: {
        name: null,
        country: "",
        website: "",
      },
      courseOfStudy: {
        old: "",
        new: null,
      },
      selectedUniversity: null,
    }
  },

  // Computed properties
  computed: {
    /**
     * Retrieves the list of available universities from the store.
     * @returns {Array} List of available universities.
     */
    universities() {
      return this.$store.state.university.universities;
    },

    /**
     * Retrieves the list of available study plans from the store.
     * @returns {Array} List of available study plans.
     */
    studyPlans() {
      return this.$store.state.module.studyPlans
    }
  },

  // Watchers
  watch: {
    /**
     * Watches for changes in selectedUniversity and updates university data accordingly.
     * @param {any} newValue The new value of selectedUniversity.
     */
    selectedUniversity: function (newValue) {
      if (typeof newValue === 'string') {
        this.university.name = newValue
      } else {
        if (newValue == null) {
          this.university.name = '';
          return;
        }
        this.university.name = newValue.name
        this.university.country = newValue.country
        this.university.website = newValue.web_pages.toString()
      }
    },

    /**
     * Watches for changes in university data and commits updates to the store.
     * @param {object} newVal The new value of university data.
     */
    university: {
      handler(newVal) {
        this.$store.commit('updateUniversityData', newVal)
      },
      deep: true,
    },

    /**
     * Watches for changes in courseOfStudy data and commits updates to the store.
     * @param {object} newVal The new value of courseOfStudy data.
     */
    courseOfStudy: {
      handler(newVal) {
        this.$store.commit('updateCourseOfStudyData', newVal)
      },
      deep: true,
    },

    /**
     * Watches for changes in courseOfStudy.new and fetches modules accordingly.
     * @param {any} newValue The new value of courseOfStudy.new.
     */
    'courseOfStudy.new': function (newValue) {
      this.$store.dispatch('fetchModules', newValue);
    }
  },

  // Lifecycle hooks
  beforeCreate() {
    if (!this.$store.state.module.universities || !this.$store.state.module.universities.length) {
      // If universities data is not in the store, fetch it
      this.$store.dispatch('fetchUniversities');
    }
    if (!this.$store.state.module.studyPlans || !this.$store.state.module.studyPlans.length) {
      // If modules data is not in the store, fetch it
      this.$store.dispatch('fetchStudyPlans');
    }
  },
}

</script>
