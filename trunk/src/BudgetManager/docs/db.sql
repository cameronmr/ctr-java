-- phpMyAdmin SQL Dump
-- version 2.6.1-pl3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Feb 22, 2006 at 08:21 PM
-- Server version: 4.1.10
-- PHP Version: 4.3.10
-- 
-- Database: `AccountManagerReal`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `ACCOUNT`
-- 

CREATE TABLE `ACCOUNT` (
  `PKEY` varchar(36) NOT NULL default '',
  `ACCOUNT_NAME` varchar(255) NOT NULL default '',
  `ACCOUNT_NUMBER` varchar(32) NOT NULL default '',
  `ACCOUNT_DESCRIPTION` tinytext NOT NULL,
  PRIMARY KEY  (`PKEY`),
  UNIQUE KEY `ACCOUNT_NAME` (`ACCOUNT_NAME`),
  KEY `ACCOUNT_NUMBER` (`ACCOUNT_NUMBER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Account Information';

-- --------------------------------------------------------

-- 
-- Table structure for table `CATEGORY`
-- 

CREATE TABLE `CATEGORY` (
  `PKEY` varchar(36) NOT NULL default '',
  `CATEGORY_NAME` varchar(255) NOT NULL default '',
  `CATEGORY_DESCRIPTION` tinytext,
  `CATEGORY_ACTIVE` tinyint(1) NOT NULL default '1',
  `CATEGORY_PARENT_FKEY` varchar(36) default NULL,
  `CATEGORY_ACC_FKEY` varchar(36) default NULL,
  PRIMARY KEY  (`PKEY`),
  UNIQUE KEY `CATEGORY_NAME` (`CATEGORY_NAME`,`CATEGORY_PARENT_FKEY`),
  KEY `CATEGORY_PARENT_FKEY` (`CATEGORY_PARENT_FKEY`),
  KEY `CATEGORY_ACC_FKEY` (`CATEGORY_ACC_FKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Store information regarding the categories available.';
-- 
-- Dumping data for table `CATEGORY`
-- 
INSERT INTO `CATEGORY` (`PKEY`, `CATEGORY_NAME`, `CATEGORY_DESCRIPTION`, `CATEGORY_PARENT_FKEY` ) VALUES ('0', 'Categories', 'Root node for all categories', '0' );

-- --------------------------------------------------------

-- 
-- Table structure for table `RECONCILIATION`
-- 

CREATE TABLE `RECONCILIATION` (
  `PKEY` varchar(36) NOT NULL default '',
  `RECON_TRANS_FKEY` varchar(36) NOT NULL default '',
  `RECON_CATEGORY_FKEY` varchar(36) NOT NULL default '',
  `RECON_VALUE` int(11) NOT NULL default '0',
  `RECON_NOTE` tinytext,
  PRIMARY KEY  (`PKEY`),
  KEY `RECON_TRANS_FKEY` (`RECON_TRANS_FKEY`,`RECON_CATEGORY_FKEY`,`RECON_VALUE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Store the reconciliations for transactions';

-- --------------------------------------------------------

-- 
-- Table structure for table `RULE`
-- 

CREATE TABLE `RULE` (
  `PKEY` varchar(36) NOT NULL default '',
  `RULE_NAME` varchar(255) NOT NULL default '',
  `RULE_DESCRIPTION` tinytext NOT NULL,
  `RULE_TYPE` enum('ANY','ALL') NOT NULL default 'ANY',
  PRIMARY KEY  (`PKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Rules for processing transactions';

-- --------------------------------------------------------

-- 
-- Table structure for table `RULE_CRITERIA`
-- 

CREATE TABLE `RULE_CRITERIA` (
  `PKEY` varchar(36) NOT NULL default '',
  `RULE_FKEY` varchar(36) NOT NULL default '',
  `CRITERIA` varchar(32) NOT NULL default '0',
  `CRITERIA_VALUE` varchar(255) NOT NULL default '',
  `CRITERIA_TYPE` varchar(32) NOT NULL default '0',
  PRIMARY KEY  (`PKEY`),
  KEY `RULE_FKEY` (`RULE_FKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='For a given rule the criteria must be met';

-- --------------------------------------------------------

-- 
-- Table structure for table `RULE_RESULT`
-- 

CREATE TABLE `RULE_RESULT` (
  `PKEY` varchar(36) NOT NULL default '',
  `RULE_FKEY` varchar(36) NOT NULL default '',
  `RESULT_TYPE` varchar(32) NOT NULL default '',
  `RESULT_AMOUNT` int(11) NOT NULL default '0',
  `CATEGORY_FKEY` varchar(36) NOT NULL default '',
  PRIMARY KEY  (`PKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='For a given rule the results must be configured';

-- --------------------------------------------------------

-- 
-- Table structure for table `STATEMENT`
-- 

CREATE TABLE `STATEMENT` (
  `PKEY` varchar(36) NOT NULL default '',
  `STATEMENT_ACC_FKEY` varchar(36) NOT NULL default '',
  `STATEMENT_BEGIN` date NOT NULL default '0000-00-00',
  `STATEMENT_END` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`PKEY`),
  KEY `STATEMENT_ACC_FKEY` (`STATEMENT_ACC_FKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Statement Information';

-- --------------------------------------------------------

-- 
-- Table structure for table `TRANSACTION`
-- 

CREATE TABLE `TRANSACTION` (
  `PKEY` varchar(36) NOT NULL default '',
  `TRANS_NARRATIVE` varchar(255) NOT NULL default '',
  `TRANS_ACCOUNT_FKEY` varchar(36) NOT NULL default '',
  `TRANS_VALUE` int(11) NOT NULL default '0',
  `TRANS_DATE` date default '0000-00-00',
  `TRANS_NOTE` tinytext,
  PRIMARY KEY  (`PKEY`),
  UNIQUE KEY `TRANS_NARRATIVE` (`TRANS_NARRATIVE`,`TRANS_ACCOUNT_FKEY`,`TRANS_VALUE`,`TRANS_DATE`),
  KEY `TRANS_ACCOUNT_FKEY` (`TRANS_ACCOUNT_FKEY`,`TRANS_DATE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Transaction history.';
