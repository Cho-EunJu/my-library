<script setup lang="ts">
import { ref, computed } from "vue";

const email = ref('');
const password = ref('');
const passwordConfirm = ref('');

// 입력 상태 (포커스되었거나 입력 시작하면 true)
const emailTouched = ref(false);
const passwordTouched = ref(false);
const passwordConfirmTouched = ref(false);

// 비밀번호 보기 토글
const showPassword = ref(false);
const showPasswordConfirm = ref(false);

const togglePassword = (field) => {
  if (field === "password") {
    showPassword.value = !showPassword.value;
  } else if (field === "confirm") {
    showPasswordConfirm.value = !showPasswordConfirm.value;
  }
};

// 이메일 유효성 체크
const isEmailValid = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[a-zA-Z]{2,}$/;
  return emailRegex.test(email.value);
});

// 비밀번호 규칙 체크
const validPwRule1 = computed(() => {
  // 영문/숫자/특수문자 중 2가지 이상 포함
  let count = 0;
  if (/[a-zA-Z]/.test(password.value)) count++;
  if (/\d/.test(password.value)) count++;
  if (/[^a-zA-Z0-9]/.test(password.value)) count++;
  return count >= 2;
});

const validPwRule2 = computed(() => {
  // 8자 ~ 20자, 공백 제외
  return password.value.length >= 8 && password.value.length <= 20 && !/\s/.test(password.value);
});

// 비밀번호 확인 일치 여부
const isPasswordConfirmValid = computed(() => {
  return password.value === passwordConfirm.value;
});

const onRegister = () => {
  if (!isEmailValid.value) {
    document.getElementById('registerEmail').focus();
    return;
  }
  if (!validPwRule1.value || !validPwRule2.value) {
    document.getElementById('registerPassword').focus();
    return;
  }
  if (!isPasswordConfirmValid.value) {
    passwordConfirmTouched.value = true;
    document.getElementById('registerPasswordConfirm').focus();
    return;
  }

  alert("회원가입?!");
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

      <!-- 이메일 입력 -->
      <div class="form-floating" style="margin-bottom: 1.25rem;">
        <input
            type="text"
            class="form-control"
            id="registerEmail"
            placeholder="Email"
            v-model="email"
            @focus="emailTouched = true"
            @input="emailTouched = true"
        />
        <label for="registerEmail">이메일</label>
      </div>
      <div v-if="emailTouched && !isEmailValid" class="text-danger text-start small mb-3">
        <i class="bi bi-x-lg"></i> 유효하지 않은 이메일 입니다.
      </div>

      <!-- 비밀번호 -->
      <div class="form-floating password-wrapper" style="margin-bottom: 1.25rem;">
        <input
            :type="showPassword ? 'text' : 'password'"
            class="form-control"
            id="registerPassword"
            placeholder="Password"
            v-model="password"
            @focus="passwordTouched = true"
            @input="passwordTouched = true"
        />
        <label for="registerPassword">비밀번호</label>
        <i
            class="bi"
            :class="showPassword ? 'bi-eye-slash' : 'bi-eye'"
            @click="togglePassword('password')"
        ></i>
      </div>
      <!-- 비밀번호 규칙 -->
      <div class="form-floating mt-2">
        <div class="pw-info-box" :class="passwordTouched ? (validPwRule1 ? 'color-pass' : 'color-miss') : ''">
          <!-- 아이콘 -->
          <i v-if="!passwordTouched" class="bi bi-check-lg" style="color: #bcb8b8;"></i> <!-- 기본 회색 -->
          <i v-else-if="validPwRule1" class="bi bi-check-lg"></i>
          <i v-else class="bi bi-x-lg"></i>
          <p>영문/숫자/특수문자 중 2가지 이상 포함</p>
        </div>
        <div class="pw-info-box" :class="passwordTouched ? (validPwRule2 ? 'color-pass' : 'color-miss') : ''">
          <!-- 아이콘 -->
          <i v-if="!passwordTouched" class="bi bi-check-lg" style="color: #bcb8b8;"></i> <!-- 기본 회색 -->
          <i v-else-if="validPwRule2" class="bi bi-check-lg"></i>
          <i v-else class="bi bi-x-lg"></i>
          <p>8자 ~ 20자 (공백제외)</p>
        </div>
      </div>

      <!-- 비밀번호 확인 -->
      <div class="form-floating password-wrapper">
        <input
            :type="showPasswordConfirm ? 'text' : 'password'"
            class="form-control"
            id="registerPasswordConfirm"
            placeholder="Confirm Password"
            v-model="passwordConfirm"
            @input="passwordConfirmTouched = true"
        />
        <label for="registerPasswordConfirm">비밀번호 확인</label>
        <i
            class="bi"
            :class="showPasswordConfirm ? 'bi-eye-slash' : 'bi-eye'"
            @click="togglePassword('confirm')"
        ></i>
      </div>
      <!-- 비밀번호 확인 경고 -->
      <div v-if="passwordConfirmTouched && !isPasswordConfirmValid" class="text-danger text-start small">
        <i class="bi bi-x-lg"></i> 비밀번호가 일치하지 않습니다.
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
          >로그인</router-link>
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

  .pw-info-box {
    display: flex;
    justify-content: left;
    gap: 5px;
    font-size: 0.875rem;
  }

  .color-pass {
    color: rgb(0, 196, 113);
  }

  .color-miss {
    color: crimson;
  }
</style>