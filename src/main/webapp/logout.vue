<template>
    <div>Bye Bye!!!!</div>
</template>

<script setup>
    import {ref, onMounted, defineEmits } from 'vue';
    const emit=defineEmits(["login"])
    onMounted(function() {
       get("/api/logout", function() {
           emit("login",{});
       });
    })
</script>
   
<style scoped>
    
</style>
