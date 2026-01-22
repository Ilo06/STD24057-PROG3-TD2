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
