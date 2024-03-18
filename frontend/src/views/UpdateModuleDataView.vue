<template>
  <div>
    <!-- File input for uploading JSON file -->
    <v-file-input
        v-model="file"
        class="userInput"
        accept=".json"
        show-size
        :label="$t('updateModuleDataView.uploadJSON')"
        variant="outlined"
        hide-details
        prepend-icon=""
    />
    <!-- Expansion panels to display JSON data -->
    <v-expansion-panels v-if="isValidJson">
      <v-expansion-panel v-for="(course, index) in jsonData.courses" :key="index">
        <v-expansion-panel-title>
          {{ course.name }}
        </v-expansion-panel-title>
        <v-expansion-panel-text>
          <!-- Data table to display course modules -->
          <v-data-table :items="course.modules" :headers="headers">
            <template v-slot:items="props">
              <td>{{ props.item.number }}</td>
              <td>{{ props.item.name }}</td>
            </template>
          </v-data-table>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
    <br/>
    <!-- Button to submit JSON data -->
    <v-btn @click="submit()" :disabled="!isValidJson || !file">
      {{ $t("updateModuleDataView.submit") }}
    </v-btn>
  </div>
</template>

<script>
import axios from '@/plugins/axios';

export default {
  data() {
    return {
      file: null,
      jsonData: null, // Store parsed JSON data
      headers: [
        {title: 'Module Number', key: 'number'},
        {title: 'Module Name', key: 'name'}
      ]
    };
  },
  computed: {
    /**
     * Checks if the parsed JSON data is valid.
     * @returns {boolean} True if JSON is valid, otherwise false.
     */
    isValidJson() {
      if (!this.jsonData) return false; // JSON not parsed yet
      if (!Array.isArray(this.jsonData.courses)) return false;
      for (const course of this.jsonData.courses) {
        if (typeof course.name !== 'string' || !Array.isArray(course.modules)) return false;
        for (const module of course.modules) {
          if (typeof module.number !== 'string' || typeof module.name !== 'string') return false;
        }
      }
      return true;
    }
  },
  watch: {
    file: {
      handler(newFile) {
        if (!newFile) return; // If no file, do nothing
        this.parseJson();
      },
      deep: true
    }
  },
  methods: {
    /**
     * Submits the parsed JSON data to the server.
     */
    submit() {
      if (!this.file) {
        // No file selected, handle this case as needed
        console.error("No file selected");
        return;
      }

      // If JSON is valid, send it via Axios
      axios.put('/api/unidata/update', this.jsonData, {
        headers: {
          'Content-Type': 'application/json',
        }
      }).then(response => {
        // Handle response
        console.log(response);
      }).catch(error => {
        // Handle error
        console.error(error);
      });
    },
    /**
     * Parses the uploaded JSON file.
     */
    parseJson() {
      var reader = new FileReader();
      reader.onload = (event) => {
        try {
          this.jsonData = JSON.parse(event.target.result);
        } catch (error) {
          // Handle JSON parsing error
          console.error("Error parsing JSON:", error);
          this.jsonData = null; // Reset jsonData if parsing fails
        }
      };
      reader.readAsText(this.file[0]);
    }
  }
};
</script>

<style scoped>
.userInput {
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>
