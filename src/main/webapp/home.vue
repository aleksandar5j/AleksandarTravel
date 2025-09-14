<template>   

    <div class="hero-image">
        <img src="slike/slikazaregister.jpg" class="bg-blur" />

        <div class="overlay-content">
            <h1 id="h1maintext" style="font-family: 'Kanit', sans-serif; font-weight: 5px; opacity: 0.8;">Tvoja sledeća avantura počinje sa nama!</h1>
            <div class="search-form">

                <select v-model="selektovanaDrzava" @change="promeniDrzavu">
                    <option disabled value="">Država</option>
                    <option v-for="drz in drzave" :key="drz.drz_id" :value="drz.drz_id">
                        {{ drz.drz_naziv }}
                    </option>
                </select>

                <select v-model="selektovanaDestinacija" @change="ucitajAranzmane">
                    <option disabled value="">Grad</option>
                    <option v-for="des in destinacije" :key="des.des_id" :value="des.des_id">
                        {{ des.des_naziv }}
                    </option>
                </select>

                <label for="datum" style="color: white;">  Izaberi datum: </label>
                <input id="datum" type="date" v-model="selektovaniDatum" @change="datumSelekt" placeholder="Izaberi datum"
                       style="padding: 9px; border-radius: 5px; border: 0; font-size: 16px;"
                       >

                <button @click="resetuj()" style="padding: 10px;">Resetuj filtere</button>
            </div>
        </div>
    </div>

    <h1 id="h1ponude"><strong style="color: #254878;">Aleksandar Travel</strong> turistička agencija</h1>
    <h2 id="h2ponude">Licencirani organizator, atraktivne destinacije, najbolje ponude! ↓</h2>

    <div class="destinations">
        <div class="destination-card" v-for="ara in aranzmani" :key="ara.ara_id" style="position: relative;" v-if="aranzmani.length > 0">

            <!-- Ikona srca u gornjem levom uglu -->
            <i 
                :class="[
                 'pi',
                 jeOmiljeni(ara.obj_id) ? 'pi-heart-fill' : 'pi-heart',
                 'favorite-icon'
               ]"
               @click="toggleOmiljeni(ara.des_id, ara.obj_id)"
                title="Dodaj ili ukloni iz omiljenih">
            </i>

            <!-- Slika -->
            <div class="image-wrapper">
                <img
                    v-if="ara.des_id"
                    :src="'/api/slike?des_id=' + ara.des_id"
                    alt="Slika"
                    />
            </div>

            <!-- Tekst -->
            <div class="info">
                <h1 class="drzava">{{ ara.drz_naziv }}</h1>
                <p class="destinacija">{{ ara.des_naziv }}</p>
            </div>

            <!-- Dugme -->
            <div class="dugme-container">
                <button @click="otvoriModal(ara)">Detaljnije</button>
            </div>

            <!-- Cena badge -->
            <div class="badge-cena">{{ ara.ara_cena }}€</div>
        </div>

        <h1 v-else style="color: #254878; font-family: 'Kanit', sans-serif; font-size: 30px; text-align: center;"><strong>
                Trenutno nemamo aranžmane u tom periodu!<br>
                Žao nam je!
            </strong>
        </h1>

    </div>
    <!-- Modal statički prikaz -->
    <div class="modal-overlay" v-if="prikaziModal" @click="prikaziModal = false">
        <div class="modal-content" @click.stop>
            <h2>{{ selektovaniAranzman.des_naziv }}</h2>
            <p><strong>{{ selektovaniAranzman.drz_naziv }}</strong></p>
            <p><strong>{{ selektovaniAranzman.ara_opis }}</strong></p>
            <p><strong>Cena:</strong> {{ selektovaniAranzman.ara_cena }}€</p>
            <p><strong>Polazak:</strong> {{ formatirajVreme(selektovaniAranzman.ara_od) }}h</p>
            <p><strong>Povratak:</strong> {{ formatirajVreme(selektovaniAranzman.ara_do) }}h</p>

            <button id="zakazidugme" @click="zakaziDugme(selektovaniAranzman.ara_id)">Zakaži</button>
            <button class="modal-close-btn" @click="prikaziModal = false">×</button>
            <button id="slikedugme" @click="prikaziSlike(selektovaniAranzman.obj_id)">Prikaži slike</button>
            <button @click="otvoriModalKom()">Dodaj komentar</button>

            <div class="forma-komentar" v-if="prikaziModalKom">
                <h3>Unesite komentar</h3>
                <label>Ocena: </label>
                <select v-model="ocena">
                    <option value="5">5 - Odlično</option>
                    <option value="4">4 - Vrlo dobro</option>
                    <option value="3">3 - Dobro</option>
                    <option value="2">2 - Slabo</option>
                    <option value="1">1 - Loše</option>
                </select><br><br>
                <textarea rows="4" v-model="komentar" placeholder="Unesite komentar..."></textarea>
                <button @click="posaljiKomentar()">Pošalji</button><br>
                <button id="otkaziKomBtn" @click="prikaziModalKom = false">Otkazi komentar</button>
            </div>

            <div class="komentari-blok">
                <div v-if="komentari.length === 0">
                    <h4>Nema komentara!</h4>
                </div>
                <div v-else>
                    <div class="komentar" v-for="kom in komentari">
                        <p><strong>Korisnik:</strong> {{ kom.kor_imeiprezime }}</p>
                        <p><strong>Ocena:</strong> {{ kom.kom_ocena }}</p>
                        <p><strong>Tekst:</strong> {{ kom.kom_tekst }}</p>
                        <small><strong>Datum:</strong> {{ formatirajVreme(kom.kom_datum) }}h</small>
                    </div>      
                </div>

            </div>
        </div>
    </div>

    <div class="ukrasnidiv">
        <img src="https://upload.wikimedia.org/wikipedia/commons/7/71/Earth_icon_2.png">
        <p>Isplaniraj nezaboravno putovanje na vreme i ugrabi najbolje 
            destinacije po najpovoljnijim cenama! Bilo da sanjaš o čarobnim plažama Santorinija, 
            ludim noćima na Ibizi, imamo destinaciju za tebe! 
            U ponudi su još Saint Lucia i New York.</p>
    </div>

    <div class="footer-footer">
        <div class="footer-content">
            <h1 class="footer-text" style="font-family: 'Kanit', sans-serif; font-weight: 5px;">Sa nama se i završava!</h1>
            <p class="footer-copy" style="color: white">&copy; 2025 Putovanja d.o.o. Sva prava zadržana.</p>
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
    
    import { ref, onMounted } from 'vue';

    const drzave = ref([]);
    const destinacije = ref([]);
    const selektovanaDrzava = ref('');
    const selektovanaDestinacija = ref('');
    const selektovaniAranzman = ref(null);
    const aranzmani = ref([]);
    const prikaziModal = ref(false);
    const prikaziModalKom = ref(false);

    function ucitajDrzave() {
        get('/api/drzave', (data) => {
            drzave.value = data.data;
        });
    }

    function ucitajDestinacije(drz_id) {
        if(!drz_id) {
            destinacije.value = [];
            return;
        }     
        get("/api/destinacije", { drz_id: drz_id }, (data) => {
            destinacije.value = data.data;
        })
    }
    
    onMounted(() => {
        ucitajDrzave();
        ucitajAranzmane();
    });
    
    function resetuj() {
        selektovanaDrzava.value = "";
        selektovanaDestinacija.value = "";
        selektovaniDatum.value = undefined;
        ucitajAranzmane();
    }
    
    function ucitajAranzmane() {
        if(!selektovanaDrzava.value && !selektovanaDestinacija.value) {
            get("/api/aranzmani", {}, (data) => {
                aranzmani.value = data.data;
            })
        }
        else {
            selektovaniDatum.value = undefined;
            get("/api/aranzmani",
            {   
                drz_id: selektovanaDrzava.value || undefined, 
                des_id: selektovanaDestinacija.value || undefined
            }, 
            (data) => {
                aranzmani.value = data.data;
            })
        }
    }
    
    const selektovaniDatum = ref();
    
    function datumSelekt() {
        if(selektovaniDatum.value) {
            selektovanaDestinacija.value = "";
            selektovanaDrzava.value = "";
            get("/api/aranzmanidatum", { ara_od: selektovaniDatum.value }, (data) => {
                aranzmani.value = data.data;
            })
        }
    }

    function promeniDrzavu() {
        ucitajDestinacije(selektovanaDrzava.value); // učitaj destinacije za izabranu državu
        selektovanaDestinacija.value = ''; // resetuj selekciju grada
        ucitajAranzmane(); // učitaj aranžmane prema novom filteru
    }
    
    function otvoriModal(ara) {
        selektovaniAranzman.value = ara;
        prikaziModal.value = true;
        ucitajKomentare(); // Učitaj komentare samo za tu destinaciju
    }
    
    function otvoriModalKom() {
        if(user.value.kor_id) {
            prikaziModalKom.value = true;
        }
        else {
            showToast("Morate biti prijavljeni!", "error");
            router.push("/ulogujse");
        }
    }
    
    const komentar = ref("");
    const ocena = ref(5);
    function posaljiKomentar() {
    if (!komentar.value.trim()) return;

        post('/api/dodajkomentar', {
            des_id: selektovaniAranzman.value.des_id,
            obj_id: selektovaniAranzman.value.obj_id,
            kom_ocena: ocena.value,
            kom_tekst: komentar.value
            }, (res) => {
                if (res.res === 'OK') {
                    showToast("Komentar uspešno dodat!", "success");
                    komentar.value = "";
                    ocena.value = 5;
                    prikaziModalKom.value = false;
                    ucitajKomentare(); // osveži komentare
                } else {
                    alert(res.msg);
                }
        });
    }
    
    const komentari = ref([]);
    function ucitajKomentare() {
    if (!selektovaniAranzman.value) return;
        get("/api/komentari", { obj_id: selektovaniAranzman.value.obj_id }, (data) => {
            komentari.value = data.data;
        });
    }
   
    const slike = ref([]);
    function prikaziSlike(obj_id) {
        router.push("/galerija?obj_id=" + obj_id);        
    }
    
    const user = ref({});
    const jeUlogovan = ref(false);
    
    onMounted(() => {
        get("/api/logincheck", (data) => {
            if(data.res) {
                user.value = data.data;
            }
        })
    })   
    
    function zakaziDugme(ara_id) {
        if (user.value.kor_id) {
            router.push("/zakazivanje?ara_id=" + ara_id);
        } else {
            router.push("/ulogujse");
        }
    }
    
    const omiljeniObjekti = ref([]) // lista obj_id

    function jeOmiljeni(obj_id) {
        return omiljeniObjekti.value.includes(obj_id);
    }
    
    function toggleOmiljeni(des_id, obj_id) {
        if (jeOmiljeni(obj_id)) {
            post("/api/ukloniomiljene", { obj_id: obj_id }, (data) => {
                if (data.res === "OK") {
                    const index = omiljeniObjekti.value.indexOf(obj_id);
                    if (index !== -1) {
                        omiljeniObjekti.value.splice(index, 1);
                    }
                } else {
                    alert(data.msg);
            }
        });        } else {
            post("/api/dodajomiljene", {
                obj_id: obj_id,
                des_id: des_id
            }, (data) => {
            if (data.res == "OK") {
                omiljeniObjekti.value.push(obj_id);
            } else if(data.res === "ERRLOG") {
                showToast(data.msg, "error");
                router.push("/ulogujse");
            }
            else {
                alert(data.msg);
            }
            });
        }
    }
    
    onMounted(() => {
        get('/api/omiljene', (data) => {
            if (data.res === 'OK') {
                omiljeniObjekti.value = [];
                data.data.forEach(function(e) {
                    omiljeniObjekti.value.push(e.obj_id);
                });
            } else {
                alert(data.msg);
            }
        });
    });
    
    function formatirajVreme(isoString) {
        let date = new Date(isoString); // "2025-10-12T13:00"

        // Konvertuj u lokalno vreme (Srbija)
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

    #otkaziKomBtn {
        background-color: red;
    }

    #slikedugme {
        margin: 10px;
        background-color: #00a8ff;
    }
    #slikedugme:hover {
        background-color: #007bb5;
    }

    .bg-blur {
        filter: blur(4px);
    }

    .hero-image {
        position: relative;
        width: 100%;
        height: 50vh;
        overflow: hidden;
    }

    .bg-blur {
        width: 100%;
        height: 100%;
        object-fit: cover;
        filter: blur(5px);
        position: absolute;
        top: 0;
        left: 0;
        z-index: 1;
    }

    .overlay-content {
        position: relative;
        z-index: 2;
        height: 100%;                /* zauzima celu visinu hero-image */
        display: flex;
        flex-direction: column;
        justify-content: center;     /* centrirano po visini unutar 50vh */
        align-items: center;         /* centrirano po širini */
        color: white;
        text-align: center;
        padding: 10px;
    }

    .search-form {
        display: flex;
        gap: 15px;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;
        background: linear-gradient(180deg, #4A90E2 0%, #1A3D7C 100%),
            linear-gradient(0deg, #EFF2F9, #EFF2F9);
        padding: 7px 40px;
        border-radius: 40px;
        opacity: 0.8;
    }

    #h1maintext {
        text-shadow:
            0 0 5px #000,
            0 0 10px #000,
        0 0 20px #000;

        font-size: 35px;
        color: white;
        position: relative;
        animation: glow 2s infinite alternate ease-in-out;
    }

    @keyframes glow {
        0% {
            text-shadow:
                0 0 5px #000,
                0 0 10px #000,
                0 0 20px #000;
            transform: scale(1);
        }
        100% {
            text-shadow:
                0 0 10px #000,
                0 0 20px #000,
                0 0 40px #000;
            transform: scale(1.1);
        }
    }

    .search-form select {
        padding: 10px 15px;
        border-radius: 5px;
        border: none;
        font-size: 16px;
    }

    .search-form select {
        min-width: 160px;
    }

    .search-form button {
        background-color: #5f9e21;
        color: black;
        cursor: pointer;
        transition: background-color 0.3s ease;
        margin: 10px 10px;

        border-radius: 15px;
        font-size: 10px;
    }

    .search-form button:hover {
        background-color: #4b7f1a;
    }

    #h1ponude {
        padding-top: 10px;
        text-align: center;
        color: black;
        font-size: 25px;
        font-family: "Poppins", sans-serif;
    }

    #h2ponude {
        text-align: center;
        color: black;
        font-size: 15px;
        font-family: "Poppins", sans-serif;
        padding-bottom: 15px;
    }

    .destinations {
        display: flex;
        flex-wrap: wrap;
        gap: 1.5rem;       /* malo veći razmak između kartica */
        justify-content: center;
        margin-top: 30px;
    }

    .destination-card {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 320px;                     /* povećano sa 300px */
        background: #fff;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        text-align: center;
        cursor: pointer;
        transition: transform 0.4s ease, box-shadow 0.4s ease;
        min-height: 450px;  /* veća kartica */
        width: 323px;       /* širina po želji */
    }

    /* Efekat na hover */
    .destination-card:hover {
        transform: scale(1.05) translateY(-6px);   /* malo veće uvećanje i podizanje */
        box-shadow: 0 10px 25px rgba(0,0,0,0.25); /* jača senka */
    }

    .image-wrapper {
        height: 80%;  /* povećano sa npr. 70% ili 75% */
        width: 100%;
        overflow: hidden;
    }

    /* Efekat slike na hover da malo „zooma“ */
    .destination-card:hover .image-wrapper img {
        transform: scale(1.05);
        transition: transform 0.3s ease;
    }

    .image-wrapper img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .image-wrapper img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
        border-top-left-radius: 12px;
        border-top-right-radius: 12px;
    }

    .info {
        padding: 0.5rem 1rem;
        height: 15%; /* Smanjeno da bi stao ostatak */
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-content: center;
        font-size: 0.95rem;
        align-items: center;
        text-align: center;
        margin-top: 10px;

    }

    .opis {
        color: #555;
        font-size: 0.85rem;
        overflow: hidden;
        text-overflow: ellipsis;
        flex-grow: 1;
        margin-top: 5px;
    }

    .drzava {
        font-size: 1.1rem;
        font-weight: bold;
        margin-bottom: 3px;
        color: black;
    }

    .destinacija {
        font-weight: 500;
        color: #333;
    }

    .dugme-container {
        margin-top: auto;
        width: 100px;
        display: flex;
        align-self: center;
        margin-bottom: 20px;
    }

    .dugme-container button {
        background-color: #0077cc;
        color: white;
        border: none;
        padding: 0.5rem 1rem;
        border-radius: 8px;
        cursor: pointer;
        width: 100%;
        font-weight: bold;
        transition: background-color 0.3s;
    }

    .dugme-container button:hover {
        background-color: #005fa3;
    }

    /* Cena u gornjem desnom uglu */
    .badge-cena {
        position: absolute;
        top: 10px;
        right: 10px;
        background: red;
        color: white;
        padding: 0.4rem 0.7rem;
        border-radius: 8px;
        font-size: 0.95rem;
        font-weight: bold;
        z-index: 2;
    }


    /* Donji sadržaj kartice - ispod slike */
    .destination-card .content {
        padding: 15px;
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        text-align: center;
    }

    /* Naslovi i tekstovi */
    .destination-card h2 {
        font-size: 20px;
        margin: 10px 0 5px;
        color: #333;
    }

    .destination-card p {
        font-size: 14px;
        color: #666;
        margin: 4px 0;
        flex-grow: 1; /* Da tekst popuni prostor pre dugmeta */
    }

    /* Dugme */
    .destination-card button {
        margin-top: 10px;
        padding: 8px 20px;
        background-color: #4f80ff;
        border: none;
        border-radius: 20px;
        color: white;
        font-weight: 600;
        font-size: 14px;
        cursor: pointer;
        user-select: none;

        transition: transform 0.3s ease, box-shadow 0.3s ease;
        transform-origin: center top;  /* hover povećava od vrha, ne pomera ikonu */

    }

    .destination-card button:hover {
        background-color: #3a5ec8;
    }

    .img-container {
        height: 60%;  /* Slika zauzima 60% visine kartice */
        overflow: hidden;
    }

    .img-container img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
    }

    .content {
        padding: 15px 20px;
        height: 40%;  /* Ostatak za tekst i dugme */
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    .content h2 {
        font-size: 18px;
        margin: 0 0 5px 0;
        color: #333;
    }

    .content p {
        font-size: 14px;
        color: #666;
        margin: 3px 0;
        flex-grow: 1;  /* Da opis zauzme prostor ako je duži */
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .content button {
        margin-top: 10px;
        padding: 10px 15px;
        background-color: #4f80ff;
        border: none;
        border-radius: 20px;
        color: white;
        font-weight: 600;
        font-size: 14px;
        cursor: pointer;
        user-select: none;
        transition: background-color 0.3s ease;
    }

    .content button:hover {
        background-color: #3a5ec8;
    }

    .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background-color: rgba(0, 0, 0, 0.6);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    }

    .modal-content {
        position: relative;
        background-color: white;
        padding: 30px;
        border-radius: 15px;
        width: 90%;
        max-width: 500px;
        text-align: center;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    }

    .modal-content h2 {
        margin-bottom: 10px;
        color: #4f80ff;
    }

    .modal-content p {
        margin: 8px 0;
        color: #333;
    }

    .modal-content button {
        margin-top: 20px;
        padding: 10px 20px;
        background-color: #b33d3d;
        border: none;
        border-radius: 8px;
        color: white;
        font-weight: bold;
        cursor: pointer;
    }

    .modal-content button:hover {
        background-color: #851515;
    }

    #zakazidugme {
        margin: 10px;
        background-color: #4f80ff;
    }

    #zakazidugme:hover {
        background-color: #3967d1;
    }

    .komentari-blok {
        background-color: #f2f2f2;
        padding: 10px;
        border-radius: 8px;
        margin: 10px;
        text-align: left;
    }

    .komentar {
        border-top: 1px solid #ccc;
        padding: 5px 0;
    }

    .komentar:first-child {
        border-top: none;
    }

    .forma-komentar {
        margin-top: 15px;
        background-color: #f9f9f9;
        padding: 10px;
        border-radius: 10px;
    }

    .forma-komentar input,
    .forma-komentar textarea {
        width: 100%;
        padding: 8px;
        margin-bottom: 10px;
        border-radius: 5px;
        border: 1px solid #ccc;
    }

    .forma-komentar button {
        background-color: #5f9e21;
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 6px;
        cursor: pointer;
    }

    .modal-close-btn {
        position: absolute;
        top: 10px;
        right: 15px;
        background: none;
        border: none;
        font-size: 28px;
        font-weight: bold;
        color: #555;
        cursor: pointer;
        z-index: 10;
    }

    .modal-close-btn:hover {
        color: #d00;
    }

    .footer-footer {
        position: relative;
        height: 200px;
        margin-top: 50px;
        overflow: hidden;
    }

    .footer-footer::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-image: url('slike/slikazaregister.jpg');
        background-size: cover;
        background-position: center;
        filter: blur(4px);
        transform: scale(1.05);
        z-index: 0;
    }

    .footer-text {
        position: relative;
        z-index: 1;
        color: white;
        font-size: 30px;
        font-weight: bold;
        text-align: center;
        padding-top: -10px;
        text-shadow: 1px 1px 6px rgba(0, 0, 0, 0.7);
    }

    .footer-content {
        position: relative;
        z-index: 1;
        color: white;
        text-align: center;
        padding-top: 40px;
    }

    .footer-copy {
        margin-top: 30px;
        font-size: 14px;
        opacity: 0.8;
    }

    .star-rating {
        font-size: 24px;
        color: #ccc;
        cursor: pointer;
        user-select: none;
        margin-bottom: 10px;
    }

    .star {
        padding: 0 5px;
        transition: color 0.3s;
    }

    .star:hover,
    .star:hover ~ .star {
        color: #ffc107;
    }

    .active-star {
        color: #ffc107;
    }

    .favorite-icon {
        position: absolute;
        top: 10px;
        left: 10px;           /* gornji levi ugao */
        font-size: 1.5rem;
        cursor: pointer;
        padding: 10px;
        transition: color 0.3s;
        color: white;
        z-index: 5;            /* dovoljno visoko da bude iznad slike i transform efekta */
    }

    .favorite-icon:hover {
        color: red;
    }

    .pi-heart-fill {
        color: red !important;
    }

    .ukrasnidiv {
        width: auto;
        max-width: 1000px;   /* 👈 možeš menjati širinu po želji */
        height: auto;        /* da raste sa tekstom */
        border-radius: 20px;
        box-shadow: 0 2px 15px rgba(0,0,0,0.5);
        margin: 70px 165px;
        display: flex;              /* 👈 postavi flex */
        align-items: center;        /* vertikalno centriraj sliku i tekst */
        gap: 20px;                  /* razmak između slike i teksta */
        padding: 20px;              /* unutrašnji razmak */
        background: linear-gradient(180deg, #2E2D5B 0%, #081A2F 100%), linear-gradient(0deg, #EFF2F9, #EFF2F9);
        color: white; /* da tekst bude čitljiv */
    }

    .ukrasnidiv img {
        height: 50px;
        flex-shrink: 0; /* da slika ne menja veličinu */
        margin-left: 20px; /* 👈 ako baš hoćeš 50px od leve ivice, ovde kontrolišeš */
        margin-right: 20px;
    }

    .ukrasnidiv p {
        margin: 0;
        line-height: 1.5;
        font-size: 16px;
        color: white;
        margin-right: 20px;
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