-- Create tables
CREATE TABLE household (
  id INT AUTO_INCREMENT PRIMARY KEY,
  adress VARCHAR(255),
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,
  amount_water FLOAT NOT NULL DEFAULT 0,
  last_water_change DATETIME
);

CREATE TABLE `user` (
  id INT AUTO_INCREMENT PRIMARY KEY,
  household_id INT,
  email VARCHAR(255) NOT NULL UNIQUE,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email_confirmed BOOLEAN DEFAULT FALSE,
  is_admin BOOLEAN DEFAULT FALSE,
  is_super_admin BOOLEAN DEFAULT FALSE,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  share_position_household BOOLEAN DEFAULT FALSE,
  share_position_group BOOLEAN DEFAULT FALSE,
  last_latitude FLOAT,
  last_longitude FLOAT,
  picture BLOB,
  FOREIGN KEY (household_id) REFERENCES household(id) ON DELETE SET NULL
);

CREATE TABLE admin_registration_key (
  user_id INT PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  `key` TEXT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE email_confirmaiation_key (
  user_id INT PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  `key` TEXT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE change_password_key (
  user_id INT PRIMARY KEY,
  created_at TIMESTAMP NOT NULL,
  `key` TEXT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE extra_resident_type (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  consumption_water FLOAT NOT NULL,
  consumption_food FLOAT NOT NULL
);

CREATE TABLE extra_resident (
  id INT AUTO_INCREMENT PRIMARY KEY,
  household_id INT,
  type_id INT,
  name VARCHAR(100),
  FOREIGN KEY (household_id) REFERENCES household(id) ON DELETE CASCADE,
  FOREIGN KEY (type_id) REFERENCES extra_resident_type(id) ON DELETE CASCADE
);

CREATE TABLE household_invite (
  household_id INT,
  user_id INT,
  created_at TIMESTAMP NOT NULL,
  PRIMARY KEY (household_id, user_id),
  FOREIGN KEY (household_id) REFERENCES household(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES `user`(id) ON DELETE CASCADE
);

CREATE TABLE kit (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT
);

CREATE TABLE household_kit (
  household_id INT,
  kit_id INT,
  PRIMARY KEY (household_id, kit_id),
  FOREIGN KEY (household_id) REFERENCES household(id) ON DELETE CASCADE,
  FOREIGN KEY (kit_id) REFERENCES kit(id) ON DELETE CASCADE
);

CREATE TABLE food_type (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  unit VARCHAR(50) NOT NULL,
  calories_per_unit FLOAT NOT NULL,
  picture BLOB,
  CONSTRAINT uk_food_type_name_unit UNIQUE (name, unit)
);

CREATE TABLE food (
  id INT AUTO_INCREMENT PRIMARY KEY,
  type_id INT,
  household_id INT,
  expiration_date DATE NOT NULL,
  amount FLOAT NOT NULL,
  FOREIGN KEY (type_id) REFERENCES food_type(id) ON DELETE CASCADE,
  FOREIGN KEY (household_id) REFERENCES household(id) ON DELETE CASCADE
);

CREATE TABLE emergency_group (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT
);

CREATE TABLE group_household (
  id INT AUTO_INCREMENT PRIMARY KEY,
  household_id INT,
  group_id INT,
  FOREIGN KEY (household_id) REFERENCES household(id) ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES emergency_group(id) ON DELETE CASCADE
);

CREATE TABLE shared_food (
  food_id INT,
  group_household_id INT,
  amount FLOAT NOT NULL,
  PRIMARY KEY (food_id, group_household_id),
  FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE,
  FOREIGN KEY (group_household_id) REFERENCES group_household(id) ON DELETE CASCADE
);

CREATE TABLE group_invite (
  group_id INT,
  household_id INT,
  created_at TIMESTAMP NOT NULL,
  PRIMARY KEY (group_id, household_id),
  FOREIGN KEY (group_id) REFERENCES emergency_group(id) ON DELETE CASCADE,
  FOREIGN KEY (household_id) REFERENCES household(id) ON DELETE CASCADE
);

CREATE TABLE serverity (
  id INT AUTO_INCREMENT PRIMARY KEY,
  colour VARCHAR(15) NOT NULL,
  name VARCHAR(50),
  description VARCHAR(100)
);

CREATE TABLE info_page (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL UNIQUE,
  short_description TEXT,
  content TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event (
  id INT AUTO_INCREMENT PRIMARY KEY,
  info_page_id INT,
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,
  radius FLOAT NOT NULL,
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  end_time TIMESTAMP,
  severity_id INT NOT NULL,
  recomendation TEXT,
  FOREIGN KEY (info_page_id) REFERENCES info_page(id) ON DELETE SET NULL,
  FOREIGN KEY (severity_id) REFERENCES serverity(id) ON DELETE CASCADE
);

CREATE TABLE article (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  source TEXT,
  link TEXT
);

CREATE TABLE quiz (
  id INT AUTO_INCREMENT PRIMARY KEY,
  info_page_id INT,
  name VARCHAR(255),
  FOREIGN KEY (info_page_id) REFERENCES info_page(id)
);

CREATE TABLE map_object_type (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  icon VARCHAR(255) NOT NULL
);

CREATE TABLE map_object (
  id INT AUTO_INCREMENT PRIMARY KEY,
  type_id INT,
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,
  opening TIME,
  closing TIME,
  contact_phone VARCHAR(30),
  contact_email VARCHAR(100),
  contact_name VARCHAR(100),
  description TEXT,
  picture BLOB,
  FOREIGN KEY (type_id) REFERENCES map_object_type(id) ON DELETE CASCADE
);