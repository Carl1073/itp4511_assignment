-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2026-04-24 14:40:53
-- 伺服器版本： 10.4.32-MariaDB
-- PHP 版本： 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `itp4511_assignment_db`
--

-- --------------------------------------------------------

--
-- 資料表結構 `appointment`
--

CREATE TABLE `appointment` (
  `appId` int(11) NOT NULL,
  `patientId` int(11) NOT NULL,
  `timeslotId` int(11) NOT NULL,
  `status` enum('Confirmed','Arrived','No-show','Completed','Cancelled') DEFAULT 'Confirmed',
  `cancelReason` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `clinic`
--

CREATE TABLE `clinic` (
  `clinicId` int(11) NOT NULL,
  `clinicName` varchar(50) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `openTime` time DEFAULT '09:00:00',
  `closeTime` time DEFAULT '18:00:00',
  `isWalkinEnabled` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `clinic`
--

INSERT INTO `clinic` (`clinicId`, `clinicName`, `address`, `openTime`, `closeTime`, `isWalkinEnabled`) VALUES
(1, 'Chai Wan', '12, ABC Street, Chai Wan', '09:00:00', '18:00:00', 1),
(2, 'Tseung Kwan O', '34, DEF Street, Tseung Kwan O', '09:00:00', '18:00:00', 1),
(3, 'Sha Tin', '56, GHI Street, Sha Tin', '09:00:00', '18:00:00', 1),
(4, 'Tuen Mun', '78, JKL Street, Tuen Mun', '09:00:00', '18:00:00', 1),
(5, 'Tsing Yi', '90, BNM Street, Tsing Yi', '09:00:00', '18:00:00', 1);

-- --------------------------------------------------------

--
-- 資料表結構 `incident_log`
--

CREATE TABLE `incident_log` (
  `logId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `eventType` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `notification`
--

CREATE TABLE `notification` (
  `notifId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `message` text NOT NULL,
  `isRead` tinyint(1) DEFAULT 0,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `queue`
--

CREATE TABLE `queue` (
  `queueId` int(11) NOT NULL,
  `patientId` int(11) NOT NULL,
  `clinicId` int(11) NOT NULL,
  `serviceId` int(11) NOT NULL,
  `queueNumber` int(11) NOT NULL,
  `entryTime` time DEFAULT current_timestamp(),
  `status` enum('Waiting','Called','Skipped','Served') DEFAULT 'Waiting'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `service`
--

CREATE TABLE `service` (
  `serviceId` int(11) NOT NULL,
  `serviceName` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `duration` int(11) NOT NULL DEFAULT 60
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `service`
--

INSERT INTO `service` (`serviceId`, `serviceName`, `description`, `price`, `duration`) VALUES
(1, 'General consultation', 'Standard check-up and medical advice.', 150.00, 60),
(2, 'Vaccination', 'Immunization shots for disease prevention.', 100.00, 60),
(3, 'Basic Health Screening', 'Testing vital signs and key health markers.', 200.00, 60);

-- --------------------------------------------------------

--
-- 資料表結構 `timeslot`
--

CREATE TABLE `timeslot` (
  `timeslotId` int(11) NOT NULL,
  `clinicId` int(11) NOT NULL,
  `serviceId` int(11) NOT NULL,
  `quotaPerSlot` int(11) NOT NULL DEFAULT 2,
  `date` date DEFAULT curdate(),
  `openTime` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `timeslot`
--

INSERT INTO `timeslot` (`timeslotId`, `clinicId`, `serviceId`, `quotaPerSlot`, `date`, `openTime`) VALUES
(1, 1, 1, 2, '2026-04-24', '09:00:00'),
(2, 1, 1, 2, '2026-04-24', '10:00:00'),
(3, 1, 1, 2, '2026-04-24', '11:00:00'),
(4, 1, 1, 2, '2026-04-24', '12:00:00'),
(5, 1, 1, 2, '2026-04-24', '13:00:00'),
(6, 1, 1, 2, '2026-04-24', '14:00:00'),
(7, 1, 1, 2, '2026-04-24', '15:00:00'),
(8, 1, 1, 2, '2026-04-24', '16:00:00'),
(9, 1, 1, 2, '2026-04-24', '17:00:00'),
(10, 1, 2, 2, '2026-04-24', '09:00:00'),
(11, 1, 2, 2, '2026-04-24', '10:00:00'),
(12, 1, 2, 2, '2026-04-24', '11:00:00'),
(13, 1, 2, 2, '2026-04-24', '12:00:00'),
(14, 1, 2, 2, '2026-04-24', '13:00:00'),
(15, 1, 2, 2, '2026-04-24', '14:00:00'),
(16, 1, 2, 2, '2026-04-24', '15:00:00'),
(17, 1, 2, 2, '2026-04-24', '16:00:00'),
(18, 1, 2, 2, '2026-04-24', '17:00:00'),
(19, 1, 3, 2, '2026-04-24', '09:00:00'),
(20, 1, 3, 2, '2026-04-24', '10:00:00'),
(21, 1, 3, 2, '2026-04-24', '11:00:00'),
(22, 1, 3, 2, '2026-04-24', '12:00:00'),
(23, 1, 3, 2, '2026-04-24', '13:00:00'),
(24, 1, 3, 2, '2026-04-24', '14:00:00'),
(25, 1, 3, 2, '2026-04-24', '15:00:00'),
(26, 1, 3, 2, '2026-04-24', '16:00:00'),
(27, 1, 3, 2, '2026-04-24', '17:00:00'),
(28, 2, 1, 2, '2026-04-24', '09:00:00'),
(29, 2, 1, 2, '2026-04-24', '10:00:00'),
(30, 2, 1, 2, '2026-04-24', '11:00:00'),
(31, 2, 1, 2, '2026-04-24', '12:00:00'),
(32, 2, 1, 2, '2026-04-24', '13:00:00'),
(33, 2, 1, 2, '2026-04-24', '14:00:00'),
(34, 2, 1, 2, '2026-04-24', '15:00:00'),
(35, 2, 1, 2, '2026-04-24', '16:00:00'),
(36, 2, 1, 2, '2026-04-24', '17:00:00'),
(37, 2, 2, 2, '2026-04-24', '09:00:00'),
(38, 2, 2, 2, '2026-04-24', '10:00:00'),
(39, 2, 2, 2, '2026-04-24', '11:00:00'),
(40, 2, 2, 2, '2026-04-24', '12:00:00'),
(41, 2, 2, 2, '2026-04-24', '13:00:00'),
(42, 2, 2, 2, '2026-04-24', '14:00:00'),
(43, 2, 2, 2, '2026-04-24', '15:00:00'),
(44, 2, 2, 2, '2026-04-24', '16:00:00'),
(45, 2, 2, 2, '2026-04-24', '17:00:00'),
(46, 2, 3, 2, '2026-04-24', '09:00:00'),
(47, 2, 3, 2, '2026-04-24', '10:00:00'),
(48, 2, 3, 2, '2026-04-24', '11:00:00'),
(49, 2, 3, 2, '2026-04-24', '12:00:00'),
(50, 2, 3, 2, '2026-04-24', '13:00:00'),
(51, 2, 3, 2, '2026-04-24', '14:00:00'),
(52, 2, 3, 2, '2026-04-24', '15:00:00'),
(53, 2, 3, 2, '2026-04-24', '16:00:00'),
(54, 2, 3, 2, '2026-04-24', '17:00:00'),
(55, 3, 1, 2, '2026-04-24', '09:00:00'),
(56, 3, 1, 2, '2026-04-24', '10:00:00'),
(57, 3, 1, 2, '2026-04-24', '11:00:00'),
(58, 3, 1, 2, '2026-04-24', '12:00:00'),
(59, 3, 1, 2, '2026-04-24', '13:00:00'),
(60, 3, 1, 2, '2026-04-24', '14:00:00'),
(61, 3, 1, 2, '2026-04-24', '15:00:00'),
(62, 3, 1, 2, '2026-04-24', '16:00:00'),
(63, 3, 1, 2, '2026-04-24', '17:00:00'),
(64, 3, 2, 2, '2026-04-24', '09:00:00'),
(65, 3, 2, 2, '2026-04-24', '10:00:00'),
(66, 3, 2, 2, '2026-04-24', '11:00:00'),
(67, 3, 2, 2, '2026-04-24', '12:00:00'),
(68, 3, 2, 2, '2026-04-24', '13:00:00'),
(69, 3, 2, 2, '2026-04-24', '14:00:00'),
(70, 3, 2, 2, '2026-04-24', '15:00:00'),
(71, 3, 2, 2, '2026-04-24', '16:00:00'),
(72, 3, 2, 2, '2026-04-24', '17:00:00'),
(73, 3, 3, 2, '2026-04-24', '09:00:00'),
(74, 3, 3, 2, '2026-04-24', '10:00:00'),
(75, 3, 3, 2, '2026-04-24', '11:00:00'),
(76, 3, 3, 2, '2026-04-24', '12:00:00'),
(77, 3, 3, 2, '2026-04-24', '13:00:00'),
(78, 3, 3, 2, '2026-04-24', '14:00:00'),
(79, 3, 3, 2, '2026-04-24', '15:00:00'),
(80, 3, 3, 2, '2026-04-24', '16:00:00'),
(81, 3, 3, 2, '2026-04-24', '17:00:00'),
(82, 4, 1, 2, '2026-04-24', '09:00:00'),
(83, 4, 1, 2, '2026-04-24', '10:00:00'),
(84, 4, 1, 2, '2026-04-24', '11:00:00'),
(85, 4, 1, 2, '2026-04-24', '12:00:00'),
(86, 4, 1, 2, '2026-04-24', '13:00:00'),
(87, 4, 1, 2, '2026-04-24', '14:00:00'),
(88, 4, 1, 2, '2026-04-24', '15:00:00'),
(89, 4, 1, 2, '2026-04-24', '16:00:00'),
(90, 4, 1, 2, '2026-04-24', '17:00:00'),
(91, 4, 2, 2, '2026-04-24', '09:00:00'),
(92, 4, 2, 2, '2026-04-24', '10:00:00'),
(93, 4, 2, 2, '2026-04-24', '11:00:00'),
(94, 4, 2, 2, '2026-04-24', '12:00:00'),
(95, 4, 2, 2, '2026-04-24', '13:00:00'),
(96, 4, 2, 2, '2026-04-24', '14:00:00'),
(97, 4, 2, 2, '2026-04-24', '15:00:00'),
(98, 4, 2, 2, '2026-04-24', '16:00:00'),
(99, 4, 2, 2, '2026-04-24', '17:00:00'),
(100, 4, 3, 2, '2026-04-24', '09:00:00'),
(101, 4, 3, 2, '2026-04-24', '10:00:00'),
(102, 4, 3, 2, '2026-04-24', '11:00:00'),
(103, 4, 3, 2, '2026-04-24', '12:00:00'),
(104, 4, 3, 2, '2026-04-24', '13:00:00'),
(105, 4, 3, 2, '2026-04-24', '14:00:00'),
(106, 4, 3, 2, '2026-04-24', '15:00:00'),
(107, 4, 3, 2, '2026-04-24', '16:00:00'),
(108, 4, 3, 2, '2026-04-24', '17:00:00'),
(109, 5, 1, 2, '2026-04-24', '09:00:00'),
(110, 5, 1, 2, '2026-04-24', '10:00:00'),
(111, 5, 1, 2, '2026-04-24', '11:00:00'),
(112, 5, 1, 2, '2026-04-24', '12:00:00'),
(113, 5, 1, 2, '2026-04-24', '13:00:00'),
(114, 5, 1, 2, '2026-04-24', '14:00:00'),
(115, 5, 1, 2, '2026-04-24', '15:00:00'),
(116, 5, 1, 2, '2026-04-24', '16:00:00'),
(117, 5, 1, 2, '2026-04-24', '17:00:00'),
(118, 5, 2, 2, '2026-04-24', '09:00:00'),
(119, 5, 2, 2, '2026-04-24', '10:00:00'),
(120, 5, 2, 2, '2026-04-24', '11:00:00'),
(121, 5, 2, 2, '2026-04-24', '12:00:00'),
(122, 5, 2, 2, '2026-04-24', '13:00:00'),
(123, 5, 2, 2, '2026-04-24', '14:00:00'),
(124, 5, 2, 2, '2026-04-24', '15:00:00'),
(125, 5, 2, 2, '2026-04-24', '16:00:00'),
(126, 5, 2, 2, '2026-04-24', '17:00:00'),
(127, 5, 3, 2, '2026-04-24', '09:00:00'),
(128, 5, 3, 2, '2026-04-24', '10:00:00'),
(129, 5, 3, 2, '2026-04-24', '11:00:00'),
(130, 5, 3, 2, '2026-04-24', '12:00:00'),
(131, 5, 3, 2, '2026-04-24', '13:00:00'),
(132, 5, 3, 2, '2026-04-24', '14:00:00'),
(133, 5, 3, 2, '2026-04-24', '15:00:00'),
(134, 5, 3, 2, '2026-04-24', '16:00:00'),
(135, 5, 3, 2, '2026-04-24', '17:00:00');

-- --------------------------------------------------------

--
-- 資料表結構 `user`
--

CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `fullName` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `gender` enum('M','F','O') DEFAULT NULL,
  `role` enum('Patient','Staff','Admin') NOT NULL,
  `clinicId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `user`
--

INSERT INTO `user` (`userId`, `username`, `password`, `fullName`, `email`, `phone`, `gender`, `role`, `clinicId`) VALUES
(1, 'abc123', '123', 'Alex Tong', '1231@xxx.jiji', '12312312', 'F', 'Patient', 0);

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `appointment`
--
ALTER TABLE `appointment`
  ADD PRIMARY KEY (`appId`),
  ADD KEY `patientId` (`patientId`),
  ADD KEY `timeslotId` (`timeslotId`);

--
-- 資料表索引 `clinic`
--
ALTER TABLE `clinic`
  ADD PRIMARY KEY (`clinicId`),
  ADD UNIQUE KEY `clinicName` (`clinicName`);

--
-- 資料表索引 `incident_log`
--
ALTER TABLE `incident_log`
  ADD PRIMARY KEY (`logId`),
  ADD KEY `userId` (`userId`);

--
-- 資料表索引 `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notifId`),
  ADD KEY `userId` (`userId`);

--
-- 資料表索引 `queue`
--
ALTER TABLE `queue`
  ADD PRIMARY KEY (`queueId`),
  ADD KEY `patientId` (`patientId`),
  ADD KEY `clinicId` (`clinicId`),
  ADD KEY `serviceId` (`serviceId`);

--
-- 資料表索引 `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`serviceId`);

--
-- 資料表索引 `timeslot`
--
ALTER TABLE `timeslot`
  ADD PRIMARY KEY (`timeslotId`),
  ADD KEY `clinicId` (`clinicId`),
  ADD KEY `serviceId` (`serviceId`);

--
-- 資料表索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`),
  ADD UNIQUE KEY `username` (`username`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `appointment`
--
ALTER TABLE `appointment`
  MODIFY `appId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `clinic`
--
ALTER TABLE `clinic`
  MODIFY `clinicId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `incident_log`
--
ALTER TABLE `incident_log`
  MODIFY `logId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `notification`
--
ALTER TABLE `notification`
  MODIFY `notifId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `queue`
--
ALTER TABLE `queue`
  MODIFY `queueId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `service`
--
ALTER TABLE `service`
  MODIFY `serviceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `timeslot`
--
ALTER TABLE `timeslot`
  MODIFY `timeslotId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=136;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `appointment`
--
ALTER TABLE `appointment`
  ADD CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patientId`) REFERENCES `user` (`userId`),
  ADD CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`timeslotId`) REFERENCES `timeslot` (`timeslotId`);

--
-- 資料表的限制式 `incident_log`
--
ALTER TABLE `incident_log`
  ADD CONSTRAINT `incident_log_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);

--
-- 資料表的限制式 `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);

--
-- 資料表的限制式 `queue`
--
ALTER TABLE `queue`
  ADD CONSTRAINT `queue_ibfk_1` FOREIGN KEY (`patientId`) REFERENCES `user` (`userId`),
  ADD CONSTRAINT `queue_ibfk_2` FOREIGN KEY (`clinicId`) REFERENCES `clinic` (`clinicId`),
  ADD CONSTRAINT `queue_ibfk_3` FOREIGN KEY (`serviceId`) REFERENCES `service` (`serviceId`);

--
-- 資料表的限制式 `timeslot`
--
ALTER TABLE `timeslot`
  ADD CONSTRAINT `timeslot_ibfk_1` FOREIGN KEY (`clinicId`) REFERENCES `clinic` (`clinicId`),
  ADD CONSTRAINT `timeslot_ibfk_2` FOREIGN KEY (`serviceId`) REFERENCES `service` (`serviceId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
