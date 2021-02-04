
INSERT INTO users(username,password,enabled) VALUES ('admin','admin',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin','admin');



INSERT INTO tipo_Trabajador VALUES (1,'Administrador');
INSERT INTO tipo_Trabajador VALUES (2,'Taxista');


INSERT INTO users(username,password,enabled) VALUES ('taxista1','taxista1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'taxista1','taxista');

INSERT INTO users(username,password,enabled) VALUES ('roberta1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'roberta1','taxista');


INSERT INTO users(username,password,enabled) VALUES ('paco1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'paco1','taxista');


INSERT INTO users(username,password,enabled) VALUES ('sonia1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'sonia1','taxista');


INSERT INTO users(username,password,enabled) VALUES ('alberto1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (12,'alberto1','taxista');


INSERT INTO users(username,password,enabled) VALUES ('sara1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (13,'sara1','taxista');


INSERT INTO users(username,password,enabled) VALUES ('eduardo1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (14,'eduardo1','taxista');


INSERT INTO users(username,password,enabled) VALUES ('maria1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (15,'maria1','taxista');

INSERT INTO users(username,password,enabled) VALUES ('cliente1','cliente1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (16,'cliente1','cliente');
INSERT INTO clientes(nombre,apellidos,dni,email,telefono,username)VALUES('cliente1', 'cliente1','80097910K','cliente@gmail.com',659874123,'cliente1');

INSERT INTO users(username,password,enabled) VALUES ('manuel84','manuel84',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (17,'manuel84','cliente');
INSERT INTO clientes(id,nombre,apellidos,DNI,email,telefono,username) VALUES(4,'Manuel','Perez Barrasa','29666111L','manuel@gmail.com',666555789,'manuel84');

INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (1,'80090030P','Rodolfo','García Ordóñez','rodolfo@gmail.com',632587419,1,'taxista1');
INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (2,'90065864L','Roberta','Maestre','maestre@gmail.com',635874921,2,'roberta1');
INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (3,'80550030C','Paco','Pérez González','paco@gmail.com',632583419,2,'paco1');
INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (4,'90665864A','Sonia','Marquez','sonia@gmail.com',635874121,2,'sonia1');
INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (5,'80770030K','Alberto','López Ordóñez','alberto@gmail.com',632387419,2,'alberto1');
INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (6,'90995864H','Sara','Jimenez','sara@gmail.com',635871121,2,'sara1');
INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (7,'80220030G','Eduardo','García Suarez','eduardo@gmail.com',655587419,2,'eduardo1');
INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (8,'90115864J','Maria','Rodríguez','maria@gmail.com',635871121,2,'maria1');

INSERT INTO AUTOMOVIL(id,marca,modelo,num_Plazas,km_Recorridos) VALUES (1,'Toyota','Verso',5, 2000);
INSERT INTO AUTOMOVIL(id,marca,modelo,num_Plazas,km_Recorridos) VALUES (2,'Seat','Alhambra',7, 40000);
INSERT INTO AUTOMOVIL(id,marca,modelo,num_Plazas,km_Recorridos) VALUES (3,'Ford','Fiesta',5, 20000);
INSERT INTO AUTOMOVIL(id,marca,modelo,num_Plazas,km_Recorridos) VALUES (4,'Seat','Ibiza',4, 200);
/*No asociar el automóvil 4 a ninguna reserva o servicio, para que funciona el test de borrar automóviles no usados*/

INSERT INTO talleres(id,name,ubicacion,telefono) VALUES (1,'Talleres Manolito','Calle Sol', 698585858);
INSERT INTO talleres(id,name,ubicacion,telefono) VALUES (2,'Chapa y pintura Ramirez','Calle Luna', 698585837);

INSERT INTO servicios(id,name,fecha,precio,trabajador_id, automovil_id,talleres_id,descripcion) VALUES (1,'Revisión aceite','2013-01-04', 70.50, 1, 1, 1, 'Revisión periódica aceite y filtros');
INSERT INTO servicios(id,name,fecha,precio,trabajador_id, automovil_id,talleres_id,descripcion) VALUES (2,'Arreglo luna','2013-01-04', 50.00, 3, 2, 2, 'Arreglar picotazo parabrisas');

INSERT INTO Tarifa(id,precio_Por_Km,porcentaje_Iva_Repercutido,precio_Espera_Por_Hora,activado,original) VALUES (1,0.3,10,4,true,true);
INSERT INTO Tarifa(id,precio_Por_Km,porcentaje_Iva_Repercutido,precio_Espera_Por_Hora,activado,original) VALUES (2,0.5,10,4,false,true);
INSERT INTO Tarifa(id,precio_Por_Km,porcentaje_Iva_Repercutido,precio_Espera_Por_Hora,activado,original) VALUES (3,0.7,10,6,false,true);
INSERT INTO Tarifa(id,precio_Por_Km,porcentaje_Iva_Repercutido,precio_Espera_Por_Hora,activado,original) VALUES (4,0.9,10,8,false,false);

INSERT INTO Estado_Reserva(id,name)VALUES(1,'Solicitada');
INSERT INTO Estado_Reserva(id,name)VALUES(2,'Aceptada');
INSERT INTO Estado_Reserva(id,name)VALUES(3,'Rechazada');
INSERT INTO Estado_Reserva(id,name)VALUES(4,'Completada');
INSERT INTO Estado_Reserva(id,name)VALUES(5,'Incompleta');


INSERT INTO Ruta(id,origen_cliente,destino_cliente,num_Km_Totales,horas_Estimadas_Cliente)VALUES(1,'Zahinos','Badajoz',144.6,2.23);

INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(1,'Zahinos','Badajoz',72.3,1.15);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(2,'Badajoz','Zahinos',72.3,1.08);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(3,'Zahinos','Jerez de los Caballeros',19.1,0.28);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(4,'Jerez de los Caballeros','Zahinos',19.1,0.27);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(5,'Zahinos','Oliva de la Frontera',10.9,0.22);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(6,'Oliva de la Frontera','Zahinos',10.9,0.22);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(7,'Oliva de la Frontera','Jerez de los Caballeros',16.9,0.27);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(8,'Jerez de los Caballeros','Oliva de la Frontera',17,0.25);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(9,'Oliva de la Frontera','Badajoz',90.5,1.25);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(10,'Badajoz','Oliva de la Frontera',90.7,1.22);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(11,'Jerez de los Caballeros','Badajoz',74.8,1);
INSERT INTO Trayecto(id,origen,destino,num_Km_Totales,horas_Estimadas)VALUES(12,'Badajoz','Jerez de los Caballeros',73.7,1.03);

INSERT INTO Ruta_Trayectos(ruta_id,trayectos_id)VALUES(1,1);
INSERT INTO Ruta_Trayectos(ruta_id,trayectos_id)VALUES(1,2);

INSERT INTO Reserva(id,cliente_id,ruta_id,fecha_Salida,fecha_Llegada,hora_Salida,hora_Llegada,horas_Espera,plazas_Ocupadas,descripcion_Equipaje,estado_Reserva_id,precio_Total,precio_Distancia,precio_Espera,precio_IVA_Repercutivo,base_Imponible)VALUES(1,4,1,'2020-12-16','2020-12-16','17:00','19:23',0,4,'Llevo una maleta pequeña',1,59.29,59.29,0,5.92,53.36);
INSERT INTO Reserva(id,cliente_id,ruta_id,fecha_Salida,fecha_Llegada,hora_Salida,hora_Llegada,horas_Espera,plazas_Ocupadas,descripcion_Equipaje,estado_Reserva_id,precio_Total,precio_Distancia,precio_Espera,precio_IVA_Repercutivo,base_Imponible)VALUES(2,4,1,'2020-12-16','2020-12-16','12:00','14:23',0,4,'Llevo una maleta pequeña',2,59.29,59.29,0,5.92,53.36);