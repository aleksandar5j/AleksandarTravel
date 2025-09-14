<template>
    <div class="container" v-if="userAdmin">
        <h1>Sve Drzave</h1>
        <div class="center-plus">
            <i class="pi pi-plus plus-icon" @click="dodajDrzavu()" title="Dodaj drzavu">
                Dodaj drzavu
            </i>
        </div>

        <table class="drzave-tabela">
            <thead>
                <tr>
                    <th>ID Drzave</th>
                    <th>Naziv drzave</th>
                    <th>Kod drzave</th>
                    <th>Akcija</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="drz in drzave" :key="drz.drz_id">
                    <td>{{ drz.drz_id }}</td>
                    <td>{{ drz.drz_naziv }}</td>
                    <td>{{ drz.drz_kod }}</td>
                    <td>
                        <i class="pi pi-trash action-icon" @click="obrisiDrzavu(drz)"></i>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="modal" v-if="modalDrzavaDodaj" @click.self="zatvoriModal">
            <form @submit.prevent="submit">
                <h1>Dodaj drzavu</h1>
                <input type="text" v-model="drz_naziv" placeholder="Naziv drzave" required />
                <input type="text" v-model="drz_kod" placeholder="Kod drzave npr. (Srbija - RS)" required />
                <button type="submit">Dodaj</button>
                <button type="button" @click="zatvoriModal" id="redbtn">Otkaži</button>
            </form>
        </div>
    </div>
</template>

<script setup>
    
    import { ref, onMounted } from 'vue';
    
    const userAdmin = ref(false);
    
    onMounted(() => {
        get("/api/logincheck", {}, (data) => {
            if(data.res && data.data?.kor_jeadmin === true) {
                userAdmin.value = true;
            }
            else {
                userAdmin.value = false;
                alert("NEMATE PRAVO PRISTUPA");
                router.push("/");
            }
        });
    });
    
    const drzave = ref([]);
    
    function ucitajDrzave() {
        get("/api/drzave", {}, (data) => {
            drzave.value = data.data;
        });
    };
    
    const drz_id = ref(null);
    const drz_naziv = ref("");
    const drz_kod = ref("");
    
    const modalDrzavaDodaj = ref(false);
    
    function dodajDrzavu() {
        modalDrzavaDodaj.value = true;
    }
    
    function zatvoriModal() {
        modalDrzavaDodaj.value = false;
    }
    
    function submit() {
        post("/api/admindodajdrzavu", { drz_naziv: drz_naziv.value, drz_kod: drz_kod.value }, (data) => {
            if(data.res === "OK") {
                alert("Uspesno ste dodali drzavu!");
                drz_naziv.value = "";
                drz_kod.value = "";
                zatvoriModal();
                ucitajDrzave();
            }
            else {
                alert(data.msg);
            }
        })
    };
    
    function obrisiDrzavu(drz) {
        if (confirm(`Da li ste sigurni da želite da obrišete drzavu ID ${drz.drz_id}?`)) {
            post("/api/adminuklonidrzavu", {drz_id: drz.drz_id}, (data) => {
                if(data.res === "OK") {
                    alert("Drzava je uspešno obrisana.");
                    ucitajDrzave();
                }
                else {
                    alert(data.msg || "Greška pri brisanju");
                }
            });
        }
    }
    
    onMounted(() => {
        ucitajDrzave();
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

    .drzave-tabela {
        width: 100%;
        border-collapse: collapse;
        font-family: Arial, sans-serif;
    }

    .drzave-tabela-tabela th,
    .drzave-tabela td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    .drzave-tabela th {
        background-color: #57b6fa;
        color: white;
        font-weight: bold;
    }

    .drzave-tabela tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .drzave-tabela tr:hover {
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

    .drzave-tabela td:last-child,
    .drzave-tabela th:last-child {
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
