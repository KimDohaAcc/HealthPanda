<template>
    <div id="myPage">
    <h1>{{ userStore.loginUser.nickname }}님의 마이페이지</h1>
    <p>이메일: {{ userStore.loginUser.email }}</p>
    <p>알림 설정 여부 : (추후 생성)</p>
    <p>알림 시간 : </p>
    <RouterLink :to="{ name: 'myMeal' }">내 식단 기록</RouterLink>
  </div>
</template>

<script setup>
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
console.log(userStore.loginUser)
</script>

<style  scoped>

</style>