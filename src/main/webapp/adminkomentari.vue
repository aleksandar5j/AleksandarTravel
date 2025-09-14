<template>
    <div>
        <h1>Spisak svih komentara</h1>
        <table>
            <tr>
                <th>Ime i Prezime</th>
                <th>Datum</th>
                <th>Tekst</th>
                <th>Ocena</th>
                <th>Akcija</th>
            </tr>
            <tr v-for="kom in komentari" :key="kom.kom_id">
                <td>{{kom.kor_imeiprezime}}</td>
                <td>{{formatirajVreme(kom.kom_datum)}}</td>
                <td>{{kom.kom_tekst}}</td>
                <td>{{kom.kom_ocena}}</td>
                <td>
                    <i class="pi pi-trash action-icon" @click="obrisiKomentar(kom)"></i>
                </td>
            </tr>
        </table>
    </div>
</template>

<script setup>
    
    import {ref, onMounted} from 'vue';

    const komentari = ref([]);
    
    onMounted(() => {
        ucitajKomentare();
    })
    
    function ucitajKomentare() {
        get("/api/adminkomentari", {}, (data) => {
            if(data.res === "OK") {
                komentari.value = data.data;
            }
            else {
                alert(data.msg);
            }
        })
    }

    function obrisiKomentar(kom) {
        if (confirm("Da li ste sigurni da zelite da izbrisete ovaj komentar?")) {
            post("/api/adminuklonikomentar", { kom_id: kom.kom_id }, (data) => {
                if(data.res === "OK") {
                    alert("Komentar obrisan!")
                    ucitajKomentare();
                }
                else {
                    alert(data.msg);
                }
            })
        }
    }
    
    onMounted(() => {
        ucitajKomentare();
    })
    
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

    h1 {
        text-align: center;
        color: #333;
        margin-bottom: 20px;
        font-family: Arial, sans-serif;
    }

    table {
        width: 80%;
        margin: 0 auto;
        border-collapse: collapse;
        font-family: Arial, sans-serif;
        background-color: #fff;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        border-radius: 8px;
        overflow: hidden;
    }

    th {
        background-color: #0077cc;
        color: white;
        padding: 12px 15px;
        text-align: center;
    }

    td {
        padding: 12px 15px;
        border-bottom: 1px solid #ddd;
        text-align: center;
    }

    tr:hover {
        background-color: #f1f1f1;
    }

    .action-icon {
        color: #cc0000;
        cursor: pointer;
        font-size: 1.2rem;
        transition: color 0.2s, transform 0.2s;
    }

    .action-icon:hover {
        color: #ff4444;
        transform: scale(1.1);
    }

</style>

