<template>
    <div class="page-wrapper">
        <!-- Pozadinska slika -->
        <div class="background-image"></div>

        <div class="lastminute-page">

            <h1 v-if="lastminute.length === 0" style="padding-top: 100px;">Trenutno nema Last Minute ponuda!</h1>
            <h1 v-if="lastminute.length > 0">Last Minute</h1>
            <h2 v-if="lastminute.length > 0">Požurite, ostalo je još malo!</h2>


            <div class="destinations">
                <div class="destination-card" v-for="ara in lastminute" :key="ara.ara_id">
                    <!-- Slika -->
                    <div class="image-wrapper">
                        <img
            v-if="ara.des_id"
            :src="'/api/slike?des_id=' + ara.des_id"
                            alt="Slika destinacije"
                            />
                    </div>

                    <!-- Tekst -->
                    <div class="info">
                        <h3 class="drzava">{{ ara.drz_naziv }}</h3>
                        <p class="destinacija" v-else>{{ ara.des_naziv }}</p>
                        <p class="datum">
                            <strong style="color: black;">Od:  </strong> {{ formatirajVreme(ara.ara_od) }}h<br> 
                            <strong style="color: black;">Do:  </strong> {{ formatirajVreme(ara.ara_do) }}h
                        </p>

                        <div class="dugme-container">
                            <button @click="otvoriModal(ara)">Detaljnije</button>
                        </div>
                    </div>

                    <!-- Cena badge -->
                    <div class="badge-cena">{{ ara.ara_cena }}€</div>
                    <div class="badge-cenaa">{{ ara.cena_sa_popustom }}€</div>
                </div>
            </div>

        </div>

        <div
            class="modal-overlay" v-if="prikaziModal" @click="prikaziModal = false"
            >
            <div class="modal-content" @click.stop v-if="selektovaniAranzman">
                <h2>{{ selektovaniAranzman.des_naziv }}</h2>
                <h3>{{ selektovaniAranzman.drz_naziv }}</h3>
                <p style="text-align: center"> {{ selektovaniAranzman.ara_opis }}</p>
                <p style="text-align: center"><strong>Cena:  </strong> {{ selektovaniAranzman.ara_cena }}€</p>
                <p style="text-align: center"><strong>Polazak:  </strong>{{ formatirajVreme(selektovaniAranzman.ara_od) }}h</p>
                <p style="text-align: center"><strong>Povratak:  </strong>{{ formatirajVreme(selektovaniAranzman.ara_do) }}h</p>

                <div class="dugmici">
                    <button id="slikedugme" @click="prikaziSlike(selektovaniAranzman.obj_id)">
                        Prikaži slike
                    </button>
                    <button id="zakazidugme" @click="zakaziDugme(selektovaniAranzman.ara_id)">
                        Zakaži odmah
                    </button>
                </div>

                <button class="modal-close-btn" @click="prikaziModal = false">×</button>
            </div>
        </div>
    </div>
</template>

<script setup>

    import { ref, onMounted } from 'vue';

    const lastminute = ref([]);

    onMounted(() => {
        get("/api/lastminute", {}, (data) => {
            if(data.res === "OK") {
                lastminute.value = data.data;
            }
            else {
                alert(data.msg);
            }
        })
    })
    
    const user = ref({});
    
    onMounted(() => {
        get("/api/logincheck", {}, (data) => {
            if(data.res) {
                user.value = data.data;
            }
        })
    })

    const prikaziModal = ref(false);
    const selektovaniAranzman = ref(null);

    function otvoriModal(ara) {
        selektovaniAranzman.value = ara; // sačuvaš samo taj aranžman
        prikaziModal.value = true;       // otvori modal
    }

    function prikaziSlike(obj_id) {
        router.push("/galerija?obj_id=" + obj_id);
    }

    function zakaziDugme(ara_id) {
        if(user.value.kor_id) {
            router.push("/zakazivanje?ara_id=" + ara_id);
        }
        else {
            router.push("/ulogujse")
        }
    }

    function formatirajVreme(isoString) {
        let date = new Date(isoString);

        let options = {
            timeZone: "Europe/Belgrade",
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        };

        return date.toLocaleString("sr-RS", options);
    }

</script>

