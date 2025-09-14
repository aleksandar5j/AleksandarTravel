<template>
    <div class="container" v-if="userAdmin">
        <h1>Sve Destinacije</h1>
        <div class="center-plus">
            <i class="pi pi-plus plus-icon" @click="dodajDestinaciju" title="Dodaj destinaciju">
                Dodaj destinaciju
            </i>
        </div>

        <table class="destinacije-tabela">
            <thead>
                <tr>
                    <th>ID Destinacija</th>
                    <th>ID Drzave - Ime drzave</th>
                    <th>Naziv destinacije</th>
                    <th>Akcija</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="des in destinacije" :key="des.des_id">
                    <td>{{ des.des_id }}</td>
                    <td>{{ des.drz_id }} - {{ des.drz_naziv }}</td>
                    <td>{{ des.des_naziv }}</td>
                    <td>
                        <i class="pi pi-trash action-icon" @click="obrisiDestinaciju(des)"></i>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="modal" v-if="modalDestinacijaDodaj" @click.self="zatvoriModal">
            <form @submit.prevent="submit">
                <h1>Dodaj destinaciju</h1>
                <input type="text" v-model="drz_id" placeholder="ID Drzave" required />
                <input type="text" v-model="des_naziv" placeholder="Naziv destinacije" required />
                <button type="submit">Dodaj</button>
                <button type="button" @click="zatvoriModal" id="redbtn">Otkaži</button>
            </form>
        </div>
    </div>
</template>

<script setup>
    
    import { ref, onMounted } from "vue";
    
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

    const destinacije = ref([]);
    
    function ucitajDestinacije() {
        get("/api/destinacije", {}, (data) => {
            destinacije.value = data.data;
        });
    }
    
    
    const des_id = ref(null);
    const drz_id = ref("");
    const des_naziv = ref("");
    
    const modalDestinacijaDodaj = ref(false);
    
    function dodajDestinaciju() {
        modalDestinacijaDodaj.value = true;
    }

    function zatvoriModal() {
        modalDestinacijaDodaj.value = false;
    }

    function submit() {
        post("/api/admindodajdestinaciju", {
            drz_id: parseInt(drz_id.value),
            des_naziv: des_naziv.value,
        }, (data) => {
            if (data.res === "OK") {
                alert("Uspešno ste dodali destinaciju!");
                drz_id.value = "";
                des_naziv.value = "";
                zatvoriModal();      
                ucitajDestinacije();
            } else {
                alert(data.msg);
            }
        });
    }
    
    function obrisiDestinaciju(des) {
        if (confirm(`Da li ste sigurni da želite da obrišete destinaciju ID ${des.des_id}?`)) {
            post("/api/adminuklonidestinaciju", { des_id: des.des_id }, (data) => {
                if (data.res === "OK") {
                    alert("Destinacija je uspešno obrisana.");
                    ucitajDestinacije(); // osveži listu posle brisanja
                } else {
                    alert(data.msg || "Greška pri brisanju");
                }
            });
        }
    }

    onMounted(() => {
        ucitajDestinacije();
    });
    
</script>

<style scoped>
    
    #redbtn {
        background-color: red;
    }
    
    .container {
        padding: 20px;
        overflow-x: auto;
    }

    .destinacije-tabela {
        width: 100%;
        border-collapse: collapse;
        font-family: Arial, sans-serif;
    }

    .destinacije-tabela-tabela th,
    .destinacije-tabela td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    .destinacije-tabela th {
        background-color: #57b6fa;
        color: white;
        font-weight: bold;
    }

    .destinacije-tabela tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .destinacije-tabela tr:hover {
        background-color: #ddd;
    }

    h1 {
        text-align: center;
        color: black;
    }

    .action-icon {
        font-size: 1.3rem;
        color: #555;
        margin: 0 10px;
        cursor: pointer;
        transition: color 0.3s ease;
    }

    .action-icon:hover {
        color: #2196F3; /* plava boja na hover */
    }

    .destinacije-tabela td:last-child,
    .destinacije-tabela th:last-child {
        text-align: center;
    }

    .center-plus {
        margin: 20px 0;
        text-align: center;
    }

    .plus-icon {
        font-size: 1.3rem;
        color: #57b6fa;
        cursor: pointer;
        padding: 8px 16px;
        border: 1px solid #57b6fa;
        border-radius: 8px;
        display: inline-block;
        transition: all 0.3s ease;
    }

    .plus-icon:hover {
        background-color: #57b6fa;
        color: white;
    }

    .modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.6); /* zatamnjenje */
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    }

    .modal form {
        background-color: white;
        padding: 2rem;
        border-radius: 12px;
        width: 100%;
        max-width: 500px;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }

    /* Stilovi za inpute i textarea */
    .modal form input,
    .modal form textarea {
        width: 100%;
        padding: 0.7rem;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 1rem;
    }

    .modal form button {
        background-color: #007bff;
        color: white;
        padding: 0.7rem;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .modal form button:hover {
        background-color: #0056b3;
    }

</style>
