-- phpMyAdmin SQL Dump
-- version 2.6.1-pl3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Jul 20, 2005 at 07:27 PM
-- Server version: 4.1.8
-- PHP Version: 4.3.10
-- 
-- Database: `AccountManager`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `ACCOUNT`
-- 

CREATE TABLE `ACCOUNT` (
  `PKEY` varchar(32) NOT NULL default '',
  `ACCOUNT_NAME` varchar(255) NOT NULL default '',
  `ACCOUNT_DESCRIPTION` tinytext NOT NULL,
  PRIMARY KEY  (`PKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Account Information';

-- 
-- Dumping data for table `ACCOUNT`
-- 

INSERT INTO `ACCOUNT` (`PKEY`, `ACCOUNT_NAME`, `ACCOUNT_DESCRIPTION`) VALUES ('4564717010664547', 'Credit Card', 'none');

-- --------------------------------------------------------

-- 
-- Table structure for table `CATEGORY`
-- 

CREATE TABLE `CATEGORY` (
  `PKEY` varchar(36) NOT NULL default '',
  `CATEGORY_NAME` varchar(255) NOT NULL default '',
  `CATEGORY_DESCRIPTION` tinytext NOT NULL,
  `CATEGORY_PARENT_FKEY` varchar(36) default NULL,
  `CATEGORY_ACC_FKEY` varchar(32) NOT NULL default '',
  PRIMARY KEY  (`PKEY`),
  UNIQUE KEY `CATEGORY_NAME` (`CATEGORY_NAME`),
  KEY `CATEGORY_PARENT_FKEY` (`CATEGORY_PARENT_FKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Store information regarding the categories available.';

-- 
-- Dumping data for table `CATEGORY`
-- 

INSERT INTO `CATEGORY` (`PKEY`, `CATEGORY_NAME`, `CATEGORY_DESCRIPTION`, `CATEGORY_PARENT_FKEY`, `CATEGORY_ACC_FKEY`) VALUES ('0', 'ROOT', 'Root node for all categories', '0', '');

-- --------------------------------------------------------

-- 
-- Table structure for table `RECONCILIATION`
-- 

CREATE TABLE `RECONCILIATION` (
  `PKEY` varchar(36) NOT NULL default '',
  `RECON_TRANS_FKEY` varchar(36) NOT NULL default '',
  `RECON_CATEGORY_FKEY` varchar(36) NOT NULL default '',
  `RECON_ACCOUNT_FKEY` varchar(32) NOT NULL default '',
  `RECON_VALUE` int(11) NOT NULL default '0',
  `RECON_NOTE` tinytext,
  PRIMARY KEY  (`PKEY`),
  KEY `RECON_TRANS_FKEY` (`RECON_TRANS_FKEY`,`RECON_CATEGORY_FKEY`,`RECON_ACCOUNT_FKEY`,`RECON_VALUE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Store the reconciliations for transactions';

-- 
-- Dumping data for table `RECONCILIATION`
-- 

INSERT INTO `RECONCILIATION` (`PKEY`, `RECON_TRANS_FKEY`, `RECON_CATEGORY_FKEY`, `RECON_ACCOUNT_FKEY`, `RECON_VALUE`, `RECON_NOTE`) VALUES ('1234', 'c76452e8-4f33-414f-8123-ccd20126c000', '3', '3', 1000, 'Test Note!');
INSERT INTO `RECONCILIATION` (`PKEY`, `RECON_TRANS_FKEY`, `RECON_CATEGORY_FKEY`, `RECON_ACCOUNT_FKEY`, `RECON_VALUE`, `RECON_NOTE`) VALUES ('1235', 'c76452e8-4f33-414f-8123-ccd20126c000', '3', '3', 502, 'Another test');

-- --------------------------------------------------------

-- 
-- Table structure for table `STATEMENT`
-- 

CREATE TABLE `STATEMENT` (
  `PKEY` varchar(36) NOT NULL default '',
  `STATEMENT_NAME` varchar(255) NOT NULL default '',
  `STATEMENT_ACC_FKEY` varchar(32) NOT NULL default '',
  `STATEMENT_BEGIN` date NOT NULL default '0000-00-00',
  `STATEMENT_END` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`PKEY`),
  KEY `STATEMENT_ACC_FKEY` (`STATEMENT_ACC_FKEY`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Statement Information';

-- 
-- Dumping data for table `STATEMENT`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `TRANSACTION`
-- 

CREATE TABLE `TRANSACTION` (
  `PKEY` varchar(36) NOT NULL default '',
  `TRANS_NARRATIVE` varchar(255) NOT NULL default '',
  `TRANS_ACCOUNT_FKEY` varchar(32) NOT NULL default '',
  `TRANS_VALUE` int(11) NOT NULL default '0',
  `TRANS_DATE` date default '0000-00-00',
  `TRANS_NOTE` tinytext,
  PRIMARY KEY  (`PKEY`),
  UNIQUE KEY `TRANS_NARRATIVE` (`TRANS_NARRATIVE`,`TRANS_ACCOUNT_FKEY`,`TRANS_VALUE`,`TRANS_DATE`),
  KEY `TRANS_ACCOUNT_FKEY` (`TRANS_ACCOUNT_FKEY`,`TRANS_DATE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Transaction history.';
