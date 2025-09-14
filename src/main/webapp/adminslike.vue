<template>
    <div v-if="userAdmin">
        <h1>Sve slike</h1>
        <div class="center-plus">
            <i class="pi pi-plus plus-icon" @click="dodajSliku" title="Dodaj sliku">
                 Dodaj sliku
            </i>
        </div>
        <table class="slike-tabela">
            <thead>
                <tr>
                    <th>ID Slike</th>
                    <th>ID Objekta</th>
                    <th>ID Destinacije</th>
                    <th>Slika</th>
                    <th>Akcija</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="sli in slike" :key="sli.sli_id">
                    <td>{{ sli.sli_id }}</td>
                    <td>{{ sli.obj_id }}</td>
                    <td>{{ sli.des_id }}</td>
                    <td>
                        <img :src="`/api/slika?id=${sli.sli_id}`" alt="Slika" />
                    </td>
                    <td>
                        <i class="pi pi-trash action-icon" @click="obrisiSliku(sli)"></i>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="modal" v-if="prikaziModal" @click.self="zatvoriModal">
            <h1>Upload slika</h1>
            <div>
                <label for="objid">ID Objekta:</label>
                <input id="objid" type="number" v-model="objid" />
            </div>
            <div>
                <label for="desid">ID Destinacije:</label>
                <input id="desid" type="number" v-model="desid" />
            </div>
            <div>
                <input type="file" @change="onFileChange" />
            </div>
            <button @click="send()">Upload</button>
        </div>
    </div>
</template>

<script setup>
    
    import { ref, onMounted } from 'vue';

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

    const slike = ref([]);
    const prikaziModal = ref(false); // ili false, zavisi kad želiš da se prikazuje modal
    const objid = ref('');
    const desid = ref('');
    const file = ref(null);

    function onFileChange(event) {
      const files = event.target.files;
      if (files.length > 0) {
        file.value = files[0];
      } else {
        file.value = null;
      }
    }

    function ucitajSlike() {
      get('/api/adminslike', {}, (data) => {
        if (data.res === 'OK') {
          slike.value = data.slike || data.data || []; // proveri šta vraća backend
        } else {
          alert('Greška pri učitavanju slika');
        }
      });
    }

    function send() {
      if (!file.value) {
        alert('Morate izabrati sliku!');
        return;
      }
      if (!objid.value || !desid.value) {
        alert('Morate uneti ID objekta i ID destinacije!');
        return;
      }

      postMP(
        '/api/imagesadd',
        {
          image: file.value,
          obj_id: objid.value,
          des_id: desid.value,
        },
        (data) => {
          if (data.res === 'OK') {
            alert('Uspešno dodata slika!');
            objid.value = '';
            desid.value = '';
            file.value = null;
            ucitajSlike();
          } else {
            alert('Greška: ' + (data.msg || data.message || 'Nepoznata greška'));
          }
        }
      );
    }

    onMounted(() => {
      ucitajSlike();
    });

    function dodajSliku() {
        prikaziModal.value = true;
    }

    function zatvoriModal() {
        prikaziModal.value = false;
    }

    function obrisiSliku(sli) {
         if (confirm(`Da li ste sigurni da želite da obrišete ovu sliku?`)) {
             post("/api/adminuklonisliku", { sli_id: sli.sli_id }, (data) => {
                if(data.res === "OK") {
                    alert(data.msg);
                    ucitajSlike();
                }
                else {
                    alert(data.msg);
                }
             })
         }
    }
    
</script>

<style scoped>

    .slike-tabela {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 20px;
        font-family: Arial, sans-serif;
        box-shadow: 0 2px 8px rgb(0 0 0 / 0.1);
        border-radius: 8px;
        overflow: hidden;
    }

    .slike-tabela thead {
        background-color: #1976d2;
        color: white;
        text-align: left;
    }

    .slike-tabela th,
    .slike-tabela td {
        padding: 12px 15px;
        border-bottom: 1px solid #ddd;
        text-align: center;
    }

    .slike-tabela tbody tr:hover {
        background-color: #f1f9ff;
        cursor: pointer;
    }

    .action-icon {
        color: #e53935;
        font-size: 1.2rem;
        cursor: pointer;
        transition: color 0.3s ease;
    }

    .action-icon:hover {
        color: #b71c1c;
    }

    h1 {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        margin-bottom: 15px;
        color: #333;
    }

    div[v-if] > div {
        margin-bottom: 15px;
        display: flex;
        flex-direction: column;
        max-width: 300px;
    }

    label {
        font-weight: 600;
        margin-bottom: 6px;
        color: #555;
    }

    input[type="number"],
    input[type="file"] {
        padding: 8px 10px;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 1rem;
        transition: border-color 0.3s ease;
    }

    input[type="number"]:focus,
    input[type="file"]:focus {
        outline: none;
        border-color: #1976d2;
        box-shadow: 0 0 4px #1976d2aa;
    }

    button {
        background-color: #black;
        color: black;
        border: none;
        padding: 10px 18px;
        border-radius: 6px;
        font-size: 1rem;
        font-weight: 600;
        cursor: pointer;
        transition: background-color 0.3s ease;
        max-width: 150px;
    }

    button:hover {
        color: white;
        background-color: #115293;
    }

    /* Responsive prilagođavanje */
    @media (max-width: 480px) {
        .slike-tabela th, .slike-tabela td {
            padding: 8px 10px;
        }

        div[v-if] > div {
            max-width: 100%;
        }
    }

    img {
        height: 150px;
    }

    .plus-icon {
        font-size: 1.3rem;
        color: #57b6fa;
        cursor: pointer;
        padding: 8px 16px;
        border: 1px solid #57b6fa;
        border-radius: 8px;
        display: flex;
        justify-self: center;
        margin-bottom: 20px;
        transition: all 0.3s ease;
    }
    
    h1 {
        text-align: center;
    }

    .plus-icon:hover {
        background-color: #57b6fa;
        color: white;
    }

    .modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background-color: rgba(0, 0, 0, 0.5);

        display: flex;
        flex-direction: column; /* da bude sadržaj jedan ispod drugog */
        justify-content: center; /* vertikalno centriranje */
        align-items: center;     /* horizontalno centriranje */

        padding: 20px; /* malo prostora od ivica ekrana */
        box-sizing: border-box;

        z-index: 10000;
        overflow-y: auto;
    }

    .modal > * {
        background-color: white;
        padding: 20px 30px;
        border-radius: 8px;
        box-shadow: 0 3px 15px rgba(0,0,0,0.3);
        width: 100%;
        max-width: 400px; /* širina sadržaja */
        margin-bottom: 15px; /* razmak između elemenata */
    }

    /* poslednji element ne treba margin-bottom */
    .modal > *:last-child {
        margin-bottom: 0;
    }

</style>

