# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.22)
# Database: bookinglite
# Generation Time: 2018-08-07 09:05:35 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table addresses
# ------------------------------------------------------------

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;

INSERT INTO `addresses` (`id`, `address_line`, `zip`, `city_id`)
VALUES
	(2,'Groove St. 27','12312',1),
	(3,'Shes-11','79026',2);

/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table amenities
# ------------------------------------------------------------

LOCK TABLES `amenities` WRITE;
/*!40000 ALTER TABLE `amenities` DISABLE KEYS */;

INSERT INTO `amenities` (`id`, `name`)
VALUES
	(1,'Wifi'),
	(2,'Condition'),
	(3,'TV');

/*!40000 ALTER TABLE `amenities` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table apartment_amenities
# ------------------------------------------------------------

LOCK TABLES `apartment_amenities` WRITE;
/*!40000 ALTER TABLE `apartment_amenities` DISABLE KEYS */;

INSERT INTO `apartment_amenities` (`apartment_id`, `amenity_id`)
VALUES
	(1,1),
	(2,1);

/*!40000 ALTER TABLE `apartment_amenities` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table apartment_types
# ------------------------------------------------------------

LOCK TABLES `apartment_types` WRITE;
/*!40000 ALTER TABLE `apartment_types` DISABLE KEYS */;

INSERT INTO `apartment_types` (`id`, `name`)
VALUES
	(1,'Single'),
	(2,'Double'),
	(3,'Triple'),
	(4,'Quadruple');

/*!40000 ALTER TABLE `apartment_types` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table apartments
# ------------------------------------------------------------

LOCK TABLES `apartments` WRITE;
/*!40000 ALTER TABLE `apartments` DISABLE KEYS */;

INSERT INTO `apartments` (`id`, `name`, `number_of_guests`, `price`, `apartment_type_id`, `property_id`)
VALUES
	(1,'ApartmentName1',2,444.00,2,1),
	(2,'ApartmentName2',2,444.00,2,1);

/*!40000 ALTER TABLE `apartments` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table booking_statuses
# ------------------------------------------------------------

LOCK TABLES `booking_statuses` WRITE;
/*!40000 ALTER TABLE `booking_statuses` DISABLE KEYS */;

INSERT INTO `booking_statuses` (`id`, `name`)
VALUES
	(1,'Resarved'),
	(2,'Paid'),
	(3,'Canceled');

/*!40000 ALTER TABLE `booking_statuses` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table bookings
# ------------------------------------------------------------

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;

INSERT INTO `bookings` (`id`, `check_in`, `check_out`, `total_price`, `apartment_id`, `status_id`, `review_id`, `user_id`)
VALUES
	(2,'2018-08-10 14:00:00','2018-08-13 12:00:00',1200.00,1,2,NULL,1),
	(3,'2018-08-20 14:00:00','2018-08-23 12:00:00',1200.00,1,2,NULL,1),
	(4,'2018-08-01 14:00:00','2018-08-03 12:00:00',1200.00,1,2,NULL,1);

/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cities
# ------------------------------------------------------------

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;

INSERT INTO `cities` (`id`, `name`, `country_id`)
VALUES
	(1,'Lviv',1),
	(2,'Kyiv',1);

/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table countries
# ------------------------------------------------------------

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;

INSERT INTO `countries` (`id`, `name`)
VALUES
	(1,'Ukraine');

/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table facilities
# ------------------------------------------------------------

LOCK TABLES `facilities` WRITE;
/*!40000 ALTER TABLE `facilities` DISABLE KEYS */;

INSERT INTO `facilities` (`id`, `name`)
VALUES
	(1,'Parking'),
	(2,'Pool'),
	(3,'Restarunt');

/*!40000 ALTER TABLE `facilities` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table photos
# ------------------------------------------------------------

LOCK TABLES `photos` WRITE;
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;

INSERT INTO `photos` (`id`, `url`, `property_id`)
VALUES
	(1,'http://res.cloudinary.com/lv326/image/upload/v1533588070/properties/a5cd5dc9-b1c2-4dc4-a372-64da0f6268da.jpg',1);

/*!40000 ALTER TABLE `photos` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table properties
# ------------------------------------------------------------

LOCK TABLES `properties` WRITE;
/*!40000 ALTER TABLE `properties` DISABLE KEYS */;

INSERT INTO `properties` (`id`, `contact_email`, `description`, `name`, `phone_number`, `rating`, `address_id`, `property_type_id`, `owner_id`)
VALUES
	(1,'Hodasdgmail.com','The best hotel in this city','1111','123-48-78',NULL,3,2,1);

/*!40000 ALTER TABLE `properties` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table property_facilities
# ------------------------------------------------------------

LOCK TABLES `property_facilities` WRITE;
/*!40000 ALTER TABLE `property_facilities` DISABLE KEYS */;

INSERT INTO `property_facilities` (`property_id`, `facility_id`)
VALUES
	(1,2);

/*!40000 ALTER TABLE `property_facilities` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table property_types
# ------------------------------------------------------------

LOCK TABLES `property_types` WRITE;
/*!40000 ALTER TABLE `property_types` DISABLE KEYS */;

INSERT INTO `property_types` (`id`, `name`)
VALUES
	(1,'Hotel'),
	(2,'Hostel');

/*!40000 ALTER TABLE `property_types` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table reviews
# ------------------------------------------------------------



# Dump of table roles
# ------------------------------------------------------------

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;

INSERT INTO `roles` (`id`, `name`)
VALUES
	(1,'ROLE_USER'),
	(2,'ROLE_OWNER');

/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_roles
# ------------------------------------------------------------

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;

INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES
	(1,1),
	(1,2);

/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table users
# ------------------------------------------------------------

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`id`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `verified`, `address_id`)
VALUES
	(1,'zynki@gmail.com','ivan','zhun','$2a$10$CoO9rISN7KTi62b0qku6MehFMKSMaBASs5/.jygOfrZK05Oe0JFNW','12313123',b'1',2);

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table verification_tokens
# ------------------------------------------------------------




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
