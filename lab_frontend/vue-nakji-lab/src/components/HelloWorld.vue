<script setup>
defineProps({
  msg: {
    type: String,
    required: true,
  },
});
</script>

<template>
  <div class="greetings">
    <h1 class="my-blue">{{ msg }}</h1>
    <h3>UPGRADE working on it!!</h3>
    <h3>
      <a href="https://blog.naver.com/ratatou2_" target="_blank" rel="noopener">naver blog</a> +
      <a href="https://ratatou2.tistory.com/" target="_blank" rel="noopener">tistory blog</a>.
    </h3>
  </div>
</template>

<style scoped>
h1 {
  font-weight: 500;
  font-size: 2.6rem;
  position: relative;
  top: -10px;
}

h3 {
  font-size: 1.2rem;
}

.greetings h1,
.greetings h3 {
  text-align: center;
}

@media (min-width: 1024px) {
  .greetings h1,
  .greetings h3 {
    text-align: left;
  }
}
</style>
