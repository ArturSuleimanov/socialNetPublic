<template>
  <iframe
      width="560"
      height="315"
      :src="targetSrc"
      frameborder="0"
      allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
      allowfullscreen
  ></iframe>
</template>

<script>
export default {
  name: 'YouTube',
  props: ['src'],
  computed: {
    targetSrc() {
      let parts;
      if (this.src.indexOf("watch") != -1) {
        parts = this.src.split('?v=')
        let cur = parts[parts.length - 1]
        parts = cur.split("\&")[0]
        console.log(parts)
      } else {
        parts = this.src.split("/")
        parts = parts[parts.length - 1]
      }
      return `https://www.youtube.com/embed/${parts}`
    }
  }
}
</script>

<style scoped>

</style>