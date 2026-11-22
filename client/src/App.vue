<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <div class="relative min-h-screen">
    <div class="pointer-events-none fixed inset-0 z-40 scanlines"></div>
    <div
      class="pointer-events-none fixed inset-0 z-30"
      style="box-shadow: inset 0 0 180px rgba(0, 0, 0, 0.75)"
    ></div>
    <RouterView />
  </div>
</template>
