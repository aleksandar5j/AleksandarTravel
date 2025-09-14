<template>
    <div class="gallery-wrapper">
        <h1>{{ des_naziv }}</h1>
        <h2>{{ obj_naziv }}</h2>

        <div v-if="images.length === 0" class="no-images">Nema slika za ovaj objekat.</div>

        <div v-else class="gallery-grid">
            <img
        v-for="(img, index) in images"
        :key="index"
        :src="'data:image/jpeg;base64,' + img"
                alt="Slika"
                class="gallery-image"
        @click="openModal(img)"
                />
        </div>

        <!-- Modal -->
        <div v-if="showModal" class="modal" @click.self="closeModal">
            <img :src="'data:image/jpeg;base64,' + currentImage" class="modal-image" />
            <button class="close-button" @click="closeModal">×</button>
        </div>
    </div>
</template>

<script setup>
    
    import { ref, onMounted } from 'vue';
    import { useRoute } from 'vue-router';

    const route = useRoute();
    const obj_id = route.query.obj_id;

    const images = ref([]);
    const obj_naziv = ref("");
    const des_naziv = ref("");

    onMounted(() => {
        get("/api/imglistforobj?obj_id=" + obj_id, (data) => {
            if (data.res === "OK") {
                images.value = data.data;
                obj_naziv.value = data.obj_naziv;
                des_naziv.value = data.des_naziv;
            } else {
                images.value = [];
            }
        });
    });

    const showModal = ref(false);
    const currentImage = ref(null);

    function openModal(img) {
        currentImage.value = img;
        showModal.value = true;
    }

    function closeModal() {
        showModal.value = false;
        currentImage.value = null;
    }

</script>

<style scoped>
    .gallery-wrapper {
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px;
        text-align: center;
    }

    .gallery-grid {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 20px;
    }

    .gallery-image {
        width: 370px;
        height: 300px;
        object-fit: cover;
        border-radius: 12px;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    .gallery-image:hover {
        transform: scale(1.05);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    }

    h1, h2 {
        color: #222;
        margin-bottom: 10px;
    }

    .no-images {
        font-style: italic;
        color: gray;
        margin-top: 30px;
    }

    .modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.8);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 9999;
    }

    .modal-image {
        max-width: 90%;
        max-height: 80vh;
        border-radius: 12px;
        box-shadow: 0 0 20px rgba(255, 255, 255, 0.3);
        transition: transform 0.3s ease;
    }

    .close-button {
        position: absolute;
        top: 30px;
        right: 40px;
        font-size: 2rem;
        color: white;
        background: none;
        border: none;
        cursor: pointer;
    }
</style>