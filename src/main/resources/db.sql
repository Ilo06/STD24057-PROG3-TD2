CREATE DATABASE mini_dish_db;

CREATE USER mini_dish_db_manager WITH PASSWORD '123456';

GRANT CONNECT ON DATABASE mini_dish_db TO mini_dish_db_manager;

\c mini_dish_db;

GRANT SELECT, UPDATE, INSERT, DELETE ON Ingredient TO mini_dish_db_manager;
GRANT SELECT, UPDATE, INSERT, DELETE ON Dish TO mini_dish_db_manager;

