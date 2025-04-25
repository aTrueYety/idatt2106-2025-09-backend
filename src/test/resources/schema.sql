CREATE TABLE food_type (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(255) NOT NULL,
    calories_per_unit FLOAT NOT NULL,
    picture BLOB
);

CREATE TABLE food (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_id INT NOT NULL,
    household_id INT NOT NULL,
    expiration_date DATE NOT NULL,
    amount INT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES food_type(id)
);

CREATE TABLE extra_resident_type (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  consumption_water FLOAT NOT NULL,
  consumption_food FLOAT NOT NULL
);