-- MySQL dump 10.13  Distrib 5.1.32, for Win64 (unknown)
--
-- Host: localhost    Database: dms
-- ------------------------------------------------------
-- Server version	5.1.32-community-log

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account` (
  `account_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_type` int(11) NOT NULL,
  `max_size` bigint(20) DEFAULT NULL,
  `max_users` int(11) DEFAULT NULL,
  `unchecked_api_enabled` tinyint(1) DEFAULT '1',
  `validated_api_enabled` tinyint(1) DEFAULT '1',
  `unchecked_api_allowed` tinyint(1) DEFAULT '1',
  `validated_api_allowed` tinyint(1) DEFAULT '1',
  `dynamic_api_enabled` tinyint(1) DEFAULT '1',
  `basic_auth_allowed` tinyint(1) DEFAULT '1',
  `name` varchar(255) DEFAULT NULL,
  `account_state` int(11) NOT NULL DEFAULT '2',
  `group_id` bigint(11) DEFAULT NULL,
  `account_key` varchar(20) DEFAULT NULL,
  `account_secret_key` varchar(20) DEFAULT NULL,
  `billing_information_given` tinyint(4) DEFAULT NULL,
  `billing_transactionid` varchar(255) DEFAULT NULL,
  `billing_day_of_month` int(11) DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_activation`
--

DROP TABLE IF EXISTS `account_activation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_activity`
--

