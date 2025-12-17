CREATE TYPE category AS ENUM ('VEGETABLE','ANIMAL', 'MARINE', 'DAIRY', 'OTHER');

CREATE Type dish_type AS ENUM ('STARTER', 'MAIN', 'DESSERT');

CREATE TABLE dish (
    id serial primary key ,
    name varchar(255) not null,
    type dish_type not null
);

CREATE TABLE Ingredient (
    id serial primary key ,
    name varchar(255) not null ,
    price numeric(10,2) not null,
    category category not null,
    id_dish int,
    CONSTRAINT fk_id_dish FOREIGN KEY (id_dish) REFERENCES dish(id)
);