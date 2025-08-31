<script setup lang="ts">
  import { onMounted, ref } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  import apiClient from '@/api/axiosInstance'
  import { useUserStore } from '@/store/user'

  const route = useRoute()
  const router = useRouter()
  const userStore = useUserStore()

  const loading = ref(true)
  const error = ref<string | null>(null)

  onMounted(async () => {
    const code = route.query.code as string

    if (!code) {
      error.value = '구글 로그인 실패: code 없음'
      loading.value = false
      return
    }

    try {
      // Spring Boot에 code 전달 → JWT + 유저정보 받기
      const res = await apiClient.post('/auth/oauth2/cb/google', {
        code : code
      });

      const { jwt, user } = res.data;

      // JWT를 localStorage에 저장
      localStorage.setItem('jwt', jwt);

      // Pinia store 갱신
      userStore.login(user.email);

      // 메인 페이지로 이동
      router.push('/');
    } catch (e) {
      error.value = '로그인 처리 중 오류 발생';
      console.error(e);
    } finally {
      loading.value = false;
    }
  });
</script>

<template>
  <div class="p-4">
    <p v-if="loading">로그인 처리 중...</p>
    <p v-else-if="error">{{ error }}</p>
  </div>
</template>

<style scoped>

</style>