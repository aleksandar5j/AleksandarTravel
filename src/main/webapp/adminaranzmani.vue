<template>
    <div class="container" v-if="userAdmin">
        <h1>Svi Aranžmani</h1>
        <div class="center-plus">
            <i class="pi pi-plus plus-icon" @click="dodajAranzman" title="Dodaj aranžman">
                Dodaj aranžman
            </i>
        </div>

        <table class="aranzmani-tabela">
            <thead>
                <tr>
                    <th>ID Aranzman</th>
                    <th>ID Objekat</th>
                    <th>Od</th>
                    <th>Do</th>
                    <th>Cena</th>
                    <th>Opis</th>
                    <th>Akcije</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="ara in aranzmani" :key="ara.ara_id">
                    <td>{{ ara.ara_id }}</td>
                    <td>{{ ara.obj_id }}</td>
                    <td>{{ formatirajVreme(ara.ara_od) }}</td>
                    <td>{{ formatirajVreme(ara.ara_do) }}</td>
                    <td>{{ ara.ara_cena }}</td>
                    <td>{{ ara.ara_opis }}</td>
                    <td>
                        <i class="pi pi-pencil action-icon" @click="izmeniAranzman(ara)"></i>
                        <i class="pi pi-trash action-icon" @click="obrisiAranzman(ara)"></i>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="modal" v-if="modalAranzmanIzmeni" @click.self="zatvoriModal">
            <form @submit.prevent="submit">
                <h1>Izmeni aranzman</h1>
                <input type="text" v-model="araod" placeholder="Polazak" required />
                <input type="text" v-model="arado" placeholder="Povratak" required />
                <input type="number" v-model="aracena" placeholder="Cena" required />
                <textarea rows="4" v-model="araopis" placeholder="Opis" required></textarea>
                <button type="submit">Izmeni</button>
                <button type="button" @click="zatvoriModal" id="redbtn">Otkaži</button>
            </form>
        </div>

        <div class="modal" v-if="modalAranzmanDodaj" @click.self="zatvoriModal">
            <form @submit.prevent="submitt">
                <h1>Dodaj aranzman</h1>
                <input type="number" v-model="objid" placeholder="Objekat ID" required />
                <input type="text" v-model="araod" placeholder="Polazak" required />
                <input type="text" v-model="arado" placeholder="Povratak" required />
                <input type="number" v-model="aracena" placeholder="Cena" required />
                <textarea rows="4" v-model="araopis" placeholder="Opis" required></textarea>
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

    const aranzmani = ref([]);
    
    function ucitajAranzmane() {
        get("/api/aranzmani", {}, (data) => {
            aranzmani.value = data.data;
        });
    }
    
    
    const ara_id = ref(null);
    const objid = ref("");
    const araod = ref("");
    const arado = ref("");
    const aracena = ref("");
    const araopis = ref("");
    
    const modalAranzmanIzmeni = ref(false);
    const modalAranzmanDodaj = ref(false);
    
    function dodajAranzman() {
        objid.value = "";
        araod.value = "";
        arado.value = "";
        aracena.value = "";
        araopis.value = "";

        modalAranzmanDodaj.value = true;
    }

    function izmeniAranzman(ara) {
        ara_id.value = ara.ara_id;
        araod.value = ara.ara_od;
        arado.value = ara.ara_do;
        aracena.value = ara.ara_cena;
        araopis.value = ara.ara_opis;
        modalAranzmanIzmeni.value = true;
    }

    function zatvoriModal() {
        modalAranzmanIzmeni.value = false;
        modalAranzmanDodaj.value = false;
    }

    function submit() {
        post(
          "/api/adminizmeniaranzman",
          {
              ara_id: ara_id.value,
              araod: araod.value,
              arado: arado.value,
              aracena: parseInt(aracena.value),
              araopis: araopis.value,
          },
          (data) => {
                if (data.res === "OK") {
                    alert(data.msg);
                    zatvoriModal();
                    ucitajAranzmane();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
     
    function submitt() {
        post("/api/admindodajaranzman", {
            objid: parseInt(objid.value),
            araod: araod.value,
            arado: arado.value,
            aracena: parseInt(aracena.value),
            araopis: araopis.value
        }, (data) => {
            if (data.res === "OK") {
                alert("Uspešno ste dodali aranžman!");
                zatvoriModal();      
                ucitajAranzmane();
            } else {
                alert(data.msg);
            }
        });
    }
    
    function obrisiAranzman(ara) {
        if (confirm(`Da li ste sigurni da želite da obrišete aranžman ID ${ara.ara_id}?`)) {
            post("/api/adminukloniaranzman", { ara_id: ara.ara_id }, (data) => {
                if (data.res === "OK") {
                    alert("Aranžman je uspešno obrisan.");
                    ucitajAranzmane();
                } else {
                    alert(data.msg || "Greška pri brisanju");
                }
            });
        }
    }

    onMounted(() => {
        ucitajAranzmane();
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
    
    #redbtn {
        background-color: red;
    }
    
    .container {
        padding: 20px;
        overflow-x: auto;
    }

    .aranzmani-tabela {
        width: 100%;
        border-collapse: collapse;
        font-family: Arial, sans-serif;
    }

    .aranzmani-tabela th,
    .aranzmani-tabela td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: left;
    }

    .aranzmani-tabela th {
        background-color: #57b6fa;
        color: white;
        font-weight: bold;
    }

    .aranzmani-tabela tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .aranzmani-tabela tr:hover {
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

    .aranzmani-tabela td:last-child,
    .aranzmani-tabela th:last-child {
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