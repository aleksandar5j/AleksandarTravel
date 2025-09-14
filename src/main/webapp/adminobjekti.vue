<template>
    <div class="container" v-if="userAdmin">
        <h1>Svi Objekti</h1>
        <div class="center-plus">
            <i class="pi pi-plus plus-icon" @click="dodajObjekat" title="Dodaj objekat">
                Dodaj objekat
            </i>
        </div>

        <table class="objekti-tabela">
            <thead>
                <tr>
                    <th>ID Objekta - Naziv Destinacija</th>
                    <th>Objekat</th>
                    <th>Adresa</th>
                    <th>Telefon</th>
                    <th>Broj zvezdica</th>
                    <th>Opis</th>
                    <th>Akcije</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="obj in objekti" :key="obj.obj_id">
                    <td>{{ obj.obj_id }} - {{ obj.des_naziv }}</td>
                    <td>{{ obj.obj_naziv }}</td>
                    <td>{{ obj.obj_adresa }}</td>
                    <td>{{ obj.obj_telefon }}</td>
                    <td>{{ obj.obj_brojzvezdica }}</td>
                    <td>{{ obj.obj_opis }}</td>
                    <td>
                        <i class="pi pi-pencil action-icon" @click="izmeniObjekat(obj)"></i>
                        <i class="pi pi-trash delete-icon" @click="obrisiObjekat(obj)"></i>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="modal" v-if="modalObjekatIzmeni" @click.self="zatvoriModal">
            <form @submit.prevent="submit">
                <h1>Izmeni objekat</h1>
                <input type="text" v-model="obj_naziv" placeholder="Naziv" required />
                <input type="text" v-model="obj_adresa" placeholder="Adresa" required />
                <input type="text" v-model="obj_telefon" placeholder="Telefon" required />
                <input type="number" v-model="obj_brojzvezdica" placeholder="Broj zvezdica" required />
                <textarea rows="4" v-model="obj_opis" placeholder="Opis" required></textarea>
                <button type="submit">Izmeni</button>
                <button type="button" @click="zatvoriModal" id="redbtn">Otkaži</button>
            </form>
        </div>

        <div class="modal" v-if="modalObjekatDodaj" @click.self="zatvoriModal">
            <form @submit.prevent="submitt">
                <h1>Dodaj objekat</h1>
                <input type="number" v-model="des_id" placeholder="ID Destinacije" required />
                <input type="text" v-model="obj_naziv" placeholder="Naziv" required />
                <input type="text" v-model="obj_adresa" placeholder="Adresa" required />
                <input type="text" v-model="obj_telefon" placeholder="Telefon" required />
                <input type="number" v-model="obj_brojzvezdica" placeholder="Broj zvezdica" required />
                <textarea rows="4" v-model="obj_opis" placeholder="Opis" required></textarea>
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
                alert("NEMATE PRAVO PRISTUPA!");
                router.push("/");
            }
        })
    })
    
    const objekti = ref([]);
    
    function ucitajObjekte() {
        get("/api/objekti", {}, (data) => {
            objekti.value = data.data;
        })
    }
    
    const obj_id = ref(null);
    const des_id = ref();
    const obj_naziv = ref();
    const obj_adresa = ref();
    const obj_telefon = ref();
    const obj_brojzvezdica = ref();
    const obj_opis = ref();
    
    const modalObjekatIzmeni = ref(false);
    const modalObjekatDodaj = ref(false);
    
    function dodajObjekat() {
        obj_naziv.value = "";
        obj_adresa.value = "";
        obj_telefon.value = "";
        obj_brojzvezdica.value = "";
        obj_opis.value = "";
        modalObjekatDodaj.value = true;
    }
    
    function izmeniObjekat(obj) {
        obj_id.value = obj.obj_id;          // OVO JE KLJUČNO 
        obj_naziv.value = obj.obj_naziv;
        obj_adresa.value = obj.obj_adresa;
        obj_telefon.value = obj.obj_telefon;
        obj_brojzvezdica.value = obj.obj_brojzvezdica;
        obj_opis.value = obj.obj_opis;
        modalObjekatIzmeni.value = true;
    }
    
    function zatvoriModal() {
        modalObjekatDodaj.value = false;
        modalObjekatIzmeni.value = false;
    }
    
    function submit() {
        post("/api/adminizmeniobjekat", 
        {
            obj_id: obj_id.value,
            obj_naziv: obj_naziv.value,
            obj_adresa: obj_adresa.value,
            obj_telefon: obj_telefon.value,
            obj_brojzvezdica: obj_brojzvezdica.value,
            obj_opis: obj_opis.value
        }, (data) => {
            if(data.res === "OK") {
                alert(data.msg);
                zatvoriModal();
                ucitajObjekte();
            }
            else {
                alert(data.msg);
            }
        })
    }
    
    function submitt() {
        post("/api/admindodajobjekat", {
            des_id: des_id.value,
            obj_naziv: obj_naziv.value,
            obj_adresa: obj_adresa.value,
            obj_telefon: obj_telefon.value,
            obj_brojzvezdica: obj_brojzvezdica.value,
            obj_opis: obj_opis.value
        }, (data) => {
            if(data.res === "OK") {
                alert("Uspesno ste dodali objekat!");
                zatvoriModal();
                ucitajObjekte();
            }
            else {
                alert(data.msg);
            }
        })
    }
    
    function obrisiObjekat(obj) {
        if (confirm(`Da li ste sigurni da želite da obrišete objekat ID ${obj.obj_id}?`)) {
            post("/api/adminukloniobjekat", { obj_id: obj.obj_id }, (data) => {
                if(data.res === "OK") {
                    alert(data.msg) 
                    ucitajObjekte();
                }
                else {
                    alert(data.msg || "Greška pri brisanju");
                }
            })
        }
    }
    
    onMounted(() => {
        ucitajObjekte();
    })

</script>

<style scoped>

    #redbtn {
        background-color: red;
    }

    .container {
        padding: 20px;
        overflow-x: auto;
    }

    .objekti-tabela {
        width: 100%;
        border-collapse: collapse;
        font-family: Arial, sans-serif;
    }

    .objekti-tabela th,
    .objekti-tabela td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    .objekti-tabela th {
        background-color: #57b6fa;
        color: white;
        font-weight: bold;
    }

    .objekti-tabela tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .objekti-tabela tr:hover {
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
    
    .delete-icon {
        font-size: 1.3rem;
        color: #555;
        margin: 0 10px;
        cursor: pointer;
        transition: color 0.3s ease;
    }
    
    .delete-icon:hover {
        color: #ff4444;
        transform: scale(1.1);
    }

    .objekti-tabela td:last-child {
        display: flex;
        justify-content: center; /* centrirano po horizontali */
            /* centrirano po vertikali */
                       /* razmak između ikonica */
    }

    .objekti-tabela th:last-child {
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