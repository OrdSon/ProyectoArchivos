CREATE DATABASE proyecto1;


CREATE TABLE IF NOT EXISTS cliente(
    nit VARCHAR PRIMARY KEY,
    nombre VARCHAR(255) 
);

CREATE TABLE IF NOT EXISTS sucursal(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    PRIMARY KEY(id),
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS empleado(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100 INCREMENT BY 7),
    PRIMARY KEY(id),
    nombre VARCHAR(255) NOT NULL,
    dpi BIGINT NOT NULL,
    nacimiento DATE NOT NULL,
    sucursal INT,
    tipo VARCHAR(50),
    fecha_contratacion TIMESTAMPTZ DEFAULT Now(),
    CONSTRAINT fk_empleado_sucursal
        FOREIGN KEY (sucursal) REFERENCES sucursal(id)
);


;

CREATE TABLE IF NOT EXISTS producto(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100000 INCREMENT BY 10),
    PRIMARY KEY(id),
    nombre VARCHAR(255) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    precio DECIMAL NOT NULL
);

CREATE TABLE IF NOT EXISTS inventario(
    id_producto INTEGER REFERENCES producto(id),
    id_sucursal INTEGER REFERENCES sucursal(id),
    cantidad INTEGER
);


CREATE TABLE IF NOT EXISTS solicitud(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 700 INCREMENT BY 11),
    PRIMARY KEY(id),
    id_producto INTEGER REFERENCES producto(id),
    id_sucursal INTEGER REFERENCES sucursal(id),
    id_empleado INTEGER REFERENCES empleado(id),
    cantidad INTEGER,
    fecha_solicitud DATE NOT NULL,
    fecha_resolucion DATE NOT NULL,
    resolucion BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS venta(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    PRIMARY KEY (id),
    id_cliente VARCHAR REFERENCES cliente(nit),
    id_sucursal INTEGER REFERENCES sucursal(id),
    id_empleado INTEGER REFERENCES empleado(id),
    fecha TIMESTAMPTZ DEFAULT Now(),
    descuento DECIMAL,
    subtotal DECIMAL,
    total DECIMAL 
);

CREATE TABLE IF NOT EXISTS lista_venta(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    PRIMARY KEY(id),
    id_venta INTEGER REFERENCES venta(id),
    id_producto INTEGER REFERENCES producto(id),
    cantidad INTEGER NOT NULL
);

--REPORTES--
--TOP 10 PRODUCTOS CON MAS VENTAS --
SELECT l.id_producto AS id_producto, p.nombre AS nombre, p.marca AS marca, 
COUNT(*) AS cantidad,p.precio, SUM(p.precio) AS subtotal 
FROM lista_venta AS l 
INNER JOIN producto AS p ON l.id_producto = p.id 
GROUP BY l.id_producto, p.nombre, p.marca, p.precio ORDER BY cantidad DESC LIMIT 10;
--TOP 10 CLIENTES QUE MAS GANANCIAS GENERAN--
SELECT c.nit AS nit, 
       c.nombre AS nombre, 
       SUM(v.total) AS total 
FROM cliente AS c 
INNER JOIN venta AS v 
ON c.nit = v.id_cliente 
GROUP BY c.nit, c.nombre
ORDER BY total DESC LIMIT 10;
--TOP 3 SUCURSALES CON MAS VENTAS--
SELECT s.id AS id, 
       s.nombre AS nombre, 
       s.direccion AS direccion, 
       COUNT(*) AS ventas, 
       SUM(v.total) AS total 
FROM sucursal AS s 
INNER JOIN venta AS v 
ON s.id = v.id_sucursal 
GROUP BY s.id, s.nombre, s.direccion 
ORDER BY ventas DESC 
LIMIT 3;

--TOP 3 SUCURSALES CON MAS INGRESOS --
SELECT s.id AS id, 
       s.nombre AS nombre, 
       s.direccion AS direccion, 
       COUNT(*) AS ventas, 
       SUM(v.total) AS total 
FROM sucursal AS s 
INNER JOIN venta AS v 
ON s.id = v.id_sucursal 
GROUP BY s.id, s.nombre, s.direccion 
ORDER BY total DESC 
LIMIT 3;

