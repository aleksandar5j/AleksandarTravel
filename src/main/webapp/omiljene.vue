<template>

    <div class="omiljene-wrapper">

        <div class="background-image"></div>

        <div class="omiljene-page">
            <h1 v-if="omiljene.length > 0">Vaše omiljene destinacije</h1>
            <div v-if="omiljene.length > 0" class="kartice-wrapper">

                <div v-for="(omi, index) in omiljene" :key="omi.obj_id" class="omiljeni-karton">

                    <i class="pi pi-heart-fill favorite-icon" @click="ukloniOmiljene(omi.obj_id, index)"></i>

                    </i>


                    <div class="image-wrapper">
                        <img 
                    :src="'/api/slike?des_id=' + omi.des_id"
                            alt="Slika"
                            />
                    </div>

                    <h2>{{ omi.des_naziv }}</h2>
                    <h3>{{ omi.obj_naziv }}</h3>

                    <button @click="otvoriModal(omi)">Detaljnije</button>

                </div>
            </div>
            <div v-else>
                <h1>Nemate omiljene destinacije</h1>
            </div>
        </div>
    </div>

    <div class="modal-overlay" v-if="prikaziModal" @click="prikaziModal = false">
        <div class="modal-content"  @click.stop>
            <h2>{{ selektovaniAranzman.des_naziv }}</h2>
            <p><strong>{{ selektovaniAranzman.drz_naziv }}</strong></p>
            <p><strong>{{ selektovaniAranzman.ara_opis }}</strong></p>
            <p><strong>Cena:</strong> {{ selektovaniAranzman.ara_cena }}€</p>
            <p><strong>Polazak:</strong> {{ formatirajVreme(selektovaniAranzman.ara_od) }}h</p>
            <p><strong>Povratak:</strong> {{ formatirajVreme(selektovaniAranzman.ara_do) }}h</p>

            <div class="modal-buttons">
                <button id="zakazidugme" @click="zakaziDugme">Zakaži</button>
                <button id="slikedugme" @click="prikaziSlike(selektovaniAranzman.obj_id)" class="secondary">Prikaži slike</button>
                <button @click="otvoriModalKom()">Dodaj komentar</button>
            </div>
            <button class="modal-close-btn" @click="prikaziModal = false">×</button>

            <div class="forma-komentar" v-if="prikaziModalKom">
                <h3>Unesite komentar</h3>
                <label>Ocena:</label>
                <select v-model="ocena">
                    <option value="5">5 - Odlično</option>
                    <option value="4">4 - Vrlo dobro</option>
                    <option value="3">3 - Dobro</option>
                    <option value="2">2 - Slabo</option>
                    <option value="1">1 - Loše</option>
                </select>
                <label>Komentar:</label>
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

</template>

