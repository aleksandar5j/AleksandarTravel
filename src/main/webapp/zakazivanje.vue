<template>
    <div class="main-wrapper" v-if="aranzman"> 
        <img
            class="background-blur"
            :src="'/api/slike?des_id=' + aranzman.des_id"
            alt="Pozadinska slika"
            />

        <div class="center-wrapper">
            <div class="aranzman-card">
                <!-- ceo sadržaj kao do sada -->
                <h1><strong>{{ aranzman.des_naziv }}</strong></h1>
                <h2><strong>{{ aranzman.obj_naziv }}</strong></h2>
                <h3><strong></strong> {{ aranzman.ara_opis }}</h3>
                <p><strong>Cena:</strong> {{ aranzman.ara_cena }}€</p>
                <p><strong>Polazak:</strong> {{ formatirajVreme(aranzman.ara_od) }}</p>
                <p><strong>Povratak:</strong> {{ formatirajVreme(aranzman.ara_do) }}</p>
                <p><strong>Opis objekta:</strong> {{ aranzman.obj_opis }}</p>

                <hr>

                <table>
                    <!-- unos podataka -->
                    <tr>
                        <td>Ime i Prezime: </td>
                        <td><input type="text" v-model="imeiprezime"></td>
                    </tr>
                    <tr>
                        <td>Telefon: </td>
                        <td><input type="text" v-model="telefon"></td>
                    </tr>
                    <tr>
                        <td>Broj osoba: </td>
                        <td><input type="number" v-model="brosoba"></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center;">
                            <button @click="zakazi" class="zakazi-btn">Zakaži</button>
                        </td>
                    </tr>
                </table>

                <div v-if="popupVisible" class="popup-overlay">
                    <div class="popup-message">
                        ✅ Uspešno ste zakazali aranžman!
                    </div>
                </div>
            </div>


        </div>
        
    </div>
        <div v-else>
            Učitavanje aranžmana...
        </div>
</template>
</template>

<script setup>
    
    import { ref, onMounted } from 'vue';
    import { useRoute } from 'vue-router';

    const route = useRoute();
    const aranzman = ref(null);
    const imeiprezime = ref('');
    const telefon = ref('');
    const brosoba = ref('');

    const popupVisible = ref(false);

    function zakazi() {
        if (!imeiprezime.value || !telefon.value || !brosoba.value) {
            alert("Popunite sva polja!");
            return;
        }

      // Prikazi popup
        popupVisible.value = true;

      // Posle 2s skloni popup i idi na /
        setTimeout(() => {
            popupVisible.value = false;
            router.push('/');
        }, 2000);
    }
    
    onMounted(() => {
        const ara_id = route.query.ara_id;
        if (ara_id) {
            get("/api/aranzmani", { ara_id: ara_id }, (data) => {
                aranzman.value = data.data[0];
            });
        }
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

</script>

<style scoped>

    .main-wrapper {
        position: relative; /* dodaj ovo */
        min-height: 60vh;
        overflow: hidden; /* sprečava da blur prelazi ivice */
    }

    .center-wrapper {
        position: relative;
        z-index: 1;
        display: flex;
        justify-content: center;
        align-items: center;
        padding-bottom: 100px;
        margin-top: 100px;
    }

    .aranzman-card {
        height: auto;
        width: 600px;
        border: 2px solid #ddd;
        border-radius: 15px;

        background-color: #e8eaeb;
        box-shadow: 0 2px 15px rgba(0, 112, 255, 0.8);
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 30px 30px;
        z-index: 1;

    }

    strong {
        color: #1e80fc;
    }

    p {
        font-size: 20px;
        text-align: center;
    }

    h3 {
        color: black;
    }

    table {
        width: 90%;
        border-collapse: separate;
        border-spacing: 10px 15px;
        margin-top: 40px;
        margin-bottom: 40px;
        background-color: #f9f9f9;
        padding: 10px;
        border-radius: 12px;
    }

    td {
        font-size: 16px;
        padding: 8px;
        vertical-align: middle;
        color: #333;
    }

    input[type="text"],
    input[type="number"] {
        width: 100%;
        padding: 8px 12px;
        font-size: 16px;
        border: 1px solid #ccc;
        border-radius: 8px;
        box-sizing: border-box;
        transition: border 0.2s ease;
    }

    input:focus {
        border-color: #007bff;
        outline: none;
    }

    .zakazi-btn {
        background-color: #1e80fc;
        color: white;
        padding: 10px 20px;
        font-size: 18px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .zakazi-btn:hover {
        background-color: #1760bd;
    }

    .popup-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 9999;
    }

    .popup-message {
        background-color: #28a745;
        color: white;
        padding: 30px 50px;
        border-radius: 12px;
        font-size: 24px;
        font-weight: bold;
        box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
    }

    .background-blur {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        object-fit: cover;
        filter: blur(4px);
        z-index: 0;
        opacity: 0.8;
    }

</style>