function validarFormulario() {
    var contrasena = document.getElementById("password").value;
    var correo = document.getElementById("email").value;

    if (contrasena.length < 8 || !/[A-Z]/.test(contrasena)) {
        alert("La contraseña debe tener al menos una mayúscula y más de 7 caracteres.");
        return false;
    }

    var correoRegex = /^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
    if (!correoRegex.test(correo)) {
        alert("Por favor, ingrese una dirección de correo electrónico válida.");
        return false;
    }

    return true;
}






