import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'

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
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach((to, from, next) => {
    const userStore = useUserStore();
    const publicPath = ['/login', '/sign-up', '/'];
    const authRequired = !publicPath.includes(to.path);

    if(authRequired && !userStore.isLogin){
        alert('로그인 후 이용 가능합니다.');
        // console.log(authRequired, '//', to.path, '//', userStore.isLogin);
        next('/login');
    } else {
        next();
    }
});

export default router