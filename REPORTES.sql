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
