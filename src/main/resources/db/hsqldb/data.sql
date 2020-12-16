
INSERT INTO users(username,password,enabled) VALUES ('admin','admin',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin','admin');



INSERT INTO tipo_Trabajador VALUES (1,'Administrador');
INSERT INTO tipo_Trabajador VALUES (2,'Taxista');


INSERT INTO users(username,password,enabled) VALUES ('rodolfo1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'rodolfo1','owner');

INSERT INTO users(username,password,enabled) VALUES ('roberta1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'roberta1','owner');


INSERT INTO users(username,password,enabled) VALUES ('paco1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'paco1','owner');


INSERT INTO users(username,password,enabled) VALUES ('sonia1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'sonia1','owner');


INSERT INTO users(username,password,enabled) VALUES ('alberto1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (12,'alberto1','owner');


INSERT INTO users(username,password,enabled) VALUES ('sara1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (13,'sara1','owner');


INSERT INTO users(username,password,enabled) VALUES ('eduardo1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (14,'eduardo1','owner');


INSERT INTO users(username,password,enabled) VALUES ('maria1','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (15,'maria1','owner');

INSERT INTO users(username,password,enabled) VALUES ('cliente1','cliente1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (16,'cliente1','owner');
INSERT INTO clientes(nombre,apellidos,dni,email,telefono,username)VALUES('cliente1', 'cliente1','80097910K','cliente@gmail.com',659874123,'cliente1');

INSERT INTO users(username,password,enabled) VALUES ('manuel84','manuel84',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (17,'manuel84','cliente');
INSERT INTO clientes(id,nombre,apellidos,DNI,email,telefono,username) VALUES(4,'Manuel','Perez Barrasa','29666111L','manuel@gmail.com',666555789,'manuel84');

INSERT INTO Trabajador(id,dni,nombre,apellidos,email,telefono,tipo_trabajador_id,username) VALUES (1,'80090030P','Rodolfo','García Ordóñez','rodolfo@gmail.com',632587419,1,'rodolfo1');
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