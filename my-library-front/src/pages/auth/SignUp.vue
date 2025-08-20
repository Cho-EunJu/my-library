<script setup lang="ts">
import { ref } from "vue";

const showPassword = ref(false);
const showPasswordConfirm = ref(false);

const togglePassword = (field) => {
  if (field === "password") {
    showPassword.value = !showPassword.value;
  } else if (field === "confirm") {
    showPasswordConfirm.value = !showPasswordConfirm.value;
  }
};
</script>

<style scoped src="@/assets/css/login.css"></style>

<template>
  <main class="form-signin text-center">
    <form>
      <h1 class="site-title">
        <i class="bi bi-book-half"></i>
        모두의 <span class="highlight">도서관</span>
      </h1>

      <!-- 이메일 입력 -->
      <div class="form-floating" style="margin-bottom: 10px;">
        <input
            type="text"
            class="form-control"
            id="registerEmail"
            placeholder="Email"
            v-model="email"
        />
        <label for="registerEmail">이메일</label>
      </div>

      <!-- 비밀번호 -->
      <div class="form-floating password-wrapper">
        <input
            :type="showPassword ? 'text' : 'password'"
            class="form-control"
            id="registerPassword"
            placeholder="Password"
            v-model="password"
        />
        <label for="registerPassword">비밀번호</label>
        <i
            class="bi"
            :class="showPassword ? 'bi-eye-slash' : 'bi-eye'"
            @click="togglePassword('password')"
        ></i>
      </div>

      <!-- 비밀번호 확인 -->
      <div class="form-floating password-wrapper">
        <input
            :type="showPasswordConfirm ? 'text' : 'password'"
            class="form-control"
            id="registerPasswordConfirm"
            placeholder="Confirm Password"
            v-model="passwordConfirm"
        />
        <label for="registerPasswordConfirm">비밀번호 확인</label>
        <i
            class="bi"
            :class="showPasswordConfirm ? 'bi-eye-slash' : 'bi-eye'"
            @click="togglePassword('confirm')"
        ></i>
      </div>

      <!-- 회원가입 버튼 -->
      <button
          class="w-100 btn btn-lg btn-primary mt-3 mb-4"
          type="button"
          @click="onRegister"
      >
        가입하기
      </button>

      <!-- 하단 네비게이션 -->
      <ul class="nav nav-pills nav-gray-list">
        <li class="nav-gray-item">
          <router-link to="/" class="nav-link" active-class="active"
          >메인으로</router-link
          >
        </li>
        <li class="nav-gray-item">
          <router-link to="/login" class="nav-link" active-class="active"
          >로그인</router-link
          >
        </li>
      </ul>
    </form>
  </main>
</template>

<style scoped>
  .password-wrapper {
    position: relative;
  }

  .password-wrapper i {
    position: absolute;
    right: 12px;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    font-size: 1.2rem;
    color: #bcb8b8;
  }
  .password-wrapper i:hover {
    color: #666;
  }
</style>