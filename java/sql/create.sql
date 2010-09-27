-- MySQL dump 10.13  Distrib 5.1.36, for apple-darwin9.5.0 (i386)
--
-- Host: localhost    Database: dms
-- ------------------------------------------------------
-- Server version	5.1.36-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `account_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_type` int(11) NOT NULL,
  `max_size` bigint(20) DEFAULT NULL,
  `max_users` int(11) DEFAULT NULL,
  `basic_auth_allowed` tinyint(1) DEFAULT '1',
  `name` varchar(255) DEFAULT NULL,
  `account_state` int(11) NOT NULL DEFAULT '2',
  `group_id` bigint(11) DEFAULT NULL,
  `account_key` varchar(20) DEFAULT NULL,
  `account_secret_key` varchar(20) DEFAULT NULL,
  `billing_information_given` tinyint(4) DEFAULT NULL,
  `billing_day_of_month` int(11) DEFAULT NULL,
  `activated` tinyint(4) NOT NULL,
  `api_enabled` tinyint(4) NOT NULL DEFAULT '1',
  `opt_in_email` tinyint(4) NOT NULL DEFAULT '0',
  `creation_date` datetime DEFAULT NULL,
  `marketplace_enabled` tinyint(4) NOT NULL DEFAULT '1',
  `public_data_enabled` tinyint(4) NOT NULL DEFAULT '1',
  `report_share_enabled` tinyint(4) NOT NULL DEFAULT '1',
  `upgraded` tinyint(4) NOT NULL DEFAULT '0',
  `renewal_option_available` tinyint(4) NOT NULL DEFAULT '0',
  `date_format` int(11) NOT NULL DEFAULT '0',
  `default_reporting_sharing` tinyint(4) NOT NULL DEFAULT '1',
  `manual_invoicing` tinyint(4) NOT NULL DEFAULT '0',
  `billing_month_of_year` int(11) DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_activation`
--

DROP TABLE IF EXISTS `account_activation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_activation` (
  `account_activation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(11) NOT NULL,
  `activation_key` varchar(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  `target_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`account_activation_id`),
  UNIQUE KEY `activation_key` (`activation_key`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_activation_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_activity`
--

DROP TABLE IF EXISTS `account_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_activity` (
  `account_activity_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `activity_date` datetime NOT NULL,
  `account_type` int(11) NOT NULL,
  `activity_type` int(11) NOT NULL,
  `activity_notes` text,
  `user_licenses` int(11) NOT NULL,
  `account_state` int(11) DEFAULT NULL,
  `max_users` int(11) NOT NULL DEFAULT '0',
  `max_size` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_activity_id`),
  KEY `account_id` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_billing_task`
--

DROP TABLE IF EXISTS `account_billing_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_billing_task` (
  `account_billing_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_billing_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_credit_card_billing_info`
--

DROP TABLE IF EXISTS `account_credit_card_billing_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_credit_card_billing_info` (
  `account_credit_card_billing_info_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `response` varchar(255) DEFAULT NULL,
  `transaction_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `response_string` varchar(255) DEFAULT NULL,
  `response_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`account_credit_card_billing_info_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_credit_card_billing_info_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_payment`
--

DROP TABLE IF EXISTS `account_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_payment` (
  `account_payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `payment_required` double NOT NULL,
  `billing_date` datetime NOT NULL,
  `payment_made` tinyint(4) NOT NULL,
  `payment_amount` double DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `transaction_id` varchar(100) DEFAULT NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_payment_id`),
  KEY `account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_time_scheduler`
--

DROP TABLE IF EXISTS `account_time_scheduler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_time_scheduler` (
  `account_time_scheduler_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_time_scheduler_id`),
  KEY `task_generator_id` (`task_generator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_time_task`
--

DROP TABLE IF EXISTS `account_time_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_time_task` (
  `account_time_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_time_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_timed_state`
--

DROP TABLE IF EXISTS `account_timed_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_timed_state` (
  `account_timed_state_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `state_change_time` datetime NOT NULL,
  `account_state` int(11) NOT NULL,
  PRIMARY KEY (`account_timed_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_to_feed`
--

DROP TABLE IF EXISTS `account_to_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_to_feed` (
  `account_to_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `data_feed_id` bigint(20) DEFAULT NULL,
  `account_role` int(11) DEFAULT NULL,
  PRIMARY KEY (`account_to_feed_id`),
  KEY `account_id` (`account_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `account_to_feed_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_feed_ibfk_2` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_to_guest_user`
--

DROP TABLE IF EXISTS `account_to_guest_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_to_guest_user` (
  `account_to_guest_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(11) NOT NULL,
  `guest_user_id` bigint(11) NOT NULL,
  PRIMARY KEY (`account_to_guest_user_id`),
  KEY `account_id` (`account_id`),
  KEY `guest_user_id` (`guest_user_id`),
  CONSTRAINT `account_to_guest_user_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_guest_user_ibfk2` FOREIGN KEY (`guest_user_id`) REFERENCES `guest_user` (`guest_user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_to_merchant`
--

DROP TABLE IF EXISTS `account_to_merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_to_merchant` (
  `account_to_merchant_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `binding_type` int(11) NOT NULL,
  PRIMARY KEY (`account_to_merchant_id`),
  KEY `merchant_id` (`merchant_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_to_merchant_ibfk_1` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`merchant_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_merchant_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_to_user`
--

DROP TABLE IF EXISTS `account_to_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_to_user` (
  `account_to_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_to_user_id`),
  KEY `account_id` (`account_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `account_to_user_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_user_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_user_license`
--

DROP TABLE IF EXISTS `account_user_license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_user_license` (
  `account_user_license_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) NOT NULL,
  `creation_date` datetime NOT NULL,
  `account_id` bigint(11) NOT NULL,
  PRIMARY KEY (`account_user_license_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_user_license_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `additional_items`
--

DROP TABLE IF EXISTS `additional_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `additional_items` (
  `additional_items_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`additional_items_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `additional_items_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `additional_items_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis`
--

DROP TABLE IF EXISTS `analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis` (
  `analysis_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) DEFAULT NULL,
  `analysis_type` varchar(40) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `policy` int(11) DEFAULT NULL,
  `genre` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `views` int(11) DEFAULT NULL,
  `rating_count` int(11) DEFAULT NULL,
  `rating_average` double DEFAULT NULL,
  `root_definition` tinyint(4) DEFAULT '0',
  `marketplace_visible` tinyint(4) DEFAULT '0',
  `publicly_visible` tinyint(4) DEFAULT '0',
  `feed_visibility` tinyint(4) DEFAULT NULL,
  `report_state_id` bigint(20) DEFAULT NULL,
  `report_type` int(11) NOT NULL DEFAULT '1',
  `author_name` varchar(255) DEFAULT NULL,
  `description` text,
  `solution_visible` tinyint(4) NOT NULL DEFAULT '0',
  `temporary_report` tinyint(4) NOT NULL DEFAULT '0',
  `url_key` varchar(100) DEFAULT NULL,
  `account_visible` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`analysis_id`),
  KEY `data_feed_id` (`data_feed_id`),
  KEY `analysis_idx3` (`marketplace_visible`),
  KEY `analysis_idx4` (`publicly_visible`),
  KEY `analysis_idx5` (`solution_visible`),
  KEY `analysis_idx6` (`root_definition`),
  KEY `analysis_url_idx` (`url_key`),
  KEY `analysis_idx7` (`temporary_report`),
  CONSTRAINT `analysis_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_based_feed`
--

DROP TABLE IF EXISTS `analysis_based_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_based_feed` (
  `analysis_based_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) NOT NULL,
  `data_feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_based_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `analysis_based_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_based_feed_ibfk_2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_calculation`
--

DROP TABLE IF EXISTS `analysis_calculation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_calculation` (
  `analysis_calculation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `calculation_string` text NOT NULL,
  `apply_before_aggregation` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`analysis_calculation_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_calculation_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_coordinate`
--

DROP TABLE IF EXISTS `analysis_coordinate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_coordinate` (
  `analysis_coordinate_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `analysis_zip_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_coordinate_id`),
  KEY `analysis_coordinate_ibfk1` (`analysis_item_id`),
  KEY `analysis_coordinate_ibfk2` (`analysis_zip_id`),
  CONSTRAINT `analysis_coordinate_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_coordinate_ibfk2` FOREIGN KEY (`analysis_zip_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_date`
--

DROP TABLE IF EXISTS `analysis_date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_date` (
  `analysis_date_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_level` int(11) DEFAULT NULL,
  `custom_date_format` varchar(100) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_date_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_date_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2907 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_dimension`
--

DROP TABLE IF EXISTS `analysis_dimension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_dimension` (
  `analysis_dimension_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_by` tinyint(4) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `key_dimension_id` bigint(20) DEFAULT NULL,
  `summary` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`analysis_dimension_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `key_dimension_id` (`key_dimension_id`),
  CONSTRAINT `analysis_dimension_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_dimension_ibfk_2` FOREIGN KEY (`key_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17222 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_hierarchy_item`
--

DROP TABLE IF EXISTS `analysis_hierarchy_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_hierarchy_item` (
  `analysis_hierarchy_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `hierarchy_level_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_hierarchy_item_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `analysis_hierarchy_item_ibfk1` (`hierarchy_level_id`),
  CONSTRAINT `analysis_hierarchy_item_ibfk1` FOREIGN KEY (`hierarchy_level_id`) REFERENCES `hierarchy_level` (`hierarchy_level_id`) ON DELETE SET NULL,
  CONSTRAINT `analysis_hierarchy_item_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_hierarchy_item_to_hierarchy_level`
--

DROP TABLE IF EXISTS `analysis_hierarchy_item_to_hierarchy_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_hierarchy_item_to_hierarchy_level` (
  `analysis_hierarchy_item_to_hierarchy_level_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `hierarchy_level_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_hierarchy_item_to_hierarchy_level_id`),
  KEY `hierarchy_level_id` (`hierarchy_level_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_hierarchy_item_to_hierarchy_level_ibfk1` FOREIGN KEY (`hierarchy_level_id`) REFERENCES `hierarchy_level` (`hierarchy_level_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_hierarchy_item_to_hierarchy_level_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_item`
--

DROP TABLE IF EXISTS `analysis_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_item` (
  `analysis_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_key_id` bigint(20) NOT NULL,
  `hidden` tinyint(4) DEFAULT NULL,
  `formatting_configuration_id` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `width` int(11) DEFAULT '0',
  `virtual_dimension_id` bigint(20) DEFAULT NULL,
  `url_pattern` varchar(255) DEFAULT NULL,
  `high_is_good` tinyint(4) NOT NULL DEFAULT '1',
  `item_position` int(11) NOT NULL DEFAULT '0',
  `description` text,
  `concrete` tinyint(4) NOT NULL DEFAULT '1',
  `lookup_table_id` bigint(20) DEFAULT NULL,
  `sort_sequence` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`analysis_item_id`),
  KEY `item_key_id` (`item_key_id`),
  KEY `analysis_item_ibfk7` (`lookup_table_id`),
  CONSTRAINT `analysis_item_ibfk7` FOREIGN KEY (`lookup_table_id`) REFERENCES `lookup_table` (`lookup_table_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_item_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20078 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_item_to_filter`
--

DROP TABLE IF EXISTS `analysis_item_to_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_item_to_filter` (
  `analysis_item_to_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_item_to_filter_id`),
  KEY `analysis_item_to_filter_ibfk1` (`analysis_item_id`),
  KEY `analysis_item_to_filter_ibfk2` (`filter_id`),
  CONSTRAINT `analysis_item_to_filter_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_item_to_filter_ibfk2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_item_to_link`
--

DROP TABLE IF EXISTS `analysis_item_to_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_item_to_link` (
  `analysis_item_to_link_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `link_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_item_to_link_id`),
  KEY `analysis_item_to_link_ibfk1` (`analysis_item_id`),
  KEY `analysis_item_to_link_ibfk2` (`link_id`),
  CONSTRAINT `analysis_item_to_link_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_item_to_link_ibfk2` FOREIGN KEY (`link_id`) REFERENCES `link` (`link_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=274 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_latitude`
--

DROP TABLE IF EXISTS `analysis_latitude`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_latitude` (
  `analysis_latitude_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_latitude_id`),
  KEY `analysis_latitude_ibfk1` (`analysis_item_id`),
  CONSTRAINT `analysis_latitude_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_list`
--

DROP TABLE IF EXISTS `analysis_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_list` (
  `analysis_list_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `delimiter` varchar(10) DEFAULT NULL,
  `expanded` tinyint(4) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_list_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_list_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_longitude`
--

DROP TABLE IF EXISTS `analysis_longitude`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_longitude` (
  `analysis_longitude_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_longitude_id`),
  KEY `analysis_longitude_ibfk1` (`analysis_item_id`),
  CONSTRAINT `analysis_longitude_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_measure`
--

DROP TABLE IF EXISTS `analysis_measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_measure` (
  `analysis_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `aggregation` int(11) DEFAULT NULL,
  `measure_condition_range_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_measure_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `measure_condition_range_id` (`measure_condition_range_id`),
  CONSTRAINT `analysis_measure_ibfk2` FOREIGN KEY (`measure_condition_range_id`) REFERENCES `measure_condition_range` (`measure_condition_range_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_measure_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2849 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_measure_grouping`
--

DROP TABLE IF EXISTS `analysis_measure_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_measure_grouping` (
  `analysis_measure_grouping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_measure_grouping_id`),
  KEY `analysis_measure_grouping_ibfk1` (`analysis_item_id`),
  CONSTRAINT `analysis_measure_grouping_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_measure_grouping_join`
--

DROP TABLE IF EXISTS `analysis_measure_grouping_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_measure_grouping_join` (
  `analysis_measure_grouping_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `measure_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_measure_grouping_join_id`),
  KEY `analysis_measure_grouping_join_ibfk1` (`analysis_item_id`),
  KEY `analysis_measure_grouping_join_ibfk2` (`measure_id`),
  CONSTRAINT `analysis_measure_grouping_join_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`),
  CONSTRAINT `analysis_measure_grouping_join_ibfk2` FOREIGN KEY (`measure_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_range`
--

DROP TABLE IF EXISTS `analysis_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_range` (
  `analysis_range_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `lower_range_option_id` bigint(20) DEFAULT NULL,
  `higher_range_option_id` bigint(20) DEFAULT NULL,
  `aggregation_type` int(11) NOT NULL,
  PRIMARY KEY (`analysis_range_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `analysis_range_ibfk2` (`lower_range_option_id`),
  KEY `analysis_range_ibfk3` (`higher_range_option_id`),
  CONSTRAINT `analysis_range_ibfk2` FOREIGN KEY (`lower_range_option_id`) REFERENCES `range_option` (`range_option_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_range_ibfk3` FOREIGN KEY (`higher_range_option_id`) REFERENCES `range_option` (`range_option_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_range_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_range_to_range_option`
--

DROP TABLE IF EXISTS `analysis_range_to_range_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_range_to_range_option` (
  `analysis_range_to_range_option_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `range_option_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_range_to_range_option_id`),
  KEY `analysis_range_to_range_option_ibfk1` (`analysis_item_id`),
  KEY `analysis_range_to_range_option_ibfk2` (`range_option_id`),
  CONSTRAINT `analysis_range_to_range_option_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_range_to_range_option_ibfk2` FOREIGN KEY (`range_option_id`) REFERENCES `range_option` (`range_option_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_step`
--

DROP TABLE IF EXISTS `analysis_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_step` (
  `analysis_step_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `correlation_dimension_id` bigint(20) DEFAULT NULL,
  `start_date_dimension_id` bigint(20) DEFAULT NULL,
  `end_date_dimension_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_step_id`),
  KEY `analysis_step_ibfk1` (`correlation_dimension_id`),
  KEY `analysis_step_ibfk2` (`start_date_dimension_id`),
  KEY `analysis_step_ibfk3` (`end_date_dimension_id`),
  KEY `analysis_step_ibfk4` (`analysis_item_id`),
  CONSTRAINT `analysis_step_ibfk1` FOREIGN KEY (`correlation_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL,
  CONSTRAINT `analysis_step_ibfk2` FOREIGN KEY (`start_date_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL,
  CONSTRAINT `analysis_step_ibfk3` FOREIGN KEY (`end_date_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL,
  CONSTRAINT `analysis_step_ibfk4` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_tags`
--

DROP TABLE IF EXISTS `analysis_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_tags` (
  `analysis_tags_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(100) DEFAULT NULL,
  `use_count` int(11) DEFAULT '0',
  PRIMARY KEY (`analysis_tags_id`)
) ENGINE=InnoDB AUTO_INCREMENT=595 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_text`
--

DROP TABLE IF EXISTS `analysis_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_text` (
  `analysis_text_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `html` tinyint(4) NOT NULL,
  PRIMARY KEY (`analysis_text_id`),
  KEY `analysis_text_ibfk1` (`analysis_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_to_analysis_item`
--

DROP TABLE IF EXISTS `analysis_to_analysis_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_to_analysis_item` (
  `analysis_to_analysis_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `field_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`analysis_to_analysis_item_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_to_analysis_item_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_analysis_item_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_to_data_scrub`
--

DROP TABLE IF EXISTS `analysis_to_data_scrub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_to_data_scrub` (
  `analysis_to_data_scrub_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) NOT NULL,
  `data_scrub_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_to_data_scrub_id`),
  KEY `analysis_to_data_scrub_ibfk2` (`data_scrub_id`),
  KEY `analysis_to_data_scrub_ibfk1` (`analysis_id`),
  CONSTRAINT `analysis_to_data_scrub_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_data_scrub_ibfk2` FOREIGN KEY (`data_scrub_id`) REFERENCES `data_scrub` (`data_scrub_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_to_filter_join`
--

DROP TABLE IF EXISTS `analysis_to_filter_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_to_filter_join` (
  `analysis_to_filter_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `filter_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_to_filter_join_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `analysis_to_filter_join_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_filter_join_ibfk_2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_to_tag`
--

DROP TABLE IF EXISTS `analysis_to_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_to_tag` (
  `analysis_to_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_tags_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_to_tag_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `analysis_to_tag_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_zip`
--

DROP TABLE IF EXISTS `analysis_zip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_zip` (
  `analysis_zip_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_zip_id`),
  KEY `analysis_zip_ibfk1` (`analysis_item_id`),
  CONSTRAINT `analysis_zip_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bandwidth_usage`
--

DROP TABLE IF EXISTS `bandwidth_usage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bandwidth_usage` (
  `bandwidth_usage_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `used_bandwidth` bigint(20) NOT NULL DEFAULT '0',
  `bandwidth_date` date NOT NULL,
  PRIMARY KEY (`bandwidth_usage_id`),
  KEY `account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `basecamp`
--

DROP TABLE IF EXISTS `basecamp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `basecamp` (
  `basecamp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `include_archived` tinyint(4) NOT NULL DEFAULT '0',
  `include_inactive` tinyint(4) NOT NULL DEFAULT '0',
  `include_comments` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`basecamp_id`),
  KEY `data_feed_id` (`data_feed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=609 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `benchmark`
--

DROP TABLE IF EXISTS `benchmark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `benchmark` (
  `category` varchar(40) NOT NULL,
  `elapsed_time` int(11) NOT NULL,
  `benchmark_date` datetime NOT NULL,
  KEY `category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `billing_scheduled_task`
--

DROP TABLE IF EXISTS `billing_scheduled_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billing_scheduled_task` (
  `billing_scheduled_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`billing_scheduled_task_id`),
  KEY `billing_scheduled_task_ibfk1` (`scheduled_task_id`),
  CONSTRAINT `billing_scheduled_task_ibfk1` FOREIGN KEY (`scheduled_task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `billing_task_generator`
--

DROP TABLE IF EXISTS `billing_task_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billing_task_generator` (
  `billing_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`billing_generator_id`),
  KEY `task_generator_id` (`task_generator_id`),
  CONSTRAINT `billing_task_generator_ibfk1` FOREIGN KEY (`task_generator_id`) REFERENCES `task_generator` (`task_generator_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `buy_our_stuff_todo`
--

DROP TABLE IF EXISTS `buy_our_stuff_todo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buy_our_stuff_todo` (
  `buy_our_stuff_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `todo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`buy_our_stuff_id`),
  KEY `buy_our_stuff_todo_ibfk1` (`todo_id`),
  CONSTRAINT `buy_our_stuff_todo_ibfk1` FOREIGN KEY (`todo_id`) REFERENCES `todo_base` (`todo_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chart_definition`
--

DROP TABLE IF EXISTS `chart_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chart_definition` (
  `chart_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `graphic_definition_id` bigint(20) DEFAULT NULL,
  `chart_type` int(11) DEFAULT NULL,
  `chart_family` int(11) DEFAULT NULL,
  `limits_metadata_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`chart_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `limits_metadata_id` (`limits_metadata_id`),
  CONSTRAINT `chart_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `chart_definition_ibfk_2` FOREIGN KEY (`limits_metadata_id`) REFERENCES `limits_metadata` (`limits_metadata_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chart_limits_metadata`
--

DROP TABLE IF EXISTS `chart_limits_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chart_limits_metadata` (
  `list_limits_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `top_items` tinyint(4) DEFAULT NULL,
  `number_items` int(11) DEFAULT NULL,
  PRIMARY KEY (`list_limits_metadata_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `charts_limits_metadata_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chart_report`
--

DROP TABLE IF EXISTS `chart_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chart_report` (
  `chart_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `chart_type` int(11) DEFAULT NULL,
  `chart_family` int(11) DEFAULT NULL,
  `limits_metadata_id` bigint(20) DEFAULT NULL,
  `elevation_angle` float NOT NULL DEFAULT '0',
  `rotation_angle` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`chart_report_id`),
  KEY `report_state_id` (`report_state_id`),
  KEY `limits_metadata_id` (`limits_metadata_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `community_group`
--

DROP TABLE IF EXISTS `community_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community_group` (
  `community_group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `publicly_visible` tinyint(4) DEFAULT NULL,
  `publicly_joinable` tinyint(4) DEFAULT NULL,
  `description` text,
  `tag_cloud_id` bigint(20) DEFAULT NULL,
  `url_key` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`community_group_id`),
  KEY `tag_cloud_id` (`tag_cloud_id`),
  KEY `community_group_url_idx` (`url_key`),
  CONSTRAINT `community_group_ibfk1` FOREIGN KEY (`tag_cloud_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `complex_analysis_measure`
--

DROP TABLE IF EXISTS `complex_analysis_measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `complex_analysis_measure` (
  `complex_analysis_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `wrapped_aggregation` int(11) DEFAULT NULL,
  PRIMARY KEY (`complex_analysis_measure_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `complex_analysis_measure_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `composite_connection`
--

DROP TABLE IF EXISTS `composite_connection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `composite_connection` (
  `composite_connection_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `composite_feed_id` bigint(20) NOT NULL,
  `source_feed_node_id` bigint(20) NOT NULL,
  `target_feed_node_id` bigint(20) NOT NULL,
  `source_join` bigint(20) NOT NULL,
  `target_join` bigint(20) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`composite_connection_id`),
  KEY `composite_feed_id` (`composite_feed_id`),
  KEY `source_feed_node_id` (`source_feed_node_id`),
  KEY `target_feed_node_id` (`target_feed_node_id`),
  KEY `source_join` (`source_join`),
  KEY `target_join` (`target_join`),
  CONSTRAINT `composite_connection_ibfk_1` FOREIGN KEY (`composite_feed_id`) REFERENCES `composite_feed` (`composite_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `composite_connection_ibfk_2` FOREIGN KEY (`source_feed_node_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `composite_connection_ibfk_3` FOREIGN KEY (`target_feed_node_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `composite_connection_ibfk_4` FOREIGN KEY (`source_join`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE,
  CONSTRAINT `composite_connection_ibfk_5` FOREIGN KEY (`target_join`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2996 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `composite_feed`
--

DROP TABLE IF EXISTS `composite_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `composite_feed` (
  `composite_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`composite_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `composite_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1024 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `composite_node`
--

DROP TABLE IF EXISTS `composite_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `composite_node` (
  `composite_node_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `composite_feed_id` bigint(20) NOT NULL,
  `data_feed_id` bigint(20) NOT NULL,
  `x` int(11) NOT NULL DEFAULT '50',
  `y` int(11) NOT NULL DEFAULT '50',
  PRIMARY KEY (`composite_node_id`),
  KEY `composite_feed_id` (`composite_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `composite_node_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `composite_node_ibfk_2` FOREIGN KEY (`composite_feed_id`) REFERENCES `composite_feed` (`composite_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4640 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `configure_data_feed_todo`
--

DROP TABLE IF EXISTS `configure_data_feed_todo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configure_data_feed_todo` (
  `configure_data_feed_todo_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `todo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`configure_data_feed_todo_id`),
  KEY `todo_id` (`todo_id`),
  KEY `data_source_id` (`data_source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crosstab_definition`
--

DROP TABLE IF EXISTS `crosstab_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crosstab_definition` (
  `crosstab_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`crosstab_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `crosstab_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crosstab_report`
--

DROP TABLE IF EXISTS `crosstab_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crosstab_report` (
  `crosstab_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`crosstab_report_id`),
  KEY `report_state_id` (`report_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_data_source`
--

DROP TABLE IF EXISTS `custom_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_data_source` (
  `custom_data_source_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) NOT NULL,
  `wsdl_url` varchar(255) NOT NULL,
  PRIMARY KEY (`custom_data_source_id`),
  KEY `custom_data_source_ibfk1` (`data_feed_id`),
  CONSTRAINT `custom_data_source_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_n_filter`
--

DROP TABLE IF EXISTS `custom_n_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_n_filter` (
  `custom_n_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  `before_or_after` tinyint(4) NOT NULL,
  `interval_type` int(11) NOT NULL,
  `interval_amount` int(11) NOT NULL,
  PRIMARY KEY (`custom_n_filter_id`),
  KEY `custom_n_filter_ibfk1` (`analysis_item_id`),
  KEY `custom_n_filter_ibfk2` (`filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `daily_schedule`
--

DROP TABLE IF EXISTS `daily_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `daily_schedule` (
  `daily_schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `schedule_id` bigint(20) NOT NULL,
  PRIMARY KEY (`daily_schedule_id`),
  KEY `daily_schedule_ibfk1` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_activity_task_generator`
--

DROP TABLE IF EXISTS `data_activity_task_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_activity_task_generator` (
  `data_activity_task_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  `scheduled_activity_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_activity_task_generator_id`),
  KEY `data_activity_task_generator_ibfk1` (`task_generator_id`),
  KEY `data_activity_task_generator_ibfk2` (`scheduled_activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_feed`
--

DROP TABLE IF EXISTS `data_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_feed` (
  `data_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_name` varchar(100) DEFAULT NULL,
  `feed_type` varchar(100) DEFAULT NULL,
  `policy` int(11) DEFAULT NULL,
  `feed_size` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `feed_views` int(11) DEFAULT NULL,
  `feed_rating_count` int(11) DEFAULT NULL,
  `feed_rating_average` double DEFAULT NULL,
  `description` text,
  `attribution` varchar(255) DEFAULT NULL,
  `owner_name` varchar(255) DEFAULT NULL,
  `dynamic_service_definition_id` bigint(20) DEFAULT '0',
  `PUBLICLY_VISIBLE` tinyint(4) DEFAULT NULL,
  `MARKETPLACE_VISIBLE` tinyint(4) DEFAULT NULL,
  `api_key` varchar(100) DEFAULT NULL,
  `unchecked_api_enabled` tinyint(1) DEFAULT '0',
  `unchecked_api_basic_auth` tinyint(1) DEFAULT '0',
  `validated_api_enabled` tinyint(1) DEFAULT '0',
  `validated_api_basic_auth` tinyint(1) DEFAULT '0',
  `inherit_account_api_settings` tinyint(1) DEFAULT '1',
  `refresh_interval` bigint(20) DEFAULT NULL,
  `current_version` int(11) DEFAULT NULL,
  `visible` tinyint(4) NOT NULL DEFAULT '1',
  `parent_source_id` bigint(20) DEFAULT NULL,
  `version` bigint(20) NOT NULL DEFAULT '1',
  `account_visible` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`data_feed_id`),
  KEY `dynamic_service_definition_id` (`dynamic_service_definition_id`),
  KEY `data_feed_idx1` (`PUBLICLY_VISIBLE`),
  KEY `data_feed_idx2` (`MARKETPLACE_VISIBLE`),
  KEY `data_feed_idx3` (`visible`),
  KEY `data_feed_idx4` (`parent_source_id`),
  CONSTRAINT `data_feed_ibfk1` FOREIGN KEY (`dynamic_service_definition_id`) REFERENCES `dynamic_service_definition` (`dynamic_service_definition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1166 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_scrub`
--

DROP TABLE IF EXISTS `data_scrub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_scrub` (
  `data_scrub_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`data_scrub_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `data_scrub_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_source_audit_message`
--

DROP TABLE IF EXISTS `data_source_audit_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source_audit_message` (
  `data_source_audit_message_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `audit_time` datetime NOT NULL,
  PRIMARY KEY (`data_source_audit_message_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `data_source_audit_message_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `data_source_audit_message_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_source_comment`
--

DROP TABLE IF EXISTS `data_source_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source_comment` (
  `data_source_comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `time_created` datetime NOT NULL,
  `time_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`data_source_comment_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `data_source_comment_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`),
  CONSTRAINT `data_source_comment_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_source_query`
--

DROP TABLE IF EXISTS `data_source_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source_query` (
  `data_source_query_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_source_query_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`),
  KEY `data_source_id` (`data_source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_source_refresh_lock`
--

DROP TABLE IF EXISTS `data_source_refresh_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source_refresh_lock` (
  `data_source_refresh_lock_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `lock_time` bigint(20) NOT NULL,
  PRIMARY KEY (`data_source_refresh_lock_id`),
  UNIQUE KEY `data_source_refresh_lock_idx1` (`data_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_source_task_generator`
--

DROP TABLE IF EXISTS `data_source_task_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source_task_generator` (
  `data_source_task_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_source_task_generator_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `task_generator_id` (`task_generator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_source_to_group_notification`
--

DROP TABLE IF EXISTS `data_source_to_group_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source_to_group_notification` (
  `data_source_to_group_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  `feed_role` int(11) NOT NULL,
  `feed_action` int(11) NOT NULL,
  PRIMARY KEY (`data_source_to_group_notification_id`),
  KEY `feed_id` (`feed_id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `data_source_to_group_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE,
  CONSTRAINT `data_source_to_group_notification_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_source_to_virtual_dimension`
--

DROP TABLE IF EXISTS `data_source_to_virtual_dimension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_source_to_virtual_dimension` (
  `data_source_to_virtual_dimension_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `virtual_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_source_to_virtual_dimension_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `virtual_dimension_id` (`virtual_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_transaction`
--

DROP TABLE IF EXISTS `data_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_transaction` (
  `data_transaction_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `data_source_name` varchar(255) NOT NULL,
  `replace_data` tinyint(4) NOT NULL,
  `change_data_source_to_match` tinyint(4) NOT NULL,
  `external_txn_id` varchar(20) NOT NULL,
  `txn_date` datetime NOT NULL,
  `txn_status` tinyint(4) NOT NULL,
  PRIMARY KEY (`data_transaction_id`),
  KEY `data_transaction_ibfk1` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `data_transaction_command`
--

DROP TABLE IF EXISTS `data_transaction_command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_transaction_command` (
  `data_transaction_command_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `command_blob` blob NOT NULL,
  `data_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_transaction_command_id`),
  KEY `data_transaction_command_ibfk1` (`data_transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `database_version`
--

DROP TABLE IF EXISTS `database_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `database_version` (
  `database_version_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '37',
  PRIMARY KEY (`database_version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `date_range_filter`
--

DROP TABLE IF EXISTS `date_range_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `date_range_filter` (
  `date_range_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `low_value` datetime NOT NULL,
  `high_value` datetime NOT NULL,
  `sliding` tinyint(4) NOT NULL DEFAULT '0',
  `bounding_start_date` datetime DEFAULT NULL,
  `bounding_end_date` datetime DEFAULT NULL,
  `start_dimension` bigint(20) DEFAULT NULL,
  `end_dimension` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`date_range_filter_id`),
  KEY `filter_id` (`filter_id`),
  KEY `date_range_filter_ibfk3` (`start_dimension`),
  KEY `date_range_filter_ibfk4` (`end_dimension`),
  CONSTRAINT `date_range_filter_ibfk3` FOREIGN KEY (`start_dimension`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL,
  CONSTRAINT `date_range_filter_ibfk4` FOREIGN KEY (`end_dimension`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL,
  CONSTRAINT `date_range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `date_sequence`
--

DROP TABLE IF EXISTS `date_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `date_sequence` (
  `date_sequence_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_type` int(11) NOT NULL,
  `report_sequence_id` bigint(20) NOT NULL,
  PRIMARY KEY (`date_sequence_id`),
  KEY `date_sequence_ibfk1` (`report_sequence_id`),
  CONSTRAINT `date_sequence_ibfk1` FOREIGN KEY (`report_sequence_id`) REFERENCES `report_sequence` (`report_sequence_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `date_value`
--

DROP TABLE IF EXISTS `date_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `date_value` (
  `date_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  `date_contet` datetime NOT NULL,
  PRIMARY KEY (`date_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `date_value_ibfk1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `db_snapshot_scheduler`
--

DROP TABLE IF EXISTS `db_snapshot_scheduler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `db_snapshot_scheduler` (
  `db_snapshot_scheduler_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`db_snapshot_scheduler_id`),
  KEY `task_generator_id` (`task_generator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `db_snapshot_task`
--

DROP TABLE IF EXISTS `db_snapshot_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `db_snapshot_task` (
  `db_snapshot_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`db_snapshot_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_schedule`
--

DROP TABLE IF EXISTS `delivery_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_schedule` (
  `delivery_schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  PRIMARY KEY (`delivery_schedule_id`),
  KEY `delivery_schedule_ibfk1` (`scheduled_account_activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_scheduled_task`
--

DROP TABLE IF EXISTS `delivery_scheduled_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_scheduled_task` (
  `delivery_scheduled_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`delivery_scheduled_task_id`),
  KEY `delivery_scheduled_task_ibfk1` (`scheduled_account_activity_id`),
  KEY `delivery_scheduled_task_ibfk2` (`scheduled_task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_task_generator`
--

DROP TABLE IF EXISTS `delivery_task_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_task_generator` (
  `delivery_task_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  PRIMARY KEY (`delivery_task_generator_id`),
  KEY `delivery_task_generator_ibfk1` (`task_generator_id`),
  KEY `delivery_task_generator_ibfk2` (`scheduled_account_activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_to_email`
--

DROP TABLE IF EXISTS `delivery_to_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_to_email` (
  `delivery_to_email_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  PRIMARY KEY (`delivery_to_email_id`),
  KEY `delivery_to_email_ibfk1` (`scheduled_account_activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `delivery_to_user`
--

DROP TABLE IF EXISTS `delivery_to_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_to_user` (
  `delivery_to_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`delivery_to_user_id`),
  KEY `delivery_to_user_ibfk1` (`scheduled_account_activity_id`),
  KEY `delivery_to_user_ibfk2` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `derived_analysis_dimension`
--

DROP TABLE IF EXISTS `derived_analysis_dimension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `derived_analysis_dimension` (
  `derived_analysis_dimension_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `derivation_code` text NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`derived_analysis_dimension_id`),
  KEY `derived_analysis_dimension_ibfk1` (`analysis_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `derived_item_key`
--

DROP TABLE IF EXISTS `derived_item_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `derived_item_key` (
  `derived_item_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_item_key_id` bigint(20) NOT NULL,
  `item_key_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`derived_item_key_id`),
  KEY `item_key_id` (`item_key_id`),
  KEY `parent_item_key_id` (`parent_item_key_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `derived_item_key_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE,
  CONSTRAINT `derived_item_key_ibfk_2` FOREIGN KEY (`parent_item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE,
  CONSTRAINT `derived_item_key_ibfk_3` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9049 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `distributed_lock`
--

DROP TABLE IF EXISTS `distributed_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `distributed_lock` (
  `distributed_lock_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lock_name` varchar(100) DEFAULT NULL,
  `lock_time` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`distributed_lock_id`),
  UNIQUE KEY `lock_name` (`lock_name`)
) ENGINE=InnoDB AUTO_INCREMENT=817 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drill_through`
--

DROP TABLE IF EXISTS `drill_through`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drill_through` (
  `drill_through_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link_id` bigint(20) NOT NULL,
  `report_id` bigint(20) NOT NULL,
  PRIMARY KEY (`drill_through_id`),
  KEY `drill_through_ibfk1` (`link_id`),
  KEY `drill_through_ibfk2` (`report_id`),
  CONSTRAINT `drill_through_ibfk1` FOREIGN KEY (`link_id`) REFERENCES `link` (`link_id`) ON DELETE CASCADE,
  CONSTRAINT `drill_through_ibfk2` FOREIGN KEY (`report_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dynamic_service_code`
--

DROP TABLE IF EXISTS `dynamic_service_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dynamic_service_code` (
  `dynamic_service_code_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  `interface_bytecode` blob,
  `impl_bytecode` blob,
  `bean_bytecode` blob,
  `bean_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dynamic_service_code_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `dynamic_service_code_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dynamic_service_descriptor`
--

DROP TABLE IF EXISTS `dynamic_service_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dynamic_service_descriptor` (
  `dynamic_service_descriptor_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`dynamic_service_descriptor_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `dynamic_service_descriptor_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dynamic_service_method`
--

DROP TABLE IF EXISTS `dynamic_service_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dynamic_service_method` (
  `dynamic_service_method_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `method_name` varchar(255) NOT NULL,
  `dynamic_service_descriptor_id` bigint(20) NOT NULL,
  `method_type` int(11) NOT NULL,
  PRIMARY KEY (`dynamic_service_method_id`),
  KEY `dynamic_service_descriptor_id` (`dynamic_service_descriptor_id`),
  CONSTRAINT `dynamic_service_method_ibfk_1` FOREIGN KEY (`dynamic_service_descriptor_id`) REFERENCES `dynamic_service_descriptor` (`dynamic_service_descriptor_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dynamic_service_method_key`
--

DROP TABLE IF EXISTS `dynamic_service_method_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dynamic_service_method_key` (
  `dynamic_service_method_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dynamic_service_method_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`dynamic_service_method_key_id`),
  KEY `dynamic_service_method_id` (`dynamic_service_method_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `dynamic_service_method_key_ibfk_1` FOREIGN KEY (`dynamic_service_method_id`) REFERENCES `dynamic_service_method` (`dynamic_service_method_id`) ON DELETE CASCADE,
  CONSTRAINT `dynamic_service_method_key_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email_audit`
--

DROP TABLE IF EXISTS `email_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_audit` (
  `email_audit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `email_category` varchar(255) NOT NULL,
  PRIMARY KEY (`email_audit_id`),
  KEY `email_audit_ibfk1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `empty_value`
--

DROP TABLE IF EXISTS `empty_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `empty_value` (
  `empty_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`empty_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `empty_value_ibfk1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `excel_export`
--

DROP TABLE IF EXISTS `excel_export`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `excel_export` (
  `excel_export_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `excel_file` longblob NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`excel_export_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `excel_upload_format`
--

DROP TABLE IF EXISTS `excel_upload_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `excel_upload_format` (
  `excel_upload_format_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `excel_model` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`excel_upload_format_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `excel_upload_format_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_commercial_policy`
--

DROP TABLE IF EXISTS `feed_commercial_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_commercial_policy` (
  `feed_commercial_policy_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `price_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feed_commercial_policy_id`),
  KEY `feed_id` (`feed_id`),
  KEY `merchant_id` (`merchant_id`),
  KEY `price_id` (`price_id`),
  CONSTRAINT `feed_commercial_policy_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_commercial_policy_ibfk_2` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`merchant_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_commercial_policy_ibfk_3` FOREIGN KEY (`price_id`) REFERENCES `price` (`price_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_group_policy`
--

DROP TABLE IF EXISTS `feed_group_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_group_policy` (
  `feed_group_policy_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feed_group_policy_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `feed_group_policy_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_group_policy_group`
--

DROP TABLE IF EXISTS `feed_group_policy_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_group_policy_group` (
  `feed_group_policy_group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_group_policy_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feed_group_policy_group_id`),
  KEY `group_id` (`group_id`),
  KEY `feed_group_policy_id` (`feed_group_policy_id`),
  CONSTRAINT `feed_group_policy_group_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_group_policy_group_ibfk_2` FOREIGN KEY (`feed_group_policy_id`) REFERENCES `feed_group_policy` (`feed_group_policy_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_persistence_metadata`
--

DROP TABLE IF EXISTS `feed_persistence_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_persistence_metadata` (
  `feed_persistence_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `database_name` varchar(100) DEFAULT NULL,
  `last_data_time` datetime DEFAULT NULL,
  PRIMARY KEY (`feed_persistence_metadata_id`),
  UNIQUE KEY `feed_pers_cidx1` (`feed_id`,`version`),
  KEY `feed_id` (`feed_id`),
  KEY `feed_fpm_idx1` (`version`),
  CONSTRAINT `feed_persistence_metadata_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1147 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_popularity`
--

DROP TABLE IF EXISTS `feed_popularity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_popularity` (
  `feed_popularity_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_views` int(11) DEFAULT NULL,
  `feed_rating_count` int(11) DEFAULT NULL,
  `feed_rating_average` double DEFAULT NULL,
  `feed_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`feed_popularity_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `feed_popularity_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_to_analysis_item`
--

DROP TABLE IF EXISTS `feed_to_analysis_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_to_analysis_item` (
  `feed_to_analysis_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feed_to_analysis_item_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `feed_to_analysis_item_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_to_analysis_item_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=72711 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `feed_to_tag`
--

DROP TABLE IF EXISTS `feed_to_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feed_to_tag` (
  `feed_to_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  `analysis_tags_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`feed_to_tag_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `feed_to_tag_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_process_create_scheduled_task`
--

DROP TABLE IF EXISTS `file_process_create_scheduled_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_process_create_scheduled_task` (
  `file_process_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL,
  `upload_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `upload_name` varchar(255) NOT NULL,
  PRIMARY KEY (`file_process_id`),
  KEY `file_process_create_scheduled_task_ibfk1` (`task_id`),
  KEY `file_process_create_scheduled_task_ibfk2` (`upload_id`),
  KEY `file_process_create_scheduled_task_ibfk3` (`user_id`),
  KEY `file_process_create_scheduled_task_ibfk4` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_process_update_scheduled_task`
--

DROP TABLE IF EXISTS `file_process_update_scheduled_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_process_update_scheduled_task` (
  `file_process_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL,
  `upload_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `update_flag` tinyint(1) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`file_process_id`),
  KEY `file_process_update_scheduled_task_ibfk1` (`task_id`),
  KEY `file_process_update_scheduled_task_ibfk2` (`upload_id`),
  KEY `file_process_update_scheduled_task_ibfk3` (`feed_id`),
  KEY `file_process_update_scheduled_task_ibfk4` (`user_id`),
  KEY `file_process_update_scheduled_task_ibfk5` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `filter`
--

DROP TABLE IF EXISTS `filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filter` (
  `filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_type` varchar(40) DEFAULT NULL,
  `inclusive` tinyint(4) DEFAULT NULL,
  `optional` tinyint(4) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `apply_before_aggregation` tinyint(4) DEFAULT NULL,
  `intrinsic` tinyint(4) NOT NULL DEFAULT '0',
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`filter_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `filter_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=403 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `filter_analysis_measure`
--

DROP TABLE IF EXISTS `filter_analysis_measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filter_analysis_measure` (
  `filter_analysis_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aggregation` varchar(40) DEFAULT NULL,
  `filter_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`filter_analysis_measure_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `filter_analysis_measure_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `filter_to_analysis_item`
--

DROP TABLE IF EXISTS `filter_to_analysis_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filter_to_analysis_item` (
  `filter_to_analysis_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`filter_to_analysis_item_id`),
  KEY `filter_id` (`filter_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `filter_to_analysis_item_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE,
  CONSTRAINT `filter_to_analysis_item_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `filter_to_value`
--

DROP TABLE IF EXISTS `filter_to_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filter_to_value` (
  `filter_to_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `value_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`filter_to_value_id`),
  KEY `filter_id` (`filter_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `filter_to_value_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE,
  CONSTRAINT `filter_to_value_ibfk_2` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `filter_value`
--

DROP TABLE IF EXISTS `filter_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filter_value` (
  `filter_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_based_filter_id` bigint(20) DEFAULT NULL,
  `filter_value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`filter_value_id`),
  KEY `value_based_filter_id` (`value_based_filter_id`),
  CONSTRAINT `filter_value_ibfk_1` FOREIGN KEY (`value_based_filter_id`) REFERENCES `value_based_filter` (`value_based_filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `first_value_filter`
--

DROP TABLE IF EXISTS `first_value_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `first_value_filter` (
  `first_value_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`first_value_filter_id`),
  KEY `first_value_filter_ibfk1` (`filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flat_file_upload_format`
--

DROP TABLE IF EXISTS `flat_file_upload_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flat_file_upload_format` (
  `flat_file_upload_format_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `delimiter_escape` varchar(5) NOT NULL,
  `delimiter_pattern` varchar(5) NOT NULL,
  PRIMARY KEY (`flat_file_upload_format_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `flat_file_upload_format_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder` (
  `folder_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `folder_name` varchar(255) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`folder_id`),
  KEY `folder_ibfk1` (`data_source_id`),
  CONSTRAINT `folder_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4767 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `folder_to_analysis_item`
--

DROP TABLE IF EXISTS `folder_to_analysis_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder_to_analysis_item` (
  `folder_to_analysis_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `folder_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`folder_to_analysis_item_id`),
  KEY `folder_to_analysis_item_ibfk1` (`folder_id`),
  KEY `folder_to_analysis_item_ibfk2` (`analysis_item_id`),
  CONSTRAINT `folder_to_analysis_item_ibfk1` FOREIGN KEY (`folder_id`) REFERENCES `folder` (`folder_id`) ON DELETE CASCADE,
  CONSTRAINT `folder_to_analysis_item_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44964 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `folder_to_folder`
--

DROP TABLE IF EXISTS `folder_to_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder_to_folder` (
  `folder_to_folder_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_folder_id` bigint(20) NOT NULL,
  `child_folder_id` bigint(20) NOT NULL,
  PRIMARY KEY (`folder_to_folder_id`),
  KEY `folder_to_folder_ibfk1` (`parent_folder_id`),
  KEY `folder_to_folder_ibfk2` (`child_folder_id`),
  CONSTRAINT `folder_to_folder_ibfk1` FOREIGN KEY (`parent_folder_id`) REFERENCES `folder` (`folder_id`) ON DELETE CASCADE,
  CONSTRAINT `folder_to_folder_ibfk2` FOREIGN KEY (`child_folder_id`) REFERENCES `folder` (`folder_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `formatting_configuration`
--

DROP TABLE IF EXISTS `formatting_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formatting_configuration` (
  `formatting_configuration_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `formatting_type` int(11) NOT NULL,
  `text_uom` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`formatting_configuration_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20752 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `freshbooks`
--

DROP TABLE IF EXISTS `freshbooks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `freshbooks` (
  `freshbooks_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  `token_key` varchar(255) DEFAULT NULL,
  `token_secret_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`freshbooks_id`),
  KEY `freshbooks_ibfk1` (`data_source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gauge_report`
--

DROP TABLE IF EXISTS `gauge_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gauge_report` (
  `gauge_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `gauge_type` int(11) NOT NULL,
  `max_value` int(11) NOT NULL,
  PRIMARY KEY (`gauge_report_id`),
  KEY `report_state_id` (`report_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gnip`
--

DROP TABLE IF EXISTS `gnip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gnip` (
  `gnip_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `publisher_id` varchar(255) NOT NULL,
  `publisher_scope` varchar(255) NOT NULL,
  `filter_id` varchar(255) NOT NULL,
  PRIMARY KEY (`gnip_id`),
  KEY `data_feed_id` (`data_feed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_history`
--

DROP TABLE IF EXISTS `goal_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_history` (
  `goal_history_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `evaluation_date` datetime NOT NULL,
  `evaluation_result` double NOT NULL,
  PRIMARY KEY (`goal_history_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  CONSTRAINT `goal_history_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_node_to_user`
--

DROP TABLE IF EXISTS `goal_node_to_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_node_to_user` (
  `goal_node_to_user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  PRIMARY KEY (`goal_node_to_user_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `goal_node_to_user_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_node_to_user_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_outcome`
--

DROP TABLE IF EXISTS `goal_outcome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_outcome` (
  `goal_outcome_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `evaluation_date` datetime NOT NULL,
  `problem_evaluated` tinyint(4) NOT NULL,
  `start_value` double DEFAULT NULL,
  `end_value` double DEFAULT NULL,
  `outcome_value` int(11) NOT NULL,
  `direction` int(11) NOT NULL,
  PRIMARY KEY (`goal_outcome_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `goal_outcome_eval_idx` (`evaluation_date`),
  CONSTRAINT `goal_outcome_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_to_filter`
--

DROP TABLE IF EXISTS `goal_to_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_to_filter` (
  `goal_to_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_to_filter_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `goal_to_filter_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_to_filter_ibfk2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_to_problem_filter`
--

DROP TABLE IF EXISTS `goal_to_problem_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_to_problem_filter` (
  `goal_to_problem_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_to_problem_filter_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `goal_to_problem_filter_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_to_problem_filter_ibfk2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_tree`
--

DROP TABLE IF EXISTS `goal_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_tree` (
  `goal_tree_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` text,
  `root_node` bigint(20) DEFAULT NULL,
  `goal_tree_icon` varchar(255) DEFAULT NULL,
  `default_milestone_id` bigint(20) DEFAULT NULL,
  `url_key` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`goal_tree_id`),
  KEY `goal_tree_ibfk3` (`default_milestone_id`),
  KEY `goal_tree_url_idx` (`url_key`),
  CONSTRAINT `goal_tree_ibfk3` FOREIGN KEY (`default_milestone_id`) REFERENCES `milestone` (`milestone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_tree_node`
--

DROP TABLE IF EXISTS `goal_tree_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_tree_node` (
  `goal_tree_node_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_goal_tree_node_id` bigint(20) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `description` text,
  `icon_image` varchar(255) DEFAULT NULL,
  `sub_tree_id` bigint(11) DEFAULT NULL,
  `goal_tree_id` bigint(20) NOT NULL,
  `kpi_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`goal_tree_node_id`),
  KEY `parent_goal_tree_node_id` (`parent_goal_tree_node_id`),
  KEY `goal_tree_node_ibfk2` (`kpi_id`),
  CONSTRAINT `goal_tree_node_ibfk1` FOREIGN KEY (`parent_goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_ibfk2` FOREIGN KEY (`kpi_id`) REFERENCES `kpi` (`kpi_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_tree_node_tag`
--

DROP TABLE IF EXISTS `goal_tree_node_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_tree_node_tag` (
  `goal_tree_node_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `tag` varchar(255) NOT NULL,
  PRIMARY KEY (`goal_tree_node_tag_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  CONSTRAINT `goal_tree_node_tag_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_tree_node_to_feed`
--

DROP TABLE IF EXISTS `goal_tree_node_to_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_tree_node_to_feed` (
  `goal_tree_node_to_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_tree_node_to_feed_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `goal_tree_node_to_feed_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_feed_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_tree_node_to_insight`
--

DROP TABLE IF EXISTS `goal_tree_node_to_insight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_tree_node_to_insight` (
  `goal_tree_node_to_insight_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `insight_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_tree_node_to_insight_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `insight_id` (`insight_id`),
  CONSTRAINT `goal_tree_node_to_insight_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_insight_ibfk2` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_tree_node_to_solution`
--

DROP TABLE IF EXISTS `goal_tree_node_to_solution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_tree_node_to_solution` (
  `goal_tree_node_to_solution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `solution_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_tree_node_to_solution_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `solution_id` (`solution_id`),
  CONSTRAINT `goal_tree_node_to_solution_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_solution_ibfk2` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goal_tree_to_group`
--

DROP TABLE IF EXISTS `goal_tree_to_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goal_tree_to_group` (
  `goal_tree_to_group_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `goal_tree_id` bigint(11) NOT NULL,
  `role` int(8) NOT NULL,
  `group_id` bigint(11) NOT NULL,
  PRIMARY KEY (`goal_tree_to_group_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `goal_tree_to_group_ibfk1` FOREIGN KEY (`goal_tree_id`) REFERENCES `goal_tree` (`goal_tree_id`),
  CONSTRAINT `goal_tree_to_group_ibfk2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `google_analytics_data_source`
--

DROP TABLE IF EXISTS `google_analytics_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `google_analytics_data_source` (
  `google_analytics_data_source_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) NOT NULL,
  `analytics_id` varchar(50) NOT NULL,
  `website` varchar(255) NOT NULL,
  PRIMARY KEY (`google_analytics_data_source_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `google_analytics_data_source_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `google_feed`
--

DROP TABLE IF EXISTS `google_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `google_feed` (
  `google_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) DEFAULT NULL,
  `worksheeturl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`google_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `google_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `graphic_definition`
--

DROP TABLE IF EXISTS `graphic_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `graphic_definition` (
  `graphic_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`graphic_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `graphic_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_audit_message`
--

DROP TABLE IF EXISTS `group_audit_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_audit_message` (
  `group_audit_message_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `audit_time` datetime NOT NULL,
  PRIMARY KEY (`group_audit_message_id`),
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `group_audit_message_ibfk1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE,
  CONSTRAINT `group_audit_message_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_comment`
--

DROP TABLE IF EXISTS `group_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_comment` (
  `group_comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `time_created` datetime NOT NULL,
  `time_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`group_comment_id`),
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `group_comment_ibfk1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE,
  CONSTRAINT `group_comment_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_comment_notification`
--

DROP TABLE IF EXISTS `group_comment_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_comment_notification` (
  `group_comment_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `comment` varchar(255) NOT NULL,
  PRIMARY KEY (`group_comment_notification_id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `group_comment_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_notification`
--

DROP TABLE IF EXISTS `group_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_notification` (
  `group_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_notification_id`),
  KEY `group_id` (`group_id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `group_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE,
  CONSTRAINT `group_notification_ibfk2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_to_goal_tree_join`
--

DROP TABLE IF EXISTS `group_to_goal_tree_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_to_goal_tree_join` (
  `group_to_goal_tree_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `goal_tree_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`group_to_goal_tree_join_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_to_goal_tree_node_join`
--

DROP TABLE IF EXISTS `group_to_goal_tree_node_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_to_goal_tree_node_join` (
  `group_to_goal_tree_node_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `goal_tree_node_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`group_to_goal_tree_node_join_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_to_insight`
--

DROP TABLE IF EXISTS `group_to_insight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_to_insight` (
  `group_to_insight_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `insight_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`group_to_insight_id`),
  KEY `insight_id` (`insight_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_insight_join_ibfk_1` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_insight_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_to_report_package`
--

DROP TABLE IF EXISTS `group_to_report_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_to_report_package` (
  `group_to_report_package_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `report_package_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`group_to_report_package_id`),
  KEY `group_to_report_package_ibfk1` (`group_id`),
  KEY `group_to_report_package_ibfk2` (`report_package_id`),
  CONSTRAINT `group_to_report_package_ibfk1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_report_package_ibfk2` FOREIGN KEY (`report_package_id`) REFERENCES `report_package` (`report_package_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_to_tag`
--

DROP TABLE IF EXISTS `group_to_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_to_tag` (
  `group_to_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `analysis_tags_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`group_to_tag_id`),
  KEY `group_id` (`group_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `group_to_tag_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_to_user_join`
--

DROP TABLE IF EXISTS `group_to_user_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_to_user_join` (
  `group_to_user_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `binding_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`group_to_user_join_id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_user_join_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_user_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `guest_user`
--

DROP TABLE IF EXISTS `guest_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `guest_user` (
  `guest_user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`guest_user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `guest_user_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `heat_map`
--

DROP TABLE IF EXISTS `heat_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `heat_map` (
  `heat_map_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) NOT NULL,
  `map_type` int(11) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `zoom_level` int(11) NOT NULL,
  `min_long` double DEFAULT NULL,
  `max_long` double DEFAULT NULL,
  `min_lat` double DEFAULT NULL,
  `max_lat` double DEFAULT NULL,
  PRIMARY KEY (`heat_map_id`),
  KEY `heat_map_ibfk1` (`report_state_id`),
  CONSTRAINT `heat_map_ibfk1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hierarchy`
--

DROP TABLE IF EXISTS `hierarchy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hierarchy` (
  `hierarchy_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`hierarchy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hierarchy_level`
--

DROP TABLE IF EXISTS `hierarchy_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hierarchy_level` (
  `hierarchy_level_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` int(11) DEFAULT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  `position` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`hierarchy_level_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `hierarchy_level_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `highrise`
--

DROP TABLE IF EXISTS `highrise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `highrise` (
  `highrise_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `url` varchar(255) NOT NULL,
  `include_emails` tinyint(4) NOT NULL DEFAULT '0',
  `join_deals_to_contacts` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`highrise_id`),
  KEY `highrise_ibfk1` (`feed_id`),
  CONSTRAINT `highrise_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `icon`
--

DROP TABLE IF EXISTS `icon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `icon` (
  `icon_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `icon_name` varchar(100) DEFAULT NULL,
  `icon_file` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`icon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `innodb_lock_monitor`
--

DROP TABLE IF EXISTS `innodb_lock_monitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `innodb_lock_monitor` (
  `a` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `insight_audit_message`
--

DROP TABLE IF EXISTS `insight_audit_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insight_audit_message` (
  `insight_audit_message_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `insight_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `audit_time` datetime NOT NULL,
  PRIMARY KEY (`insight_audit_message_id`),
  KEY `insight_id` (`insight_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `insight_audit_message_ibfk1` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`),
  CONSTRAINT `insight_audit_message_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `insight_comment`
--

DROP TABLE IF EXISTS `insight_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insight_comment` (
  `insight_comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `insight_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `time_created` datetime NOT NULL,
  `time_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`insight_comment_id`),
  KEY `insight_id` (`insight_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `insight_comment_ibfk1` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`),
  CONSTRAINT `insight_comment_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `insight_policy_users`
--

DROP TABLE IF EXISTS `insight_policy_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insight_policy_users` (
  `insight_policy_users_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `insight_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`insight_policy_users_id`),
  KEY `insight_id` (`insight_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `insight_policy_users_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `insight_policy_users_ibfk_2` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_key`
--

DROP TABLE IF EXISTS `item_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_key` (
  `item_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_key_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19370 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jira`
--

DROP TABLE IF EXISTS `jira`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira` (
  `jira_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`jira_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `jira_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kpi`
--

DROP TABLE IF EXISTS `kpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kpi` (
  `kpi_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) NOT NULL,
  `analysis_measure_id` bigint(20) NOT NULL,
  `kpi_name` varchar(200) NOT NULL,
  `connection_visible` tinyint(4) NOT NULL DEFAULT '0',
  `description` text,
  `high_is_good` tinyint(4) DEFAULT NULL,
  `goal_defined` tinyint(4) NOT NULL DEFAULT '0',
  `goal_value` double DEFAULT NULL,
  `icon_image` varchar(255) DEFAULT NULL,
  `temporary` tinyint(4) NOT NULL DEFAULT '0',
  `day_window` int(11) NOT NULL DEFAULT '2',
  `threshold` double NOT NULL DEFAULT '0',
  `date_dimension_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`kpi_id`),
  KEY `kpi_ibfk1` (`data_feed_id`),
  KEY `kpi_ibfk2` (`analysis_measure_id`),
  KEY `kpi_ibfk3` (`date_dimension_id`),
  CONSTRAINT `kpi_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `kpi_ibfk2` FOREIGN KEY (`analysis_measure_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `kpi_ibfk3` FOREIGN KEY (`date_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kpi_role`
--

DROP TABLE IF EXISTS `kpi_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kpi_role` (
  `kpi_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `kpi_id` bigint(20) NOT NULL,
  `owner` tinyint(4) NOT NULL DEFAULT '0',
  `responsible` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`kpi_role_id`),
  KEY `kpi_role_ibfk1` (`kpi_id`),
  KEY `kpi_role_ibfk2` (`user_id`),
  KEY `kpi_role_ibfk3` (`group_id`),
  CONSTRAINT `kpi_role_ibfk1` FOREIGN KEY (`kpi_id`) REFERENCES `kpi` (`kpi_id`) ON DELETE CASCADE,
  CONSTRAINT `kpi_role_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `kpi_role_ibfk3` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kpi_to_filter`
--

DROP TABLE IF EXISTS `kpi_to_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kpi_to_filter` (
  `kpi_to_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kpi_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`kpi_to_filter_id`),
  KEY `kpi_to_filter_ibfk1` (`kpi_id`),
  KEY `kpi_to_filter_ibfk2` (`filter_id`),
  CONSTRAINT `kpi_to_filter_ibfk1` FOREIGN KEY (`kpi_id`) REFERENCES `kpi` (`kpi_id`) ON DELETE CASCADE,
  CONSTRAINT `kpi_to_filter_ibfk2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kpi_to_problem_filter`
--

DROP TABLE IF EXISTS `kpi_to_problem_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kpi_to_problem_filter` (
  `kpi_to_problem_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kpi_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`kpi_to_problem_filter_id`),
  KEY `kpi_to_problem_filter_ibfk1` (`kpi_id`),
  KEY `kpi_to_problem_filter_ibfk2` (`filter_id`),
  CONSTRAINT `kpi_to_problem_filter_ibfk1` FOREIGN KEY (`kpi_id`) REFERENCES `kpi` (`kpi_id`) ON DELETE CASCADE,
  CONSTRAINT `kpi_to_problem_filter_ibfk2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kpi_value`
--

DROP TABLE IF EXISTS `kpi_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kpi_value` (
  `kpi_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `start_value` double DEFAULT NULL,
  `end_value` double DEFAULT NULL,
  `evaluation_date` datetime DEFAULT NULL,
  `outcome_value` int(11) NOT NULL,
  `problem_evaluated` tinyint(4) NOT NULL,
  `direction` int(11) NOT NULL,
  `kpi_id` bigint(20) NOT NULL,
  `percent_change` double DEFAULT NULL,
  `directional` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`kpi_value_id`),
  KEY `kpi_value_ibfk1` (`kpi_id`),
  CONSTRAINT `kpi_value_ibfk1` FOREIGN KEY (`kpi_id`) REFERENCES `kpi` (`kpi_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `last_n_filter`
--

DROP TABLE IF EXISTS `last_n_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `last_n_filter` (
  `last_n_filter_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(11) NOT NULL,
  `result_limit` int(11) NOT NULL,
  PRIMARY KEY (`last_n_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `last_n_filter_ibfk1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `last_value_filter`
--

DROP TABLE IF EXISTS `last_value_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `last_value_filter` (
  `last_value_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`last_value_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `last_value_filter_ibfk1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `license_subscription`
--

DROP TABLE IF EXISTS `license_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `license_subscription` (
  `license_subscription_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`license_subscription_id`),
  KEY `feed_id` (`feed_id`),
  KEY `user_id` (`user_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `license_subscription_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `license_subscription_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `license_subscription_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `limits_metadata`
--

DROP TABLE IF EXISTS `limits_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `limits_metadata` (
  `limits_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `top_items` tinyint(4) DEFAULT NULL,
  `number_items` int(11) DEFAULT NULL,
  PRIMARY KEY (`limits_metadata_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link`
--

DROP TABLE IF EXISTS `link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `linkedin_data_source`
--

DROP TABLE IF EXISTS `linkedin_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkedin_data_source` (
  `linkedin_data_source_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `token_key` varchar(255) DEFAULT NULL,
  `token_secret_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`linkedin_data_source_id`),
  KEY `linkedin_data_source_ibfk1` (`feed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `list_analysis_field`
--

DROP TABLE IF EXISTS `list_analysis_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_analysis_field` (
  `analysis_field_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `position` int(11) NOT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_field_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `list_analysis_field_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `list_analysis_field_ibfk_2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `list_definition`
--

DROP TABLE IF EXISTS `list_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_definition` (
  `list_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `list_limits_metadata_id` bigint(20) DEFAULT NULL,
  `show_row_numbers` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`list_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `list_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `list_limits_metadata`
--

DROP TABLE IF EXISTS `list_limits_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_limits_metadata` (
  `list_limits_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `limits_metadata_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`list_limits_metadata_id`),
  KEY `limits_metadata_id` (`limits_metadata_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `list_limits_metadata_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `list_limits_metadata_ibfk_2` FOREIGN KEY (`limits_metadata_id`) REFERENCES `limits_metadata` (`limits_metadata_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `list_report`
--

DROP TABLE IF EXISTS `list_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_report` (
  `list_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `list_limits_metadata_id` bigint(20) DEFAULT NULL,
  `show_row_numbers` tinyint(4) DEFAULT '0',
  `summarize_all` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`list_report_id`),
  KEY `report_state_id` (`report_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=192 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lookup_pair`
--

DROP TABLE IF EXISTS `lookup_pair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lookup_pair` (
  `lookup_pair_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lookup_table_id` bigint(20) NOT NULL,
  `source_value` text NOT NULL,
  `target_value` varchar(200) DEFAULT NULL,
  `target_date_value` datetime DEFAULT NULL,
  `target_measure` double DEFAULT NULL,
  PRIMARY KEY (`lookup_pair_id`),
  KEY `lookup_pair_ibfk1` (`lookup_table_id`),
  CONSTRAINT `lookup_pair_ibfk1` FOREIGN KEY (`lookup_table_id`) REFERENCES `lookup_table` (`lookup_table_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22334 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lookup_table`
--

DROP TABLE IF EXISTS `lookup_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lookup_table` (
  `lookup_table_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lookup_table_name` varchar(200) NOT NULL,
  `source_item_id` bigint(20) NOT NULL,
  `target_item_id` bigint(20) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  `url_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lookup_table_id`),
  KEY `lookup_table_ibfk1` (`data_source_id`),
  KEY `lookup_table_ibfk2` (`source_item_id`),
  KEY `lookup_table_ibfk3` (`target_item_id`),
  CONSTRAINT `lookup_table_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `lookup_table_ibfk2` FOREIGN KEY (`source_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `lookup_table_ibfk3` FOREIGN KEY (`target_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lookup_table_scrub`
--

DROP TABLE IF EXISTS `lookup_table_scrub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lookup_table_scrub` (
  `lookup_table_scrub_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_scrub_id` bigint(20) NOT NULL,
  `source_key` bigint(20) NOT NULL,
  `target_key` bigint(20) NOT NULL,
  PRIMARY KEY (`lookup_table_scrub_id`),
  KEY `lookup_table_scrub_ibfk1` (`data_scrub_id`),
  KEY `lookup_table_scrub_ibfk2` (`source_key`),
  KEY `lookup_table_scrub_ibfk3` (`target_key`),
  CONSTRAINT `lookup_table_scrub_ibfk1` FOREIGN KEY (`data_scrub_id`) REFERENCES `data_scrub` (`data_scrub_id`) ON DELETE CASCADE,
  CONSTRAINT `lookup_table_scrub_ibfk2` FOREIGN KEY (`source_key`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE,
  CONSTRAINT `lookup_table_scrub_ibfk3` FOREIGN KEY (`target_key`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lookup_table_scrub_pair`
--

DROP TABLE IF EXISTS `lookup_table_scrub_pair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lookup_table_scrub_pair` (
  `lookup_table_scrub_pair_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_value` bigint(20) NOT NULL,
  `target_value` bigint(20) NOT NULL,
  `data_scrub_id` bigint(20) NOT NULL,
  PRIMARY KEY (`lookup_table_scrub_pair_id`),
  KEY `lookup_table_scrub_pair_ibfk1` (`data_scrub_id`),
  KEY `lookup_table_scrub_pair_ibfk2` (`source_value`),
  KEY `lookup_table_scrub_pair_ibfk3` (`target_value`),
  CONSTRAINT `lookup_table_scrub_pair_ibfk1` FOREIGN KEY (`data_scrub_id`) REFERENCES `data_scrub` (`data_scrub_id`) ON DELETE CASCADE,
  CONSTRAINT `lookup_table_scrub_pair_ibfk2` FOREIGN KEY (`source_value`) REFERENCES `value` (`value_id`) ON DELETE CASCADE,
  CONSTRAINT `lookup_table_scrub_pair_ibfk3` FOREIGN KEY (`target_value`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `map_definition`
--

DROP TABLE IF EXISTS `map_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `map_definition` (
  `map_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `graphic_definition_id` bigint(20) DEFAULT NULL,
  `map_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`map_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `graphic_definition_id` (`graphic_definition_id`),
  CONSTRAINT `map_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `map_definition_ibfk_2` FOREIGN KEY (`graphic_definition_id`) REFERENCES `graphic_definition` (`graphic_definition_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `map_report`
--

DROP TABLE IF EXISTS `map_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `map_report` (
  `map_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `map_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`map_report_id`),
  KEY `report_state_id` (`report_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marketo_data_source`
--

DROP TABLE IF EXISTS `marketo_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marketo_data_source` (
  `marketo_data_source_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`marketo_data_source_id`),
  KEY `marketo_data_source_ibfk1` (`data_feed_id`),
  CONSTRAINT `marketo_data_source_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `measure_condition`
--

DROP TABLE IF EXISTS `measure_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure_condition` (
  `measure_condition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `low_color` int(11) NOT NULL,
  `low_value` int(11) NOT NULL,
  `high_color` int(11) NOT NULL,
  `high_value` int(11) NOT NULL,
  PRIMARY KEY (`measure_condition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `measure_condition_range`
--

DROP TABLE IF EXISTS `measure_condition_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure_condition_range` (
  `measure_condition_range_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `low_measure_condition_id` bigint(20) DEFAULT NULL,
  `high_measure_condition_id` bigint(20) DEFAULT NULL,
  `value_range_type` int(11) NOT NULL,
  PRIMARY KEY (`measure_condition_range_id`),
  KEY `low_measure_condition_id` (`low_measure_condition_id`),
  KEY `high_measure_condition_id` (`high_measure_condition_id`),
  CONSTRAINT `measure_condition_range_ibfk_1` FOREIGN KEY (`low_measure_condition_id`) REFERENCES `measure_condition` (`measure_condition_id`) ON DELETE CASCADE,
  CONSTRAINT `measure_condition_range_ibfk_2` FOREIGN KEY (`high_measure_condition_id`) REFERENCES `measure_condition` (`measure_condition_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `merchant`
--

DROP TABLE IF EXISTS `merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `merchant` (
  `merchant_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) NOT NULL,
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `milestone`
--

DROP TABLE IF EXISTS `milestone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `milestone` (
  `milestone_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `milestone_date` date NOT NULL,
  `milestone_name` varchar(100) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`milestone_id`),
  KEY `account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `monthly_schedule`
--

DROP TABLE IF EXISTS `monthly_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monthly_schedule` (
  `monthly_schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `schedule_id` bigint(20) NOT NULL,
  `day_of_month` int(11) NOT NULL,
  PRIMARY KEY (`monthly_schedule_id`),
  KEY `monthly_schedule_ibfk1` (`schedule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mwf_schedule`
--

DROP TABLE IF EXISTS `mwf_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mwf_schedule` (
  `mwf_schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `schedule_id` bigint(20) NOT NULL,
  PRIMARY KEY (`mwf_schedule_id`),
  KEY `mwf_schedule_ibfk1` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `named_item_key`
--

DROP TABLE IF EXISTS `named_item_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `named_item_key` (
  `named_item_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `item_key_id` bigint(20) NOT NULL,
  PRIMARY KEY (`named_item_key_id`),
  KEY `item_key_id` (`item_key_id`),
  CONSTRAINT `named_item_key_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10298 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notification_base`
--

DROP TABLE IF EXISTS `notification_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification_base` (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_date` datetime NOT NULL,
  `notification_type` int(11) DEFAULT NULL,
  `acting_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`notification_id`),
  KEY `acting_user_id` (`acting_user_id`),
  CONSTRAINT `notification_base_ibfk1` FOREIGN KEY (`acting_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `null_filter`
--

DROP TABLE IF EXISTS `null_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `null_filter` (
  `null_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`null_filter_id`),
  KEY `null_filter_ibfk1` (`filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `numeric_value`
--

DROP TABLE IF EXISTS `numeric_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `numeric_value` (
  `numeric_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  `numeric_content` double NOT NULL,
  PRIMARY KEY (`numeric_value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `or_filter`
--

DROP TABLE IF EXISTS `or_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `or_filter` (
  `or_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`or_filter_id`),
  KEY `or_filter_ibfk1` (`filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `or_filter_to_filter`
--

DROP TABLE IF EXISTS `or_filter_to_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `or_filter_to_filter` (
  `or_filter_to_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `or_filter_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`or_filter_to_filter_id`),
  KEY `or_filter_to_filter_ibfk1` (`or_filter_id`),
  KEY `or_filter_id_filter_ibfk2` (`filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ordered_filter`
--

DROP TABLE IF EXISTS `ordered_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordered_filter` (
  `ordered_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ordered_filter_id`),
  KEY `ordered_filter_ibfk1` (`filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ordered_filter_to_filter`
--

DROP TABLE IF EXISTS `ordered_filter_to_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordered_filter_to_filter` (
  `ordered_filter_to_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ordered_filter_id` bigint(20) NOT NULL,
  `filter_id` bigint(20) NOT NULL,
  `order_value` int(11) NOT NULL,
  PRIMARY KEY (`ordered_filter_to_filter_id`),
  KEY `ordered_filter_to_filter_ibfk1` (`ordered_filter_id`),
  KEY `ordered_filter_id_filter_ibfk2` (`filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `password_reset`
--

DROP TABLE IF EXISTS `password_reset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_reset` (
  `password_reset_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `request_date` datetime NOT NULL,
  `password_request_string` varchar(255) NOT NULL,
  PRIMARY KEY (`password_reset_id`),
  UNIQUE KEY `password_reset_unique1` (`password_request_string`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `password_reset_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `password_storage`
--

DROP TABLE IF EXISTS `password_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_storage` (
  `password_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`password_id`),
  KEY `data_feed_id` (`data_feed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pattern_filter`
--

DROP TABLE IF EXISTS `pattern_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pattern_filter` (
  `pattern_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) NOT NULL,
  `case_sensitive` tinyint(4) NOT NULL,
  `regex` tinyint(4) NOT NULL,
  `pattern` varchar(255) NOT NULL,
  PRIMARY KEY (`pattern_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `pattern_filter_ibfk1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment` (
  `payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `paid_amount` double NOT NULL,
  `payment_date` datetime NOT NULL,
  `purchase_id` bigint(20) NOT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `persona` (
  `persona_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `persona_name` varchar(255) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`persona_id`),
  KEY `persona_id_ibfk1` (`account_id`),
  CONSTRAINT `persona_id_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `png_export`
--

DROP TABLE IF EXISTS `png_export`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `png_export` (
  `png_export_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `png_image` longblob NOT NULL,
  PRIMARY KEY (`png_export_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `price`
--

DROP TABLE IF EXISTS `price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `price` (
  `price_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cost` double NOT NULL,
  PRIMARY KEY (`price_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase` (
  `purchase_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` int(11) NOT NULL,
  `buyer_account_id` int(11) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  `purchase_date` datetime NOT NULL,
  `canceled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`purchase_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `range_filter`
--

DROP TABLE IF EXISTS `range_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `range_filter` (
  `range_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `low_value` double DEFAULT NULL,
  `high_value` double DEFAULT NULL,
  `low_value_variable` varchar(255) DEFAULT NULL,
  `high_value_variable` varchar(255) DEFAULT NULL,
  `low_value_defined` tinyint(4) NOT NULL DEFAULT '1',
  `high_value_defined` tinyint(4) NOT NULL DEFAULT '1',
  `current_low_value` double NOT NULL DEFAULT '0',
  `current_high_value` double NOT NULL DEFAULT '0',
  `current_low_value_defined` tinyint(4) NOT NULL DEFAULT '0',
  `current_high_value_defined` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `range_option`
--

DROP TABLE IF EXISTS `range_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `range_option` (
  `range_option_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) DEFAULT NULL,
  `low_value` double NOT NULL,
  `high_value` double NOT NULL,
  PRIMARY KEY (`range_option_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `redirect_data_source`
--

DROP TABLE IF EXISTS `redirect_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `redirect_data_source` (
  `redirect_data_source_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `delegate_data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`redirect_data_source_id`),
  KEY `redirect_data_source_ibfk1` (`data_source_id`),
  KEY `redirect_data_source_ibfk2` (`delegate_data_source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_based_data_source`
--

DROP TABLE IF EXISTS `report_based_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_based_data_source` (
  `report_based_data_source_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) NOT NULL,
  `report_id` bigint(20) NOT NULL,
  PRIMARY KEY (`report_based_data_source_id`),
  KEY `report_based_data_source_ibfk1` (`data_feed_id`),
  KEY `report_based_data_source_ibfk2` (`report_id`),
  CONSTRAINT `report_based_data_source_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `report_based_data_source_ibfk2` FOREIGN KEY (`report_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_deliveree`
--

DROP TABLE IF EXISTS `report_deliveree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_deliveree` (
  `report_deliveree_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `report_schedule_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  PRIMARY KEY (`report_deliveree_id`),
  KEY `user_id` (`user_id`),
  KEY `report_schedule_id` (`report_schedule_id`),
  CONSTRAINT `report_deliveree_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `report_deliveree_ibfk2` FOREIGN KEY (`report_schedule_id`) REFERENCES `report_schedule` (`report_schedule_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_delivery`
--

DROP TABLE IF EXISTS `report_delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_delivery` (
  `report_delivery_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL,
  `delivery_format` int(11) NOT NULL,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `body` text NOT NULL,
  `html_email` tinyint(4) NOT NULL DEFAULT '1',
  `timezone_offset` int(11) NOT NULL DEFAULT '0',
  `sender_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`report_delivery_id`),
  KEY `report_delivery_ibfk1` (`report_id`),
  KEY `report_delivery_ibfk2` (`scheduled_account_activity_id`),
  KEY `report_delivery_ibfk3` (`sender_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_delivery_audit`
--

DROP TABLE IF EXISTS `report_delivery_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_delivery_audit` (
  `report_delivery_audit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `report_delivery_id` bigint(20) NOT NULL,
  `successful` tinyint(4) NOT NULL,
  `message` text,
  `target_email` varchar(255) NOT NULL,
  `send_date` datetime NOT NULL,
  PRIMARY KEY (`report_delivery_audit_id`),
  KEY `report_delivery_audit_ibfk1` (`account_id`),
  KEY `report_delivery_audit_ibfk2` (`report_delivery_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_image`
--

DROP TABLE IF EXISTS `report_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_image` (
  `report_image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL,
  `report_image` longblob NOT NULL,
  PRIMARY KEY (`report_image_id`),
  KEY `report_id` (`report_id`),
  CONSTRAINT `report_image_ibfk1` FOREIGN KEY (`report_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_numeric_property`
--

DROP TABLE IF EXISTS `report_numeric_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_numeric_property` (
  `report_numeric_property_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_property_id` bigint(20) NOT NULL,
  `property_value` double NOT NULL,
  PRIMARY KEY (`report_numeric_property_id`),
  KEY `report_numeric_property_ibfk1` (`report_property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_package`
--

DROP TABLE IF EXISTS `report_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_package` (
  `report_package_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `package_name` varchar(255) NOT NULL,
  `connection_visible` tinyint(4) NOT NULL DEFAULT '0',
  `publicly_visible` tinyint(4) NOT NULL DEFAULT '0',
  `marketplace_visible` tinyint(4) NOT NULL DEFAULT '0',
  `data_source_id` bigint(20) DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `author_name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `limited_source` tinyint(4) NOT NULL DEFAULT '0',
  `temporary_package` tinyint(4) NOT NULL DEFAULT '0',
  `url_key` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`report_package_id`),
  KEY `report_package_ibfk1` (`data_source_id`),
  KEY `report_package_url_idx` (`url_key`),
  CONSTRAINT `report_package_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_package_to_report`
--

DROP TABLE IF EXISTS `report_package_to_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_package_to_report` (
  `report_package_to_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_package_id` bigint(20) NOT NULL,
  `report_id` bigint(20) NOT NULL,
  `report_order` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`report_package_to_report_id`),
  KEY `report_package_to_report_ibfk1` (`report_package_id`),
  KEY `report_package_to_report_ibfk2` (`report_id`),
  CONSTRAINT `report_package_to_report_ibfk1` FOREIGN KEY (`report_package_id`) REFERENCES `report_package` (`report_package_id`) ON DELETE CASCADE,
  CONSTRAINT `report_package_to_report_ibfk2` FOREIGN KEY (`report_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_property`
--

DROP TABLE IF EXISTS `report_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_property` (
  `report_property_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `property_name` varchar(255) NOT NULL,
  PRIMARY KEY (`report_property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=185 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_schedule`
--

DROP TABLE IF EXISTS `report_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_schedule` (
  `report_schedule_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(11) NOT NULL,
  `run_interval` int(11) NOT NULL,
  PRIMARY KEY (`report_schedule_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `report_schedule_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_sequence`
--

DROP TABLE IF EXISTS `report_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_sequence` (
  `report_sequence_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`report_sequence_id`),
  KEY `report_sequence_ibfk1` (`analysis_item_id`),
  CONSTRAINT `report_sequence_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_state`
--

DROP TABLE IF EXISTS `report_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_state` (
  `report_state_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`report_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_string_property`
--

DROP TABLE IF EXISTS `report_string_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_string_property` (
  `report_string_property_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_property_id` bigint(20) NOT NULL,
  `property_value` varchar(255) NOT NULL,
  PRIMARY KEY (`report_string_property_id`),
  KEY `report_string_property_ibfk1` (`report_property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_structure`
--

DROP TABLE IF EXISTS `report_structure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_structure` (
  `report_structure_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `structure_key` varchar(20) NOT NULL,
  `analysis_id` bigint(11) NOT NULL,
  `analysis_item_id` bigint(11) NOT NULL,
  PRIMARY KEY (`report_structure_id`),
  KEY `report_structure_ibfk1` (`analysis_id`),
  KEY `report_structure_ibfk2` (`analysis_item_id`),
  CONSTRAINT `report_structure_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `report_structure_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_to_group_notification`
--

DROP TABLE IF EXISTS `report_to_group_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_to_group_notification` (
  `report_to_group_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `analysis_id` bigint(20) NOT NULL,
  `analysis_role` int(11) NOT NULL,
  `analysis_action` int(11) NOT NULL,
  PRIMARY KEY (`report_to_group_notification_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `report_to_group_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE,
  CONSTRAINT `report_to_group_notification_ibfk2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_to_report_property`
--

DROP TABLE IF EXISTS `report_to_report_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_to_report_property` (
  `report_to_report_property_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) NOT NULL,
  `report_property_id` bigint(20) NOT NULL,
  PRIMARY KEY (`report_to_report_property_id`),
  KEY `report_to_report_property_ibfk1` (`analysis_id`),
  KEY `report_to_report_property_ibfk2` (`report_property_id`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rolling_range_filter`
--

DROP TABLE IF EXISTS `rolling_range_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolling_range_filter` (
  `rolling_range_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interval_value` int(11) NOT NULL,
  `filter_id` bigint(20) DEFAULT NULL,
  `before_or_after` tinyint(4) NOT NULL DEFAULT '0',
  `interval_type` int(11) DEFAULT '0',
  `interval_amount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`rolling_range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `rolling_range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `salesforce_definition`
--

DROP TABLE IF EXISTS `salesforce_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `salesforce_definition` (
  `salesforce_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`salesforce_definition_id`),
  KEY `salesforce_definition_ibfk1` (`data_source_id`),
  CONSTRAINT `salesforce_definition_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  `schedule_type` int(11) NOT NULL,
  `schedule_hour` int(11) NOT NULL,
  `schedule_minute` int(11) NOT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `schedule_ibfk1` (`scheduled_account_activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduled_account_activity`
--

DROP TABLE IF EXISTS `scheduled_account_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduled_account_activity` (
  `scheduled_account_activity_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `activity_type` int(11) NOT NULL,
  PRIMARY KEY (`scheduled_account_activity_id`),
  KEY `scheduled_user_activity_ibfk1` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduled_data_source_refresh`
--

DROP TABLE IF EXISTS `scheduled_data_source_refresh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduled_data_source_refresh` (
  `scheduled_data_source_refresh_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`scheduled_data_source_refresh_id`),
  KEY `scheduled_data_source_refresh_ibfk1` (`scheduled_account_activity_id`),
  KEY `scheduled_data_source_refresh_ibfk2` (`data_source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduled_task`
--

DROP TABLE IF EXISTS `scheduled_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduled_task` (
  `scheduled_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL,
  `scheduled_time` datetime NOT NULL,
  `started_time` datetime DEFAULT NULL,
  `stopped_time` datetime DEFAULT NULL,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`scheduled_task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=305 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scorecard`
--

DROP TABLE IF EXISTS `scorecard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scorecard` (
  `scorecard_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scorecard_name` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `scorecard_order` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`scorecard_id`),
  KEY `scorecard_ibfk1` (`user_id`),
  KEY `scorecard_ibfk2` (`group_id`),
  CONSTRAINT `scorecard_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `scorecard_ibfk2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scorecard_to_kpi`
--

DROP TABLE IF EXISTS `scorecard_to_kpi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scorecard_to_kpi` (
  `scorecard_to_kpi_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scorecard_id` bigint(20) NOT NULL,
  `kpi_id` bigint(20) NOT NULL,
  PRIMARY KEY (`scorecard_to_kpi_id`),
  KEY `scorecard_to_kpi_ibfk1` (`scorecard_id`),
  KEY `scorecard_to_kpi_ibfk2` (`kpi_id`),
  CONSTRAINT `scorecard_to_kpi_ibfk1` FOREIGN KEY (`scorecard_id`) REFERENCES `scorecard` (`scorecard_id`) ON DELETE CASCADE,
  CONSTRAINT `scorecard_to_kpi_ibfk2` FOREIGN KEY (`kpi_id`) REFERENCES `kpi` (`kpi_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `server_refresh_task`
--

DROP TABLE IF EXISTS `server_refresh_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server_refresh_task` (
  `server_refresh_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`server_refresh_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `server_refresh_task_ibfk3` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `session_id_storage`
--

DROP TABLE IF EXISTS `session_id_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `session_id_storage` (
  `session_id_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `session_id` varchar(255) NOT NULL,
  PRIMARY KEY (`session_id_id`),
  KEY `data_feed_id` (`data_feed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `six_sigma_measure`
--

DROP TABLE IF EXISTS `six_sigma_measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `six_sigma_measure` (
  `six_sigma_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `sigma_type` int(11) NOT NULL,
  `defects_measure_id` bigint(20) NOT NULL,
  `opportunities_measure_id` bigint(20) NOT NULL,
  PRIMARY KEY (`six_sigma_measure_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `defects_measure_id` (`defects_measure_id`),
  KEY `opportunities_measure_id` (`opportunities_measure_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solution`
--

DROP TABLE IF EXISTS `solution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `solution` (
  `solution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` text,
  `archive` longblob,
  `solution_archive_name` varchar(255) DEFAULT NULL,
  `copy_data` tinyint(4) DEFAULT NULL,
  `industry` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `goal_tree_id` bigint(20) DEFAULT NULL,
  `solution_tier` int(11) NOT NULL DEFAULT '2',
  `solution_image` longblob,
  `category` int(11) NOT NULL DEFAULT '1',
  `screencast_directory` varchar(40) DEFAULT NULL,
  `screencast_mp4_name` varchar(40) DEFAULT NULL,
  `footer_text` text,
  `logo_link` varchar(255) DEFAULT NULL,
  `detail_page_class` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`solution_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solution_install`
--

DROP TABLE IF EXISTS `solution_install`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `solution_install` (
  `solution_install_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `solution_id` bigint(20) NOT NULL,
  `original_data_source_id` bigint(20) NOT NULL,
  `installed_data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`solution_install_id`),
  KEY `solution_install_ibfk1` (`solution_id`),
  KEY `solution_install_ibfk2` (`original_data_source_id`),
  KEY `solution_install_ibfk3` (`installed_data_source_id`),
  CONSTRAINT `solution_install_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE,
  CONSTRAINT `solution_install_ibfk2` FOREIGN KEY (`original_data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `solution_install_ibfk3` FOREIGN KEY (`installed_data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solution_tag`
--

DROP TABLE IF EXISTS `solution_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `solution_tag` (
  `solution_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `solution_id` bigint(20) NOT NULL,
  `tag_name` varchar(255) NOT NULL,
  PRIMARY KEY (`solution_tag_id`),
  KEY `solution_id` (`solution_id`),
  CONSTRAINT `solution_tag_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solution_to_feed`
--

DROP TABLE IF EXISTS `solution_to_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `solution_to_feed` (
  `solution_to_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `solution_id` bigint(20) NOT NULL,
  PRIMARY KEY (`solution_to_feed_id`),
  KEY `solution_to_feed_ibfk1` (`solution_id`),
  KEY `solution_to_feed_ibfk2` (`feed_id`),
  CONSTRAINT `solution_to_feed_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE,
  CONSTRAINT `solution_to_feed_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solution_to_goal_tree`
--

DROP TABLE IF EXISTS `solution_to_goal_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `solution_to_goal_tree` (
  `solution_to_goal_tree_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `solution_id` bigint(20) NOT NULL,
  `goal_tree_id` bigint(20) NOT NULL,
  PRIMARY KEY (`solution_to_goal_tree_id`),
  KEY `solution_id` (`solution_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  CONSTRAINT `solution_to_goal_tree_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE,
  CONSTRAINT `solution_to_goal_tree_ibfk2` FOREIGN KEY (`goal_tree_id`) REFERENCES `goal_tree` (`goal_tree_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store` (
  `store_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_name` varchar(100) NOT NULL,
  `store_description` text,
  `merchant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`store_id`),
  KEY `merchant_id` (`merchant_id`),
  CONSTRAINT `store_ibfk_1` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`merchant_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `string_value`
--

DROP TABLE IF EXISTS `string_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `string_value` (
  `string_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  `string_content` varchar(255) NOT NULL,
  PRIMARY KEY (`string_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `string_value_ibfk_1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=375 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag_cloud`
--

DROP TABLE IF EXISTS `tag_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_cloud` (
  `tag_cloud_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`tag_cloud_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag_cloud_to_tag`
--

DROP TABLE IF EXISTS `tag_cloud_to_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag_cloud_to_tag` (
  `tag_cloud_to_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_cloud_id` bigint(20) DEFAULT NULL,
  `analysis_tags_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tag_cloud_to_tag_id`),
  KEY `tag_cloud_id` (`tag_cloud_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `tag_cloud_to_tag_ibfk_1` FOREIGN KEY (`tag_cloud_id`) REFERENCES `tag_cloud` (`tag_cloud_id`) ON DELETE CASCADE,
  CONSTRAINT `tag_cloud_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_generator`
--

DROP TABLE IF EXISTS `task_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_generator` (
  `task_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_interval` int(11) NOT NULL,
  `last_task_date` datetime DEFAULT NULL,
  `start_task_date` datetime NOT NULL,
  `requires_backfill` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`task_generator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `text_replace_scrub`
--

DROP TABLE IF EXISTS `text_replace_scrub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_replace_scrub` (
  `text_replace_scrub_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_scrub_id` bigint(20) NOT NULL,
  `source_text` varchar(255) DEFAULT NULL,
  `target_text` varchar(255) DEFAULT NULL,
  `regex` tinyint(4) DEFAULT NULL,
  `case_sensitive` tinyint(4) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`text_replace_scrub_id`),
  KEY `text_replace_scrub_ibfk1` (`data_scrub_id`),
  KEY `text_replace_scrub_ibfk2` (`analysis_item_id`),
  CONSTRAINT `text_replace_scrub_ibfk1` FOREIGN KEY (`data_scrub_id`) REFERENCES `data_scrub` (`data_scrub_id`) ON DELETE CASCADE,
  CONSTRAINT `text_replace_scrub_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `time_based_analysis_measure`
--

DROP TABLE IF EXISTS `time_based_analysis_measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_based_analysis_measure` (
  `time_based_analysis_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `date_dimension` bigint(20) DEFAULT NULL,
  `wrapped_aggregation` int(11) DEFAULT NULL,
  PRIMARY KEY (`time_based_analysis_measure_id`),
  KEY `time_based_analysis_measure_ibfk1` (`analysis_item_id`),
  KEY `time_based_analysis_measure_ibfk2` (`date_dimension`),
  CONSTRAINT `time_based_analysis_measure_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `time_based_analysis_measure_ibfk2` FOREIGN KEY (`date_dimension`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `timeline_report`
--

DROP TABLE IF EXISTS `timeline_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timeline_report` (
  `timeline_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contained_report_id` bigint(20) NOT NULL,
  `report_sequence_id` bigint(20) NOT NULL,
  `report_state_id` bigint(20) NOT NULL,
  PRIMARY KEY (`timeline_report_id`),
  KEY `timeline_report_ibfk1` (`contained_report_id`),
  KEY `timeline_report_ibfk2` (`report_sequence_id`),
  KEY `timeline_report_ibfk3` (`report_state_id`),
  CONSTRAINT `timeline_report_ibfk1` FOREIGN KEY (`contained_report_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `timeline_report_ibfk2` FOREIGN KEY (`report_sequence_id`) REFERENCES `report_sequence` (`report_sequence_id`) ON DELETE CASCADE,
  CONSTRAINT `timeline_report_ibfk3` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `todo_base`
--

DROP TABLE IF EXISTS `todo_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todo_base` (
  `todo_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `todo_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`todo_id`),
  KEY `todo_base_ibfk1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `token_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `token_type` int(11) NOT NULL,
  `token_value` varchar(255) NOT NULL,
  `data_source_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`token_id`),
  KEY `user_id` (`user_id`),
  KEY `token_ibfk3` (`data_source_id`),
  CONSTRAINT `token_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `token_ibfk3` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tr_schedule`
--

DROP TABLE IF EXISTS `tr_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_schedule` (
  `tr_schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `schedule_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tr_schedule_id`),
  KEY `tr_schedule_ibfk1` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tree_report`
--

DROP TABLE IF EXISTS `tree_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tree_report` (
  `tree_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tree_report_id`),
  KEY `report_state_id` (`report_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `treemap_report`
--

DROP TABLE IF EXISTS `treemap_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treemap_report` (
  `treemap_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `color_scheme` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`treemap_report_id`),
  KEY `report_state_id` (`report_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twitter`
--

DROP TABLE IF EXISTS `twitter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `twitter` (
  `data_feed_id` bigint(20) NOT NULL,
  `search` varchar(160) NOT NULL,
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `twitter_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ui_visibility_setting`
--

DROP TABLE IF EXISTS `ui_visibility_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ui_visibility_setting` (
  `ui_visibility_setting_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `persona_id` bigint(20) NOT NULL,
  `config_element` varchar(255) NOT NULL,
  `visible` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ui_visibility_setting_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2574 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload_policy_groups`
--

DROP TABLE IF EXISTS `upload_policy_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_policy_groups` (
  `upload_policy_groups_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`upload_policy_groups_id`),
  KEY `upload_policy_groups_ibfk1` (`feed_id`),
  KEY `upload_policy_groups_ibfk2` (`group_id`),
  CONSTRAINT `upload_policy_groups_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `upload_policy_groups_ibfk2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload_policy_users`
--

DROP TABLE IF EXISTS `upload_policy_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload_policy_users` (
  `upload_policy_users_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`upload_policy_users_id`),
  KEY `upload_policy_users_ibfk2` (`user_id`),
  KEY `upload_policy_users_ibfk1` (`feed_id`),
  CONSTRAINT `upload_policy_users_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `upload_policy_users_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3376 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `url_link`
--

DROP TABLE IF EXISTS `url_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `url_link` (
  `url_link_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link_id` bigint(20) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`url_link_id`),
  KEY `url_link_ibfk1` (`link_id`),
  CONSTRAINT `url_link_ibfk1` FOREIGN KEY (`link_id`) REFERENCES `link` (`link_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `password` varchar(40) NOT NULL,
  `name` varchar(60) NOT NULL,
  `email` varchar(60) NOT NULL,
  `username` varchar(40) NOT NULL,
  `account_admin` tinyint(4) NOT NULL DEFAULT '1',
  `data_source_creator` tinyint(4) NOT NULL DEFAULT '0',
  `insight_creator` tinyint(4) NOT NULL DEFAULT '0',
  `user_key` varchar(20) DEFAULT NULL,
  `user_secret_key` varchar(20) DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `persona_id` bigint(20) DEFAULT NULL,
  `profile_image` blob,
  `first_name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `initial_setup_done` tinyint(4) NOT NULL DEFAULT '1',
  `renewal_option_available` tinyint(4) NOT NULL DEFAULT '0',
  `opt_in_email` tinyint(4) NOT NULL DEFAULT '0',
  `guest_user` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`),
  UNIQUE KEY `username` (`username`),
  KEY `account_id` (`account_id`),
  KEY `user_ibfk5` (`persona_id`),
  CONSTRAINT `user_ibfk5` FOREIGN KEY (`persona_id`) REFERENCES `persona` (`persona_id`) ON DELETE SET NULL,
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_action_audit`
--

DROP TABLE IF EXISTS `user_action_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_action_audit` (
  `user_action_audit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `action_name` varchar(255) NOT NULL,
  `action_date` datetime NOT NULL,
  PRIMARY KEY (`user_action_audit_id`),
  KEY `user_action_audit_ibfk1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1186 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_data_source_rating`
--

DROP TABLE IF EXISTS `user_data_source_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_data_source_rating` (
  `user_data_source_rating_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  PRIMARY KEY (`user_data_source_rating_id`),
  KEY `user_data_source_rating_ibfk1` (`data_source_id`),
  KEY `user_data_source_rating_ibfk2` (`user_id`),
  CONSTRAINT `user_data_source_rating_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `user_data_source_rating_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_notification`
--

DROP TABLE IF EXISTS `user_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_notification` (
  `user_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_notification_id`),
  KEY `user_id` (`user_id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `user_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE,
  CONSTRAINT `user_notification_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_package_rating`
--

DROP TABLE IF EXISTS `user_package_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_package_rating` (
  `user_package_rating_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `report_package_id` bigint(20) NOT NULL,
  `rating` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_package_rating_id`),
  KEY `user_package_rating_ibfk1` (`user_id`),
  KEY `user_package_rating_ibfk2` (`report_package_id`),
  CONSTRAINT `user_package_rating_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_package_rating_ibfk2` FOREIGN KEY (`report_package_id`) REFERENCES `report_package` (`report_package_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_permission`
--

DROP TABLE IF EXISTS `user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_permission` (
  `user_permission_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(100) DEFAULT NULL,
  `accounts_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_permission_id`),
  KEY `accounts_id` (`accounts_id`),
  CONSTRAINT `user_permission_ibfk_1` FOREIGN KEY (`accounts_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_preferences`
--

DROP TABLE IF EXISTS `user_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_preferences` (
  `user_preferences_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `intro_option` tinyint(4) NOT NULL,
  `mydata_option` tinyint(4) NOT NULL,
  `marketplace_option` tinyint(4) NOT NULL,
  `groups_option` tinyint(4) NOT NULL,
  `kpi_option` tinyint(4) NOT NULL,
  `solutions_option` tinyint(4) NOT NULL,
  `api_option` tinyint(4) NOT NULL,
  `accounts_options` tinyint(4) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_preferences_id`),
  KEY `user_preferences_ibfk1` (`user_id`),
  CONSTRAINT `user_preferences_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_report_rating`
--

DROP TABLE IF EXISTS `user_report_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_report_rating` (
  `user_report_rating_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `rating` int(11) NOT NULL,
  PRIMARY KEY (`user_report_rating_id`),
  KEY `user_report_rating_ibfk1` (`report_id`),
  KEY `user_report_rating_ibfk2` (`user_id`),
  CONSTRAINT `user_report_rating_ibfk1` FOREIGN KEY (`report_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `user_report_rating_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_scorecard_display`
--

DROP TABLE IF EXISTS `user_scorecard_display`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_scorecard_display` (
  `user_scorecard_display_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `html_email` tinyint(4) NOT NULL,
  `scheduled_account_activity_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_scorecard_display_id`),
  KEY `user_scorecard_display_ibfk1` (`user_id`),
  KEY `user_scorecard_display_ibfk2` (`scheduled_account_activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_scorecard_ordering`
--

DROP TABLE IF EXISTS `user_scorecard_ordering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_scorecard_ordering` (
  `user_scorecard_ordering_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `scorecard_id` bigint(20) NOT NULL,
  `scorecard_order` bigint(20) NOT NULL,
  PRIMARY KEY (`user_scorecard_ordering_id`),
  KEY `user_scorecard_ordering_ibfk1` (`user_id`),
  KEY `user_scorecard_ordering_ibfk2` (`scorecard_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_session`
--

DROP TABLE IF EXISTS `user_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_session` (
  `user_session_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `session_number` varchar(255) NOT NULL,
  `user_session_date` datetime NOT NULL,
  PRIMARY KEY (`user_session_id`),
  KEY `user_session_ibfk1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_analysis`
--

DROP TABLE IF EXISTS `user_to_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_analysis` (
  `user_to_analysis_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `analysis_id` bigint(20) DEFAULT NULL,
  `relationship_type` int(11) DEFAULT NULL,
  `open` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`user_to_analysis_id`),
  KEY `user_id` (`user_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `user_to_analysis_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_analysis_ibfk_2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_data_source_notification`
--

DROP TABLE IF EXISTS `user_to_data_source_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_data_source_notification` (
  `user_to_data_source_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `feed_action` int(11) NOT NULL,
  `feed_role` int(11) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_to_data_source_notification_id`),
  KEY `feed_id` (`feed_id`),
  KEY `user_to_data_source_notification_ibfk1` (`notification_id`),
  CONSTRAINT `user_to_data_source_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_data_source_notification_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_feed`
--

DROP TABLE IF EXISTS `user_to_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_feed` (
  `user_to_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `data_feed_id` bigint(20) DEFAULT NULL,
  `user_role` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_to_feed_id`),
  KEY `user_id` (`user_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `user_to_feed_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_feed_ibfk_2` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_goal_tree`
--

DROP TABLE IF EXISTS `user_to_goal_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_goal_tree` (
  `user_to_goal_tree_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `user_role` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_to_goal_tree_id`),
  KEY `user_id` (`user_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  CONSTRAINT `user_to_goal_tree_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_goal_tree_ibfk2` FOREIGN KEY (`goal_tree_id`) REFERENCES `goal_tree` (`goal_tree_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_group_notification`
--

DROP TABLE IF EXISTS `user_to_group_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_group_notification` (
  `user_to_group_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `user_action` int(11) NOT NULL,
  PRIMARY KEY (`user_to_group_notification_id`),
  KEY `user_id` (`user_id`),
  KEY `notification_id` (`notification_id`),
  CONSTRAINT `user_to_group_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_group_notification_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_license_subscription`
--

DROP TABLE IF EXISTS `user_to_license_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_license_subscription` (
  `user_to_license_subscription_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `license_subscription_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_to_license_subscription_id`),
  KEY `user_id` (`user_id`),
  KEY `license_subscription_id` (`license_subscription_id`),
  CONSTRAINT `user_to_license_subscription_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_license_subscription_ibfk_2` FOREIGN KEY (`license_subscription_id`) REFERENCES `license_subscription` (`license_subscription_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_report_notification`
--

DROP TABLE IF EXISTS `user_to_report_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_report_notification` (
  `user_to_report_notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_id` bigint(20) NOT NULL,
  `analysis_action` int(11) NOT NULL,
  `analysis_role` int(11) NOT NULL,
  `analysis_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_to_report_notification_id`),
  KEY `notification_id` (`notification_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `user_to_report_notification_ibfk1` FOREIGN KEY (`notification_id`) REFERENCES `notification_base` (`notification_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_report_notification_ibfk2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_report_package`
--

DROP TABLE IF EXISTS `user_to_report_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_report_package` (
  `user_to_report_package_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  `report_package_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_to_report_package_id`),
  KEY `user_to_report_package_ibfk1` (`user_id`),
  KEY `user_to_report_package_ibfk2` (`report_package_id`),
  CONSTRAINT `user_to_report_package_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_report_package_ibfk2` FOREIGN KEY (`report_package_id`) REFERENCES `report_package` (`report_package_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_solution`
--

DROP TABLE IF EXISTS `user_to_solution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_solution` (
  `user_solution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `solution_id` bigint(20) DEFAULT NULL,
  `user_role` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_solution_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_to_solution_to_feed`
--

DROP TABLE IF EXISTS `user_to_solution_to_feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_to_solution_to_feed` (
  `user_to_solution_to_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_to_solution_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_to_solution_to_feed_id`),
  KEY `user_to_solution_id` (`user_to_solution_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `user_to_solution_to_feed_ibfk_1` FOREIGN KEY (`user_to_solution_id`) REFERENCES `user_solution` (`user_solution_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_solution_to_feed_ibfk_2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_unsubscribe_key`
--

DROP TABLE IF EXISTS `user_unsubscribe_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_unsubscribe_key` (
  `user_unsubscribe_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `unsubscribe_key` varchar(255) NOT NULL,
  PRIMARY KEY (`user_unsubscribe_key_id`),
  KEY `user_ibkf1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_upload`
--

DROP TABLE IF EXISTS `user_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_upload` (
  `user_upload_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `user_data` longblob,
  `data_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_upload_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `user_upload_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `value`
--

DROP TABLE IF EXISTS `value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `value` (
  `value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`value_id`)
) ENGINE=InnoDB AUTO_INCREMENT=375 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `value_based_filter`
--

DROP TABLE IF EXISTS `value_based_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `value_based_filter` (
  `value_based_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `inclusive` tinyint(4) NOT NULL,
  PRIMARY KEY (`value_based_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `value_based_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `virtual_dimension`
--

DROP TABLE IF EXISTS `virtual_dimension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `virtual_dimension` (
  `virtual_dimension_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_dimension_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `default_dimension_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`virtual_dimension_id`),
  KEY `analysis_dimension_id` (`analysis_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `virtual_dimension_to_virtual_transform`
--

DROP TABLE IF EXISTS `virtual_dimension_to_virtual_transform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `virtual_dimension_to_virtual_transform` (
  `virtual_dimension_to_virtual_transform_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `virtual_transform_id` bigint(20) NOT NULL,
  `virtual_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`virtual_dimension_to_virtual_transform_id`),
  KEY `virtual_transform_id` (`virtual_transform_id`),
  KEY `virtual_dimension_id` (`virtual_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `virtual_transform`
--

DROP TABLE IF EXISTS `virtual_transform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `virtual_transform` (
  `virtual_transform_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transform_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`virtual_transform_id`),
  KEY `transform_dimension_id` (`transform_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `virtual_transform_to_value`
--

DROP TABLE IF EXISTS `virtual_transform_to_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `virtual_transform_to_value` (
  `virtual_transform_to_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `virtual_transform_id` bigint(20) NOT NULL,
  `value_id` bigint(20) NOT NULL,
  PRIMARY KEY (`virtual_transform_to_value_id`),
  KEY `virtual_transform_id` (`virtual_transform_id`),
  KEY `value_id` (`value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weekday_schedule`
--

DROP TABLE IF EXISTS `weekday_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weekday_schedule` (
  `weekday_schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `schedule_id` bigint(20) NOT NULL,
  PRIMARY KEY (`weekday_schedule_id`),
  KEY `weekday_schedule_ibfk1` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weekly_schedule`
--

DROP TABLE IF EXISTS `weekly_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weekly_schedule` (
  `weekly_schedule_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `schedule_id` bigint(20) NOT NULL,
  `day_of_week` int(11) NOT NULL,
  PRIMARY KEY (`weekly_schedule_id`),
  KEY `weekly_schedule_ibfk1` (`schedule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wow_armory`
--

DROP TABLE IF EXISTS `wow_armory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wow_armory` (
  `wow_armory_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `server` varchar(255) NOT NULL,
  `guild_name` varchar(255) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`wow_armory_id`),
  KEY `wow_armory_ibfk1` (`data_source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `yahoo_map_definition`
--

DROP TABLE IF EXISTS `yahoo_map_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `yahoo_map_definition` (
  `yahoo_map_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `graphic_definition_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`yahoo_map_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `graphic_definition_id` (`graphic_definition_id`),
  CONSTRAINT `yahoo_map_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `yahoo_map_definition_ibfk_2` FOREIGN KEY (`graphic_definition_id`) REFERENCES `graphic_definition` (`graphic_definition_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `zip_code_geocode`
--

DROP TABLE IF EXISTS `zip_code_geocode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zip_code_geocode` (
  `zip_code_geocode_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `zip_code` varchar(20) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  PRIMARY KEY (`zip_code_geocode_id`),
  KEY `zip_code` (`zip_code`)
) ENGINE=InnoDB AUTO_INCREMENT=29471 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-09-15  9:23:57
