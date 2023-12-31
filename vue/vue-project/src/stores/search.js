import { ref } from 'vue'
import { defineStore } from 'pinia'
import { axiosInstance } from '@/util/http-common'


export const useSearchStore = defineStore('search', () => {
    const searchList = ref(null);
    const type = ref('');
    const D_API = `https://healthpanda.site`

    function loadSession() {
        const storedSearchList = sessionStorage.getItem('searchList');
        const storedType = sessionStorage.getItem('type');
        if (storedSearchList) {
            searchList.value = JSON.parse(storedSearchList);
        }

        if (type.value === '' && storedType) {
            type.value = storedType;
        }
    }

    const searchBoardList = function (searchInfo) {
        searchList.value = null;
        axiosInstance({
            method: "GET",
            url: `${D_API}/board/search`,
            params: {
                key: searchInfo.key,
                word: searchInfo.word,
            }
        })
            .then((response) => {
                if (response.data.length > 0) {
                    const list = [];
                    for (let i = 0; i < response.data.length; i++) {
                        const board = response.data[i];
                        let created = new Date(response.data[i].createdAt);
                        let updated = new Date(response.data[i].updatedAt);
                        board.createdAtFormat = created.toISOString().split('T')[0];
                        board.updatedAtFormat = updated.toISOString().split('T')[0];
                        list.push(board);
                    };

                    list.sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt));
                    searchList.value = list;
                    sessionStorage.setItem("searchList", JSON.stringify(searchList.value))
                    sessionStorage.setItem("type", type.value);
                } else { searchList.value = null; }
            })
            .catch((err) => {
                console.log(err);
            })
    }


    const searchVideoList = function (searchInfo) {
        searchList.value = null;
        axiosInstance({
            method: "GET",
            url: `${D_API}/video/search`,
            params: {
                key: searchInfo.key,
                word: searchInfo.word,
            }
        })
            .then((res) => {
                if (res.data.length > 0) {
                    searchList.value = res.data;
                    sessionStorage.setItem("searchList", JSON.stringify(searchList.value))
                    sessionStorage.setItem("type", type.value);
                } else { searchList.value = null; }
            })
            .catch((err) => {
                console.log(err);
            })
    }


    return { searchBoardList, searchList, searchVideoList, type, loadSession }
}, {
    persist: {
        storage: sessionStorage,
    }
})