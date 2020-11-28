-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');
INSERT INTO users(username,password,enabled) VALUES ('josmacpor','j0smacp0r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'josmacpor','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'vet1','veterinarian');

-- One owner user, named joscuegal with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('joscuegal1','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'joscuegal1','owner');

-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('manrivlla','manrivlla',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'manrivlla','owner');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('vicdiacor','qwerty123',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'vicdiacor','owner');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');
INSERT INTO owners VALUES (11, 'Elena', 'Nold', '666 Friendly St.', 'Madison', '6665545487', 'owner1');
INSERT INTO owners VALUES (12, 'Jose Antonio', 'Macias', '2693 Commerce St.', 'Madison', '6022533487', 'josmacpor');
INSERT INTO owners VALUES (13, 'Jose Manuel', 'Cuevas', 'Manuel de Falla 32', 'Viso del Alcor', '6022533488', 'joscuegal1');
INSERT INTO owners VALUES (14, 'Manuel', 'Rivas', '4 Reina Mercedes', 'Sevilla', '6085559877', 'manrivlla');
INSERT INTO owners VALUES (15, 'Vicente', 'Diaz Correa', 'Castillo Alcalá de Guadaira 25', 'Sevilla', '638240587', 'vicdiacor');


INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (14, 'Bagheera', '2015-03-17', 1, 11);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (15, 'Dina', '2013-06-30', 1, 11);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (16, 'Tina', '2019-04-16', 1, 13);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (17, 'Messi', '2015-08-18', 2, 12);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (18, 'Tom', '2016-08-06', 2, 14);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (19, 'Pan', '2016-09-06', 1, 15);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

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


INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (1,'80090030P','Rodolfo','García Ordóñez','rodolfo@gmail.com',632587419,1,'rodolfo1');
INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (2,'90065864L','Roberta','Maestre','maestre@gmail.com',635874921,2,'roberta1');
INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (3,'80550030C','Paco','Pérez González','paco@gmail.com',632583419,2,'paco1');
INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (4,'90665864A','Sonia','Marquez','sonia@gmail.com',635874121,2,'sonia1');
INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (5,'80770030K','Alberto','López Ordóñez','alberto@gmail.com',632387419,2,'alberto1');
INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (6,'90995864H','Sara','Jimenez','sara@gmail.com',635871121,2,'sara1');
INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (7,'80220030G','Eduardo','García Suarez','eduardo@gmail.com',655587419,2,'eduardo1');
INSERT INTO Trabajador(id,DNI,nombre,apellidos,correo_Electronico,telefono,tipo_trabajador_id,username) VALUES (8,'90115864J','Maria','Rodríguez','maria@gmail.com',635871121,2,'maria1');


INSERT INTO AUTOMOVIL(id,marca,modelo,num_Plazas,km_Recorridos,trabajador_id) VALUES (1,'Toyota','Verso',5, 2000,1);
INSERT INTO AUTOMOVIL(id,marca,modelo,num_Plazas,km_Recorridos,trabajador_id) VALUES (2,'Seat','Alhambra',7, 40000,2);
INSERT INTO AUTOMOVIL(id,marca,modelo,num_Plazas,km_Recorridos,trabajador_id) VALUES (3,'Ford','Fiesta',5, 20000,3);

INSERT INTO talleres(id,name,ubicacion,telefono) VALUES (1,'Talleres Manolito','Calle Sol', 698585858);
INSERT INTO talleres(id,name,ubicacion,telefono) VALUES (2,'Chapa y pintura Ramirez','Calle Luna', 698585837);

INSERT INTO servicios(id,name,fecha,precio,trabajador_id, automovil_id,talleres_id,descripcion) VALUES (1,'Revisión aceite','2013-01-04', 70.50, 1, 1, 1, 'Revisión periódica aceite y filtros');
INSERT INTO servicios(id,name,fecha,precio,trabajador_id, automovil_id,talleres_id,descripcion) VALUES (2,'Arreglo luna','2013-01-04', 50.00, 3, 2, 2, 'Arreglar picotazo parabrisas');