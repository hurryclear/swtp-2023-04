<template>
  <li v-for="tests in testArray" v-bind:key="tests" class="list">
    {{ tests }}
  </li>
</template>

<script setup>
  const testArray = [1, 2, 3, 4, 5]
</script>

<style scoped>
  .list {
    padding: 30px;
    margin-left: 5%;
  }
</style>