<script setup lang="ts">

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
          <input type="text" class="form-control" id="floatingInput" placeholder="ID">
          <label for="floatingInput">아이디</label>
        </div>
        <div class="form-floating">
          <input type="password" class="form-control" id="floatingPassword" placeholder="Password">
          <label for="floatingPassword">비밀번호</label>
        </div>

        <button class="w-100 btn btn-lg btn-primary mt-3 mb-4" type="submit">로그인</button>

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
            <button class="social-btn kakao" aria-label="카카오 로그인" title="카카오"></button>
            <button class="social-btn naver" aria-label="네이버 로그인" title="네이버"></button>
            <button class="social-btn google" aria-label="구글 로그인" title="구글"></button>
          </div>
        </div>
      </form>
    </main>
</template>

<style scoped>

</style>