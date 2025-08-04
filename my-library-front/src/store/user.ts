import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        isLogin: false,
        userName: ''
    }),
    actions: {
        login(name: string) {
            this.isLogin = true
            this.userName = name
        },
        logout() {
            this.isLogin = false
            this.userName = ''
        }
    }
})