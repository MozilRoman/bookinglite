# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.22)
# Database: bookinglite
# Generation Time: 2018-07-30 13:53:26 +0000
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

DROP TABLE IF EXISTS `addresses`;

CREATE TABLE `addresses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adress_line` varchar(255) NOT NULL,
  `zip` varchar(255) NOT NULL,
  `city_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9fkb8qaj71tiyr9htkmn7r8y5` (`city_id`),
  CONSTRAINT `FK9fkb8qaj71tiyr9htkmn7r8y5` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table amenities
# ------------------------------------------------------------

DROP TABLE IF EXISTS `amenities`;

CREATE TABLE `amenities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table apartment_amenities
# ------------------------------------------------------------

DROP TABLE IF EXISTS `apartment_amenities`;

CREATE TABLE `apartment_amenities` (
  `apartment_id` bigint(20) NOT NULL,
  `amenity_id` bigint(20) NOT NULL,
  PRIMARY KEY (`apartment_id`,`amenity_id`),
  KEY `FKxl58u97smgso1loa79i7ablu` (`amenity_id`),
  CONSTRAINT `FK1iulgjpk6ksf57mrwmev67uol` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`id`),
  CONSTRAINT `FKxl58u97smgso1loa79i7ablu` FOREIGN KEY (`amenity_id`) REFERENCES `amenities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table apartment_types
# ------------------------------------------------------------

DROP TABLE IF EXISTS `apartment_types`;

CREATE TABLE `apartment_types` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table apartments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `apartments`;

CREATE TABLE `apartments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `number_of_guests` int(11) NOT NULL,
  `price` decimal(8,2) NOT NULL,
  `apartment_type_id` bigint(20) NOT NULL,
  `property_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcjtofgve3pv48tqxbxlnsgqu` (`apartment_type_id`),
  KEY `FKeoic474we9fxpyd2hn4snt23` (`property_id`),
  CONSTRAINT `FKcjtofgve3pv48tqxbxlnsgqu` FOREIGN KEY (`apartment_type_id`) REFERENCES `apartment_types` (`id`),
  CONSTRAINT `FKeoic474we9fxpyd2hn4snt23` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table booking_statuses
# ------------------------------------------------------------

DROP TABLE IF EXISTS `booking_statuses`;

CREATE TABLE `booking_statuses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table bookings
# ------------------------------------------------------------

DROP TABLE IF EXISTS `bookings`;

CREATE TABLE `bookings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_in` datetime NOT NULL,
  `check_out` datetime NOT NULL,
  `total_price` decimal(8,2) NOT NULL,
  `apartment_id` bigint(20) NOT NULL,
  `status_id` bigint(20) NOT NULL,
  `review_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5a9b3jqsxk2ke9o7xl9w918u3` (`apartment_id`),
  KEY `FK6gwy0t862jf1lse2ujgudynyh` (`status_id`),
  KEY `FK5kh29npp97yyxsxlouuhb35vr` (`review_id`),
  KEY `FKeyog2oic85xg7hsu2je2lx3s6` (`user_id`),
  CONSTRAINT `FK5a9b3jqsxk2ke9o7xl9w918u3` FOREIGN KEY (`apartment_id`) REFERENCES `apartments` (`id`),
  CONSTRAINT `FK5kh29npp97yyxsxlouuhb35vr` FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`),
  CONSTRAINT `FK6gwy0t862jf1lse2ujgudynyh` FOREIGN KEY (`status_id`) REFERENCES `booking_statuses` (`id`),
  CONSTRAINT `FKeyog2oic85xg7hsu2je2lx3s6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table cities
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cities`;

CREATE TABLE `cities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `country_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6gatmv9dwedve82icy8wrkdmk` (`country_id`),
  CONSTRAINT `FK6gatmv9dwedve82icy8wrkdmk` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table countries
# ------------------------------------------------------------

DROP TABLE IF EXISTS `countries`;

CREATE TABLE `countries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table facilities
# ------------------------------------------------------------

DROP TABLE IF EXISTS `facilities`;

CREATE TABLE `facilities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table photos
# ------------------------------------------------------------

DROP TABLE IF EXISTS `photos`;

CREATE TABLE `photos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `property_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqil9x0kfrij489vhcorfcm44s` (`property_id`),
  CONSTRAINT `FKqil9x0kfrij489vhcorfcm44s` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table properties
# ------------------------------------------------------------

DROP TABLE IF EXISTS `properties`;

CREATE TABLE `properties` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_email` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(30) NOT NULL,
  `phone_number` varchar(30) NOT NULL,
  `rating` float DEFAULT NULL,
  `address_id` bigint(20) NOT NULL,
  `property_type_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa14pem3vquh6umb7s5jmb81hh` (`address_id`),
  KEY `FK9kogppid3dqet1tptnohat21d` (`property_type_id`),
  KEY `FK32k2h9s30s0ukftb8hj947ef2` (`owner_id`),
  CONSTRAINT `FK32k2h9s30s0ukftb8hj947ef2` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9kogppid3dqet1tptnohat21d` FOREIGN KEY (`property_type_id`) REFERENCES `property_types` (`id`),
  CONSTRAINT `FKa14pem3vquh6umb7s5jmb81hh` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table property_facilities
# ------------------------------------------------------------

DROP TABLE IF EXISTS `property_facilities`;

CREATE TABLE `property_facilities` (
  `property_id` bigint(20) NOT NULL,
  `facility_id` bigint(20) NOT NULL,
  PRIMARY KEY (`property_id`,`facility_id`),
  KEY `FKoerw2j7q2ua1lsrrbfg4fd329` (`facility_id`),
  CONSTRAINT `FKey23yutk37chxd08wr4yeuovw` FOREIGN KEY (`property_id`) REFERENCES `properties` (`id`),
  CONSTRAINT `FKoerw2j7q2ua1lsrrbfg4fd329` FOREIGN KEY (`facility_id`) REFERENCES `facilities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table property_types
# ------------------------------------------------------------

DROP TABLE IF EXISTS `property_types`;

CREATE TABLE `property_types` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table reviews
# ------------------------------------------------------------

DROP TABLE IF EXISTS `reviews`;

CREATE TABLE `reviews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message` varchar(1000) NOT NULL,
  `rating` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table roles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table user_roles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_roles`;

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `address_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKe8vydtk7hf0y16bfm558sywbb` (`address_id`),
  CONSTRAINT `FKe8vydtk7hf0y16bfm558sywbb` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
