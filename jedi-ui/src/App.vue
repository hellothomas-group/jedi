<template>
  <div id="app">
    <!--<img src="./assets/logo.png">-->
    <div style="font-size: 2em;font-weight: bold;padding: 20px;text-align: center">Jedi线程池管理平台</div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
