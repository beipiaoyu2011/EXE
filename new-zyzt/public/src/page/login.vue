<template>
    <div id="app">
        <div class="loginMain" v-show="!showPage">
            <p>
                账号：
                <input type="text" v-model="account">
            </p>
            <p>
                密码：
                <input type="password" v-model="password">
            </p>
            <p>
                <button class="loginBtn" @click="handleLogin()">登录</button>
            </p>
        </div>
        <div class="pages" v-show="showPage">
            <a class="link" href="/private.html">私募圈-绝密内参</a><br/>
        </div>
    </div>

</template>

<script>
export default {
    name: 'app',
    data() {
        return {
            account: 'E00000282',
            password: '1qaz2wsx3ed',
            showPage: false
        }
    },
    created() {
        console.log(dataService);
    },
    methods: {
        handleLogin: function () {  
            this.dataService.loginIn({
                _this: this,
                option:{
                    login_name: this.account,
                    password: this.password
                },
                callback0: (res)=>{
                    if(res.data.code == 2){
                        alert('账号或者密码错误');
                        return false;
                    }
                    this.showPage = true;;

                }
            })
        }
    }
}
</script>

<style lang="less">
.loginMain {
    width: 500px;
    height: 200px;
    position: absolute;
    left: 0;
    right: 0;
    top: 30%;
    bottom: 0;
    margin: 0 auto;
    p {
        margin: 10px;
    }
    .loginBtn {
        width: 80px;
        margin-left: 60px;
        margin-top: 10px;
    }
}
</style>
