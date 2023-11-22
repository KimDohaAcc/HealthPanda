import { defineStore } from "pinia";
import { ref } from "vue";
import { axiosInstance, axiosInstanceWithToken } from '@/util/http-common'
import { useRouter } from 'vue-router';

export const useUserStore = defineStore('user', () => {
      const loginUser = ref(null);
      const router = useRouter();
      let user = {};
      let mealList = ref(null);
      let followerList = ref(null);
      let followeeList = ref(null);

      const REST_API_URL = `http://localhost:8080`;
      const originalLogin = async () => {
        axiosInstance({
          method: "POST",
          url: "http://localhost:8080/originalLogin",
          params: {
            account: username.value,
            password: password.value,
          }
        })
        .then((response)=>{
          alert('로그인이 되었습니다.');
          sessionStorage.setItem("loginUser", JSON.stringify(response.data.user));
          sessionStorage.setItem("jwtToken", response.data['access-token']);
          loginUser.value = response.data.user;
          router.push('/');
        })
        .catch( (error) =>{
          console.error('로그인 실패:', error);
          alert("해당 계정이 존재하지 않습니다.");
        }
        );
      }

      loginUser.value = JSON.parse(sessionStorage.getItem('loginUser'));

      const getKakaoAccount = function () {
        window.Kakao.API.request({
          url: "/v2/user/me",
          success: (res) => {
            const id = res.id;
            const kakao_account = res.kakao_account;
            const nickname = kakao_account.profile.nickname;
            const email = kakao_account.email;
            console.log(id);
            console.log("kakao_account", kakao_account);
            console.log("nickname", nickname);
            console.log("email", email);

            user = {
              id: id,
              nickname: nickname,
              account: email,
              iskakao: true,
            }

            sessionStorage.setItem("loginUser", JSON.stringify(user));
            loginUser.value = user;
            login();
          },
          fail: (error) => {
            console.log(error);
          },
        });
      }

      const login = function () {
        const API_URL = REST_API_URL + `/login`
        console.log(user)

        axiosInstance
            .post(API_URL, user)
            .then((res) => {
              sessionStorage.setItem('jwtToken', res.data['access-token']);
            })
            .catch((err) => {
              console.log(err);
              alert("로그인 실패");
            });
      }


      const logoutUser = function () {
        sessionStorage.removeItem("jwtToken");
        sessionStorage.removeItem("loginUser");
        loginUser.value = null;
      }

      const getUserFollow = function (type) {
        let API_URL = REST_API_URL;
        if (type == "follower") {
          API_URL += "/follower";
        } else { API_URL += "/followee" }

        const user = loginUser.value;

        axiosInstanceWithToken
            .post(API_URL, user)
            .then((res) => {
              if (type == "follower") {
                followerList.value = res.data;
              } else {
                followeeList.value = res.data;
              }
            })
            .catch((err) => {
              console.log(err);
            });
      };

      const checkLoginAndRedirect = function (next, loginPath = '/login') {
        const isLoggedIn = computed(() => !!loginUser);
        if (!isLoggedIn.value) {
          next(loginPath);
        } else {
          next();
        }
      }
      const submitNewUser = async (newUser) => {
        try {
          
          const response = await axiosInstance.post('http://localhost:8080/user', newUser);
          console.log('서버 응답:', response.data);
          alert("회원가입이 완료되었습니다.");
          router.push('/');
        } catch (error) {
          console.error('오류 발생:', error);
          
        }
      };



      return { originalLogin, submitNewUser, loginUser, checkLoginAndRedirect, getKakaoAccount, logoutUser, mealList, getUserFollow, followerList, followeeList }
    },
    {
      persist: {
        storage: sessionStorage,
      }
    },
)