INSERT INTO foodie.locations(location_id, location_name, address, city, state, zip_code)
VALUES(1, "Taco Hut #28", "Food Truck Circle 28", "Dallas", "Texas", 71234),
(2, "Chicken Wing Spot", "Raceway Lane", "Houston", "Texas", 41234),
(3, "Curry In The Box #17", "Food Truck Circle  17", "Dallas", "Texas", 71234),
(4, "Pizza Bell #03", "Food Truck Circle  03", "Dallas", "Texas", 71234),
(5, "Lamb Chop", "Rose Drive", "San Antonio", "Texas", 81234);

INSERT INTO foodie.reviews(review_id, time_created, rating, review_text)
VALUES(1, '2020-06-18T10:34:09', 3, "Food was good."),
(2,  '2016-06-18T10:34:09', 2, "Service was good. Food was not"),
(3, '2018-06-18T10:34:09', 4, "I'll tell my freinds about this place. So good"),
(4,  '2021-06-18T10:34:09', 4, "Expensive food but worth it."),
(5,  '2022-06-18T10:34:09', 1, "Avoid this place!");


INSERT INTO foodie.roles(role_id, role_name)
VALUES(1, "admin"),
(2, "owner"),
(3, "user");

INSERT INTO foodie.users(user_id, username, password)
VALUES(1, "admin1", "12345"),
(2, "owner1", "54321"),
(3, "owner2", "12345"),
(4, "owner3", "54321"),
(5, "owner4", "12345"),
(6, "owner5", "54321"),
(7, "user1", "12345"),
(8, "user2", "54321"),
(9, "user3", "12345"),
(10, "user4", "54321"),
(11, "user5", "12345");

INSERT INTO foodie.profiles(users_id, first_name, last_name, email, phone)
VALUES(7, "Jumba", "Joe", "jumbajoe@mail.com", "000-000-0001"),
(8, "Rick", "Angel", "rickangel@mail.com", "000-000-0002"),
(9, "Alan", "Jim", "alanjim@mail.com", "000-000-0003"),
(10, "Grace", "Jones", "gracejones@mail.com", "000-000-0004"),
(11, "Ashley", "Smith", "ashleysmith@mail.com", "000-000-0005");

INSERT INTO foodie.assigned_roles(roles_id, users_id)
VALUES(1, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(3, 7),
(3, 8),
(3, 9),
(3, 10),
(3, 11);

INSERT INTO foodie.restaurants(restaurant_id, location_id, owner_id, name)
VALUES(1, 1, 2, "Taco Hut #28"),
(2, 2, 3, "Chicken Wing Spot"),
(3, 3, 4, "Curry In The Box #17"),
(4, 4, 5, "Pizza Bell #03"),
(5, 5, 6, "Lamb Chop");



INSERT INTO foodie.restaurant_reviews()
VALUES(),
(),
(),
(),
();