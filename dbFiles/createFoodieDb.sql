--  stand alone tables
-- #1
create table if not exists `foodie`.`locations` (
	location_id            int auto_increment,
	location_name varchar(45),
	address       varchar(45),
	city          varchar(45),
	state         varchar(45),
	zip_code      int,
    primary key(location_id)
);

-- #2
create table if not exists `foodie`.`reviews` (
    review_id int auto_increment,
    time_created datetime,
    rating int,
    review_text varchar(255),
    primary key(review_id)
);

-- #3
create table if not exists `foodie`.`roles` (
    role_id int auto_increment,
    role_name VARCHAR(50),
    primary key (role_id)
);

-- #4
create table if not exists `foodie`.`users` (
    user_id int auto_increment,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(45),
    primary key(user_id)
);


-- requires users table (2 / 8)
-- #5
create table if not exists `foodie`.`profiles` (
    users_id int,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50) UNIQUE NOT NULL,
    phone VARCHAR(50) UNIQUE NOT NULL,
    primary key (users_id),
    foreign key (users_id) references `foodie`.`users` (user_id)
);

-- #6
create table if not exists `foodie`.`assigned_roles`(
	roles_id  int not null,
	users_id int not null,
	primary key (roles_id, users_id),
	foreign key (roles_id) references `foodie`.`roles` (role_id),
	foreign key (users_id) references `foodie`.`users` (user_id)
);



-- requires users table + locations table (1 / 8)
-- #7
create table if not exists `foodie`.`restaurants` (
    restaurant_id int auto_increment,
    location_id int,
    owner_id int,
    name varchar(45),
    primary key(restaurant_id),
    foreign key (location_id) references `foodie`.`locations`(location_id),
    foreign key (owner_id) references `foodie`.`users`(user_id)
);



-- requires restaurants table + reviews table (1 / 8)
-- #8
create table if not exists `foodie`.`restaurant_reviews`(
	reviews_id      int not null,
	restaurants_id int not null,
	primary key (reviews_id, restaurants_id),
	foreign key (restaurants_id) references `foodie`.`restaurants` (restaurant_id),
	foreign key (reviews_id) references `foodie`.`reviews` (review_id)
);
