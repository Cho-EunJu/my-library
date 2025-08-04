import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../pages/Home.vue'
import Login from '../pages/Login.vue'
import MyPage from '../pages/MyPage.vue'
import Wishlist from '../pages/Wishlist.vue'
import History from '../pages/History.vue'
import Board from '../pages/Board.vue'
import Order from '../pages/Order.vue'
import Search from '../pages/Search.vue'
import SignUp from '../pages/SignUp.vue'

const routes: Array<RouteRecordRaw> = [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/myPage', component: MyPage },
    { path: '/wish', component: Wishlist },
    { path: '/hist', component: History },
    { path: '/board', component: Board },
    { path: '/order', component: Order },
    { path: '/search', component: Search },
    { path: '/sign-up', component: SignUp },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router