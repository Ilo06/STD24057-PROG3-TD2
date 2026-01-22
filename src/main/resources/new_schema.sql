-- ENUMs
CREATE TYPE category AS ENUM ('VEGETABLE','ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
CREATE TYPE dish_type AS ENUM ('START', 'MAIN', 'DESSERT');
CREATE TYPE movement_type AS ENUM ('IN', 'OUT');

CREATE Type unit AS ENUM ('PCS', 'KG', 'L');

-- ALTER TABLE ingredient DROP COLUMN id_dish;

-- Table des plats
CREATE TABLE dish
(
    id         serial PRIMARY KEY,
    name       varchar(255) NOT NULL UNIQUE,
    dish_type  dish_type    NOT NULL,
    unit_price numeric(10, 2)
);

CREATE TABLE ingredient
(
    id       serial PRIMARY KEY,
    name     varchar(255)   NOT NULL UNIQUE,
    price    numeric(10, 2) NOT NULL,
    category category       NOT NULL
);

CREATE TABLE DishIngredient
(
    id                serial PRIMARY KEY,
    id_dish           int     NOT NULL,
    id_ingredient     int     NOT NULL,
    quantity_required numeric NOT NULL,
    unit              unit    NOT NULL,
    CONSTRAINT fk_dish FOREIGN KEY (id_dish) REFERENCES dish (id),
    CONSTRAINT fk_ingredient FOREIGN KEY (id_ingredient) REFERENCES ingredient (id),
    CONSTRAINT unique_dish_ingredient UNIQUE (id_dish, id_ingredient)
);

CREATE TABLE StockMovement
(
    id                serial PRIMARY KEY,
    id_ingredient     int,
    quantity          numeric not null,
    type              movement_type,
    unit              unit,
    creation_datetime timestamp,
    CONSTRAINT fk_id_ingredient FOREIGN KEY (id_ingredient) REFERENCES ingredient (id)
);


INSERT INTO StockMovement (id, id_ingredient, quantity, type, unit, creation_datetime)
VALUES (1, 1, 5.0, 'IN', 'KG', '2024-01-05 08:00:00'),
       (2, 1, 0.2, 'OUT', 'KG', '2024-01-06 12:00:00'),
       (3, 2, 4.0, 'IN', 'KG', '2024-01-05 08:00:00'),
       (4, 2, 0.15, 'OUT', 'KG', '2024-01-06 12:00:00'),
       (5, 3, 10.0, 'IN', 'KG', '2024-01-04 09:00:00'),
       (6, 3, 1.0, 'OUT', 'KG', '2024-01-06 13:00:00'),
       (7, 4, 3.0, 'IN', 'KG', '2024-01-05 10:00:00'),
       (8, 4, 0.3, 'OUT', 'KG', '2024-01-06 14:00:00'),
       (9, 5, 2.5, 'IN', 'KG', '2024-01-05 10:00:00'),
       (10, 5, 0.2, 'OUT', 'KG', '2024-01-06 14:00:00')
ON CONFLICT (id) DO NOTHING;




CREATE TABLE Order (
    id serial PRIMARY KEY,
    reference varchar(50) NOT NULL UNIQUE,
    creation_datetime timestamp NOT NULL
);

CREATE TABLE DishOrder (
    id serial PRIMARY KEY,
    id_order int NOT NULL,
    id_dish int NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (id_order) REFERENCES Order (id),
    CONSTRAINT fk_dish FOREIGN KEY (id_dish) REFERENCES dish (id)
);