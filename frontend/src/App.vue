<template>
  <v-app class="v-application">
    <v-container>
      <NavigationMenu/>
    </v-container>

    <div class="main-container">
      <router-view/>
    </div>
  </v-app>
  <div>
    <p>Post ID: {{ posts.universityName }}</p>
    <p>Post Country: {{ posts.country}}</p>
    <p>Post StudyProgram: {{ posts.studyProgram }}</p>
  </div>
</template>

<script>


import {defineComponent} from "vue";
import axios from "axios"
import NavigationMenu from "@/components/NavigationMenu.vue";

export default defineComponent({
  components: {NavigationMenu},
  data() {
    return {
      posts: []
    }
  },
  mounted() {
    axios.get('http://backend:8080/greeting')
        .then(response => {
          this.posts = response.data
        })
        .catch(error => {
          console.error('Error:', error);
        });
  }
})
</script>

<style>
.v-application > * {
  transition: background-color 1s ease, color 1s ease; /*Dark Mode Transition*/
}

.main-container {
  margin: 1rem
}
</style>