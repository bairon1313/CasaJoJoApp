
-- =============================================
-- PROYECTO: LA CASA DE JOJO
-- MICROSERVICIO: MS-MENU
-- MIGRACION V2: INSERCION DE DATOS INICIALES 
-- =============================================

-- =============================================
-- 1. CATEGORIAS
-- =============================================

INSERT INTO menu.categoria
    (id_categoria, codigo_categoria, nombre_categoria, activo)
VALUES
    (1, 'PROTEINA', 'Proteínas', TRUE),
    (2, 'FONDO', 'Platos de fondo', TRUE),
    (3, 'ACOMPAÑAMIENTO', 'Acompañamientos', TRUE),
    (4, 'ENSALADA', 'Ensaladas', TRUE),
    (5, 'POSTRE', 'Postres', TRUE),
    (6, 'BEBIDA', 'Bebidas', TRUE);

-- =============================================
-- 2. PROTEINAS
-- =============================================

INSERT INTO menu.plato
    (id_plato, id_categoria, nombre_plato, descripcion,
     precio_plato, disponible)
VALUES
    (1, 1, 'Carne Mechada',
     'Vacuno suave cocinado a fuego lento',
     4000, TRUE),

    (2, 1, 'Pechuga a la Plancha',
     'Filete de pollo sazonado a las finas hierbas',
     3500, TRUE),

    (3, 1, 'Chuleta de Cerdo',
     'Chuleta dorada a la plancha',
     3800, TRUE);

-- =============================================
-- 3. PLATOS DE FONDO
-- =============================================

INSERT INTO menu.plato
    (id_plato, id_categoria, nombre_plato, descripcion,
     precio_plato, disponible)
VALUES
    (4, 2, 'Pastel de Choclo',
     'Tradicional con pino de carne, pollo, huevo y aceituna',
     4500, TRUE),

    (5, 2, 'Lasaña Bolognesa',
     'Capas de pasta casera con pino y salsa bechamel',
     4200, TRUE);

-- =============================================
-- 4. ACOMPAÑAMIENTOS
-- =============================================

INSERT INTO menu.plato
    (id_plato, id_categoria, nombre_plato, descripcion,
     precio_plato, disponible)
VALUES
    (6, 3, 'Arroz Primavera',
     'Arroz graneado con pimentón y arvejas',
     1500, TRUE),

    (7, 3, 'Puré de Papas Rústico',
     'Puré casero elaborado con mantequilla y leche',
     1500, TRUE),

    (8, 3, 'Papas Fritas',
     'Porción de papas doradas y crujientes',
     1800, TRUE);

-- =============================================
-- 5. ENSALADAS
-- =============================================

INSERT INTO menu.plato
    (id_plato, id_categoria, nombre_plato, descripcion,
     precio_plato, disponible)
VALUES
    (9, 4, 'Ensalada Chilena',
     'Tomates frescos con cebolla amortiguada y cilantro',
     1200, TRUE);

-- =============================================
-- 6. POSTRES
-- =============================================

INSERT INTO menu.plato
    (id_plato, id_categoria, nombre_plato, descripcion,
     precio_plato, disponible)
VALUES
    (10, 5, 'Leche Asada',
     'Postre tradicional al horno con caramelo',
     1500, TRUE);

-- =============================================
-- 7. BEBIDAS
-- =============================================

INSERT INTO menu.plato
    (id_plato, id_categoria, nombre_plato, descripcion,
     precio_plato, disponible)
VALUES
    (11, 6, 'Jugo Natural',
     'Jugo de fruta de la estación',
     1500, TRUE);

-- =============================================
-- 8. SINCRONIZACION DE IDENTIFICADORES
-- =============================================

ALTER TABLE menu.categoria
    ALTER COLUMN id_categoria RESTART WITH 7;

ALTER TABLE menu.plato
    ALTER COLUMN id_plato RESTART WITH 12;
