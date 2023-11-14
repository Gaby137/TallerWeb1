function copiarAlPortapapeles() {
    var codigoDeCreadorElement = document.querySelector('.codigo-de-creador');
    var codigoDeCreador = codigoDeCreadorElement.innerText;

    // Mensaje adicional y enlace
    var textoACopiar = "¡Hola! Registrate en http://localhost:8080/spring/login con este codigo    " + codigoDeCreador + "    para obtener tus primeros puntos en el Ecommerce de apuntes de la Unlam. ";

    navigator.clipboard.writeText(textoACopiar)
        .then(function() {
            alert('Codigo y mensaje copiados al portapapeles');
        })
        .catch(function(err) {
            console.error('Error al copiar al portapapeles: ', err);
        });
}