<style scoped>

    .page-wrapper {
        position: relative;
        min-height: 100vh;
        overflow: hidden;
    }

    .background-image {
        position: absolute;
        top: 0;
        left: 0;
        width: 150vw;
        height: 200vh;
        background-image: url('/slike/slikazaregister.jpg'); /* putanja do tvoje slike */
        background-size: cover;
        background-position: center;
        filter: blur(8px);
        z-index: 0;
    }

    .lastminute-page {
        position: relative;
        z-index: 1;
        max-width: 1200px;
        margin: 0 auto;
        padding: 2rem;
        font-family: Lato;
    }
    
    h1 {
        color: white;
        text-align: center;
    }
    
    h2 {
        color: white;
        text-align: center;
    }

    .destinations {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        justify-content: center;
        margin-top: 50px;
    }

    .destination-card {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 250px;
        background: #fff;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        text-align: center;
        cursor: pointer;                     /* rukica kad pređeš mišem */
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    /* Efekat na hover */
    .destination-card:hover {
        transform: scale(1.05) translateY(-5px);   /* malo se uveća i podigne */
        box-shadow: 0 8px 20px rgba(0,0,0,0.2);   /* jača senka */
    }

    .image-wrapper {
        width: 100%;
        height: 220px;
        overflow: hidden;
    }
    .image-wrapper img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .info {
        padding: 15px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .drzava {
        font-size: 1.3rem;
        margin: 0;
        font-weight: bold;
    }
    .destinacija {
        font-size: 1.1rem;
        margin: 5px 0;
    }
    .datum {
        font-size: 0.9rem;
        color: #555;
        margin: 10px 0;
    }

    .dugme-container button {
        margin-top: 10px;
        padding: 8px 14px;
        background: #3498db;
        color: white;
        border: none;
        border-radius: 8px;
        cursor: pointer;
    }
    .dugme-container button:hover {
        background: #2980b9;
    }

    /* Bedž u gornjem desnom uglu KARTICE */
    .badge-cena {
        position: absolute;
        z-index: 2;                      /* <— da bude iznad slike */
        top: 10px;
        right: 10px;
        background: red;
        color: white;
        padding: 5px 12px;
        border-radius: 8px;
        font-weight: bold;
        text-decoration: line-through;
    }
    
    .badge-cenaa {
        position: absolute;
        z-index: 3;                      /* <— da bude iznad slike */
        top: 32px;
        right: 20px;
        background: #27ae60;
        color: white;
        padding: 5px 12px;
        border-radius: 8px;
        font-weight: bold;
    }

    .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.6); /* tamna providna pozadina */
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 999;
        backdrop-filter: blur(3px);
    }

    .modal-content {
        background: #fff;
        padding: 25px 35px;
        border-radius: 15px;
        max-width: 550px;
        width: 90%;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
        position: relative;
        animation: fadeInScale 0.25s ease;
    }

    .modal-content h2{
        margin-top: 0;
        font-size: 1.6rem;
        color: #333;
        text-align: center;
    }

    h3 {
        text-align: center;
        color: black;
    }

    .modal-content p {
        margin: 8px 0;
        font-size: 1rem;
        color: #555;
    }

    .dugmici {
        display: flex;
        justify-content: center;  /* centrirano */
        gap: 15px;                /* razmak između dugmadi */
        margin-top: 20px;
    }

    #zakazidugme,
    #slikedugme {
        background: #4CAF50;
        color: white;
        border: none;
        padding: 10px 18px;
        border-radius: 8px;
        cursor: pointer;
        font-size: 0.95rem;
        transition: background 0.3s ease, transform 0.2s ease;
    }

    #slikedugme {
        background: #2196F3;
    }

    #zakazidugme:hover {
        background: #45a049;
        transform: translateY(-2px);
    }

    #slikedugme:hover {
        background: #1976D2;
        transform: translateY(-2px);
    }

    .modal-close-btn {
        position: absolute;
        top: 12px;
        right: 18px;
        background: transparent;
        border: none;
        font-size: 1.6rem;
        color: #666;
        cursor: pointer;
        transition: color 0.2s ease;
    }

    .modal-close-btn:hover {
        color: #000;
    }

    @keyframes fadeInScale {
        from {
            opacity: 0;
            transform: scale(0.85);
        }
        to {
            opacity: 1;
            transform: scale(1);
        }
    }

</style>