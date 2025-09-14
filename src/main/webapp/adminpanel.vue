<template>
  <div class="admin-wrapper" v-if="userAdmin">

    <!-- Pozadinska slika -->
    <div class="background-image"></div>

    <div class="admin-page">
      <!-- Panel dugmadi -->
      <div id="adminpanel">
        <div class="admin-btn">
          <button @click="aranzmani">Svi aranzmani</button>
          <p class="btn-desc">Dodavanje / izmena / brisanje aranžmana</p>
        </div>

        <div class="admin-btn">
          <button @click="destinacije">Sve destinacije</button>
          <p class="btn-desc">Dodavanje / brisanje destinacija</p>
        </div>

        <div class="admin-btn">
          <button @click="drzave">Sve države</button>
          <p class="btn-desc">Dodavanje / brisanje država</p>
        </div>

        <div class="admin-btn">
          <button @click="objekti">Svi objekti</button>
          <p class="btn-desc">Dodavanje / izmena / brisanje objekata</p>
        </div>

        <div class="admin-btn">
          <button @click="korisnici">Svi korisnici</button>
          <p class="btn-desc">Upravljanje korisnicima</p>
        </div>

        <div class="admin-btn">
          <button @click="komentari">Svi komentari</button>
          <p class="btn-desc">Pregled i brisanje komentara</p>
        </div>

        <div class="admin-btn">
          <button @click="slike">Sve slike</button>
          <p class="btn-desc">Dodavanje / brisanje slika</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
    
    import { ref, onMounted } from 'vue';
    
    // ADMIN PROVERA //
    
    const userAdmin = ref(false);
    onMounted(() => {
        get("/api/logincheck", {}, (data) => {
            if (data.res === true && data.data?.kor_jeadmin === true) {
                userAdmin.value = true;
            } else {
                userAdmin.value = false;
                alert("NEMATE PRAVO PRISTUPTA!");
                router.push("/");
            }
        });
    });
    
    function aranzmani() {
        router.push("/adminaranzmani");
    }
    
    function destinacije() {
        router.push("/admindestinacije");
    }
    
    function drzave() {
        router.push("/admindrzave");
    }
    
    function objekti() {
        router.push("/adminobjekti");
    }
    
    function korisnici() {
        router.push("/korisnici");
    }
    
    function komentari() {
        router.push("/adminkomentari");
    }
    
    function slike() {
        router.push("/adminslike");
    }
    
</script>

<style scoped>

    .admin-wrapper {
        position: relative;
        min-height: 100vh;
        overflow: hidden;
    }

    .background-image {
        position: absolute;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background-image: url('/slike/slikazaregister.jpg'); /* putanja do tvoje slike */
        background-size: cover;
        background-position: center;
        filter: blur(8px);
        z-index: 0;
    }
    
    .admin-page {
        position: relative;
        z-index: 1;
        max-width: 700px;
        margin: 0 auto;
        padding: 2rem;
        font-family: Arial, sans-serif;
        margin-top: 50px;
    }

    #adminpanel {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 40px;
        padding: 40px;
        justify-items: center;
        max-width: 1200px;
        margin: 0 auto;
    }

    .admin-btn {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .admin-btn button {
        background-color: #4caf50;
        color: white;
        border: none;
        padding: 25px 30px;
        border-radius: 18px;
        cursor: pointer;
        font-size: 18px;
        font-weight: 500;
        width: 250px;
        transition: 0.3s;
        box-shadow: 0 5px 10px rgba(0,0,0,0.2);
    }

    .admin-btn button:hover {
        background-color: #45a049;
        transform: translateY(-3px);
        box-shadow: 0 7px 15px rgba(0,0,0,0.25);
    }

    .btn-desc {
        font-size: 14px;
        color: #555;
        text-align: center;
        margin-top: 10px;
        line-height: 1.3;
    }
</style>