DROP TABLE IF EXISTS `account_activity`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_activity` (
  `account_activity_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `activity_date` datetime NOT NULL,
  `account_type` int(11) NOT NULL,
  `activity_type` int(11) NOT NULL,
  `activity_notes` text,
  `user_licenses` int(11) NOT NULL,
  `max_users` int(11) NOT NULL DEFAULT '0',
  `max_size` bigint(20) NOT NULL DEFAULT '0',
  `account_state` int(11) DEFAULT NULL,
  PRIMARY KEY (`account_activity_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_activity_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_billing_task`
--

DROP TABLE IF EXISTS `account_billing_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_billing_task` (
  `account_billing_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_billing_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`),
  CONSTRAINT `account_billing_task_ibfk1` FOREIGN KEY (`scheduled_task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_credit_card_billing_info`
--

DROP TABLE IF EXISTS `account_credit_card_billing_info`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=540 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_payment`
--

DROP TABLE IF EXISTS `account_payment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_payment_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_time_scheduler`
--

DROP TABLE IF EXISTS `account_time_scheduler`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_time_scheduler` (
  `account_time_scheduler_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_time_scheduler_id`),
  KEY `task_generator_id` (`task_generator_id`),
  CONSTRAINT `account_time_scheduler_ibfk1` FOREIGN KEY (`task_generator_id`) REFERENCES `task_generator` (`task_generator_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_time_task`
--

DROP TABLE IF EXISTS `account_time_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_time_task` (
  `account_time_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`account_time_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`),
  CONSTRAINT `account_time_task_ibfk1` FOREIGN KEY (`scheduled_task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_timed_state`
--

DROP TABLE IF EXISTS `account_timed_state`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_timed_state` (
  `account_timed_state_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `state_change_time` datetime NOT NULL,
  `account_state` int(11) NOT NULL,
  PRIMARY KEY (`account_timed_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_to_feed`
--

DROP TABLE IF EXISTS `account_to_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_to_guest_user`
--

DROP TABLE IF EXISTS `account_to_guest_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_to_merchant`
--

DROP TABLE IF EXISTS `account_to_merchant`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_to_user`
--

DROP TABLE IF EXISTS `account_to_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `account_user_license`
--

DROP TABLE IF EXISTS `account_user_license`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `account_user_license` (
  `account_user_license_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) NOT NULL,
  `creation_date` datetime NOT NULL,
  `account_id` bigint(11) NOT NULL,
  PRIMARY KEY (`account_user_license_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_user_license_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `additional_items`
--

DROP TABLE IF EXISTS `additional_items`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `additional_items` (
  `additional_items_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`additional_items_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `additional_items_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `additional_items_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis`
--

DROP TABLE IF EXISTS `analysis`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  `report_structure_id` bigint(11) DEFAULT NULL,
  `report_state_id` bigint(20) DEFAULT NULL,
  `report_type` int(11) NOT NULL DEFAULT '1',
  `description` text,
  PRIMARY KEY (`analysis_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `analysis_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=495 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_based_feed`
--

DROP TABLE IF EXISTS `analysis_based_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_calculation`
--

DROP TABLE IF EXISTS `analysis_calculation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_calculation` (
  `analysis_calculation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `calculation_string` text NOT NULL,
  PRIMARY KEY (`analysis_calculation_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_calculation_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_date`
--

DROP TABLE IF EXISTS `analysis_date`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_date` (
  `analysis_date_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_level` int(11) DEFAULT NULL,
  `custom_date_format` varchar(100) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_date_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_date_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=668 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_dimension`
--

DROP TABLE IF EXISTS `analysis_dimension`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_dimension` (
  `analysis_dimension_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_by` tinyint(4) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `key_dimension_id` bigint(20) DEFAULT NULL,
  `hierarchy_id` bigint(20) DEFAULT NULL,
  `hierarchy_level_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_dimension_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `key_dimension_id` (`key_dimension_id`),
  CONSTRAINT `analysis_dimension_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_dimension_ibfk_2` FOREIGN KEY (`key_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3619 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_hierarchy_item`
--

DROP TABLE IF EXISTS `analysis_hierarchy_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_hierarchy_item` (
  `analysis_hierarchy_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `hierarchy_level_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_hierarchy_item_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_hierarchy_item_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_hierarchy_item_to_hierarchy_level`
--

DROP TABLE IF EXISTS `analysis_hierarchy_item_to_hierarchy_level`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_hierarchy_item_to_hierarchy_level` (
  `analysis_hierarchy_item_to_hierarchy_level_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `hierarchy_level_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_hierarchy_item_to_hierarchy_level_id`),
  KEY `hierarchy_level_id` (`hierarchy_level_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_hierarchy_item_to_hierarchy_level_ibfk1` FOREIGN KEY (`hierarchy_level_id`) REFERENCES `hierarchy_level` (`hierarchy_level_id`),
  CONSTRAINT `analysis_hierarchy_item_to_hierarchy_level_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_item`
--

DROP TABLE IF EXISTS `analysis_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_item` (
  `analysis_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_key_id` bigint(20) NOT NULL,
  `hidden` tinyint(4) DEFAULT NULL,
  `formatting_configuration_id` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `width` int(11) DEFAULT '0',
  `virtual_dimension_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_item_id`),
  KEY `item_key_id` (`item_key_id`),
  CONSTRAINT `analysis_item_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5971 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_list`
--

DROP TABLE IF EXISTS `analysis_list`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_list` (
  `analysis_list_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `delimiter` varchar(10) DEFAULT NULL,
  `expanded` tinyint(4) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_list_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_list_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_measure`
--

DROP TABLE IF EXISTS `analysis_measure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2353 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_range`
--

DROP TABLE IF EXISTS `analysis_range`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_range` (
  `analysis_range_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_range_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_range_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_step`
--

DROP TABLE IF EXISTS `analysis_step`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_step` (
  `analysis_step_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `correlation_dimension_id` bigint(20) DEFAULT NULL,
  `start_date_dimension_id` bigint(20) DEFAULT NULL,
  `end_date_dimension_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_step_id`),
  KEY `correlation_dimension_id` (`correlation_dimension_id`),
  KEY `start_date_dimension_id` (`start_date_dimension_id`),
  KEY `end_date_dimension_id` (`end_date_dimension_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_step_ibfk1` FOREIGN KEY (`correlation_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_step_ibfk2` FOREIGN KEY (`start_date_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_step_ibfk3` FOREIGN KEY (`end_date_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_step_ibfk4` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_tags`
--

DROP TABLE IF EXISTS `analysis_tags`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_tags` (
  `analysis_tags_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(100) DEFAULT NULL,
  `use_count` int(11) DEFAULT '0',
  PRIMARY KEY (`analysis_tags_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_to_analysis_item`
--

DROP TABLE IF EXISTS `analysis_to_analysis_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_to_data_scrub`
--

DROP TABLE IF EXISTS `analysis_to_data_scrub`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_to_data_scrub` (
  `analysis_to_data_scrub_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) NOT NULL,
  `data_scrub_id` bigint(20) NOT NULL,
  PRIMARY KEY (`analysis_to_data_scrub_id`),
  KEY `analysis_to_data_scrub_ibfk1` (`analysis_id`),
  KEY `analysis_to_data_scrub_ibfk2` (`data_scrub_id`),
  CONSTRAINT `analysis_to_data_scrub_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_data_scrub_ibfk2` FOREIGN KEY (`data_scrub_id`) REFERENCES `data_scrub` (`data_scrub_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_to_filter_join`
--

DROP TABLE IF EXISTS `analysis_to_filter_join`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_to_filter_join` (
  `analysis_to_filter_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `filter_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_to_filter_join_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `analysis_to_filter_join_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_filter_join_ibfk_2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_to_hierarchy_join`
--

DROP TABLE IF EXISTS `analysis_to_hierarchy_join`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_to_hierarchy_join` (
  `analysis_to_hierarchy_join` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_to_hierarchy_join`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `analysis_to_tag`
--

DROP TABLE IF EXISTS `analysis_to_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `analysis_to_tag` (
  `analysis_to_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_tags_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`analysis_to_tag_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `analysis_to_tag_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bandwidth_usage`
--

DROP TABLE IF EXISTS `bandwidth_usage`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bandwidth_usage` (
  `bandwidth_usage_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `used_bandwidth` bigint(20) NOT NULL DEFAULT '0',
  `bandwidth_date` date NOT NULL,
  PRIMARY KEY (`bandwidth_usage_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `bandwidth_usage_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `basecamp`
--

DROP TABLE IF EXISTS `basecamp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `basecamp` (
  `basecamp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`basecamp_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `basecamp_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `benchmark`
--

DROP TABLE IF EXISTS `benchmark`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `benchmark` (
  `category` varchar(40) NOT NULL,
  `elapsed_time` int(11) NOT NULL,
  KEY `category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `billing_scheduled_task`
--

DROP TABLE IF EXISTS `billing_scheduled_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `billing_scheduled_task` (
  `billing_scheduled_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`billing_scheduled_task_id`),
  KEY `billing_scheduled_task_ibfk1` (`scheduled_task_id`),
  CONSTRAINT `billing_scheduled_task_ibfk1` FOREIGN KEY (`scheduled_task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5034 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `billing_task_generator`
--

DROP TABLE IF EXISTS `billing_task_generator`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `billing_task_generator` (
  `billing_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`billing_generator_id`),
  KEY `task_generator_id` (`task_generator_id`),
  CONSTRAINT `billing_task_generator_ibfk1` FOREIGN KEY (`task_generator_id`) REFERENCES `task_generator` (`task_generator_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `chart_definition`
--

DROP TABLE IF EXISTS `chart_definition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `chart_field`
--

DROP TABLE IF EXISTS `chart_field`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `chart_field` (
  `chart_field_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `field_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`chart_field_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `chart_field_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `chart_field_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `chart_limits_metadata`
--

DROP TABLE IF EXISTS `chart_limits_metadata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `chart_limits_metadata` (
  `list_limits_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `top_items` tinyint(4) DEFAULT NULL,
  `number_items` int(11) DEFAULT NULL,
  PRIMARY KEY (`list_limits_metadata_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `charts_limits_metadata_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `chart_report`
--

DROP TABLE IF EXISTS `chart_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  KEY `limits_metadata_id` (`limits_metadata_id`),
  CONSTRAINT `chart_report_ibfk_1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE,
  CONSTRAINT `chart_report_ibfk_2` FOREIGN KEY (`limits_metadata_id`) REFERENCES `limits_metadata` (`limits_metadata_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `community_group`
--

DROP TABLE IF EXISTS `community_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `community_group` (
  `community_group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `publicly_visible` tinyint(4) DEFAULT NULL,
  `publicly_joinable` tinyint(4) DEFAULT NULL,
  `description` text,
  `tag_cloud_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`community_group_id`),
  KEY `tag_cloud_id` (`tag_cloud_id`),
  CONSTRAINT `community_group_ibfk1` FOREIGN KEY (`tag_cloud_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `complex_analysis_measure`
--

DROP TABLE IF EXISTS `complex_analysis_measure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `complex_analysis_measure` (
  `complex_analysis_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `wrapped_aggregation` int(11) DEFAULT NULL,
  PRIMARY KEY (`complex_analysis_measure_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `complex_analysis_measure_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `composite_connection`
--

DROP TABLE IF EXISTS `composite_connection`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `composite_connection` (
  `composite_connection_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `composite_feed_id` bigint(20) NOT NULL,
  `source_feed_node_id` bigint(20) NOT NULL,
  `target_feed_node_id` bigint(20) NOT NULL,
  `source_join` bigint(20) NOT NULL,
  `target_join` bigint(20) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `composite_feed`
--

DROP TABLE IF EXISTS `composite_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `composite_feed` (
  `composite_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`composite_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `composite_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `composite_node`
--

DROP TABLE IF EXISTS `composite_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `composite_node` (
  `composite_node_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `composite_feed_id` bigint(20) NOT NULL,
  `data_feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`composite_node_id`),
  KEY `composite_feed_id` (`composite_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `composite_node_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `composite_node_ibfk_2` FOREIGN KEY (`composite_feed_id`) REFERENCES `composite_feed` (`composite_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `configure_data_feed_todo`
--

DROP TABLE IF EXISTS `configure_data_feed_todo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `configure_data_feed_todo` (
  `configure_data_feed_todo_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `todo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`configure_data_feed_todo_id`),
  KEY `todo_id` (`todo_id`),
  KEY `data_source_id` (`data_source_id`),
  CONSTRAINT `configure_data_feed_todo_ibfk1` FOREIGN KEY (`todo_id`) REFERENCES `todo_base` (`todo_id`) ON DELETE CASCADE,
  CONSTRAINT `configure_data_feed_todo_ibfk2` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`dms`@`%`*/ /*!50003 trigger cascade_datafeed_delete_todos AFTER DELETE on configure_data_feed_todo
FOR EACH ROW DELETE FROM todo_base WHERE todo_id = OLD.todo_id */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `crosstab_definition`
--

DROP TABLE IF EXISTS `crosstab_definition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crosstab_definition` (
  `crosstab_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`crosstab_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `crosstab_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `crosstab_field`
--

DROP TABLE IF EXISTS `crosstab_field`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crosstab_field` (
  `crosstab_field_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `field_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`crosstab_field_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `crosstab_field_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `crosstab_measure_column`
--

DROP TABLE IF EXISTS `crosstab_measure_column`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crosstab_measure_column` (
  `crosstab_analysis_id` bigint(20) DEFAULT NULL,
  `crosstab_measure_column_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aggregation` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`crosstab_measure_column_id`),
  KEY `crosstab_analysis_id` (`crosstab_analysis_id`),
  CONSTRAINT `crosstab_measure_column_ibfk_1` FOREIGN KEY (`crosstab_analysis_id`) REFERENCES `crosstab_field` (`crosstab_field_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `crosstab_report`
--

DROP TABLE IF EXISTS `crosstab_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `crosstab_report` (
  `crosstab_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`crosstab_report_id`),
  KEY `report_state_id` (`report_state_id`),
  CONSTRAINT `crosstab_report_ibfk_1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_feed`
--

DROP TABLE IF EXISTS `data_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_feed` (
  `data_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_name` varchar(100) DEFAULT NULL,
  `feed_key` varchar(100) DEFAULT NULL,
  `feed_type` varchar(100) DEFAULT NULL,
  `policy` int(11) DEFAULT NULL,
  `genre` varchar(100) DEFAULT NULL,
  `feed_size` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `feed_views` int(11) DEFAULT NULL,
  `feed_rating_count` int(11) DEFAULT NULL,
  `feed_rating_average` double DEFAULT NULL,
  `analysis_id` bigint(20) DEFAULT NULL,
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
  PRIMARY KEY (`data_feed_id`),
  KEY `dynamic_service_definition_id` (`dynamic_service_definition_id`),
  CONSTRAINT `data_feed_ibfk1` FOREIGN KEY (`dynamic_service_definition_id`) REFERENCES `dynamic_service_definition` (`dynamic_service_definition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=447 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_scrub`
--

DROP TABLE IF EXISTS `data_scrub`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_scrub` (
  `data_scrub_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`data_scrub_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `data_scrub_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_source_audit_message`
--

DROP TABLE IF EXISTS `data_source_audit_message`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_source_comment`
--

DROP TABLE IF EXISTS `data_source_comment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_source_query`
--

DROP TABLE IF EXISTS `data_source_query`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_source_query` (
  `data_source_query_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_source_query_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`),
  KEY `data_source_id` (`data_source_id`),
  CONSTRAINT `data_source_query_task_ibfk1` FOREIGN KEY (`scheduled_task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE,
  CONSTRAINT `data_source_query_task_ibfk2` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_source_task_generator`
--

DROP TABLE IF EXISTS `data_source_task_generator`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_source_task_generator` (
  `data_source_task_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_source_task_generator_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `task_generator_id` (`task_generator_id`),
  CONSTRAINT `data_source_task_generator_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `data_source_task_generator_ibfk2` FOREIGN KEY (`task_generator_id`) REFERENCES `task_generator` (`task_generator_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `data_source_to_virtual_dimension`
--

DROP TABLE IF EXISTS `data_source_to_virtual_dimension`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `data_source_to_virtual_dimension` (
  `data_source_to_virtual_dimension_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `virtual_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`data_source_to_virtual_dimension_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `virtual_dimension_id` (`virtual_dimension_id`),
  CONSTRAINT `data_source_to_virtual_dimension_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`),
  CONSTRAINT `data_source_to_virtual_dimension_ibfk2` FOREIGN KEY (`virtual_dimension_id`) REFERENCES `virtual_dimension` (`virtual_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `database_version`
--

DROP TABLE IF EXISTS `database_version`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `database_version` (
  `database_version_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '37',
  PRIMARY KEY (`database_version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `date_range_filter`
--

DROP TABLE IF EXISTS `date_range_filter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `date_range_filter` (
  `date_range_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `low_value` datetime NOT NULL,
  `high_value` datetime NOT NULL,
  PRIMARY KEY (`date_range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `date_range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `date_value`
--

DROP TABLE IF EXISTS `date_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `date_value` (
  `date_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  `date_contet` datetime NOT NULL,
  PRIMARY KEY (`date_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `date_value_ibfk1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `db_snapshot_scheduler`
--

DROP TABLE IF EXISTS `db_snapshot_scheduler`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `db_snapshot_scheduler` (
  `db_snapshot_scheduler_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`db_snapshot_scheduler_id`),
  KEY `task_generator_id` (`task_generator_id`),
  CONSTRAINT `db_snapshot_scheduler_ibfk1` FOREIGN KEY (`task_generator_id`) REFERENCES `task_generator` (`task_generator_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `db_snapshot_task`
--

DROP TABLE IF EXISTS `db_snapshot_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `db_snapshot_task` (
  `db_snapshot_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`db_snapshot_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`),
  CONSTRAINT `db_snapshot_task_ibfk1` FOREIGN KEY (`scheduled_task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `derived_item_key`
--

DROP TABLE IF EXISTS `derived_item_key`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `distributed_lock`
--

DROP TABLE IF EXISTS `distributed_lock`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `distributed_lock` (
  `distributed_lock_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lock_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`distributed_lock_id`),
  UNIQUE KEY `lock_name` (`lock_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1052 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dynamic_service_code`
--

DROP TABLE IF EXISTS `dynamic_service_code`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dynamic_service_descriptor`
--

DROP TABLE IF EXISTS `dynamic_service_descriptor`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dynamic_service_descriptor` (
  `dynamic_service_descriptor_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`dynamic_service_descriptor_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `dynamic_service_descriptor_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dynamic_service_method`
--

DROP TABLE IF EXISTS `dynamic_service_method`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dynamic_service_method` (
  `dynamic_service_method_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `method_name` varchar(255) NOT NULL,
  `dynamic_service_descriptor_id` bigint(20) NOT NULL,
  `method_type` int(11) NOT NULL,
  PRIMARY KEY (`dynamic_service_method_id`),
  KEY `dynamic_service_descriptor_id` (`dynamic_service_descriptor_id`),
  CONSTRAINT `dynamic_service_method_ibfk_1` FOREIGN KEY (`dynamic_service_descriptor_id`) REFERENCES `dynamic_service_descriptor` (`dynamic_service_descriptor_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dynamic_service_method_key`
--

DROP TABLE IF EXISTS `dynamic_service_method_key`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `empty_value`
--

DROP TABLE IF EXISTS `empty_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `empty_value` (
  `empty_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`empty_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `empty_value_ibfk1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `event_base`
--

DROP TABLE IF EXISTS `event_base`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `event_base` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`event_id`),
  KEY `event_base_ibfk1` (`user_id`),
  CONSTRAINT `event_base_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `excel_export`
--

DROP TABLE IF EXISTS `excel_export`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `excel_export` (
  `excel_export_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `excel_file` longblob NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`excel_export_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `excel_upload_format`
--

DROP TABLE IF EXISTS `excel_upload_format`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `excel_upload_format` (
  `excel_upload_format_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`excel_upload_format_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `excel_upload_format_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed_commercial_policy`
--

DROP TABLE IF EXISTS `feed_commercial_policy`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed_group_policy`
--

DROP TABLE IF EXISTS `feed_group_policy`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `feed_group_policy` (
  `feed_group_policy_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feed_group_policy_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `feed_group_policy_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed_group_policy_group`
--

DROP TABLE IF EXISTS `feed_group_policy_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed_persistence_metadata`
--

DROP TABLE IF EXISTS `feed_persistence_metadata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `feed_persistence_metadata` (
  `feed_persistence_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `database_name` varchar(100) DEFAULT NULL,
  `last_data_time` datetime DEFAULT NULL,
  PRIMARY KEY (`feed_persistence_metadata_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `feed_persistence_metadata_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=495 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed_popularity`
--

DROP TABLE IF EXISTS `feed_popularity`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed_to_analysis_item`
--

DROP TABLE IF EXISTS `feed_to_analysis_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `feed_to_analysis_item` (
  `feed_to_analysis_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`feed_to_analysis_item_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `feed_to_analysis_item_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_to_analysis_item_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7866 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feed_to_tag`
--

DROP TABLE IF EXISTS `feed_to_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `feed_to_tag` (
  `feed_to_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) DEFAULT NULL,
  `analysis_tags_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`feed_to_tag_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `feed_to_tag_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `file_process_create_scheduled_task`
--

DROP TABLE IF EXISTS `file_process_create_scheduled_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  KEY `file_process_create_scheduled_task_ibfk4` (`account_id`),
  CONSTRAINT `file_process_create_scheduled_task_ibfk1` FOREIGN KEY (`task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE,
  CONSTRAINT `file_process_create_scheduled_task_ibfk2` FOREIGN KEY (`upload_id`) REFERENCES `user_upload` (`user_upload_id`) ON DELETE CASCADE,
  CONSTRAINT `file_process_create_scheduled_task_ibfk3` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `file_process_create_scheduled_task_ibfk4` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `file_process_update_scheduled_task`
--

DROP TABLE IF EXISTS `file_process_update_scheduled_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  KEY `file_process_update_scheduled_task_ibfk5` (`account_id`),
  CONSTRAINT `file_process_update_scheduled_task_ibfk1` FOREIGN KEY (`task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE,
  CONSTRAINT `file_process_update_scheduled_task_ibfk2` FOREIGN KEY (`upload_id`) REFERENCES `user_upload` (`user_upload_id`) ON DELETE CASCADE,
  CONSTRAINT `file_process_update_scheduled_task_ibfk3` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `file_process_update_scheduled_task_ibfk4` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `file_process_update_scheduled_task_ibfk5` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `filter`
--

DROP TABLE IF EXISTS `filter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `filter` (
  `filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_type` varchar(40) DEFAULT NULL,
  `inclusive` tinyint(4) DEFAULT NULL,
  `optional` tinyint(4) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  `apply_before_aggregation` tinyint(4) DEFAULT NULL,
  `intrinsic` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`filter_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `filter_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `filter_analysis_measure`
--

DROP TABLE IF EXISTS `filter_analysis_measure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `filter_analysis_measure` (
  `filter_analysis_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aggregation` varchar(40) DEFAULT NULL,
  `filter_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`filter_analysis_measure_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `filter_analysis_measure_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `filter_to_analysis_item`
--

DROP TABLE IF EXISTS `filter_to_analysis_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `filter_to_value`
--

DROP TABLE IF EXISTS `filter_to_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `filter_to_value` (
  `filter_to_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `value_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`filter_to_value_id`),
  KEY `filter_id` (`filter_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `filter_to_value_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE,
  CONSTRAINT `filter_to_value_ibfk_2` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `filter_value`
--

DROP TABLE IF EXISTS `filter_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `filter_value` (
  `filter_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_based_filter_id` bigint(20) DEFAULT NULL,
  `filter_value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`filter_value_id`),
  KEY `value_based_filter_id` (`value_based_filter_id`),
  CONSTRAINT `filter_value_ibfk_1` FOREIGN KEY (`value_based_filter_id`) REFERENCES `value_based_filter` (`value_based_filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `flat_file_upload_format`
--

DROP TABLE IF EXISTS `flat_file_upload_format`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `flat_file_upload_format` (
  `flat_file_upload_format_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `delimiter_escape` varchar(5) NOT NULL,
  `delimiter_pattern` varchar(5) NOT NULL,
  PRIMARY KEY (`flat_file_upload_format_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `flat_file_upload_format_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `formatting_configuration`
--

DROP TABLE IF EXISTS `formatting_configuration`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `formatting_configuration` (
  `formatting_configuration_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `formatting_type` int(11) NOT NULL,
  `text_uom` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`formatting_configuration_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3150 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gauge_report`
--

DROP TABLE IF EXISTS `gauge_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `gauge_report` (
  `gauge_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `gauge_type` int(11) NOT NULL,
  `max_value` int(11) NOT NULL,
  PRIMARY KEY (`gauge_report_id`),
  KEY `report_state_id` (`report_state_id`),
  CONSTRAINT `gauge_report_ibfk_1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gnip`
--

DROP TABLE IF EXISTS `gnip`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `gnip` (
  `gnip_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `publisher_id` varchar(255) NOT NULL,
  `publisher_scope` varchar(255) NOT NULL,
  `filter_id` varchar(255) NOT NULL,
  PRIMARY KEY (`gnip_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `gnip_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_history`
--

DROP TABLE IF EXISTS `goal_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `goal_history` (
  `goal_history_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `evaluation_date` datetime NOT NULL,
  `evaluation_result` double NOT NULL,
  PRIMARY KEY (`goal_history_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  CONSTRAINT `goal_history_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_node_to_user`
--

DROP TABLE IF EXISTS `goal_node_to_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_to_filter`
--

DROP TABLE IF EXISTS `goal_to_filter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_tree`
--

DROP TABLE IF EXISTS `goal_tree`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `goal_tree` (
  `goal_tree_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` text,
  `root_node` bigint(20) DEFAULT NULL,
  `goal_tree_icon` varchar(255) DEFAULT NULL,
  `default_milestone_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`goal_tree_id`),
  KEY `goal_tree_ibfk3` (`default_milestone_id`),
  CONSTRAINT `goal_tree_ibfk3` FOREIGN KEY (`default_milestone_id`) REFERENCES `milestone` (`milestone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_tree_node`
--

DROP TABLE IF EXISTS `goal_tree_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `goal_tree_node` (
  `goal_tree_node_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_goal_tree_node_id` bigint(20) DEFAULT NULL,
  `feed_id` bigint(20) DEFAULT NULL,
  `goal_value` int(11) DEFAULT NULL,
  `analysis_measure_id` bigint(20) DEFAULT NULL,
  `filter_id` bigint(20) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `description` text,
  `high_is_good` tinyint(4) DEFAULT NULL,
  `icon_image` varchar(255) DEFAULT NULL,
  `sub_tree_id` bigint(11) DEFAULT NULL,
  `goal_tree_id` bigint(20) NOT NULL,
  `goal_milestone_id` bigint(20) DEFAULT NULL,
  `goal_measure_description` varchar(255) DEFAULT NULL,
  `goal_defined` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`goal_tree_node_id`),
  KEY `parent_goal_tree_node_id` (`parent_goal_tree_node_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_measure_id` (`analysis_measure_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `goal_tree_node_ibfk1` FOREIGN KEY (`parent_goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE SET NULL,
  CONSTRAINT `goal_tree_node_ibfk3` FOREIGN KEY (`analysis_measure_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE SET NULL,
  CONSTRAINT `goal_tree_node_ibfk4` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_tree_node_tag`
--

DROP TABLE IF EXISTS `goal_tree_node_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `goal_tree_node_tag` (
  `goal_tree_node_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `tag` varchar(255) NOT NULL,
  PRIMARY KEY (`goal_tree_node_tag_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  CONSTRAINT `goal_tree_node_tag_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_tree_node_to_feed`
--

DROP TABLE IF EXISTS `goal_tree_node_to_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `goal_tree_node_to_feed` (
  `goal_tree_node_to_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_tree_node_to_feed_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `goal_tree_node_to_feed_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_feed_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_tree_node_to_insight`
--

DROP TABLE IF EXISTS `goal_tree_node_to_insight`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_tree_node_to_solution`
--

DROP TABLE IF EXISTS `goal_tree_node_to_solution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `goal_tree_node_to_solution` (
  `goal_tree_node_to_solution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `solution_id` bigint(20) NOT NULL,
  PRIMARY KEY (`goal_tree_node_to_solution_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `solution_id` (`solution_id`),
  CONSTRAINT `goal_tree_node_to_solution_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_solution_ibfk2` FOREIGN KEY (`solution_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `goal_tree_to_group`
--

DROP TABLE IF EXISTS `goal_tree_to_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `google_feed`
--

DROP TABLE IF EXISTS `google_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `google_feed` (
  `google_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(20) DEFAULT NULL,
  `worksheeturl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`google_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `google_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `graphic_definition`
--

DROP TABLE IF EXISTS `graphic_definition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `graphic_definition` (
  `graphic_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`graphic_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `graphic_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_audit_message`
--

DROP TABLE IF EXISTS `group_audit_message`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_comment`
--

DROP TABLE IF EXISTS `group_comment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_to_feed_join`
--

DROP TABLE IF EXISTS `group_to_feed_join`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `group_to_feed_join` (
  `group_to_feed_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `feed_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`group_to_feed_join_id`),
  KEY `feed_id` (`feed_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_feed_join_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_feed_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_to_goal_tree_join`
--

DROP TABLE IF EXISTS `group_to_goal_tree_join`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `group_to_goal_tree_join` (
  `group_to_goal_tree_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `goal_tree_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`group_to_goal_tree_join_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_goal_tree_join_ibfk_1` FOREIGN KEY (`goal_tree_id`) REFERENCES `goal_tree` (`goal_tree_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_goal_tree_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_to_goal_tree_node_join`
--

DROP TABLE IF EXISTS `group_to_goal_tree_node_join`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `group_to_goal_tree_node_join` (
  `group_to_goal_tree_node_join_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `goal_tree_node_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`group_to_goal_tree_node_join_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_goal_tree_node_join_ibfk_1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_goal_tree_node_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_to_insight`
--

DROP TABLE IF EXISTS `group_to_insight`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_to_tag`
--

DROP TABLE IF EXISTS `group_to_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_to_user_join`
--

DROP TABLE IF EXISTS `group_to_user_join`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `guest_user`
--

DROP TABLE IF EXISTS `guest_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `guest_user` (
  `guest_user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`guest_user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `guest_user_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `hierarchy`
--

DROP TABLE IF EXISTS `hierarchy`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `hierarchy` (
  `hierarchy_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`hierarchy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `hierarchy_level`
--

DROP TABLE IF EXISTS `hierarchy_level`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `hierarchy_level` (
  `hierarchy_level_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `level` int(11) DEFAULT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  `position` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`hierarchy_level_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `hierarchy_level_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `icon`
--

DROP TABLE IF EXISTS `icon`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `icon` (
  `icon_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `icon_name` varchar(100) DEFAULT NULL,
  `icon_file` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`icon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `insight_audit_message`
--

DROP TABLE IF EXISTS `insight_audit_message`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `insight_comment`
--

DROP TABLE IF EXISTS `insight_comment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `insight_policy_users`
--

DROP TABLE IF EXISTS `insight_policy_users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `item_key`
--

DROP TABLE IF EXISTS `item_key`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `item_key` (
  `item_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_key_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2343 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `jira`
--

DROP TABLE IF EXISTS `jira`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `jira` (
  `jira_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`jira_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `jira_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `last_n_filter`
--

DROP TABLE IF EXISTS `last_n_filter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `last_n_filter` (
  `last_n_filter_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(11) NOT NULL,
  `result_limit` int(11) NOT NULL,
  PRIMARY KEY (`last_n_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `last_n_filter_ibfk1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `license_subscription`
--

DROP TABLE IF EXISTS `license_subscription`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `limits_metadata`
--

DROP TABLE IF EXISTS `limits_metadata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `limits_metadata` (
  `limits_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `top_items` tinyint(4) DEFAULT NULL,
  `number_items` int(11) DEFAULT NULL,
  PRIMARY KEY (`limits_metadata_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `list_analysis_field`
--

DROP TABLE IF EXISTS `list_analysis_field`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `list_definition`
--

DROP TABLE IF EXISTS `list_definition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `list_definition` (
  `list_definition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  `list_limits_metadata_id` bigint(20) DEFAULT NULL,
  `show_row_numbers` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`list_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `list_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `list_limits_metadata`
--

DROP TABLE IF EXISTS `list_limits_metadata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `list_limits_metadata` (
  `list_limits_metadata_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `limits_metadata_id` bigint(20) DEFAULT NULL,
  `analysis_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`list_limits_metadata_id`),
  KEY `limits_metadata_id` (`limits_metadata_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `list_limits_metadata_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `list_limits_metadata_ibfk_2` FOREIGN KEY (`limits_metadata_id`) REFERENCES `limits_metadata` (`limits_metadata_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `list_report`
--

DROP TABLE IF EXISTS `list_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `list_report` (
  `list_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `list_limits_metadata_id` bigint(20) DEFAULT NULL,
  `show_row_numbers` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`list_report_id`),
  KEY `report_state_id` (`report_state_id`),
  CONSTRAINT `list_report_ibfk_1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_table_scrub`
--

DROP TABLE IF EXISTS `lookup_table_scrub`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `lookup_table_scrub_pair`
--

DROP TABLE IF EXISTS `lookup_table_scrub_pair`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `map_definition`
--

DROP TABLE IF EXISTS `map_definition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `map_report`
--

DROP TABLE IF EXISTS `map_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `map_report` (
  `map_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `map_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`map_report_id`),
  KEY `report_state_id` (`report_state_id`),
  CONSTRAINT `map_report_ibfk_1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `measure_condition`
--

DROP TABLE IF EXISTS `measure_condition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `measure_condition` (
  `measure_condition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `low_color` int(11) NOT NULL,
  `low_value` int(11) NOT NULL,
  `high_color` int(11) NOT NULL,
  `high_value` int(11) NOT NULL,
  PRIMARY KEY (`measure_condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `measure_condition_range`
--

DROP TABLE IF EXISTS `measure_condition_range`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `merchant`
--

DROP TABLE IF EXISTS `merchant`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `merchant` (
  `merchant_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) NOT NULL,
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `milestone`
--

DROP TABLE IF EXISTS `milestone`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `milestone` (
  `milestone_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `milestone_date` date NOT NULL,
  `milestone_name` varchar(100) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY (`milestone_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `milestone_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `named_item_key`
--

DROP TABLE IF EXISTS `named_item_key`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `named_item_key` (
  `named_item_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `item_key_id` bigint(20) NOT NULL,
  PRIMARY KEY (`named_item_key_id`),
  KEY `item_key_id` (`item_key_id`),
  CONSTRAINT `named_item_key_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2341 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `numeric_value`
--

DROP TABLE IF EXISTS `numeric_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `numeric_value` (
  `numeric_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  `numeric_content` double NOT NULL,
  PRIMARY KEY (`numeric_value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `password_storage`
--

DROP TABLE IF EXISTS `password_storage`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `password_storage` (
  `password_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`password_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `password_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `payment` (
  `payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `paid_amount` double NOT NULL,
  `payment_date` datetime NOT NULL,
  `purchase_id` bigint(20) NOT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `png_export`
--

DROP TABLE IF EXISTS `png_export`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `png_export` (
  `png_export_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `png_image` blob NOT NULL,
  PRIMARY KEY (`png_export_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `price`
--

DROP TABLE IF EXISTS `price`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `price` (
  `price_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cost` double NOT NULL,
  PRIMARY KEY (`price_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `purchase` (
  `purchase_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` int(11) NOT NULL,
  `buyer_account_id` int(11) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  `purchase_date` datetime NOT NULL,
  `canceled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`purchase_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `range_filter`
--

DROP TABLE IF EXISTS `range_filter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `range_filter` (
  `range_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `low_value` double DEFAULT NULL,
  `high_value` double DEFAULT NULL,
  PRIMARY KEY (`range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `refresh_event`
--

DROP TABLE IF EXISTS `refresh_event`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `refresh_event` (
  `refresh_event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) NOT NULL,
  `event_status` int(11) NOT NULL,
  `data_source_id` bigint(20) NOT NULL,
  PRIMARY KEY (`refresh_event_id`),
  KEY `refresh_event_ibfk1` (`event_id`),
  KEY `refresh_event_ibfk2` (`data_source_id`),
  CONSTRAINT `refresh_event_ibfk1` FOREIGN KEY (`event_id`) REFERENCES `event_base` (`event_id`) ON DELETE CASCADE,
  CONSTRAINT `refresh_event_ibfk2` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `report_deliveree`
--

DROP TABLE IF EXISTS `report_deliveree`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `report_schedule`
--

DROP TABLE IF EXISTS `report_schedule`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `report_schedule` (
  `report_schedule_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(11) NOT NULL,
  `run_interval` int(11) NOT NULL,
  PRIMARY KEY (`report_schedule_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `report_schedule_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `report_state`
--

DROP TABLE IF EXISTS `report_state`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `report_state` (
  `report_state_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`report_state_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `report_state_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `report_structure`
--

DROP TABLE IF EXISTS `report_structure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `report_structure` (
  `report_structure_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `structure_key` varchar(20) NOT NULL,
  `analysis_id` bigint(11) NOT NULL,
  `analysis_item_id` bigint(11) NOT NULL,
  PRIMARY KEY (`report_structure_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `report_structure_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `report_structure_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rolling_range_filter`
--

DROP TABLE IF EXISTS `rolling_range_filter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rolling_range_filter` (
  `rolling_range_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interval_value` int(11) NOT NULL,
  `filter_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`rolling_range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `rolling_range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `scheduled_task`
--

DROP TABLE IF EXISTS `scheduled_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `scheduled_task` (
  `scheduled_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL,
  `scheduled_time` datetime NOT NULL,
  `started_time` datetime DEFAULT NULL,
  `stopped_time` datetime DEFAULT NULL,
  `task_generator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`scheduled_task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5271 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `server_refresh_task`
--

DROP TABLE IF EXISTS `server_refresh_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `server_refresh_task` (
  `server_refresh_task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_source_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `scheduled_task_id` bigint(20) NOT NULL,
  PRIMARY KEY (`server_refresh_task_id`),
  KEY `scheduled_task_id` (`scheduled_task_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `server_refresh_task_ibfk3` (`user_id`),
  CONSTRAINT `server_refresh_task_ibfk1` FOREIGN KEY (`scheduled_task_id`) REFERENCES `scheduled_task` (`scheduled_task_id`) ON DELETE CASCADE,
  CONSTRAINT `server_refresh_task_ibfk2` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `server_refresh_task_ibfk3` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`dms`@`%`*/ /*!50003 trigger cascade_datafeed_delete_tasks AFTER DELETE on server_refresh_task
FOR EACH ROW DELETE FROM scheduled_task WHERE scheduled_task_id = OLD.scheduled_task_id */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `session_id_storage`
--

DROP TABLE IF EXISTS `session_id_storage`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `session_id_storage` (
  `session_id_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_feed_id` bigint(11) NOT NULL,
  `session_id` varchar(255) NOT NULL,
  PRIMARY KEY (`session_id_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `session_id_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `six_sigma_measure`
--

DROP TABLE IF EXISTS `six_sigma_measure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `six_sigma_measure` (
  `six_sigma_measure_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_item_id` bigint(20) NOT NULL,
  `sigma_type` int(11) NOT NULL,
  `defects_measure_id` bigint(20) NOT NULL,
  `opportunities_measure_id` bigint(20) NOT NULL,
  PRIMARY KEY (`six_sigma_measure_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `defects_measure_id` (`defects_measure_id`),
  KEY `opportunities_measure_id` (`opportunities_measure_id`),
  CONSTRAINT `six_sigma_measure_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `six_sigma_measure_ibfk2` FOREIGN KEY (`defects_measure_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `six_sigma_measure_ibfk3` FOREIGN KEY (`opportunities_measure_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `solution`
--

DROP TABLE IF EXISTS `solution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  PRIMARY KEY (`solution_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `solution_tag`
--

DROP TABLE IF EXISTS `solution_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `solution_tag` (
  `solution_tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `solution_id` bigint(20) NOT NULL,
  `tag_name` varchar(255) NOT NULL,
  PRIMARY KEY (`solution_tag_id`),
  KEY `solution_id` (`solution_id`),
  CONSTRAINT `solution_tag_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `solution_to_feed`
--

DROP TABLE IF EXISTS `solution_to_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `solution_to_feed` (
  `solution_to_feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `solution_id` bigint(20) NOT NULL,
  PRIMARY KEY (`solution_to_feed_id`),
  KEY `solution_to_feed_ibfk1` (`solution_id`),
  KEY `solution_to_feed_ibfk2` (`feed_id`),
  CONSTRAINT `solution_to_feed_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE,
  CONSTRAINT `solution_to_feed_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `solution_to_goal_tree`
--

DROP TABLE IF EXISTS `solution_to_goal_tree`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `store` (
  `store_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_name` varchar(100) NOT NULL,
  `store_description` text,
  `merchant_id` bigint(20) NOT NULL,
  PRIMARY KEY (`store_id`),
  KEY `merchant_id` (`merchant_id`),
  CONSTRAINT `store_ibfk_1` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`merchant_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `string_value`
--

DROP TABLE IF EXISTS `string_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `string_value` (
  `string_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_id` bigint(20) DEFAULT NULL,
  `string_content` varchar(255) NOT NULL,
  PRIMARY KEY (`string_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `string_value_ibfk_1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tag_cloud`
--

DROP TABLE IF EXISTS `tag_cloud`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tag_cloud` (
  `tag_cloud_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`tag_cloud_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tag_cloud_to_tag`
--

DROP TABLE IF EXISTS `tag_cloud_to_tag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `task_generator`
--

DROP TABLE IF EXISTS `task_generator`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `task_generator` (
  `task_generator_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_interval` int(11) NOT NULL,
  `last_task_date` datetime DEFAULT NULL,
  `start_task_date` datetime NOT NULL,
  `requires_backfill` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`task_generator_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `text_replace_scrub`
--

DROP TABLE IF EXISTS `text_replace_scrub`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `text_replace_scrub` (
  `text_replace_scrub_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_scrub_id` bigint(20) NOT NULL,
  `source_text` varchar(255) DEFAULT NULL,
  `target_text` varchar(255) DEFAULT NULL,
  `regex` tinyint(4) DEFAULT NULL,
  `case_sensitive` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`text_replace_scrub_id`),
  KEY `text_replace_scrub_ibfk1` (`data_scrub_id`),
  CONSTRAINT `text_replace_scrub_ibfk1` FOREIGN KEY (`data_scrub_id`) REFERENCES `data_scrub` (`data_scrub_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `time_based_analysis_measure`
--

DROP TABLE IF EXISTS `time_based_analysis_measure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `todo_base`
--

DROP TABLE IF EXISTS `todo_base`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `todo_base` (
  `todo_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `todo_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`todo_id`),
  KEY `todo_base_ibfk1` (`user_id`),
  CONSTRAINT `todo_base_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tree_report`
--

DROP TABLE IF EXISTS `tree_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tree_report` (
  `tree_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tree_report_id`),
  KEY `report_state_id` (`report_state_id`),
  CONSTRAINT `tree_report_ibfk_1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `treemap_report`
--

DROP TABLE IF EXISTS `treemap_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `treemap_report` (
  `treemap_report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `report_state_id` bigint(20) DEFAULT NULL,
  `color_scheme` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`treemap_report_id`),
  KEY `report_state_id` (`report_state_id`),
  CONSTRAINT `treemap_report_ibfk_1` FOREIGN KEY (`report_state_id`) REFERENCES `report_state` (`report_state_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `upload_policy_groups`
--

DROP TABLE IF EXISTS `upload_policy_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `upload_policy_users`
--

DROP TABLE IF EXISTS `upload_policy_users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `upload_policy_users` (
  `upload_policy_users_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `feed_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`upload_policy_users_id`),
  KEY `upload_policy_users_ibfk1` (`feed_id`),
  KEY `upload_policy_users_ibfk2` (`user_id`),
  CONSTRAINT `upload_policy_users_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `upload_policy_users_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1072 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `password` varchar(40) NOT NULL,
  `name` varchar(60) NOT NULL,
  `email` varchar(60) NOT NULL,
  `username` varchar(40) NOT NULL,
  `permissions` int(11) NOT NULL,
  `account_admin` tinyint(4) NOT NULL DEFAULT '1',
  `data_source_creator` tinyint(4) NOT NULL DEFAULT '0',
  `insight_creator` tinyint(4) NOT NULL DEFAULT '0',
  `user_key` varchar(20) DEFAULT NULL,
  `user_secret_key` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_permission`
--

DROP TABLE IF EXISTS `user_permission`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_permission` (
  `user_permission_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(100) DEFAULT NULL,
  `accounts_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_permission_id`),
  KEY `accounts_id` (`accounts_id`),
  CONSTRAINT `user_permission_ibfk_1` FOREIGN KEY (`accounts_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_to_analysis`
--

DROP TABLE IF EXISTS `user_to_analysis`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=570 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_to_feed`
--

DROP TABLE IF EXISTS `user_to_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_to_goal_tree`
--

DROP TABLE IF EXISTS `user_to_goal_tree`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_to_license_subscription`
--

DROP TABLE IF EXISTS `user_to_license_subscription`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_to_solution`
--

DROP TABLE IF EXISTS `user_to_solution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_to_solution` (
  `user_solution_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `solution_id` bigint(20) DEFAULT NULL,
  `user_role` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_solution_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_to_solution_to_feed`
--

DROP TABLE IF EXISTS `user_to_solution_to_feed`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_upload`
--

DROP TABLE IF EXISTS `user_upload`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_upload` (
  `user_upload_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) DEFAULT NULL,
  `user_data` longblob,
  `data_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_upload_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `user_upload_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `value`
--

DROP TABLE IF EXISTS `value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `value` (
  `value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `value_based_filter`
--

DROP TABLE IF EXISTS `value_based_filter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `value_based_filter` (
  `value_based_filter_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filter_id` bigint(20) DEFAULT NULL,
  `inclusive` tinyint(4) NOT NULL,
  PRIMARY KEY (`value_based_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `value_based_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `virtual_dimension`
--

DROP TABLE IF EXISTS `virtual_dimension`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `virtual_dimension` (
  `virtual_dimension_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `analysis_dimension_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `default_dimension_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`virtual_dimension_id`),
  KEY `analysis_dimension_id` (`analysis_dimension_id`),
  CONSTRAINT `virtual_dimension_ibfk1` FOREIGN KEY (`analysis_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `virtual_dimension_to_virtual_transform`
--

DROP TABLE IF EXISTS `virtual_dimension_to_virtual_transform`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `virtual_dimension_to_virtual_transform` (
  `virtual_dimension_to_virtual_transform_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `virtual_transform_id` bigint(20) NOT NULL,
  `virtual_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`virtual_dimension_to_virtual_transform_id`),
  KEY `virtual_transform_id` (`virtual_transform_id`),
  KEY `virtual_dimension_id` (`virtual_dimension_id`),
  CONSTRAINT `virtual_dimension_to_virtual_transform_ibfk1` FOREIGN KEY (`virtual_transform_id`) REFERENCES `virtual_transform` (`virtual_transform_id`) ON DELETE CASCADE,
  CONSTRAINT `virtual_dimension_to_virtual_transform_ibfk2` FOREIGN KEY (`virtual_dimension_id`) REFERENCES `virtual_dimension` (`virtual_dimension_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `virtual_transform`
--

DROP TABLE IF EXISTS `virtual_transform`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `virtual_transform` (
  `virtual_transform_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transform_dimension_id` bigint(20) NOT NULL,
  PRIMARY KEY (`virtual_transform_id`),
  KEY `transform_dimension_id` (`transform_dimension_id`),
  CONSTRAINT `virtual_transform_ibfk1` FOREIGN KEY (`transform_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `virtual_transform_to_value`
--

DROP TABLE IF EXISTS `virtual_transform_to_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `virtual_transform_to_value` (
  `virtual_transform_to_value_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `virtual_transform_id` bigint(20) NOT NULL,
  `value_id` bigint(20) NOT NULL,
  PRIMARY KEY (`virtual_transform_to_value_id`),
  KEY `virtual_transform_id` (`virtual_transform_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `virtual_transform_to_value_ibfk1` FOREIGN KEY (`virtual_transform_id`) REFERENCES `virtual_transform` (`virtual_transform_id`) ON DELETE CASCADE,
  CONSTRAINT `virtual_transform_to_value_ibfk2` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `yahoo_map_definition`
--

DROP TABLE IF EXISTS `yahoo_map_definition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-07-13 19:30:04
