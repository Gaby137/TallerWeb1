const fragment = document.createDocumentFragment();
const novedades = document.getElementById("novedades");
const apuntesDes = document.getElementById("apuntesDes");
const usuariosDes = document.getElementById("usuariosDes");
const templateCard = document.getElementById("template-card").content;
const templateCarousel = document.getElementById("template-carousel").content;

/**
 * 
 * @param {HTMLElement} mainContainer 
 * @param {*} data 
 * @param {string} idCarousel 
 * @param {number} cantidadDeElementosPorSlide 
 */
const pintarSlider = (mainContainer, data, idCarousel, cantidadDeElementosPorSlide = 4) => {
    let initialPos = 0
    let largoDeArray = data.length;
    let arraySlice; 
    let carouselClone = templateCarousel.cloneNode(true);
    
    if(data.length == 0) return;

    mainContainer.innerHTML = '';
    carouselClone.querySelector('.carousel').id = idCarousel;
    carouselClone.querySelector('.carousel-control-prev').dataTarget = `#${idCarousel}`
    carouselClone.querySelector('.carousel-control-next').dataTarget = `#${idCarousel}`
    carouselClone.querySelector('.carousel-control-prev').addEventListener('click', function(){$(`#${idCarousel}`).carousel('prev'); return;});
    carouselClone.querySelector('.carousel-control-next').addEventListener('click', function(){$(`#${idCarousel}`).carousel('next'); return;});
    
    while (initialPos < largoDeArray) {
        let carouselItem = document.createElement("div");
        let carouselItemInner = document.createElement("div");
        carouselItem.classList.add('carousel-item');
        carouselItemInner.classList.add('row', 'px-2');

        arraySlice = data.slice(initialPos, initialPos+cantidadDeElementosPorSlide);

        arraySlice.forEach((apunte) => {
            templateCard.querySelector('#card-base').classList.remove('col-3', 'col-4', 'col-6');
            templateCard.querySelector('#card-base').classList.add(`col-${12/cantidadDeElementosPorSlide}`);
            templateCard.querySelector("#apunte-nombre").textContent = apunte.nombre;
            templateCard.querySelector("#apunte-descripcion").textContent = apunte.descripcion;
            templateCard.querySelector("a").href = `/spring/detalleApunte/${apunte.id}`;
            templateCard.querySelector("#apunte-precio").textContent = apunte.precio;
            templateCard.querySelector("#apunte-promedio").textContent = apunte.promedioResenas;
        
            const clone = templateCard.cloneNode(true);
            fragment.appendChild(clone);
        });
    
        carouselItemInner.appendChild(fragment)
        if(initialPos == 0) carouselItem.classList.add('active');
        carouselItem.appendChild(carouselItemInner);

        initialPos += cantidadDeElementosPorSlide;
        carouselClone.querySelector('.carousel-inner').appendChild(carouselItem);
    }

    mainContainer.appendChild(carouselClone);
};


function pintarCarouseles() {
    if(window.innerWidth <= 768){
        pintarSlider(novedades, novedadesData, "carousel-novedadesData", 2);
        pintarSlider(apuntesDes, apuntesDesData, "carousel-apuntesDesData", 2);
        pintarSlider(usuariosDes, usuariosDesData, "carousel-usuariosDesData", 2);
    } else if(window.innerWidth < 1000){
        pintarSlider(novedades, novedadesData, "carousel-novedadesData", 3);
        pintarSlider(apuntesDes, apuntesDesData, "carousel-apuntesDesData", 3);
        pintarSlider(usuariosDes, usuariosDesData, "carousel-usuariosDesData", 3);
    } else if(window.innerWidth > 1000){
        pintarSlider(novedades, novedadesData, "carousel-novedadesData");
        pintarSlider(apuntesDes, apuntesDesData, "carousel-apuntesDesData");
        pintarSlider(usuariosDes, usuariosDesData, "carousel-usuariosDesData");
    };
}

window.onresize = () => {
  pintarCarouseles();
};

$(function() {
    pintarCarouseles();
})