--TOP 3 EMPLEADOS CON MAS VENTAS --
SELECT e.id AS id, 
       e.nombre AS nombre,
       e.sucursal AS sucursal,
       s.nombre AS nombre_sucursal,
       COUNT(*) AS cantidad,
       SUM(v.total) AS total
       FROM empleado AS e 
       INNER JOIN sucursal AS s
       ON e.sucursal = s.id
INNER JOIN venta AS v
ON v.id_empleado = e.id
GROUP BY e.id, e.nombre, e.sucursal, s.nombre
ORDER BY cantidad DESC LIMIT 3;

--TOP 3 EMPLEADOS CON MAS GANANCIAS--

SELECT e.id AS id, 
       e.nombre AS nombre,
       e.sucursal AS sucursal,
       s.nombre AS nombre_sucursal,
       COUNT(*) AS cantidad,
       SUM(v.total) AS total
       FROM empleado AS e 
       INNER JOIN sucursal AS s
       ON e.sucursal = s.id
INNER JOIN venta AS v
ON v.id_empleado = e.id
GROUP BY e.id, e.nombre, e.sucursal, s.nombre
ORDER BY total DESC LIMIT 3;

--TOP 10 Productos con mas ingresos--
SELECT p.id AS id,
       p.nombre AS producto,
       p.marca AS marca,
       COUNT(*) AS cantidad,
       SUM(p.precio) AS total
FROM producto AS p
INNER JOIN lista_venta AS l ON p.id = l.id_producto
GROUP BY p.id, p.nombre, p.marca ORDER BY total DESC LIMIT 10;

--TOP 5 PRODUCTOS MAS VENDIDOS POR SUCURSAL--

SELECT id, producto, marca, id_sucursal, nombre_sucursal, cantidad, total
FROM (
  SELECT p.id AS id,
         p.nombre AS producto,
         p.marca AS marca,
         s.id AS id_sucursal,
         s.nombre AS nombre_sucursal,
         COUNT(*) AS cantidad,
         SUM(p.precio) AS total,
         ROW_NUMBER() OVER (PARTITION BY s.id ORDER BY COUNT(*) DESC) AS rn
  FROM producto AS p
  INNER JOIN lista_venta AS l ON p.id = l.id_producto
  INNER JOIN venta AS v ON v.id = l.id_venta 
  INNER JOIN sucursal AS s ON v.id_sucursal = s.id
  GROUP BY p.id, p.nombre, p.marca, s.id, s.nombre 
) AS subquery
WHERE rn <= 5;

--TOP 5 PRODUCTOS CON MAS GANANCIA--
SELECT id, producto, marca, id_sucursal, nombre_sucursal, cantidad, total
FROM (
  SELECT p.id AS id,
         p.nombre AS producto,
         p.marca AS marca,
         s.id AS id_sucursal,
         s.nombre AS nombre_sucursal,
         COUNT(*) AS cantidad,
         SUM(p.precio) AS total,
         ROW_NUMBER() OVER (PARTITION BY s.id ORDER BY SUM(p.precio) DESC) AS rn
  FROM producto AS p
  INNER JOIN lista_venta AS l ON p.id = l.id_producto
  INNER JOIN venta AS v ON v.id = l.id_venta 
  INNER JOIN sucursal AS s ON v.id_sucursal = s.id
  GROUP BY p.id, p.nombre, p.marca, s.id, s.nombre 
) AS subquery
WHERE rn <= 5;


INSERT INTO lista_venta (id_venta, id_producto, cantidad) VALUES (???);

SELECT l.id_venta AS venta, l.id_producto AS id_producto, p.nombre AS nombre, p.marca AS marca, 
COUNT(*) AS cantidad, SUM(p.precio) AS subtotal 
FROM lista_venta AS l 
INNER JOIN producto AS p ON l.id_producto = p.id 
WHERE l.id_venta = ? 
GROUP BY l.id_venta, l.id_producto, p.nombre, p.marca;



SELECT i.id_producto, p.nombre, p.marca, i.id_sucursal, s.nombre, i.cantidad 
FROM inventario AS i 
INNER JOIN producto AS p ON p.id = i.id_producto 
INNER JOIN sucursal AS s ON s.id = i.id_sucursal;


