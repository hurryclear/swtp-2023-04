<template>
    <v-app class="v-application">
      <v-container>
        <NavigationMenu/>
      </v-container>
      
      <div class="main-container">
        <router-view/>
      </div>
    </v-app>
</template>

<script>
// Stop error ResizeObserver
const debounce = (callback, delay) => {
  let tid;
  return function (...args) {
    const ctx = this;
    tid && clearTimeout(tid);
    tid = setTimeout(() => {
      callback.apply(ctx, args);
    }, delay);
  };
};

const originalResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends originalResizeObserver {
  constructor(callback) {
    const debouncedCallback = debounce(callback, 20);
    super(debouncedCallback);
  }
};

import {defineComponent} from "vue";
import NavigationMenu from "@/components/NavigationMenu.vue";

export default defineComponent({
  components: {NavigationMenu}
});
</script>


<style>
.v-application > * {
  transition: background-color 1s ease, color 1s ease; /*Dark Mode Transition*/
}

.main-container {
  margin: 1rem
}
</style>
