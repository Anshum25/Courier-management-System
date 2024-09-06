-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 28, 2024 at 06:02 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `courier_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `couriers`
--

CREATE TABLE `couriers` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `recipient_name` varchar(100) NOT NULL,
  `recipient_address` varchar(255) NOT NULL,
  `package_description` varchar(245) DEFAULT NULL,
  `package_weight` double NOT NULL,
  `price` double DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `pickup_details` varchar(255) DEFAULT NULL,
  `pickup_time` timestamp NULL DEFAULT current_timestamp(),
  `delivery_time` timestamp NULL DEFAULT current_timestamp(),
  `worker_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `couriers`
--

INSERT INTO `couriers` (`id`, `customer_id`, `recipient_name`, `recipient_address`, `package_description`, `package_weight`, `price`, `status`, `pickup_details`, `pickup_time`, `delivery_time`, `worker_id`) VALUES
(12, 11, 'k', 'mm', 'bbb', 90, 1000, 'In Transit', NULL, '2024-08-24 05:55:00', '2024-08-24 05:55:00', NULL),
(13, 11, 'q', 'w', 'q', 12, 220, 'Pending Pickup', NULL, '2024-08-24 05:56:36', '2024-08-24 05:56:36', NULL),
(14, 11, 'q', 'w', 'q', 12, 220, 'Delivered', NULL, '2009-08-08 18:30:00', '2006-09-07 18:30:00', 6),
(15, 11, 'p', 'q', 'w', 1445, 14550, 'Pending Pickup', NULL, '2024-08-24 06:18:02', '2024-08-24 06:18:02', NULL),
(16, 11, 'q', 'w', 'q', 567, 5770, 'Pending Pickup', NULL, '2024-08-24 06:19:29', '2024-08-24 06:19:29', NULL),
(17, 11, 'q', 'w', 'q', 67, 770, 'Delivered', NULL, '2024-09-08 18:30:00', '2024-09-07 18:30:00', 6),
(18, 12, 'umang', 'b806', 'fhjdbef', 90, 1000, 'In Transit', NULL, '2024-08-28 15:54:37', '2024-08-28 15:54:37', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `couriers`
--
ALTER TABLE `couriers`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `couriers`
--
ALTER TABLE `couriers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
