import { ref } from 'vue'
import { defineStore } from 'pinia'
import { axiosInstance, axiosInstanceWithToken } from '@/util/http-common'

const REST_BOARD_API = `http://localhost:8080/video`
const REST_BOARD_API_Like = `http://localhost:8080/auth/like`

export const useVideoStore = defineStore('video', () => {
  const videoList = ref(null)
  const videoLikeCountList = ref({});
  const likeList = ref(null);
  const selectedSort = ref('전체');

  const getVideoList = async function () {
    await axiosInstance.get(REST_BOARD_API)
      .then(async function (res) {
        videoList.value = res.data;
        await Promise.all(videoList.value.map(video => getLikeCount(video.num)));

        videoList.value.sort((a, b) => {
          const likeCountA = videoLikeCountList.value[a.num] || 0;
          const likeCountB = videoLikeCountList.value[b.num] || 0;
          return likeCountB - likeCountA;
        });
      })
      .catch((err) => {
        console.log(err);
      });
  }


  const clickLike = function (video) {
    axiosInstanceWithToken
      .post(REST_BOARD_API_Like, video)
      .then((res) => {
        console.log("ok")
        getVideoList();
        likeCheck();
      })
      .catch((err) => {
        console.log(err);
      });
  }


  const likeCheck = function () {
    axiosInstanceWithToken
      .get(REST_BOARD_API_Like + "/get")
      .then((res) => {
        likeList.value = res.data;
      })
      .catch((err) => {
        console.log(err);
      });
  }

  const getLikeCount = async function (videoNum) {
    await axiosInstance.get(`http://localhost:8080/like/${videoNum}`)
      .then((res) => {
        videoLikeCountList.value[videoNum] = res.data
      })
      .catch((err) => {
        console.log(err);
      });
  }

  return { getVideoList, videoList, clickLike, likeCheck, likeList, selectedSort, getLikeCount, videoLikeCountList }
})