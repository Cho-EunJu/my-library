<script setup lang="ts">
  import { ref } from 'vue'
  import apiClient from "@/api/axiosInstance";

  const message = ref('');

  async function onLogin(event : Event){
    event.preventDefault() // 폼 제출 기본동작 막기

    const res = await apiClient.get('/hello/say');
    message.value = res.data.message;
    alert("api 호출 성공: " + message.value);
  }

  const loginWithGoogle = async () => {
    try {
      // Spring Boot API 호출 → 구글 인증 URL 받기
      const res = await apiClient.post('/auth/oauth2/google');
      const redirectUrl = res.data.redirectUrl;

      console.log("Ellen:: ", redirectUrl);
      // 구글 로그인 페이지로 이동
      window.location.href = redirectUrl;
    } catch (e) {
      console.error('구글 로그인 요청 실패', e);
    }
  }


</script>

<style scoped src="@/assets/css/login.css"></style>
<template>
    <main class="form-signin text-center">
      <form>
        <h1 class="site-title">
          <i class="bi bi-book-half"></i>
          모두의 <span class="highlight">도서관</span>
        </h1>

        <div class="form-floating">
          <input type="text" class="form-control" id="floatingInput" placeholder="ID 또는 Email">
          <label for="floatingInput">아이디/이메일</label>
        </div>
        <div class="form-floating">
          <input type="password" class="form-control" id="floatingPassword" placeholder="Password">
          <label for="floatingPassword">비밀번호</label>
        </div>

        <button class="w-100 btn btn-lg btn-primary mt-3 mb-4" type="button" @click="onLogin" >로그인</button>

        <ul class="nav nav-pills nav-gray-list">
          <li class="nav-gray-item"><router-link to="/" class="nav-link" active-class="active" aria-current="page">비밀번호 찾기</router-link></li>
          <li class="nav-gray-item"><router-link to="/wish" class="nav-link" active-class="active">회원가입</router-link></li>
          <li class="nav-gray-item"><router-link to="/hist" class="nav-link" active-class="active">아이디찾기</router-link></li>
        </ul>

        <div class="social-login-wrapper">
          <div class="social-login-title">
            <span>간편 로그인</span>
          </div>
          <div class="social-login-buttons">
            <button class="social-btn kakao" aria-label="카카오 로그인" title="카카오" type="button"></button>
            <button class="social-btn naver" aria-label="네이버 로그인" title="네이버" type="button"></button>
            <button class="social-btn google" aria-label="구글 로그인" title="구글" type="button" @click="loginWithGoogle"></button>
          </div>
        </div>
      </form>
    </main>
</template>

<style scoped>

</style>