INSERT INTO cliente VALUES (987654321, 'Mary Smith'), 
(123456789, 'John Doe'), 
(876543210, 'Emily Johnson'), 
(234567890, 'David Lee'), 
(345678901, 'Jessica Brown'), 
(789012345, 'Michael Chen'), 
(456789012, 'Sophia Kim'),
(678901234, 'Adam Jones'),
(890123456, 'Olivia Davis'), 
(567890123, 'Jacob Nguyen'), 
(901234567, 'Ava Rodriguez'), 
(321098765, 'Ethan Martinez'), 
(543210987, 'Isabella Hernandez'), 
(210987654, 'William Lee'), 
(432109876, 'Mia Perez');

INSERT INTO empleado(nombre, dpi, nacimiento, sucursal, tipo) VALUES 
                    ('Juan Perez', '1312456978123', '2000-05-04',1,'Administrador'),
                    ('Maria Garcia', '8912345678901', '1995-12-30', 1, 'Ventas'),    
                    ('Luis Hernandez', '4509876543210', '1999-02-14', 1, 'Bodega'),   
                    ('Ana Rodriguez', '7654321098765', '1998-08-22', 1, 'Inventario'),    
                    ('Pedro Martinez', '9876543210123', '1997-06-01', 2, 'Administrador'),
                    ('Sophie Turner', '3364739284736', '1996-02-21', 2,'Ventas'),
                    ('David Kim', '7406895620152', '1984-05-14', 3,'Ventas'),
                    ('Lena Chen', '2145987643218', '2001-09-06', 1,'Ventas'),
                    ('Khalid Ahmed', '5783194650275', '1990-11-30', 5,'Bodega'),
                    ('Hannah Baker', '9918702364061', '2003-04-08', 2,'Ventas'),
                    ('Nathan Lee', '3650218734562', '1978-12-17', 1,'Ventas'),
                    ('Emma Garcia', '8275164932729', '1998-07-02', 3, 'Administrador'),
                    ('William Chen', '2143859763258', '1986-09-26', 2,'Ventas'),
                    ('Ava Davis', '5638194920157', '1994-03-11', 5,'Bodega'),
                    ('Mohammed Ali', '4812569370184', '2000-08-20', 1,'Ventas'),
                    ('Ethan Kim', '3482965173092', '1989-06-23', 3,'Ventas'),
                    ('Sophia Nguyen', '8934652178534', '2002-01-17', 5,'Bodega'),
                    ('Daniel Kim', '4205789364219', '1999-10-04', 2,'Ventas'),
                    ('Mia Rodriguez', '7264819350127', '1991-05-28', 1,'Inventario'),
                    ('Aiden Smith', '1863925403871', '2005-11-11', 2,'Inventario'),
                    ('Grace Lee', '2836150972359', '1987-08-09', 3,'Inventario'),
                    ('Ella Brown', '5764093728576', '2004-02-15', 1,'Inventario'),
                    ('Benjamin Davis', '8926340712395', '1995-09-22', 5,'Bodega'),
                    ('Liam Wilson', '1536879420753', '1979-04-29', 3,'Inventario'),
                    ('Olivia Lee', '6432917806421', '2000-12-06', 2,'Inventario')
;

INSERT INTO sucursal(nombre, direccion) VALUES 
('Sucursal interplaza', 'Km. 205 Carretera a San Marcos, Municipio, Quezaltenango 09032'),
('Sucursal montblanc', '4a Calle 18-01, Quezaltenango'),
('Sucursal parque central', '11 Avenida, entre 5a y, 6a Calle 10, Quezaltenango 09001'),
('Sucursal parque familiar', '7 calle B 98-899 zona 12 Quetzaltenango'),
('Bodega','13 calle 3223, Quezaltenango');