<script setup>
    
    import { ref, onMounted } from 'vue';

    const drzave = ref([]);
    const destinacije = ref([]);
    const selektovanaDrzava = ref('');
    const selektovanaDestinacija = ref('');
    const selektovaniAranzman = ref(null);
    const selektovanaCena = ref(null);
    const aranzmani = ref([]);
    const prikaziModal = ref(false);
    const prikaziModalKom = ref(false); 
    const omiljene = ref([]);

    onMounted(() => {
        post("/api/omiljene", {}, (data) => {
            if (data.res === "OK") {
                omiljene.value = data.data;
            } else {
                alert("Greška: " + data.msg);
            }
        });
    });
    
    function ukloniOmiljene(obj_id, index) {
        post("/api/ukloniomiljene", { obj_id }, (data) => {
            if(data.res === "OK") {
                omiljene.value.splice(index, 1);
            }
            else {
                alert(data.msg);
            }
        })
    }
    
    function otvoriModal(omi) {
        // traži aranžman koji pripada toj destinaciji
        let aranzmanZaModal = null;
        for (let a of aranzmani.value) {
            if (a.obj_id === omi.obj_id) {
                aranzmanZaModal = a;
                break; // nađemo prvi odgovarajući aranžman
            }
        }

        if (aranzmanZaModal) {
            selektovaniAranzman.value = aranzmanZaModal;
            prikaziModal.value = true;
            ucitajKomentare();
        } else {
            alert("Nema aranžmana za ovu destinaciju!");
        }
    }
    
    function otvoriModalKom() {
        prikaziModalKom.value = true;
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
                        alert("Komentar uspešno dodat!");
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
    
    function zakaziDugme() {
        if (!selektovaniAranzman.value) {
            alert("Nema selektovanog aranžmana!");
            return;
        }
        // koristi obj_id jer ara_id ne postoji
        router.push("/zakazivanje?ara_id=" + selektovaniAranzman.value.ara_id);
    }
    
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
    
    function ucitajDrzave() {
        get('/api/drzave', (data) => {
            drzave.value = data.data;
        });
    }

    function ucitajDestinacije() {
        get("/api/destinacije", {}, (data) => {
            destinacije.value = data.data;
        })
    }
    
    onMounted(() => {
        ucitajDrzave();
        ucitajAranzmane();
    });
    
    function ucitajAranzmane() {
        get('/api/aranzmani', {}, (data) => {
            aranzmani.value = data.data;
        });
        
    }
    
    
    
</script>

<style scoped>
    
    .omiljene-wrapper {
        position: relative;
        min-height: 100vh;
        overflow: hidden;
    }

    .background-image {
        position: absolute;
        top: 0;
        left: 0;
        width: 100vw;
        height: 170vh;
        background-image: url('/slike/slikazaregister.jpg'); /* putanja do tvoje slike */
        background-size: cover;
        background-position: center;
        filter: blur(8px);
        z-index: 0;
    }

    /* Suženi, centrirani sadržaj */
    .omiljene-page {
        position: relative;
        z-index: 1;
        max-width: auto;
        margin: 0 auto;
        padding: 2rem;
        font-family: Arial, sans-serif;
    }

    .kartice-wrapper {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        justify-content: center;
        margin-top: 30px;
    }

    h1 {
        text-align: center;
        color: white;
        margin-top: 50px;
        margin-bottom: 50px;
    }

    .omiljeni-karton {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 250px;       /* širina kartice */
        height: 400px;      /* visina kartice */
        background: #fff;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        text-align: center;
        cursor: pointer;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    .omiljeni-karton:hover {
        transform: scale(1.05) translateY(-5px);
        box-shadow: 0 8px 20px rgba(0,0,0,0.2);
    }

    .omiljeni-karton .image-wrapper {
        width: 100%;
        height: 60%;       /* slika zauzima 60% visine kartice */
        overflow: hidden;
        margin-bottom: 20px;
    }

    .omiljeni-karton img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 6px;
    }

    .omiljeni-karton .info {
        flex: 1;            /* zauzima ostatak kartice */
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        padding: 20px 10px;
    }

    .omiljeni-karton h3 {
        text-align: center;
        color: gray;
        margin: 3px 0;
        font-size: 15px;
    }

    .omiljeni-karton h2 {
        text-align: center;
        color: black;
        margin: 3px 0;
        font-size: 20px;
    }

    .omiljeni-karton button {
        margin: 10px auto 10px auto;
        padding: 10px 18px;
        font-size: 0.95rem;
        background-color: #4f80ff;
        border: none;
        border-radius: 20px;
        cursor: pointer;
        color: white;
        font-weight: 600;
        transition: background 0.3s ease, transform 0.2s ease;
        display: block;
        width: 50%;
    }

    .omiljeni-karton button:hover {
        background-color: #3967d1;
        transform: translateY(-2px);
    }

    .favorite-icon {
        position: absolute;
        top: 10px;
        right: 10px;
        font-size: 1.5rem;
        color: #e74c3c;
        cursor: pointer;
        z-index: 10;
        transition: transform 0.2s ease;
    }

    .favorite-icon:hover {
        transform: scale(1.2);
        color: red;
    }

    /* Modal stilovi (kopirani iz prethodnog koda) */
    .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    }

    .modal-content {
        background: #fff;
        padding: 20px 25px;
        border-radius: 12px;
        width: 90%;
        max-width: 550px;
        position: relative;
        box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        display: flex;
        flex-direction: column;
        gap: 12px;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        text-align: center;
    }

    .modal-content h2 {
        margin-bottom: 8px;
    }

    .modal-content p {
        margin: 4px 0;
    }

    .modal-buttons {
        display: flex;
        gap: 8px;
        justify-content: center;
        flex-wrap: wrap;
    }

    .modal-buttons button,
    .forma-komentar button {
        padding: 12px 18px;
        min-width: 80px;
        max-width: 140px;
        font-size: 13px;
        border-radius: 6px;
        cursor: pointer;
        font-weight: 500;
        margin: 4px 3px;
        white-space: nowrap;
        background-color: #3498db;
        color: white;
        border: none;
        transition: all 0.2s ease-in-out;
    }

    .modal-buttons button:hover,
    .forma-komentar button:hover {
        background-color: #2980b9;
        transform: translateY(-1px);
        box-shadow: 0 3px 6px rgba(0,0,0,0.2);
    }

    .modal-buttons button.secondary {
        background-color: #7f8c8d;
    }

    .modal-buttons button.secondary:hover {
        background-color: #606f73;
    }

    .modal-close-btn {
        position: absolute;
        top: 10px;
        right: 10px;
        font-size: 22px;
        background: transparent;
        color: #333;
        cursor: pointer;
    }

    .modal-close-btn:hover {
        color: #e74c3c;
    }

    .forma-komentar {
        display: flex;
        flex-direction: column;
        gap: 8px;
        margin-top: 10px;
        text-align: center;
    }

    .forma-komentar select,
    .forma-komentar textarea {
        padding: 6px;
        border-radius: 6px;
        border: 1px solid #ccc;
        width: 100%;
        font-size: 13px;
        resize: vertical;
    }

    .forma-komentar button {
        align-self: center;
    }

    .komentari-blok {
        margin-top: 15px;
        display: flex;
        flex-direction: column;
        gap: 10px;
        text-align: left;
    }

    .komentar {
        background: #f7f7f7;
        padding: 10px 12px;
        border-radius: 10px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.08);
    }

</style>