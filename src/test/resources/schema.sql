CREATE TABLE household (
  id INT PRIMARY KEY AUTO_INCREMENT,
  adress VARCHAR(255),
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL,
  amount_water DOUBLE NOT NULL,
  last_water_change TIMESTAMP
);