INSERT INTO producto (nombre, marca, precio) VALUES 
                    ('Auriculares', 'Sony', 120.50),
                    ('Teclado', 'Logitech', 65.99),
                    ('Altavoces', 'Bose', 350.00),
                    ('Monitor', 'Dell', 450.00);
                    ('Keyboard', 'Logitech', 70.50), 
                    ('Headphones', 'Sony', 200.00),  
                    ('Mouse', 'Microsoft', 35.99),
                    ('Laptop', 'Apple', 1200.00),
                    ('Impresora', 'HP', 150.00),
                    ('Mouse', 'Razer', 85.00),
                    ('Monitor', 'LG', 250.00),
                    ('Cámara', 'Canon', 650.00),
                    ('Smartwatch', 'Samsung', 399.99),
                    ('Bluetooth Speaker', 'JBL', 149.99),
                    ('Gaming Mouse', 'Logitech', 79.99),
                    ('Fitness Tracker', 'Fitbit', 129.99),
                    ('Portable Charger', 'Anker', 49.99),
                    ('Wireless Headphones', 'Sony', 299.99),
                    ('Robot Vacuum', 'iRobot', 399.99),
                    ('Streaming Stick', 'Roku', 59.99),
                    ('Tablet', 'Apple', 449.99),
                    ('Desktop PC', 'HP', 599.99),
                    ('Wireless Router', 'Netgear', 129.99),
                    ('External Hard Drive', 'Western Digital', 99.99),
                    ('Smart Thermostat', 'Nest', 249.99),
                    ('Dash Cam', 'Garmin', 199.99),
                    ('Bluetooth Earbuds', 'Bose', 199.99),
                    ('Home Security Camera', 'Ring', 149.99),
                    ('Wireless Keyboard', 'Microsoft', 89.99),
                    ('Smart Speaker', 'Amazon', 99.99),
                    ('Webcam', 'Logitech', 129.99),
                    ('VR Headset', 'Oculus', 399.99),
                    ('Portable Bluetooth Speaker', 'Sony', 79.99),
                    ('Wireless Gaming Headset', 'HyperX', 149.99),
                    ('Smart Lock', 'August', 249.99),
                    ('Noise Cancelling Headphones', 'Sennheiser', 349.99),
                    ('Wireless Mouse', 'Microsoft', 49.99),
                    ('Action Camera', 'GoPro', 299.99),
                    ('Smart Scale', 'Withings', 99.99),
                    ('Wireless Charger', 'Belkin', 39.99),
                    ('Smartwatch', 'Apple', 349.99),
                    ('Fitness Tracker', 'Garmin', 179.99)
;

INSERT INTO inventario VALUES (100140, 1, 23),
(100150, 1, 87),
(100160, 1, 51),
(100170, 1, 76),
(100180, 1, 12),
(100190, 1, 69),
(100200, 1, 44),
(100210, 1, 32),
(100220, 1, 98),
(100230, 1, 9),
(100240, 1, 65),
(100250, 1, 41),
(100260, 1, 55),
(100270, 1, 83),
(100280, 1, 28),
(100290, 1, 16),
(100300, 1, 77),
(100310, 1, 62),
(100320, 1, 11),
(100330, 1, 39),
(100340, 1, 53),
(100350, 1, 72),
(100360, 1, 24),
(100370, 1, 90),
(100380, 1, 58),
(100390, 1, 47),
(100140, 1, 84),
(100150, 1, 35),
(100160, 1, 21),
(100170, 1, 46)
;
INSERT INTO inventario VALUES
(100000, 2, 56),
(100010, 2, 17),
(100020, 2, 73),
(100030, 2, 42),
(100040, 2, 89),
(100050, 2, 3),
(100060, 2, 31),
(100070, 2, 79),
(100080, 2, 11),
(100090, 2, 67),
(100100, 2, 51),
(100110, 2, 93),
(100120, 2, 12),
(100130, 2, 76),
(100140, 2, 5),
(100150, 2, 65),
(100160, 2, 21),
(100170, 2, 43),
(100180, 2, 97),
(100190, 2, 39),
(100200, 2, 81),
(100210, 2, 27),
(100220, 2, 70),
(100230, 2, 14),
(100240, 2, 60)
;

INSERT INTO inventario VALUES
(100000, 3, 25),
(100010, 3, 93),
(100020, 3, 62),
(100030, 3, 79),
(100040, 3, 45),
(100050, 3, 17),
(100060, 3, 83),
(100070, 3, 11),
(100080, 3, 36),
(100090, 3, 50),
(100100, 3, 75),
(100110, 3, 7),
(100120, 3, 58),
(100130, 3, 97),
(100140, 3, 22),
(100150, 3, 68),
(100160, 3, 39),
(100170, 3, 88),
(100180, 3, 14),
(100190, 3, 52),
(100200, 3, 80),
(100210, 3, 33),
(100220, 3, 71),
(100230, 3, 8),
(100240, 3, 59)
;