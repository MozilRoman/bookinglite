# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.22)
# Database: bookinglite
# Generation Time: 2018-07-30 14:06:09 +0000
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



# Dump of table properties
# ------------------------------------------------------------



# Dump of table property_facilities
# ------------------------------------------------------------



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



# Dump of table users
# ------------------------------------------------------------




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
