<template>
  <span></span>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "LazyLoader",
  methods: mapActions(['loadPageAction']),
  mounted() {
    window.onscroll = () => {
      const el = document.documentElement
      const isbottomOfScreen = el.scrollTop + window.innerHeight + 1 >= el.offsetHeight

      if (isbottomOfScreen) {

        this.loadPageAction()
      }
    }
  },
  beforeDestroy() {
    window.onscroll = null
  }

}
</script>

<style scoped>

</style>