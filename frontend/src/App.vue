<template>
    <v-app class="v-application">
      <v-container>
        <NavigationMenu/>
      </v-container>
      <v-container fluid>
        <v-row >
          <v-col cols="12" md="6">
            <LanguageSwitcher/>
          </v-col>
          <v-col cols="12" md="6">
            <DarkThemeToggle
                style="
                    height:100%;
                    width:100%;
                  "
            />
          </v-col>
        </v-row>
      </v-container>
      <v-divider thickness="3"/>
      <div class="main-container">
        <router-view/>
      </div>
    </v-app>
</template>

<script>
import DarkThemeToggle from "@/components/DarkThemeToggle.vue";
import LanguageSwitcher from "@/components/LanguageSwitcher.vue";
import {defineComponent} from "vue";
import NavigationMenu from "@/components/NavigationMenu.vue";

export default defineComponent({
  components: {NavigationMenu, DarkThemeToggle, LanguageSwitcher}
})

</script>

<style>
  .v-application > * {
    transition: background-color 1s ease, color 1s ease; /*Dark Mode Transition*/
  }
  .main-container {
    margin:1rem
  }
</style>






<!--<template>-->
<!--  <v-app class="v-application">-->
<!--    <DarkThemeToggle/>-->
<!--    <br/>-->
<!--    &lt;!&ndash; Display the greeting message from the backend &ndash;&gt;-->
<!--    <div v-if="greeting" class="greeting-message">-->
<!--      {{ greeting }}-->
<!--    </div>-->
<!--    <br/>-->
<!--    <ApplicationForm/>-->
<!--  </v-app>-->
<!--</template>-->

<!--<script>-->
<!--import DarkThemeToggle from "@/components/DarkThemeToggle.vue";-->
<!--import ApplicationForm from "@/components/ApplicationForm.vue";-->
<!--import { defineComponent, ref, onMounted } from "vue";-->
<!--import axios from 'axios';-->

<!--export default defineComponent({-->
<!--  components: { DarkThemeToggle, ApplicationForm },-->
<!--  setup() {-->
<!--    const greeting = ref(''); // Reactive reference for the greeting message-->

<!--    // Fetch the greeting message from the backend when the component is mounted-->
<!--    onMounted(() => {-->
<!--      axios.get('http://backend:8080/greeting')-->
<!--          .then(response => {-->
<!--            greeting.value = response.data;-->
<!--          })-->
<!--          .catch(error => {-->
<!--            console.error(error);-->
<!--          });-->
<!--    });-->

<!--    return {-->
<!--      greeting-->
<!--    };-->
<!--  }-->
<!--})-->
<!--</script>-->

<!--<style>-->
<!--.v-application {-->
<!--  transition: background-color 1s ease, color 1s ease; /*Dark Mode Transition*/-->
<!--  padding: 0.5rem;-->
<!--}-->
<!--.greeting-message {-->
<!--  margin-bottom: 1rem;-->
<!--  padding: 0.5rem;-->
<!--  border: 1px solid #ddd;-->
<!--  border-radius: 4px;-->
<!--}-->
<!--</style>-->





<!--<template>-->
<!--  <div id="app">-->
<!--    <h1>{{ greeting }}</h1>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import axios from 'axios';-->

<!--export default {-->
<!--  name: 'App',-->
<!--  data() {-->
<!--    return {-->
<!--      greeting: ''-->
<!--    };-->
<!--  },-->
<!--  mounted() {-->
<!--    axios.get('http://backend:8080/greeting')-->
<!--        .then(response => {-->
<!--          this.greeting = response.data;-->
<!--        })-->
<!--        .catch(error => {-->
<!--          console.error(error);-->
<!--        });-->
<!--  }-->
<!--};-->
<!--</script>-->
