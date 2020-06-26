CREATE DATABASE `notekeeper`;

USE notekeeper;

CREATE TABLE `Note` (
  `id` int NOT NULL AUTO_INCREMENT,
  `CreatedOn` timestamp(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `title` mediumtext,
  `note` longtext,
  PRIMARY KEY (`id`)
);

CREATE TABLE `User` (
  `id` int NOT NULL AUTO_INCREMENT,
  `CreatedOn` timestamp(6) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `resetKey` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);
