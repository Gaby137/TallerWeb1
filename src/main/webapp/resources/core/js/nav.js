function copiarAlPortapapeles() {
    var codigoDeCreadorElement = document.querySelector('.codigo-de-creador');
    var codigoDeCreador = codigoDeCreadorElement.innerText;

    navigator.clipboard.writeText(codigoDeCreador)
        .then(function() {
            alert('Codigo copiado al portapapeles');
        })
        .catch(function(err) {
            console.error('Error al copiar al portapapeles: ', err);
        });
}