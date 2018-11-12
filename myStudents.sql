-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 12, 2018 at 07:13 AM
-- Server version: 5.7.23
-- PHP Version: 7.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Assign2`
--

-- --------------------------------------------------------

--
-- Table structure for table `myStudents`
--

CREATE TABLE `myStudents` (
  `SID` int(2) NOT NULL,
  `STUD_ID` int(8) NOT NULL,
  `FNAME` varchar(20) NOT NULL,
  `SNAME` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `myStudents`
--

INSERT INTO `myStudents` (`SID`, `STUD_ID`, `FNAME`, `SNAME`) VALUES
(1, 20040001, 'John', 'Peterson'),
(2, 20040002, 'Paul', 'Barry'),
(3, 20040003, 'Sarah', 'Finnerty'),
(4, 20040004, 'Jordan', 'Long');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `myStudents`
--
ALTER TABLE `myStudents`
  ADD UNIQUE KEY `SID` (`SID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
