<template>
  <div>
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
    <v-btn @click="submit()">
      {{ $t("updateModuleDataView.submit") }}
    </v-btn>
  </div>
</template>

<script>
import axios from '@/plugins/axios'
export default {
  data() {
    return {
      file: null,
    }
  },
  methods: {
    submit() {
      if (!this.file) {
        // No file selected, handle this case as needed
        console.error("No file selected");
        return;
      }

      var reader = new FileReader();
      reader.onload = (event) => {
        try {
          const jsonData = JSON.parse(event.target.result);
          // Validate JSON structure here
          if (!this.isValidJson(jsonData)) {
            console.error("Invalid JSON structure");
            alert("Invalid JSON structure");
            return;
          }

          // If JSON is valid, send it via Axios
          axios.put('/api/unidata/update', jsonData, {
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
        } catch (error) {
          // Handle JSON parsing error
          console.error("Error parsing JSON:", error);
        }
      };
      reader.readAsText(this.file[0]);
    },
    isValidJson(jsonData) {
      // Implement your JSON structure validation logic here
      // For example:
      if (!Array.isArray(jsonData.courses)) return false;
      for (const course of jsonData.courses) {
        if (typeof course.name !== 'string' || !Array.isArray(course.modules)) return false;
        for (const module of course.modules) {
          if (typeof module.number !== 'string' || typeof module.name !== 'string') return false;
        }
      }
      return true;
    }
  }
}
</script>

<style scoped>
.userInput {
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>
