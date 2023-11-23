INSERT INTO Usuario(id, email, password, rol, activo, puntos, codigoDeCreador) VALUES(1, 'test@unlam.edu.ar', 'test', 'ADMIN', true, 0, "AsDFgHj");
INSERT INTO Usuario(id, email, password, rol, activo, puntos, codigoDeCreador) VALUES(2, 'test2@unlam.edu.ar', 'test2', 'ADMIN', true, 0, "DqrTHsdW");
INSERT INTO Usuario(id, email, password, rol, activo, puntos, codigoDeCreador) VALUES(3, 'test3@unlam.edu.ar', 'test3', 'ADMIN', true, 0, "JklWEvTu");
INSERT INTO Apunte(id, descripcion, nombre, created_at, precio, pathArchivo) VALUES(1, 'Apunte de prueba', 'apunte test', '2023-10-13 20:24:33', 15, '/path');
INSERT INTO Resena(id, cantidadDeEstrellas, created_at, descripcion) VALUES(1, 4, '2023-10-10 20:25:22', "Muy Bueno");
INSERT INTO Resena(id, cantidadDeEstrellas, created_at, descripcion) VALUES(2, 1, '2023-10-13 15:44:33', "Muy Malo");
INSERT INTO usuario_apunte_resena(apunte_id, resena_id, usuario_id) VALUES(1, 1, 3);
INSERT INTO usuario_apunte_resena(apunte_id, resena_id, usuario_id) VALUES(1, 2, 2);