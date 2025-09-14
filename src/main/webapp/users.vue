<template>
    <div>
        <h1>Spisak korisnika</h1>
        <table>
            <tr>
                <th>Korisnicko ime</th>
                <th>Ime i prezime</th>
                <th>Akcija</th>
            </tr>
            <tr v-for="kor in korisnici">
                <td>{{kor.kor_kime}}</td>
                <td>{{kor.kor_imeiprezime}}</td>
                <td>
                    <i class="pi pi-trash action-icon" @click="obrisiKorisnika(kor)"></i>
                </td>
            </tr>
        </table>
    </div>
</template>

<script setup>
    
    import {ref, onMounted} from 'vue';
    
    const userAdmin = ref(false);
    onMounted(() => {
        get("/api/logincheck", {}, (data) => {
            if(data.res && data.data?.kor_jeadmin === true) {
                userAdmin.value = true;
            }
            else {
                userAdmin.value = false;
                alert("NEMATE PRAVO PRISTUPA!");
                router.push("/");
            }
        })
    })
    
    const korisnici = ref([]);
    
    onMounted(function() {
        get("/api/korisnici", function(data) {
            korisnici.value = data.data;
        })
    })
    
    function obrisiKorisnika(kor) {
        if (confirm(`Da li ste sigurni da želite da obrišete korisnika ${kor.kor_imeiprezime}?`)) {
            post("/api/adminuklonikorisnika", { kor_id: kor.kor_id }, (data) => {
                if(data.res === "OK") {
                    alert(data.msg);
                }
                else {
                    alert(data.msg);
                }
            })
        }
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
