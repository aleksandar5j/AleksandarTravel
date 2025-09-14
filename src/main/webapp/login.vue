<template>
    <div class="login-page">
        <div class="background-image"></div> <!-- 👈 Ovo je blur pozadina -->

        <div class="login-form-wrapper">

            <table>
                <td><h1>ULOGUJ SE OVDE</h1><td>
                <tr>
                    <td>Korisničko ime: </td>
                    <td><input type="text" v-model="kor_kime" /></td>
                </tr>
                <tr>
                    <td>Šifra: </td>
                    <td><input type="password" v-model="kor_sifra" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><button @click="login()">Prijavi se</button></td>
                </tr>
            </table>
        </div>

    </div>

    <div class="toast-container">
        <div 
            v-for="t in toasts" 
            :key="t.id" 
            class="toast" 
            :class="t.type"
            >
            {{ t.message }}
        </div>
    </div>

</template>

<script setup>
    
    import {ref, defineEmits } from 'vue';
    
    const kor_kime = ref("");
    const kor_sifra = ref("");
    
    const emit = defineEmits(["login"]);
    
    function login() {
       post("/api/login", { kor_kime: kor_kime.value, kor_sifra: kor_sifra.value }, (data) => {
           if(data.res==="OK") {
               emit("login",data.data);
           } else {
               kor_kime.value = "";
               kor_sifra.value = "";
               showToast("Neuspesno logovanje!", "error");
           }
       })
    } 
    
    const toasts = ref([]);

function showToast(message, type = "success") {
  const id = Date.now();
  toasts.value.push({ id, message, type });

  // automatski nestaje posle 3 sekunde
  setTimeout(() => {
    toasts.value = toasts.value.filter(t => t.id !== id);
  }, 3000);
}
    
</script>

<style scoped>

    .login-page {
        position: relative;
        min-height: 89.6vh;
        overflow: hidden;
    }

    /* Blur slika pozadine */
    .background-image {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100vh;
        background-image: url('/slike/slikazaregister.jpg'); /* <-- Promeni putanju */
        background-size: cover;
        background-position: center;
        filter: blur(8px);
        z-index: 0;
    }

    /* Login forma iznad slike */
    .login-form-wrapper {
        position: relative;
        z-index: 1;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    table {
        margin: 150px auto;

        background: rgba(255, 255, 255, 0.8);
        border-radius: 16px;
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
        padding-left: 40px;
        padding-right: 40px;
        border-collapse: separate;
        border-spacing: 15px;
        backdrop-filter: blur(6px);
        transition: transform 0.3s ease;
        opacity: 0.9;
    }

    table:hover {
        transform: scale(1.01);
    }

    /* Table Cells */
    td {
        font-size: 16px;
        color: #4a90e2;
        padding: 8px;
    }

    /* Input Fields */
    input[type="text"],
    input[type="password"] {
        padding: 10px 15px;
        border: 1px solid #ccc;
        border-radius: 10px;
        font-size: 16px;
        width: 100%;
        background: rgba(255, 255, 255, 0.6);
        backdrop-filter: blur(3px);
        transition: all 0.3s ease;
        box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.05);
    }

    input[type="text"]:focus,
    input[type="password"]:focus {
        border-color: #409eff;
        background: rgba(255, 255, 255, 0.9);
        box-shadow: 0 0 8px rgba(64, 158, 255, 0.4);
        outline: none;
    }

    /* Button */
    button {
        padding: 12px 24px;
        background: linear-gradient(135deg, #4a90e2, #007bff);
        color: white;
        border: none;
        border-radius: 12px;
        font-size: 16px;
        font-weight: 600;
        cursor: pointer;
        transition: background 0.3s ease, transform 0.2s ease;
        box-shadow: 0 4px 14px rgba(0, 123, 255, 0.4);
    }

    button:hover {
        background: linear-gradient(135deg, #3f83d4, #005fcc);
        transform: scale(1.03);
    }

    h1 {
        text-align: center;
        color: #4a90e2;
    }

    .toast-container {
        position: fixed;
        top: 20px;
        right: 20px;
        display: flex;
        flex-direction: column;
        gap: 10px;
        z-index: 2000;
    }

    .toast {
        padding: 12px 18px;
        border-radius: 8px;
        color: white;
        font-weight: 600;
        box-shadow: 0 4px 10px rgba(0,0,0,0.2);
        animation: fadeIn 0.3s ease, fadeOut 0.3s ease 2.7s;
    }

    .toast.success {
        background-color: #28a745; /* zeleno */
    }

    .toast.error {
        background-color: #dc3545; /* crveno */
    }

    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(-20px);
        }
        to   {
            opacity: 1;
            transform: translateY(0);
        }
    }
    @keyframes fadeOut {
        from {
            opacity: 1;
            transform: translateY(0);
        }
        to   {
            opacity: 0;
            transform: translateY(-20px);
        }
    }

</style>