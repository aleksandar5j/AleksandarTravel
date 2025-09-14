<template>
    <div class="content">
        <div class="header">
            <img src="/logo.png" class="logo">
            <div class="nav">
                <router-link to="/" class="link">PUTOVANJA</router-link>
                <router-link to="/kontakt" class="link">KONTAKT</router-link>
                <router-link to="/lastminute" class="linklm">LAST MINUTE</router-link>

                <span v-if="user.kor_id">
                    <router-link to="/omiljene" class="link">OMILJENE</router-link>


                    <span v-if="user.kor_jeadmin">
                        <router-link to="/adminpanel" class="link">ADMIN PANEL</router-link>
                    </span>

                </span>

                <span v-if="user.kor_id" class="user-links">
                    <router-link to="/odjavljivanje" class="btn btn-logout">ODJAVI SE</router-link>
                </span>

                <span v-else class="user-links">
                    <router-link to="/ulogujse" class="btn btn-login">ULOGUJ SE</router-link>
                    <router-link to="/registrujse" class="btn btn-register" style="color: black;">REGISTRUJ SE</router-link>
                </span>
            </div>
        </div>
        <router-view class="view" @login="login"/>
    </div>
</template>

<script setup>

    import { ref, onMounted } from 'vue';

    const user = ref({});

    function login(userdata) {
        user.value = userdata;
        if(userdata.kor_id)
            router.push("/");
        else
            router.push("/ulogujse")
    }

    onMounted(function() {
        get("/api/logincheck", (data) => {
            if(data.res) {
                user.value = data.data;
            }
        })
    })

</script>

<style>
    @import url("https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&display=swap");
    @import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Sans:ital,wght@0,100..700;1,100..700&family=Kanit:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Sora:wght@100..800&display=swap');
    @import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Sans:ital,wght@0,100..700;1,100..700&family=Kanit:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Sora:wght@100..800&display=swap');
    body{
        margin: 0;
        font-family: Lato;
        font-weight: bold;
        color: #606063;
    }

    .content{
        box-sizing: border-box;
        margin: auto;
        max-width: 1360px;
        width: 100%;
        min-height: 100vh;
        background-color: #fff;
        box-shadow: 0px 0px 40px #727b914a;

    }
    .header{
        display:flex;
        padding: 0px;
        align-items: center;
        background-color: #57b6fa;

    }

    .user-links {
        margin-left: auto;   /* gura udesno */
        display: flex;
        gap: 12px;
        margin-right: 50px;
    }

    /* Osnovni stil za sva dugmad */
    .user-links .btn {
        padding: 14px 18px;
        border-radius: 15px;
        text-decoration: none;
        color: white;
        font-weight: 600;
        transition: background 0.3s ease, transform 0.2s ease;
    }

    /* Hover efekat */
    .user-links .btn:hover {
        transform: translateY(-2px);
    }

    .linklm {
        text-decoration: none;
        margin: 0 10px;
        color: red;
        font-size: 22px;
        font-weight: 700;
        position: relative;
        transition: all 0.3s ease;
    }

    /* Kada mišem prelaziš ili je link aktivan */
    .linklm:hover,
    .linklm:active,
    .linklm:focus {
        color: red;        /* hover boja */
    }

    /* Pojedinačne boje */
    .btn-login {
        background-color: #4f80ff; /* plava */
    }
    .btn-login:hover {
        background-color: #3a64d6;
    }

    .btn-register {
        background-color: white;
        color: black;
    }
    .btn-register:hover {
        background-color: #d9d9d9;
    }

    .btn-logout {
        background-color: #dc3545; /* crvena */
    }
    .btn-logout:hover {
        background-color: #7d1d25;
        transition: 0.4s;
    }

    .logo{
        height: 70px;
        margin-left: 70px;
    }
    .nav {
        display: flex;
        align-items: center;
        gap: 10px;
        flex: 1;          /* dodato - ovo omogućava guranje udesno */
        font-size: 18px;
        height: 95px;
        margin-left: 3%;
    }

    .link{
        text-decoration: none;
        margin: 0 10px;
        color: white;
        font-size: 22px;

        transition: all 0.3s ease;

    }
    .link:hover{
        color: #2b74fc;
    }
    .router-link-active{
        color: #2b74fc;
    }

    .view{
        min-height: calc(100% - 230px);
    }
    .footer{
        height: 50px;

    }
    .copyright{
        text-align: center;
    }


</style>