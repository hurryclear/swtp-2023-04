<template>
  <v-expansion-panels>
    <!-- Expansion panel for the "Bisherige Universität" section -->
    <v-expansion-panel>
      <v-expansion-panel-title>
        <!-- Display title and fade in/out transition for description -->
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{$t("universityForm.previousUniversity")}}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{$t("universityForm.universityDescription")}}
                </span>
                <span v-else key="1">
                  {{ university.universityName }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <!-- Text fields for entering university information -->
      <v-expansion-panel-text>
        <v-text-field
            v-model="university.universityName"
            hide-details
            :label="universityNameLabel"
            variant="outlined"
            class="userInput"
        />
        <v-text-field
            v-model="university.country"
            hide-details
            :label=countryLabel
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>

    <!-- Expansion panel for the "Bisheriger Studiengang" section -->
    <v-expansion-panel>
      <v-expansion-panel-title>
        <!-- Display title and fade in/out transition for description -->
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("universityForm.previousStudyProgram") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{$t("universityForm.studyProgramDescription")}}
                </span>
                <span v-else key="1">
                  {{ university.studyProgram }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <!-- Text field for entering study program information -->
      <v-expansion-panel-text>
        <v-text-field
            v-model="university.studyProgram"
            hide-details
            :label="studyProgramLabel"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<script>
export default {
  computed: {
    universityNameLabel(){return this.$t("universityForm.universityNameLabel")},
    countryLabel(){return this.$t("universityForm.countryLabel")},
    studyProgramLabel(){return this.$t("universityForm.studyProgramLabel")}
  },

  data: () => ({
    university: {
      universityName: '',
      studyProgram: '',
      country: ''
    }
  }),
}
</script>

<style scoped>
/* Add scoped styles if needed */
</style>
