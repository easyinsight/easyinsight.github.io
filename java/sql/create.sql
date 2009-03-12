-- MySQL dump 10.11
--
-- Host: localhost    Database: dms
-- ------------------------------------------------------
-- Server version	5.0.45-community-nt

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
CREATE TABLE `account` (
  `account_id` bigint(20) NOT NULL auto_increment,
  `account_type` int(11) NOT NULL,
  `max_size` bigint(20) default NULL,
  `max_users` int(11) default NULL,
  `unchecked_api_enabled` tinyint(1) default '1',
  `validated_api_enabled` tinyint(1) default '1',
  `unchecked_api_allowed` tinyint(1) default '1',
  `validated_api_allowed` tinyint(1) default '1',
  `dynamic_api_enabled` tinyint(1) default '1',
  `basic_auth_allowed` tinyint(1) default '1',
  `name` varchar(255) default NULL,
  `account_state` int(11) NOT NULL default '2',
  `group_id` bigint(11) default NULL,
  PRIMARY KEY  (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1;

--
-- Table structure for table `account_activation`
--

DROP TABLE IF EXISTS `account_activation`;
CREATE TABLE `account_activation` (
  `account_activation_id` bigint(20) NOT NULL auto_increment,
  `account_id` bigint(11) NOT NULL,
  `activation_key` varchar(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY  (`account_activation_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_activation_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `account_to_feed`
--

DROP TABLE IF EXISTS `account_to_feed`;
CREATE TABLE `account_to_feed` (
  `account_to_feed_id` bigint(20) NOT NULL auto_increment,
  `account_id` bigint(20) default NULL,
  `data_feed_id` bigint(20) default NULL,
  `account_role` int(11) default NULL,
  PRIMARY KEY  (`account_to_feed_id`),
  KEY `account_id` (`account_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `account_to_feed_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_feed_ibfk_2` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `account_to_guest_user`
--

DROP TABLE IF EXISTS `account_to_guest_user`;
CREATE TABLE `account_to_guest_user` (
  `account_to_guest_user_id` bigint(20) NOT NULL auto_increment,
  `account_id` bigint(11) NOT NULL,
  `guest_user_id` bigint(11) NOT NULL,
  PRIMARY KEY  (`account_to_guest_user_id`),
  KEY `account_id` (`account_id`),
  KEY `guest_user_id` (`guest_user_id`),
  CONSTRAINT `account_to_guest_user_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_guest_user_ibfk2` FOREIGN KEY (`guest_user_id`) REFERENCES `guest_user` (`guest_user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `account_to_merchant`
--

DROP TABLE IF EXISTS `account_to_merchant`;
CREATE TABLE `account_to_merchant` (
  `account_to_merchant_id` bigint(20) NOT NULL auto_increment,
  `merchant_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `binding_type` int(11) NOT NULL,
  PRIMARY KEY  (`account_to_merchant_id`),
  KEY `merchant_id` (`merchant_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_to_merchant_ibfk_1` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`merchant_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_merchant_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Table structure for table `account_to_user`
--

DROP TABLE IF EXISTS `account_to_user`;
CREATE TABLE `account_to_user` (
  `account_to_user_id` bigint(20) NOT NULL auto_increment,
  `account_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`account_to_user_id`),
  KEY `account_id` (`account_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `account_to_user_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE,
  CONSTRAINT `account_to_user_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `account_user_license`
--

DROP TABLE IF EXISTS `account_user_license`;
CREATE TABLE `account_user_license` (
  `account_user_license_id` bigint(11) NOT NULL auto_increment,
  `quantity` int(11) NOT NULL,
  `creation_date` datetime NOT NULL,
  `account_id` bigint(11) NOT NULL,
  PRIMARY KEY  (`account_user_license_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `account_user_license_ibfk1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `additional_items`
--

DROP TABLE IF EXISTS `additional_items`;
CREATE TABLE `additional_items` (
  `additional_items_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  PRIMARY KEY  (`additional_items_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `additional_items_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `additional_items_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis`
--

DROP TABLE IF EXISTS `analysis`;
CREATE TABLE `analysis` (
  `analysis_id` bigint(20) NOT NULL auto_increment,
  `data_feed_id` bigint(20) default NULL,
  `analysis_type` varchar(40) default NULL,
  `title` varchar(100) default NULL,
  `policy` int(11) default NULL,
  `genre` varchar(100) default NULL,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `views` int(11) default NULL,
  `rating_count` int(11) default NULL,
  `rating_average` double default NULL,
  `root_definition` tinyint(4) default '0',
  `marketplace_visible` tinyint(4) default '0',
  `publicly_visible` tinyint(4) default '0',
  `feed_visibility` tinyint(4) default NULL,
  PRIMARY KEY  (`analysis_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `analysis_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=355 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_based_feed`
--

DROP TABLE IF EXISTS `analysis_based_feed`;
CREATE TABLE `analysis_based_feed` (
  `analysis_based_feed_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) NOT NULL,
  `data_feed_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`analysis_based_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `analysis_based_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_based_feed_ibfk_2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_calculation`
--

DROP TABLE IF EXISTS `analysis_calculation`;
CREATE TABLE `analysis_calculation` (
  `analysis_calculation_id` bigint(20) NOT NULL auto_increment,
  `analysis_item_id` bigint(20) default NULL,
  `calculation_string` text NOT NULL,
  PRIMARY KEY  (`analysis_calculation_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_calculation_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_date`
--

DROP TABLE IF EXISTS `analysis_date`;
CREATE TABLE `analysis_date` (
  `analysis_date_id` bigint(20) NOT NULL auto_increment,
  `date_level` int(11) default NULL,
  `custom_date_format` varchar(100) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_date_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_date_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=408 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_dimension`
--

DROP TABLE IF EXISTS `analysis_dimension`;
CREATE TABLE `analysis_dimension` (
  `analysis_dimension_id` bigint(20) NOT NULL auto_increment,
  `group_by` tinyint(4) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  `key_dimension_id` bigint(20) default NULL,
  `hierarchy_id` bigint(20) default NULL,
  `hierarchy_level_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_dimension_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `key_dimension_id` (`key_dimension_id`),
  CONSTRAINT `analysis_dimension_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_dimension_ibfk_2` FOREIGN KEY (`key_dimension_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2002 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_hierarchy_item`
--

DROP TABLE IF EXISTS `analysis_hierarchy_item`;
CREATE TABLE `analysis_hierarchy_item` (
  `analysis_hierarchy_item_id` bigint(20) NOT NULL auto_increment,
  `analysis_item_id` bigint(20) NOT NULL,
  `hierarchy_level_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`analysis_hierarchy_item_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_hierarchy_item_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_item`
--

DROP TABLE IF EXISTS `analysis_item`;
CREATE TABLE `analysis_item` (
  `analysis_item_id` bigint(20) NOT NULL auto_increment,
  `item_key_id` bigint(20) NOT NULL,
  `hidden` tinyint(4) default NULL,
  `formatting_configuration_id` int(11) default NULL,
  `sort` int(11) default NULL,
  `display_name` varchar(255) default NULL,
  `width` int(11) default '0',
  PRIMARY KEY  (`analysis_item_id`),
  KEY `item_key_id` (`item_key_id`),
  CONSTRAINT `analysis_item_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3771 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_list`
--

DROP TABLE IF EXISTS `analysis_list`;
CREATE TABLE `analysis_list` (
  `analysis_list_id` bigint(20) NOT NULL auto_increment,
  `delimiter` varchar(10) default NULL,
  `expanded` tinyint(4) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_list_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_list_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_measure`
--

DROP TABLE IF EXISTS `analysis_measure`;
CREATE TABLE `analysis_measure` (
  `analysis_measure_id` bigint(20) NOT NULL auto_increment,
  `analysis_item_id` bigint(20) default NULL,
  `aggregation` int(11) default NULL,
  `measure_condition_range_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_measure_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `measure_condition_range_id` (`measure_condition_range_id`),
  CONSTRAINT `analysis_measure_ibfk2` FOREIGN KEY (`measure_condition_range_id`) REFERENCES `measure_condition_range` (`measure_condition_range_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_measure_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1770 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_range`
--

DROP TABLE IF EXISTS `analysis_range`;
CREATE TABLE `analysis_range` (
  `analysis_range_id` bigint(20) NOT NULL auto_increment,
  `analysis_item_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_range_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_range_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_tags`
--

DROP TABLE IF EXISTS `analysis_tags`;
CREATE TABLE `analysis_tags` (
  `analysis_tags_id` bigint(20) NOT NULL auto_increment,
  `tag` varchar(100) default NULL,
  `use_count` int(11) default '0',
  PRIMARY KEY  (`analysis_tags_id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_to_analysis_item`
--

DROP TABLE IF EXISTS `analysis_to_analysis_item`;
CREATE TABLE `analysis_to_analysis_item` (
  `analysis_to_analysis_item_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  `field_type` varchar(100) default NULL,
  PRIMARY KEY  (`analysis_to_analysis_item_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `analysis_to_analysis_item_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_analysis_item_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_to_data_scrub`
--

DROP TABLE IF EXISTS `analysis_to_data_scrub`;
CREATE TABLE `analysis_to_data_scrub` (
  `analysis_to_data_scrub_id` bigint(11) NOT NULL auto_increment,
  `analysis_id` bigint(11) default NULL,
  `data_scrub_id` bigint(11) default NULL,
  PRIMARY KEY  (`analysis_to_data_scrub_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_to_filter_join`
--

DROP TABLE IF EXISTS `analysis_to_filter_join`;
CREATE TABLE `analysis_to_filter_join` (
  `analysis_to_filter_join_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `filter_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_to_filter_join_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `analysis_to_filter_join_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_filter_join_ibfk_2` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_to_hierarchy_join`
--

DROP TABLE IF EXISTS `analysis_to_hierarchy_join`;
CREATE TABLE `analysis_to_hierarchy_join` (
  `analysis_to_hierarchy_join` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_to_hierarchy_join`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Table structure for table `analysis_to_tag`
--

DROP TABLE IF EXISTS `analysis_to_tag`;
CREATE TABLE `analysis_to_tag` (
  `analysis_to_tag_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `analysis_tags_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_to_tag_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `analysis_to_tag_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `analysis_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;

--
-- Table structure for table `benchmark`
--

DROP TABLE IF EXISTS `benchmark`;
CREATE TABLE `benchmark` (
  `category` varchar(40) NOT NULL,
  `elapsed_time` int(11) NOT NULL,
  KEY `category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `chart_definition`
--

DROP TABLE IF EXISTS `chart_definition`;
CREATE TABLE `chart_definition` (
  `chart_definition_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `graphic_definition_id` bigint(20) default NULL,
  `chart_type` int(11) default NULL,
  `chart_family` int(11) default NULL,
  `limits_metadata_id` bigint(20) default NULL,
  PRIMARY KEY  (`chart_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `limits_metadata_id` (`limits_metadata_id`),
  CONSTRAINT `chart_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `chart_definition_ibfk_2` FOREIGN KEY (`limits_metadata_id`) REFERENCES `limits_metadata` (`limits_metadata_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Table structure for table `chart_field`
--

DROP TABLE IF EXISTS `chart_field`;
CREATE TABLE `chart_field` (
  `chart_field_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  `position` int(11) default NULL,
  `field_type` varchar(100) default NULL,
  PRIMARY KEY  (`chart_field_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `chart_field_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `chart_field_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=latin1;

--
-- Table structure for table `chart_limits_metadata`
--

DROP TABLE IF EXISTS `chart_limits_metadata`;
CREATE TABLE `chart_limits_metadata` (
  `list_limits_metadata_id` bigint(20) NOT NULL auto_increment,
  `analysis_item_id` bigint(20) default NULL,
  `top_items` tinyint(4) default NULL,
  `number_items` int(11) default NULL,
  PRIMARY KEY  (`list_limits_metadata_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `charts_limits_metadata_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `community_group`
--

DROP TABLE IF EXISTS `community_group`;
CREATE TABLE `community_group` (
  `community_group_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `publicly_visible` tinyint(4) default NULL,
  `publicly_joinable` tinyint(4) default NULL,
  `description` text,
  `tag_cloud_id` bigint(20) default NULL,
  PRIMARY KEY  (`community_group_id`),
  KEY `tag_cloud_id` (`tag_cloud_id`),
  CONSTRAINT `community_group_ibfk1` FOREIGN KEY (`tag_cloud_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Table structure for table `complex_analysis_measure`
--

DROP TABLE IF EXISTS `complex_analysis_measure`;
CREATE TABLE `complex_analysis_measure` (
  `complex_analysis_measure_id` bigint(20) NOT NULL auto_increment,
  `analysis_item_id` bigint(20) default NULL,
  `wrapped_aggregation` int(11) default NULL,
  PRIMARY KEY  (`complex_analysis_measure_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `complex_analysis_measure_ibfk1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `composite_connection`
--

DROP TABLE IF EXISTS `composite_connection`;
CREATE TABLE `composite_connection` (
  `composite_connection_id` bigint(20) NOT NULL auto_increment,
  `composite_feed_id` bigint(20) NOT NULL,
  `source_feed_node_id` bigint(20) NOT NULL,
  `target_feed_node_id` bigint(20) NOT NULL,
  `source_join` bigint(20) NOT NULL,
  `target_join` bigint(20) NOT NULL,
  PRIMARY KEY  (`composite_connection_id`),
  KEY `composite_feed_id` (`composite_feed_id`),
  KEY `source_feed_node_id` (`source_feed_node_id`),
  KEY `target_feed_node_id` (`target_feed_node_id`),
  KEY `source_join` (`source_join`),
  KEY `target_join` (`target_join`),
  CONSTRAINT `composite_connection_ibfk1` FOREIGN KEY (`composite_feed_id`) REFERENCES `composite_feed` (`composite_feed_id`),
  CONSTRAINT `composite_connection_ibfk2` FOREIGN KEY (`source_feed_node_id`) REFERENCES `composite_node` (`composite_node_id`),
  CONSTRAINT `composite_connection_ibfk3` FOREIGN KEY (`target_feed_node_id`) REFERENCES `composite_node` (`composite_node_id`),
  CONSTRAINT `composite_connection_ibfk4` FOREIGN KEY (`source_join`) REFERENCES `item_key` (`item_key_id`),
  CONSTRAINT `composite_connection_ibfk5` FOREIGN KEY (`target_join`) REFERENCES `item_key` (`item_key_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `composite_feed`
--

DROP TABLE IF EXISTS `composite_feed`;
CREATE TABLE `composite_feed` (
  `composite_feed_id` bigint(20) NOT NULL auto_increment,
  `data_feed_id` bigint(20) default NULL,
  PRIMARY KEY  (`composite_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `composite_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `composite_node`
--

DROP TABLE IF EXISTS `composite_node`;
CREATE TABLE `composite_node` (
  `composite_node_id` bigint(20) NOT NULL auto_increment,
  `composite_feed_id` bigint(20) NOT NULL,
  `data_feed_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`composite_node_id`),
  KEY `composite_feed_id` (`composite_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `composite_node_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `composite_node_ibfk_2` FOREIGN KEY (`composite_feed_id`) REFERENCES `composite_feed` (`composite_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `crosstab_definition`
--

DROP TABLE IF EXISTS `crosstab_definition`;
CREATE TABLE `crosstab_definition` (
  `crosstab_definition_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  PRIMARY KEY  (`crosstab_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `crosstab_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Table structure for table `crosstab_field`
--

DROP TABLE IF EXISTS `crosstab_field`;
CREATE TABLE `crosstab_field` (
  `crosstab_field_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  `position` int(11) default NULL,
  `field_type` varchar(100) default NULL,
  PRIMARY KEY  (`crosstab_field_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `crosstab_field_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

--
-- Table structure for table `crosstab_measure_column`
--

DROP TABLE IF EXISTS `crosstab_measure_column`;
CREATE TABLE `crosstab_measure_column` (
  `crosstab_analysis_id` bigint(20) default NULL,
  `crosstab_measure_column_id` bigint(20) NOT NULL auto_increment,
  `aggregation` varchar(20) default NULL,
  PRIMARY KEY  (`crosstab_measure_column_id`),
  KEY `crosstab_analysis_id` (`crosstab_analysis_id`),
  CONSTRAINT `crosstab_measure_column_ibfk_1` FOREIGN KEY (`crosstab_analysis_id`) REFERENCES `crosstab_field` (`crosstab_field_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `data_feed`
--

DROP TABLE IF EXISTS `data_feed`;
CREATE TABLE `data_feed` (
  `data_feed_id` bigint(20) NOT NULL auto_increment,
  `feed_name` varchar(100) default NULL,
  `feed_key` varchar(100) default NULL,
  `feed_type` varchar(100) default NULL,
  `policy` int(11) default NULL,
  `genre` varchar(100) default NULL,
  `feed_size` int(11) default NULL,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `feed_views` int(11) default NULL,
  `feed_rating_count` int(11) default NULL,
  `feed_rating_average` double default NULL,
  `analysis_id` bigint(20) default NULL,
  `description` text,
  `attribution` varchar(255) default NULL,
  `owner_name` varchar(255) default NULL,
  `dynamic_service_definition_id` bigint(20) default '0',
  `PUBLICLY_VISIBLE` tinyint(4) default NULL,
  `MARKETPLACE_VISIBLE` tinyint(4) default NULL,
  `api_key` varchar(100) default NULL,
  `unchecked_api_enabled` tinyint(1) default '0',
  `unchecked_api_basic_auth` tinyint(1) default '0',
  `validated_api_enabled` tinyint(1) default '0',
  `validated_api_basic_auth` tinyint(1) default '0',
  `inherit_account_api_settings` tinyint(1) default '1',
  PRIMARY KEY  (`data_feed_id`),
  KEY `dynamic_service_definition_id` (`dynamic_service_definition_id`),
  CONSTRAINT `data_feed_ibfk1` FOREIGN KEY (`dynamic_service_definition_id`) REFERENCES `dynamic_service_definition` (`dynamic_service_definition_id`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=latin1;

--
-- Table structure for table `data_scrub`
--

DROP TABLE IF EXISTS `data_scrub`;
CREATE TABLE `data_scrub` (
  `data_scrub_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  PRIMARY KEY  (`data_scrub_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `data_scrub_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Table structure for table `data_source_audit_message`
--

DROP TABLE IF EXISTS `data_source_audit_message`;
CREATE TABLE `data_source_audit_message` (
  `data_source_audit_message_id` bigint(20) NOT NULL auto_increment,
  `data_source_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `audit_time` datetime NOT NULL,
  PRIMARY KEY  (`data_source_audit_message_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `data_source_audit_message_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`),
  CONSTRAINT `data_source_audit_message_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `data_source_comment`
--

DROP TABLE IF EXISTS `data_source_comment`;
CREATE TABLE `data_source_comment` (
  `data_source_comment_id` bigint(20) NOT NULL auto_increment,
  `data_source_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `time_created` datetime NOT NULL,
  `time_updated` datetime default NULL,
  PRIMARY KEY  (`data_source_comment_id`),
  KEY `data_source_id` (`data_source_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `data_source_comment_ibfk1` FOREIGN KEY (`data_source_id`) REFERENCES `data_feed` (`data_feed_id`),
  CONSTRAINT `data_source_comment_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `database_version`
--

DROP TABLE IF EXISTS `database_version`;
CREATE TABLE `database_version` (
  `database_version_id` bigint(20) NOT NULL auto_increment,
  `version` int(11) NOT NULL default '37',
  PRIMARY KEY  (`database_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Table structure for table `date_range_filter`
--

DROP TABLE IF EXISTS `date_range_filter`;
CREATE TABLE `date_range_filter` (
  `date_range_filter_id` bigint(20) NOT NULL auto_increment,
  `filter_id` bigint(20) default NULL,
  `low_value` datetime NOT NULL,
  `high_value` datetime NOT NULL,
  PRIMARY KEY  (`date_range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `date_range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `date_value`
--

DROP TABLE IF EXISTS `date_value`;
CREATE TABLE `date_value` (
  `date_value_id` bigint(20) NOT NULL auto_increment,
  `value_id` bigint(20) default NULL,
  `date_contet` datetime NOT NULL,
  PRIMARY KEY  (`date_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `date_value_ibfk1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `derived_item_key`
--

DROP TABLE IF EXISTS `derived_item_key`;
CREATE TABLE `derived_item_key` (
  `derived_item_key_id` bigint(20) NOT NULL auto_increment,
  `parent_item_key_id` bigint(20) NOT NULL,
  `item_key_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`derived_item_key_id`),
  KEY `item_key_id` (`item_key_id`),
  KEY `parent_item_key_id` (`parent_item_key_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `derived_item_key_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE,
  CONSTRAINT `derived_item_key_ibfk_2` FOREIGN KEY (`parent_item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE,
  CONSTRAINT `derived_item_key_ibfk_3` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `dynamic_service_code`
--

DROP TABLE IF EXISTS `dynamic_service_code`;
CREATE TABLE `dynamic_service_code` (
  `dynamic_service_code_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) default NULL,
  `interface_bytecode` blob,
  `impl_bytecode` blob,
  `bean_bytecode` blob,
  `bean_name` varchar(255) default NULL,
  PRIMARY KEY  (`dynamic_service_code_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `dynamic_service_code_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Table structure for table `dynamic_service_descriptor`
--

DROP TABLE IF EXISTS `dynamic_service_descriptor`;
CREATE TABLE `dynamic_service_descriptor` (
  `dynamic_service_descriptor_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) default NULL,
  PRIMARY KEY  (`dynamic_service_descriptor_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `dynamic_service_descriptor_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Table structure for table `dynamic_service_method`
--

DROP TABLE IF EXISTS `dynamic_service_method`;
CREATE TABLE `dynamic_service_method` (
  `dynamic_service_method_id` bigint(20) NOT NULL auto_increment,
  `method_name` varchar(255) NOT NULL,
  `dynamic_service_descriptor_id` bigint(20) NOT NULL,
  `method_type` int(11) NOT NULL,
  PRIMARY KEY  (`dynamic_service_method_id`),
  KEY `dynamic_service_descriptor_id` (`dynamic_service_descriptor_id`),
  CONSTRAINT `dynamic_service_method_ibfk_1` FOREIGN KEY (`dynamic_service_descriptor_id`) REFERENCES `dynamic_service_descriptor` (`dynamic_service_descriptor_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `dynamic_service_method_key`
--

DROP TABLE IF EXISTS `dynamic_service_method_key`;
CREATE TABLE `dynamic_service_method_key` (
  `dynamic_service_method_key_id` bigint(20) NOT NULL auto_increment,
  `dynamic_service_method_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`dynamic_service_method_key_id`),
  KEY `dynamic_service_method_id` (`dynamic_service_method_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `dynamic_service_method_key_ibfk_1` FOREIGN KEY (`dynamic_service_method_id`) REFERENCES `dynamic_service_method` (`dynamic_service_method_id`) ON DELETE CASCADE,
  CONSTRAINT `dynamic_service_method_key_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `empty_value`
--

DROP TABLE IF EXISTS `empty_value`;
CREATE TABLE `empty_value` (
  `empty_value_id` bigint(20) NOT NULL auto_increment,
  `value_id` bigint(20) default NULL,
  PRIMARY KEY  (`empty_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `empty_value_ibfk1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `excel_export`
--

DROP TABLE IF EXISTS `excel_export`;
CREATE TABLE `excel_export` (
  `excel_export_id` bigint(20) NOT NULL auto_increment,
  `excel_file` longblob NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`excel_export_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Table structure for table `excel_upload_format`
--

DROP TABLE IF EXISTS `excel_upload_format`;
CREATE TABLE `excel_upload_format` (
  `excel_upload_format_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`excel_upload_format_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `excel_upload_format_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Table structure for table `feed_commercial_policy`
--

DROP TABLE IF EXISTS `feed_commercial_policy`;
CREATE TABLE `feed_commercial_policy` (
  `feed_commercial_policy_id` bigint(20) NOT NULL auto_increment,
  `price_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`feed_commercial_policy_id`),
  KEY `feed_id` (`feed_id`),
  KEY `merchant_id` (`merchant_id`),
  KEY `price_id` (`price_id`),
  CONSTRAINT `feed_commercial_policy_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_commercial_policy_ibfk_2` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`merchant_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_commercial_policy_ibfk_3` FOREIGN KEY (`price_id`) REFERENCES `price` (`price_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `feed_group_policy`
--

DROP TABLE IF EXISTS `feed_group_policy`;
CREATE TABLE `feed_group_policy` (
  `feed_group_policy_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`feed_group_policy_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `feed_group_policy_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `feed_group_policy_group`
--

DROP TABLE IF EXISTS `feed_group_policy_group`;
CREATE TABLE `feed_group_policy_group` (
  `feed_group_policy_group_id` bigint(20) NOT NULL auto_increment,
  `feed_group_policy_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`feed_group_policy_group_id`),
  KEY `group_id` (`group_id`),
  KEY `feed_group_policy_id` (`feed_group_policy_id`),
  CONSTRAINT `feed_group_policy_group_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_group_policy_group_ibfk_2` FOREIGN KEY (`feed_group_policy_id`) REFERENCES `feed_group_policy` (`feed_group_policy_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `feed_persistence_metadata`
--

DROP TABLE IF EXISTS `feed_persistence_metadata`;
CREATE TABLE `feed_persistence_metadata` (
  `feed_persistence_metadata_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) default NULL,
  `size` int(11) default NULL,
  `version` int(11) default NULL,
  `database_name` varchar(100) default NULL,
  PRIMARY KEY  (`feed_persistence_metadata_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `feed_persistence_metadata_ibfk1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=363 DEFAULT CHARSET=latin1;

--
-- Table structure for table `feed_popularity`
--

DROP TABLE IF EXISTS `feed_popularity`;
CREATE TABLE `feed_popularity` (
  `feed_popularity_id` bigint(20) NOT NULL auto_increment,
  `feed_views` int(11) default NULL,
  `feed_rating_count` int(11) default NULL,
  `feed_rating_average` double default NULL,
  `feed_id` bigint(20) default NULL,
  PRIMARY KEY  (`feed_popularity_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `feed_popularity_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `feed_to_analysis_item`
--

DROP TABLE IF EXISTS `feed_to_analysis_item`;
CREATE TABLE `feed_to_analysis_item` (
  `feed_to_analysis_item_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`feed_to_analysis_item_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `feed_to_analysis_item_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_to_analysis_item_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3485 DEFAULT CHARSET=latin1;

--
-- Table structure for table `feed_to_tag`
--

DROP TABLE IF EXISTS `feed_to_tag`;
CREATE TABLE `feed_to_tag` (
  `feed_to_tag_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) default NULL,
  `analysis_tags_id` bigint(20) default NULL,
  PRIMARY KEY  (`feed_to_tag_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `feed_to_tag_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `feed_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=latin1;

--
-- Table structure for table `filter`
--

DROP TABLE IF EXISTS `filter`;
CREATE TABLE `filter` (
  `filter_id` bigint(20) NOT NULL auto_increment,
  `filter_type` varchar(40) default NULL,
  `inclusive` tinyint(4) default NULL,
  `optional` tinyint(4) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  `apply_before_aggregation` tinyint(4) default NULL,
  PRIMARY KEY  (`filter_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `filter_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=latin1;

--
-- Table structure for table `filter_analysis_measure`
--

DROP TABLE IF EXISTS `filter_analysis_measure`;
CREATE TABLE `filter_analysis_measure` (
  `filter_analysis_measure_id` bigint(20) NOT NULL auto_increment,
  `aggregation` varchar(40) default NULL,
  `filter_id` bigint(20) default NULL,
  PRIMARY KEY  (`filter_analysis_measure_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `filter_analysis_measure_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `filter_to_analysis_item`
--

DROP TABLE IF EXISTS `filter_to_analysis_item`;
CREATE TABLE `filter_to_analysis_item` (
  `filter_to_analysis_item_id` bigint(20) NOT NULL auto_increment,
  `filter_id` bigint(20) NOT NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`filter_to_analysis_item_id`),
  KEY `filter_id` (`filter_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `filter_to_analysis_item_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE,
  CONSTRAINT `filter_to_analysis_item_ibfk_2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `filter_to_value`
--

DROP TABLE IF EXISTS `filter_to_value`;
CREATE TABLE `filter_to_value` (
  `filter_to_value_id` bigint(20) NOT NULL auto_increment,
  `filter_id` bigint(20) default NULL,
  `value_id` bigint(20) default NULL,
  PRIMARY KEY  (`filter_to_value_id`),
  KEY `filter_id` (`filter_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `filter_to_value_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE,
  CONSTRAINT `filter_to_value_ibfk_2` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;

--
-- Table structure for table `filter_value`
--

DROP TABLE IF EXISTS `filter_value`;
CREATE TABLE `filter_value` (
  `filter_value_id` bigint(20) NOT NULL auto_increment,
  `value_based_filter_id` bigint(20) default NULL,
  `filter_value` varchar(100) default NULL,
  PRIMARY KEY  (`filter_value_id`),
  KEY `value_based_filter_id` (`value_based_filter_id`),
  CONSTRAINT `filter_value_ibfk_1` FOREIGN KEY (`value_based_filter_id`) REFERENCES `value_based_filter` (`value_based_filter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `flat_file_upload_format`
--

DROP TABLE IF EXISTS `flat_file_upload_format`;
CREATE TABLE `flat_file_upload_format` (
  `flat_file_upload_format_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  `delimiter_escape` varchar(5) NOT NULL,
  `delimiter_pattern` varchar(5) NOT NULL,
  PRIMARY KEY  (`flat_file_upload_format_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `flat_file_upload_format_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=latin1;

--
-- Table structure for table `formatting_configuration`
--

DROP TABLE IF EXISTS `formatting_configuration`;
CREATE TABLE `formatting_configuration` (
  `formatting_configuration_id` bigint(20) NOT NULL auto_increment,
  `formatting_type` int(11) NOT NULL,
  `text_uom` varchar(100) default NULL,
  PRIMARY KEY  (`formatting_configuration_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1595 DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_history`
--

DROP TABLE IF EXISTS `goal_history`;
CREATE TABLE `goal_history` (
  `goal_history_id` bigint(20) NOT NULL auto_increment,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `evaluation_date` datetime NOT NULL,
  `evaluation_result` double NOT NULL,
  PRIMARY KEY  (`goal_history_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  CONSTRAINT `goal_history_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_node_to_user`
--

DROP TABLE IF EXISTS `goal_node_to_user`;
CREATE TABLE `goal_node_to_user` (
  `goal_node_to_user_id` bigint(11) NOT NULL auto_increment,
  `goal_tree_node_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  PRIMARY KEY  (`goal_node_to_user_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `goal_node_to_user_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`),
  CONSTRAINT `goal_node_to_user_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_tree`
--

DROP TABLE IF EXISTS `goal_tree`;
CREATE TABLE `goal_tree` (
  `goal_tree_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(200) NOT NULL,
  `description` text,
  `root_node` bigint(20) NOT NULL,
  PRIMARY KEY  (`goal_tree_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_tree_node`
--

DROP TABLE IF EXISTS `goal_tree_node`;
CREATE TABLE `goal_tree_node` (
  `goal_tree_node_id` bigint(20) NOT NULL auto_increment,
  `parent_goal_tree_node_id` bigint(20) default NULL,
  `feed_id` bigint(20) default NULL,
  `goal_value` int(11) default NULL,
  `analysis_measure_id` bigint(20) default NULL,
  `filter_id` bigint(20) default NULL,
  `name` varchar(200) NOT NULL,
  `description` text,
  `high_is_good` tinyint(4) default NULL,
  `icon_image` varchar(255) default NULL,
  `sub_tree_id` bigint(11) default NULL,
  PRIMARY KEY  (`goal_tree_node_id`),
  KEY `parent_goal_tree_node_id` (`parent_goal_tree_node_id`),
  KEY `feed_id` (`feed_id`),
  KEY `analysis_measure_id` (`analysis_measure_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `goal_tree_node_ibfk1` FOREIGN KEY (`parent_goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_ibfk3` FOREIGN KEY (`analysis_measure_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_ibfk4` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_tree_node_tag`
--

DROP TABLE IF EXISTS `goal_tree_node_tag`;
CREATE TABLE `goal_tree_node_tag` (
  `goal_tree_node_tag_id` bigint(20) NOT NULL auto_increment,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `tag` varchar(255) NOT NULL,
  PRIMARY KEY  (`goal_tree_node_tag_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  CONSTRAINT `goal_tree_node_tag_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_tree_node_to_feed`
--

DROP TABLE IF EXISTS `goal_tree_node_to_feed`;
CREATE TABLE `goal_tree_node_to_feed` (
  `goal_tree_node_to_feed_id` bigint(20) NOT NULL auto_increment,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`goal_tree_node_to_feed_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `goal_tree_node_to_feed_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_feed_ibfk2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_tree_node_to_insight`
--

DROP TABLE IF EXISTS `goal_tree_node_to_insight`;
CREATE TABLE `goal_tree_node_to_insight` (
  `goal_tree_node_to_insight_id` bigint(20) NOT NULL auto_increment,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `insight_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`goal_tree_node_to_insight_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `insight_id` (`insight_id`),
  CONSTRAINT `goal_tree_node_to_insight_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_insight_ibfk2` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_tree_node_to_solution`
--

DROP TABLE IF EXISTS `goal_tree_node_to_solution`;
CREATE TABLE `goal_tree_node_to_solution` (
  `goal_tree_node_to_solution_id` bigint(20) NOT NULL auto_increment,
  `goal_tree_node_id` bigint(20) NOT NULL,
  `solution_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`goal_tree_node_to_solution_id`),
  KEY `goal_tree_node_id` (`goal_tree_node_id`),
  KEY `solution_id` (`solution_id`),
  CONSTRAINT `goal_tree_node_to_solution_ibfk1` FOREIGN KEY (`goal_tree_node_id`) REFERENCES `goal_tree_node` (`goal_tree_node_id`) ON DELETE CASCADE,
  CONSTRAINT `goal_tree_node_to_solution_ibfk2` FOREIGN KEY (`solution_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `goal_tree_to_group`
--

DROP TABLE IF EXISTS `goal_tree_to_group`;
CREATE TABLE `goal_tree_to_group` (
  `goal_tree_to_group_id` bigint(11) NOT NULL auto_increment,
  `goal_tree_id` bigint(11) NOT NULL,
  `role` int(8) NOT NULL,
  `group_id` bigint(11) NOT NULL,
  PRIMARY KEY  (`goal_tree_to_group_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `goal_tree_to_group_ibfk1` FOREIGN KEY (`goal_tree_id`) REFERENCES `goal_tree` (`goal_tree_id`),
  CONSTRAINT `goal_tree_to_group_ibfk2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `google_feed`
--

DROP TABLE IF EXISTS `google_feed`;
CREATE TABLE `google_feed` (
  `google_feed_id` bigint(20) NOT NULL auto_increment,
  `data_feed_id` bigint(20) default NULL,
  `worksheeturl` varchar(255) default NULL,
  PRIMARY KEY  (`google_feed_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `google_feed_ibfk_1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Table structure for table `graphic_definition`
--

DROP TABLE IF EXISTS `graphic_definition`;
CREATE TABLE `graphic_definition` (
  `graphic_definition_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  PRIMARY KEY  (`graphic_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `graphic_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Table structure for table `group_audit_message`
--

DROP TABLE IF EXISTS `group_audit_message`;
CREATE TABLE `group_audit_message` (
  `group_audit_message_id` bigint(20) NOT NULL auto_increment,
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `audit_time` datetime NOT NULL,
  PRIMARY KEY  (`group_audit_message_id`),
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `group_audit_message_ibfk1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`),
  CONSTRAINT `group_audit_message_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Table structure for table `group_comment`
--

DROP TABLE IF EXISTS `group_comment`;
CREATE TABLE `group_comment` (
  `group_comment_id` bigint(20) NOT NULL auto_increment,
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `time_created` datetime NOT NULL,
  `time_updated` datetime default NULL,
  PRIMARY KEY  (`group_comment_id`),
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `group_comment_ibfk1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`),
  CONSTRAINT `group_comment_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Table structure for table `group_to_feed_join`
--

DROP TABLE IF EXISTS `group_to_feed_join`;
CREATE TABLE `group_to_feed_join` (
  `group_to_feed_join_id` bigint(20) NOT NULL auto_increment,
  `group_id` bigint(20) default NULL,
  `feed_id` bigint(20) default NULL,
  PRIMARY KEY  (`group_to_feed_join_id`),
  KEY `feed_id` (`feed_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_feed_join_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_feed_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `group_to_insight`
--

DROP TABLE IF EXISTS `group_to_insight`;
CREATE TABLE `group_to_insight` (
  `group_to_insight_id` bigint(20) NOT NULL auto_increment,
  `insight_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY  (`group_to_insight_id`),
  KEY `insight_id` (`insight_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_insight_join_ibfk_1` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_insight_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `group_to_tag`
--

DROP TABLE IF EXISTS `group_to_tag`;
CREATE TABLE `group_to_tag` (
  `group_to_tag_id` bigint(20) NOT NULL auto_increment,
  `group_id` bigint(20) NOT NULL,
  `analysis_tags_id` bigint(20) default NULL,
  PRIMARY KEY  (`group_to_tag_id`),
  KEY `group_id` (`group_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `group_to_tag_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Table structure for table `group_to_user_join`
--

DROP TABLE IF EXISTS `group_to_user_join`;
CREATE TABLE `group_to_user_join` (
  `group_to_user_join_id` bigint(20) NOT NULL auto_increment,
  `group_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  `binding_type` int(11) default NULL,
  PRIMARY KEY  (`group_to_user_join_id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `group_to_user_join_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `group_to_user_join_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `community_group` (`community_group_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Table structure for table `guest_user`
--

DROP TABLE IF EXISTS `guest_user`;
CREATE TABLE `guest_user` (
  `guest_user_id` bigint(11) NOT NULL auto_increment,
  `user_id` bigint(11) NOT NULL,
  `state` int(11) NOT NULL default '1',
  PRIMARY KEY  (`guest_user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `guest_user_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `hierarchy`
--

DROP TABLE IF EXISTS `hierarchy`;
CREATE TABLE `hierarchy` (
  `hierarchy_id` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`hierarchy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Table structure for table `hierarchy_level`
--

DROP TABLE IF EXISTS `hierarchy_level`;
CREATE TABLE `hierarchy_level` (
  `hierarchy_level_id` bigint(20) NOT NULL auto_increment,
  `level` int(11) default NULL,
  `parent_item_id` bigint(20) default NULL,
  `analysis_item_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`hierarchy_level_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `hierarchy_level_ibfk2` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Table structure for table `icon`
--

DROP TABLE IF EXISTS `icon`;
CREATE TABLE `icon` (
  `icon_id` bigint(20) NOT NULL auto_increment,
  `icon_name` varchar(100) default NULL,
  `icon_file` varchar(100) default NULL,
  PRIMARY KEY  (`icon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `insight_audit_message`
--

DROP TABLE IF EXISTS `insight_audit_message`;
CREATE TABLE `insight_audit_message` (
  `insight_audit_message_id` bigint(20) NOT NULL auto_increment,
  `insight_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `audit_time` datetime NOT NULL,
  PRIMARY KEY  (`insight_audit_message_id`),
  KEY `insight_id` (`insight_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `insight_audit_message_ibfk1` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`),
  CONSTRAINT `insight_audit_message_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `insight_comment`
--

DROP TABLE IF EXISTS `insight_comment`;
CREATE TABLE `insight_comment` (
  `insight_comment_id` bigint(20) NOT NULL auto_increment,
  `insight_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `time_created` datetime NOT NULL,
  `time_updated` datetime default NULL,
  PRIMARY KEY  (`insight_comment_id`),
  KEY `insight_id` (`insight_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `insight_comment_ibfk1` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`),
  CONSTRAINT `insight_comment_ibfk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `insight_policy_users`
--

DROP TABLE IF EXISTS `insight_policy_users`;
CREATE TABLE `insight_policy_users` (
  `insight_policy_users_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `insight_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY  (`insight_policy_users_id`),
  KEY `insight_id` (`insight_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `insight_policy_users_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `insight_policy_users_ibfk_2` FOREIGN KEY (`insight_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `item_key`
--

DROP TABLE IF EXISTS `item_key`;
CREATE TABLE `item_key` (
  `item_key_id` bigint(20) NOT NULL auto_increment,
  `display_name` varchar(255) default NULL,
  PRIMARY KEY  (`item_key_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1423 DEFAULT CHARSET=latin1;

--
-- Table structure for table `jira`
--

DROP TABLE IF EXISTS `jira`;
CREATE TABLE `jira` (
  `jira_id` bigint(20) NOT NULL auto_increment,
  `data_feed_id` bigint(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY  (`jira_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `jira_ibfk1` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Table structure for table `last_n_filter`
--

DROP TABLE IF EXISTS `last_n_filter`;
CREATE TABLE `last_n_filter` (
  `last_n_filter_id` bigint(11) NOT NULL auto_increment,
  `filter_id` bigint(11) NOT NULL,
  `result_limit` int(11) NOT NULL,
  PRIMARY KEY  (`last_n_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `last_n_filter_ibfk1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `license_subscription`
--

DROP TABLE IF EXISTS `license_subscription`;
CREATE TABLE `license_subscription` (
  `license_subscription_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`license_subscription_id`),
  KEY `feed_id` (`feed_id`),
  KEY `user_id` (`user_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `license_subscription_ibfk_1` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE,
  CONSTRAINT `license_subscription_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `license_subscription_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `limits_metadata`
--

DROP TABLE IF EXISTS `limits_metadata`;
CREATE TABLE `limits_metadata` (
  `limits_metadata_id` bigint(20) NOT NULL auto_increment,
  `top_items` tinyint(4) default NULL,
  `number_items` int(11) default NULL,
  PRIMARY KEY  (`limits_metadata_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `list_analysis_field`
--

DROP TABLE IF EXISTS `list_analysis_field`;
CREATE TABLE `list_analysis_field` (
  `analysis_field_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `position` int(11) NOT NULL,
  `analysis_item_id` bigint(20) default NULL,
  PRIMARY KEY  (`analysis_field_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `list_analysis_field_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `list_analysis_field_ibfk_2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `list_definition`
--

DROP TABLE IF EXISTS `list_definition`;
CREATE TABLE `list_definition` (
  `list_definition_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `list_limits_metadata_id` bigint(20) default NULL,
  `show_row_numbers` tinyint(4) default '0',
  PRIMARY KEY  (`list_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `list_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=328 DEFAULT CHARSET=latin1;

--
-- Table structure for table `list_limits_metadata`
--

DROP TABLE IF EXISTS `list_limits_metadata`;
CREATE TABLE `list_limits_metadata` (
  `list_limits_metadata_id` bigint(20) NOT NULL auto_increment,
  `limits_metadata_id` bigint(20) default NULL,
  `analysis_item_id` bigint(20) default NULL,
  PRIMARY KEY  (`list_limits_metadata_id`),
  KEY `limits_metadata_id` (`limits_metadata_id`),
  KEY `analysis_item_id` (`analysis_item_id`),
  CONSTRAINT `list_limits_metadata_ibfk_1` FOREIGN KEY (`analysis_item_id`) REFERENCES `analysis_item` (`analysis_item_id`) ON DELETE CASCADE,
  CONSTRAINT `list_limits_metadata_ibfk_2` FOREIGN KEY (`limits_metadata_id`) REFERENCES `limits_metadata` (`limits_metadata_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `lookup_table_scrub`
--

DROP TABLE IF EXISTS `lookup_table_scrub`;
CREATE TABLE `lookup_table_scrub` (
  `lookup_table_scrub_id` bigint(20) NOT NULL auto_increment,
  `data_scrub_id` bigint(20) default NULL,
  `source_key` bigint(20) default NULL,
  `target_key` bigint(20) default NULL,
  PRIMARY KEY  (`lookup_table_scrub_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Table structure for table `lookup_table_scrub_pair`
--

DROP TABLE IF EXISTS `lookup_table_scrub_pair`;
CREATE TABLE `lookup_table_scrub_pair` (
  `lookup_table_scrub_pair_id` bigint(20) NOT NULL auto_increment,
  `source_value` varchar(255) default NULL,
  `target_value` varchar(255) default NULL,
  `data_scrub_id` bigint(20) default NULL,
  PRIMARY KEY  (`lookup_table_scrub_pair_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Table structure for table `map_definition`
--

DROP TABLE IF EXISTS `map_definition`;
CREATE TABLE `map_definition` (
  `map_definition_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `graphic_definition_id` bigint(20) default NULL,
  `map_type` int(11) default NULL,
  PRIMARY KEY  (`map_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `graphic_definition_id` (`graphic_definition_id`),
  CONSTRAINT `map_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `map_definition_ibfk_2` FOREIGN KEY (`graphic_definition_id`) REFERENCES `graphic_definition` (`graphic_definition_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `measure_condition`
--

DROP TABLE IF EXISTS `measure_condition`;
CREATE TABLE `measure_condition` (
  `measure_condition_id` bigint(20) NOT NULL auto_increment,
  `low_color` int(11) NOT NULL,
  `low_value` int(11) NOT NULL,
  `high_color` int(11) NOT NULL,
  `high_value` int(11) NOT NULL,
  PRIMARY KEY  (`measure_condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `measure_condition_range`
--

DROP TABLE IF EXISTS `measure_condition_range`;
CREATE TABLE `measure_condition_range` (
  `measure_condition_range_id` bigint(20) NOT NULL auto_increment,
  `low_measure_condition_id` bigint(20) default NULL,
  `high_measure_condition_id` bigint(20) default NULL,
  `value_range_type` int(11) NOT NULL,
  PRIMARY KEY  (`measure_condition_range_id`),
  KEY `low_measure_condition_id` (`low_measure_condition_id`),
  KEY `high_measure_condition_id` (`high_measure_condition_id`),
  CONSTRAINT `measure_condition_range_ibfk_1` FOREIGN KEY (`low_measure_condition_id`) REFERENCES `measure_condition` (`measure_condition_id`) ON DELETE CASCADE,
  CONSTRAINT `measure_condition_range_ibfk_2` FOREIGN KEY (`high_measure_condition_id`) REFERENCES `measure_condition` (`measure_condition_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `merchant`
--

DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
  `merchant_id` bigint(20) NOT NULL auto_increment,
  `merchant_name` varchar(100) NOT NULL,
  PRIMARY KEY  (`merchant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Table structure for table `named_item_key`
--

DROP TABLE IF EXISTS `named_item_key`;
CREATE TABLE `named_item_key` (
  `named_item_key_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `item_key_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`named_item_key_id`),
  KEY `item_key_id` (`item_key_id`),
  CONSTRAINT `named_item_key_ibfk_1` FOREIGN KEY (`item_key_id`) REFERENCES `item_key` (`item_key_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1423 DEFAULT CHARSET=latin1;

--
-- Table structure for table `numeric_value`
--

DROP TABLE IF EXISTS `numeric_value`;
CREATE TABLE `numeric_value` (
  `numeric_value_id` bigint(20) NOT NULL auto_increment,
  `value_id` bigint(20) default NULL,
  `numeric_content` double NOT NULL,
  PRIMARY KEY  (`numeric_value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `payment_id` bigint(20) NOT NULL auto_increment,
  `paid_amount` double NOT NULL,
  `payment_date` datetime NOT NULL,
  `purchase_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `png_export`
--

DROP TABLE IF EXISTS `png_export`;
CREATE TABLE `png_export` (
  `png_export_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `png_image` blob NOT NULL,
  PRIMARY KEY  (`png_export_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Table structure for table `price`
--

DROP TABLE IF EXISTS `price`;
CREATE TABLE `price` (
  `price_id` bigint(20) NOT NULL auto_increment,
  `cost` double NOT NULL,
  PRIMARY KEY  (`price_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase` (
  `purchase_id` bigint(20) NOT NULL auto_increment,
  `feed_id` int(11) NOT NULL,
  `buyer_account_id` int(11) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  `purchase_date` datetime NOT NULL,
  `canceled` tinyint(4) default NULL,
  PRIMARY KEY  (`purchase_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `range_filter`
--

DROP TABLE IF EXISTS `range_filter`;
CREATE TABLE `range_filter` (
  `range_filter_id` bigint(20) NOT NULL auto_increment,
  `filter_id` bigint(20) default NULL,
  `low_value` double default NULL,
  `high_value` double default NULL,
  PRIMARY KEY  (`range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `report_deliveree`
--

DROP TABLE IF EXISTS `report_deliveree`;
CREATE TABLE `report_deliveree` (
  `report_deliveree_id` bigint(11) NOT NULL auto_increment,
  `report_schedule_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  PRIMARY KEY  (`report_deliveree_id`),
  KEY `user_id` (`user_id`),
  KEY `report_schedule_id` (`report_schedule_id`),
  CONSTRAINT `report_deliveree_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `report_deliveree_ibfk2` FOREIGN KEY (`report_schedule_id`) REFERENCES `report_schedule` (`report_schedule_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `report_schedule`
--

DROP TABLE IF EXISTS `report_schedule`;
CREATE TABLE `report_schedule` (
  `report_schedule_id` bigint(11) NOT NULL auto_increment,
  `analysis_id` bigint(11) NOT NULL,
  `run_interval` int(11) NOT NULL,
  PRIMARY KEY  (`report_schedule_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `report_schedule_ibfk1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `rolling_range_filter`
--

DROP TABLE IF EXISTS `rolling_range_filter`;
CREATE TABLE `rolling_range_filter` (
  `rolling_range_filter_id` bigint(20) NOT NULL auto_increment,
  `interval_value` int(11) NOT NULL,
  `filter_id` bigint(20) default NULL,
  PRIMARY KEY  (`rolling_range_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `rolling_range_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;

--
-- Table structure for table `solution`
--

DROP TABLE IF EXISTS `solution`;
CREATE TABLE `solution` (
  `solution_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `description` text,
  `archive` longblob,
  `solution_archive_name` varchar(255) default NULL,
  `copy_data` tinyint(4) default NULL,
  `industry` varchar(255) default NULL,
  `author` varchar(255) default NULL,
  `goal_tree_id` bigint(20) default NULL,
  `solution_tier` int(11) NOT NULL default '2',
  PRIMARY KEY  (`solution_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Table structure for table `solution_tag`
--

DROP TABLE IF EXISTS `solution_tag`;
CREATE TABLE `solution_tag` (
  `solution_tag_id` bigint(20) NOT NULL auto_increment,
  `solution_id` bigint(20) NOT NULL,
  `tag_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`solution_tag_id`),
  KEY `solution_id` (`solution_id`),
  CONSTRAINT `solution_tag_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `solution_to_feed`
--

DROP TABLE IF EXISTS `solution_to_feed`;
CREATE TABLE `solution_to_feed` (
  `solution_to_feed_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  `solution_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`solution_to_feed_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Table structure for table `solution_to_goal_tree`
--

DROP TABLE IF EXISTS `solution_to_goal_tree`;
CREATE TABLE `solution_to_goal_tree` (
  `solution_to_goal_tree_id` bigint(20) NOT NULL auto_increment,
  `solution_id` bigint(20) NOT NULL,
  `goal_tree_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`solution_to_goal_tree_id`),
  KEY `solution_id` (`solution_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  CONSTRAINT `solution_to_goal_tree_ibfk1` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`) ON DELETE CASCADE,
  CONSTRAINT `solution_to_goal_tree_ibfk2` FOREIGN KEY (`goal_tree_id`) REFERENCES `goal_tree` (`goal_tree_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `store_id` bigint(20) NOT NULL auto_increment,
  `store_name` varchar(100) NOT NULL,
  `store_description` text,
  `merchant_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`store_id`),
  KEY `merchant_id` (`merchant_id`),
  CONSTRAINT `store_ibfk_1` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`merchant_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `string_value`
--

DROP TABLE IF EXISTS `string_value`;
CREATE TABLE `string_value` (
  `string_value_id` bigint(20) NOT NULL auto_increment,
  `value_id` bigint(20) default NULL,
  `string_content` varchar(255) NOT NULL,
  PRIMARY KEY  (`string_value_id`),
  KEY `value_id` (`value_id`),
  CONSTRAINT `string_value_ibfk_1` FOREIGN KEY (`value_id`) REFERENCES `value` (`value_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;

--
-- Table structure for table `tag_cloud`
--

DROP TABLE IF EXISTS `tag_cloud`;
CREATE TABLE `tag_cloud` (
  `tag_cloud_id` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`tag_cloud_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `tag_cloud_to_tag`
--

DROP TABLE IF EXISTS `tag_cloud_to_tag`;
CREATE TABLE `tag_cloud_to_tag` (
  `tag_cloud_to_tag_id` bigint(20) NOT NULL auto_increment,
  `tag_cloud_id` bigint(20) default NULL,
  `analysis_tags_id` bigint(20) default NULL,
  PRIMARY KEY  (`tag_cloud_to_tag_id`),
  KEY `tag_cloud_id` (`tag_cloud_id`),
  KEY `analysis_tags_id` (`analysis_tags_id`),
  CONSTRAINT `tag_cloud_to_tag_ibfk_1` FOREIGN KEY (`tag_cloud_id`) REFERENCES `tag_cloud` (`tag_cloud_id`) ON DELETE CASCADE,
  CONSTRAINT `tag_cloud_to_tag_ibfk_2` FOREIGN KEY (`analysis_tags_id`) REFERENCES `analysis_tags` (`analysis_tags_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `text_replace_scrub`
--

DROP TABLE IF EXISTS `text_replace_scrub`;
CREATE TABLE `text_replace_scrub` (
  `text_replace_scrub_id` bigint(20) NOT NULL auto_increment,
  `data_scrub_id` bigint(20) default NULL,
  `source_text` varchar(255) default NULL,
  `target_text` varchar(255) default NULL,
  `regex` tinyint(4) default NULL,
  `case_sensitive` tinyint(4) default NULL,
  PRIMARY KEY  (`text_replace_scrub_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `time_based_analysis_measure`
--

DROP TABLE IF EXISTS `time_based_analysis_measure`;
CREATE TABLE `time_based_analysis_measure` (
  `time_based_analysis_measure_id` bigint(20) NOT NULL auto_increment,
  `analysis_item_id` bigint(20) default NULL,
  `date_dimension` bigint(20) default NULL,
  `wrapped_aggregation` int(11) default NULL,
  PRIMARY KEY  (`time_based_analysis_measure_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `upload_policy_groups`
--

DROP TABLE IF EXISTS `upload_policy_groups`;
CREATE TABLE `upload_policy_groups` (
  `upload_policy_groups_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY  (`upload_policy_groups_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Table structure for table `upload_policy_users`
--

DROP TABLE IF EXISTS `upload_policy_users`;
CREATE TABLE `upload_policy_users` (
  `upload_policy_users_id` bigint(20) NOT NULL auto_increment,
  `feed_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY  (`upload_policy_users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=743 DEFAULT CHARSET=latin1;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL auto_increment,
  `account_id` bigint(20) default NULL,
  `password` varchar(40) NOT NULL,
  `name` varchar(60) NOT NULL,
  `email` varchar(60) NOT NULL,
  `username` varchar(40) NOT NULL,
  `permissions` int(11) NOT NULL,
  `account_admin` tinyint(4) NOT NULL default '1',
  `data_source_creator` tinyint(4) NOT NULL default '0',
  `insight_creator` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`user_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_permission`
--

DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE `user_permission` (
  `user_permission_id` bigint(20) NOT NULL auto_increment,
  `permission_name` varchar(100) default NULL,
  `accounts_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_permission_id`),
  KEY `accounts_id` (`accounts_id`),
  CONSTRAINT `user_permission_ibfk_1` FOREIGN KEY (`accounts_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_to_analysis`
--

DROP TABLE IF EXISTS `user_to_analysis`;
CREATE TABLE `user_to_analysis` (
  `user_to_analysis_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `analysis_id` bigint(20) default NULL,
  `relationship_type` int(11) default NULL,
  `open` tinyint(4) default NULL,
  PRIMARY KEY  (`user_to_analysis_id`),
  KEY `user_id` (`user_id`),
  KEY `analysis_id` (`analysis_id`),
  CONSTRAINT `user_to_analysis_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_analysis_ibfk_2` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=394 DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_to_feed`
--

DROP TABLE IF EXISTS `user_to_feed`;
CREATE TABLE `user_to_feed` (
  `user_to_feed_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `data_feed_id` bigint(20) default NULL,
  `user_role` int(11) default NULL,
  PRIMARY KEY  (`user_to_feed_id`),
  KEY `user_id` (`user_id`),
  KEY `data_feed_id` (`data_feed_id`),
  CONSTRAINT `user_to_feed_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_feed_ibfk_2` FOREIGN KEY (`data_feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_to_goal_tree`
--

DROP TABLE IF EXISTS `user_to_goal_tree`;
CREATE TABLE `user_to_goal_tree` (
  `user_to_goal_tree_id` bigint(20) NOT NULL auto_increment,
  `goal_tree_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `user_role` int(11) default NULL,
  PRIMARY KEY  (`user_to_goal_tree_id`),
  KEY `user_id` (`user_id`),
  KEY `goal_tree_id` (`goal_tree_id`),
  CONSTRAINT `user_to_goal_tree_ibfk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_goal_tree_ibfk2` FOREIGN KEY (`goal_tree_id`) REFERENCES `goal_tree` (`goal_tree_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_to_license_subscription`
--

DROP TABLE IF EXISTS `user_to_license_subscription`;
CREATE TABLE `user_to_license_subscription` (
  `user_to_license_subscription_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `license_subscription_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_to_license_subscription_id`),
  KEY `user_id` (`user_id`),
  KEY `license_subscription_id` (`license_subscription_id`),
  CONSTRAINT `user_to_license_subscription_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_license_subscription_ibfk_2` FOREIGN KEY (`license_subscription_id`) REFERENCES `license_subscription` (`license_subscription_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_to_solution`
--

DROP TABLE IF EXISTS `user_to_solution`;
CREATE TABLE `user_to_solution` (
  `user_solution_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `solution_id` bigint(20) default NULL,
  `user_role` int(11) default NULL,
  PRIMARY KEY  (`user_solution_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_to_solution_to_feed`
--

DROP TABLE IF EXISTS `user_to_solution_to_feed`;
CREATE TABLE `user_to_solution_to_feed` (
  `user_to_solution_to_feed_id` bigint(20) NOT NULL auto_increment,
  `user_to_solution_id` bigint(20) NOT NULL,
  `feed_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_to_solution_to_feed_id`),
  KEY `user_to_solution_id` (`user_to_solution_id`),
  KEY `feed_id` (`feed_id`),
  CONSTRAINT `user_to_solution_to_feed_ibfk_1` FOREIGN KEY (`user_to_solution_id`) REFERENCES `user_solution` (`user_solution_id`) ON DELETE CASCADE,
  CONSTRAINT `user_to_solution_to_feed_ibfk_2` FOREIGN KEY (`feed_id`) REFERENCES `data_feed` (`data_feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `user_upload`
--

DROP TABLE IF EXISTS `user_upload`;
CREATE TABLE `user_upload` (
  `user_upload_id` bigint(20) NOT NULL auto_increment,
  `account_id` bigint(20) default NULL,
  `user_data` longblob,
  `data_name` varchar(100) default NULL,
  PRIMARY KEY  (`user_upload_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `user_upload_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Table structure for table `value`
--

DROP TABLE IF EXISTS `value`;
CREATE TABLE `value` (
  `value_id` bigint(20) NOT NULL auto_increment,
  PRIMARY KEY  (`value_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;

--
-- Table structure for table `value_based_filter`
--

DROP TABLE IF EXISTS `value_based_filter`;
CREATE TABLE `value_based_filter` (
  `value_based_filter_id` bigint(20) NOT NULL auto_increment,
  `filter_id` bigint(20) default NULL,
  `inclusive` tinyint(4) NOT NULL,
  PRIMARY KEY  (`value_based_filter_id`),
  KEY `filter_id` (`filter_id`),
  CONSTRAINT `value_based_filter_ibfk_1` FOREIGN KEY (`filter_id`) REFERENCES `filter` (`filter_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;

--
-- Table structure for table `yahoo_map_definition`
--

DROP TABLE IF EXISTS `yahoo_map_definition`;
CREATE TABLE `yahoo_map_definition` (
  `yahoo_map_definition_id` bigint(20) NOT NULL auto_increment,
  `analysis_id` bigint(20) default NULL,
  `graphic_definition_id` bigint(20) default NULL,
  PRIMARY KEY  (`yahoo_map_definition_id`),
  KEY `analysis_id` (`analysis_id`),
  KEY `graphic_definition_id` (`graphic_definition_id`),
  CONSTRAINT `yahoo_map_definition_ibfk_1` FOREIGN KEY (`analysis_id`) REFERENCES `analysis` (`analysis_id`) ON DELETE CASCADE,
  CONSTRAINT `yahoo_map_definition_ibfk_2` FOREIGN KEY (`graphic_definition_id`) REFERENCES `graphic_definition` (`graphic_definition_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-03-10 20:34:29
