<template>
  <div class="joinMain">
    <div v-show="value" class="success-message">회원가입을 축하드립니다.</div>
    <div class="header">healthpanda</div>

    <div class="joinText">회원가입</div>
    <div>
      <label for="name">닉네임</label>
      <input type="text" placeholder="닉네임" v-model="nickname" @blur="checkNickname"
        :class="{ 'input-error': errorNickname }" maxlength="20" />
      <p v-show="errorNickname" class="input-error">닉네임이 올바르지 않습니다.</p>
    </div>

    <div>
      <label for="password">비밀번호</label>
      <input type="password" placeholder="비밀번호" v-model="password" @blur="checkPassword"
        :class="{ 'input-error': errorPassword }" />
      <p v-show="errorPassword" class="input-error">비밀번호는 영문+숫자를 포함한 4자 이상 12자 이내이여야 합니다.</p>
    </div>
    <div>
      <label for="passwordConfirmation">비밀번호 확인</label>
      <input type="password" placeholder="비밀번호 확인" v-model="passwordConfirmation"
        :class="{ 'input-error': errorPasswordConfirmation }" @blur="checkPasswordConfirmation" />
      <p v-show="errorPasswordConfirmation" class="input-error">비밀번호와 일치하지 않습니다.</p>
    </div>
    <div>
      <label for="password">이메일</label>
      <input type="email" placeholder="이메일" v-model="account" @blur="checkAccount"
        :class="{ 'input-error': errorAccount }" />
      <p v-show="errorAccount" class="input-error">이메일 형식이 올바르지 않습니다. 다시 한 번 확인 해주세요.</p>
    </div>

    <div>
      <button class="checkButton" @click="checkAccountAvailability">중복체크</button>
      <p v-if="accountExists" class="input-error">중복된 계정이 있습니다.</p>
      <p v-if="accountAvailable" class="success-message">사용가능한 계정입니다.</p>
    </div>
    <button type="button"
      :disabled="errorNickname || errorPassword || errorAccount || !nickname || !password || !account || accountExists || isJoinDisabled"
      @click="join">가입하기</button>
  </div>
</template>
  
<script>
import { ref } from 'vue';
import { useUserStore } from '@/stores/user.js'; // 실제 사용하는 경로로 변경해주세요
import axios from 'axios';

export default {
  setup() {
    const lApi = "http://localhost:8080";
    const dApi = "https://healthpanda.site";
    const userStore = useUserStore();
    const nickname = ref('');
    const account = ref('');
    const password = ref('');
    const passwordConfirmation = ref('');
    const errorNickname = ref(false);
    const errorPassword = ref(false);
    const errorPasswordConfirmation = ref(false);
    const errorAccount = ref(false);
    const accountExists = ref(false);
    const accountAvailable = ref(false);
    const isJoinDisabled = ref(true);

    const join = () => {
      checkNickname();
      checkPassword();
      checkAccount();
      checkPasswordConfirmation();

      if (!errorNickname.value && !errorPassword.value && !errorPasswordConfirmation.value && !errorAccount.value && nickname.value !== '' && password.value !== '' && passwordConfirmation.value !== '' && account.value !== '') {
        if (password.value !== passwordConfirmation.value) {
          alert('비밀번호가 일치하지 않습니다.');
          return;
        }

        const newUser = {
          nickname: nickname.value,
          account: account.value,
          password: password.value,
          iskakao: false,
        };

        userStore.submitNewUser(newUser);
      }
    };
    const checkPasswordConfirmation = () => {
      errorPasswordConfirmation.value = password.value !== passwordConfirmation.value;
    };
    const checkNickname = () => {
      const validateNickname = /^[A-Za-z가-힣]{1,20}$/;
      errorNickname.value = !validateNickname.test(nickname.value) || !nickname.value;
    };

    const checkPassword = () => {
      const validatePassword = /^[A-Za-z0-9]{4,12}$/;
      errorPassword.value = !validatePassword.test(password.value) || !password.value;
    };

    const checkAccount = () => {
      const validateAccount = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
      errorAccount.value = !validateAccount.test(account.value) || !account.value;
    };

    const checkAccountAvailability = async () => {
      if (!errorAccount.value && account.value !== '') {
        try {
          const response = await axios.get(`${dApi}/user/dupCheck/${account.value}`);
          if (response.status === 200) {
            // 중복된 계정이 있을 경우
            accountExists.value = true;
            accountAvailable.value = false;
            isJoinDisabled.value = true;
          } else {
            // 중복된 계정이 없을 경우
            accountExists.value = false;
            accountAvailable.value = true;
            isJoinDisabled.value = false;
          }
        } catch (error) {
          console.error('Error checking account:', error);
        }
      }
    };

    return {
      nickname,
      account,
      password,
      passwordConfirmation,
      errorNickname,
      errorPassword,
      errorPasswordConfirmation,
      errorAccount,
      join,
      checkNickname,
      checkPassword,
      checkPasswordConfirmation,
      isJoinDisabled,

      checkAccount,
      accountExists,
      accountAvailable,
      checkAccountAvailability,
    };
  },
};
</script>
  
<style scoped>
.input-error {
  color: red;
}

.joinMain {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
  font-family: Arial, sans-serif;
  margin-top: 250px;
  margin-bottom: 250px;
}

.header {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.joinText {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input[type="text"],
input[type="password"],
input[type="email"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

.input-error {
  color: red;
  margin-top: 5px;
  font-size: 14px;
}

.success-message {
  color: green;
  font-size: 16px;
}

.checkButton {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  cursor: pointer;
  background-color: #1a2a5f;
  color: white;
  transition: background-color 0.3s, color 0.3s;
  margin-bottom: 10px;
}

.checkButton:hover {
  background-color: #142040;
}

button {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  cursor: pointer;
  background-color: #1a2a5f;
  color: white;
  transition: background-color 0.3s, color 0.3s;
}

button:disabled {
  background-color: #ccc;
  color: #666;
  cursor: not-allowed;
}

button:hover {
  background-color: #142040;
}
</style>
