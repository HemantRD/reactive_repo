create table if not exists Ingredient (
 id identity,
 slug varchar(4) not null,
 name varchar(25) not null,
 type varchar(10) not null
);

create table Taco (
id identity,
name varchar(50) not null,
ingredient_ids INTEGER ARRAY[10]
);