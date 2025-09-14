<template>
    <div class="contact-wrapper">
        <div class="background-image"></div>

        <div class="contact-page">
            <section class="map-section">
                <h2 id="mainn">Naša lokacija</h2>
                <iframe
                    class="map"
                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2830.293341692164!2d20.463173815459395!3d44.817615279098!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x475a6c811f9a5a8d%3A0x1d0b495bf2f55b1a!2sTrg%20Slobode%2012%2C%20Novi%20Sad!5e0!3m2!1ssr!2srs!4v1686773330109!5m2!1ssr!2srs"
                    allowfullscreen=""
                    loading="lazy"
                    referrerpolicy="no-referrer-when-downgrade"
                    title="Aleksandar Travel lokacija"
                    ></iframe>
            </section>

            <h1>Kontaktirajte nas</h1>
            <p id="textp">Imate pitanje? Pošaljite nam poruku ili nas kontaktirajte direktno!</p>

            <div class="contact-info" v-for="a in agencije" :key="a.age_id">
                <p class="con"><strong>Sajt:</strong> {{ a.age_sajt }}</p>
                <p class="con"><strong>Telefon:</strong> {{ a.age_telefon }}</p>
                <p class="con"><strong>Email:</strong> {{ a.age_email }}</p>
            </div>
        </div>
    </div>
</template>

<script setup>



    import { ref, onMounted } from 'vue'

    const agencije = ref([]);

    onMounted(() => {
        get("/api/agencije", {}, (data) => {
            agencije.value = data.data;
        });
    })

   
</script>

<style scoped>
    
    .contact-wrapper {
        position: relative;
        min-height: 100vh;
        overflow: hidden;
    }

    .background-image {
        position: absolute;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background-image: url('/slike/slikazaregister.jpg'); /* putanja do tvoje slike */
        background-size: cover;
        background-position: center;
        filter: blur(8px);
        z-index: 0;
    }

    /* Suženi, centrirani sadržaj */
    .contact-page {
        position: relative;
        z-index: 1;
        max-width: 700px;
        margin: 0 auto;
        padding: 2rem;
        font-family: Arial, sans-serif;
    }

    /* Ostalo isto */
    .contact-info {
        margin: 1rem 0;
        background: rgba(255, 255, 255, 0.85);
        padding: 1rem;
        border-radius: 8px;
        border: 2px solid black;
        opacity: 0.6;
        backdrop-filter: blur(4px);
        box-shadow: 0 10px 10px rgba(0,0,0,0.1);
        text-align: center;
        
    }

    h1 {
        color: white;
        text-align: center;
    }
    
    h2 {
        color: white;
        text-align: center;
    }
    
    #textp {
        color: white;
        text-align: center;
    }
    
    .con {
        color: black;
    }
    
    #mainn {
        color: white;
    }

    .map-section {
        margin-bottom: 2rem;
        text-align: center;
    }

    .map-section h2 {
        color: #4f80ff;
        margin-bottom: 1rem;
    }

    .map {
        width: 100%;
        height: 350px;
        border: 3px solid black;
        border-radius: 10px;
    }

</style>