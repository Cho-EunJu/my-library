// import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'      // ★ 추가
// import store from './store'

// createApp(App).use(router).use(store).mount('#app')
createApp(App).use(createPinia()).use(router).mount('#app')
