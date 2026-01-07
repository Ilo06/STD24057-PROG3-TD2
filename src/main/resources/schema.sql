CREATE TYPE category AS ENUM ('VEGETABLE','ANIMAL', 'MARINE', 'DAIRY', 'OTHER');

CREATE Type dish_type AS ENUM ('START', 'MAIN', 'DESSERT');

CREATE TABLE dish (
    id serial primary key ,
    name varchar(255) not null,
    dish_type dish_type not null
);

CREATE TABLE Ingredient (
    id serial primary key ,
    name varchar(255) not null UNIQUE ,
    price numeric(10,2) not null,
    category category not null,
    id_dish int,
    CONSTRAINT fk_id_dish FOREIGN KEY (id_dish) REFERENCES dish(id)
);

ALTER TABLE Ingredient ADD CONSTRAINT  name UNIQUE (name);
ALTER TABLE Dish ADD CONSTRAINT  name UNIQUE (name);



-- 1. Add column idempotently (PostgreSQL syntax)
ALTER TABLE dish ADD COLUMN IF NOT EXISTS unit_price DOUBLE PRECISION;

-- 2. Update specific values as requested
UPDATE dish SET unit_price = 2000 WHERE name = 'Salade fraîche';
UPDATE dish SET unit_price = 6000 WHERE name = 'Poulet grillé';

-- 3. Explicitly ensure others are NULL (optional, as new columns default to null)
UPDATE dish SET unit_price = NULL WHERE name IN ('Riz aux légumes', 'Gâteau au chocolat', 'Salade de fruits');