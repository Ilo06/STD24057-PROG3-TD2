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