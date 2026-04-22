-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2026-04-22 16:49:09
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
  `clinicId` int(11) NOT NULL,
  `serviceId` int(11) NOT NULL,
  `appDate` date NOT NULL,
  `timeslot` time NOT NULL,
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
  `isWalkinEnabled` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `clinic_service`
--

CREATE TABLE `clinic_service` (
  `clinicId` int(11) NOT NULL,
  `serviceId` int(11) NOT NULL,
  `quotaPerSlot` int(11) NOT NULL DEFAULT 1,
  `duration` int(11) NOT NULL DEFAULT 30
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `entryTime` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('Waiting','Called','Skipped','Served') DEFAULT 'Waiting'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `service`
--

CREATE TABLE `service` (
  `serviceId` int(11) NOT NULL,
  `serviceName` varchar(100) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(1, 'test', 'test', 'Test', 'Test', 'Test', 'M', 'Patient', 0),
(3, 'edittest3', 'test', 'Test', 'Test', 'Test', 'M', 'Patient', 0),
(4, 'abc123', '123123', 'Alex Tong', 'pointy13580@gmail.com', '98042008', 'M', 'Patient', 0);

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `appointment`
--
ALTER TABLE `appointment`
  ADD PRIMARY KEY (`appId`),
  ADD KEY `patientId` (`patientId`),
  ADD KEY `clinicId` (`clinicId`),
  ADD KEY `serviceId` (`serviceId`);

--
-- 資料表索引 `clinic`
--
ALTER TABLE `clinic`
  ADD PRIMARY KEY (`clinicId`);

--
-- 資料表索引 `clinic_service`
--
ALTER TABLE `clinic_service`
  ADD PRIMARY KEY (`clinicId`,`serviceId`),
  ADD KEY `serviceId` (`serviceId`);

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
  MODIFY `clinicId` int(11) NOT NULL AUTO_INCREMENT;

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
  MODIFY `serviceId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `appointment`
--
ALTER TABLE `appointment`
  ADD CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patientId`) REFERENCES `user` (`userId`),
  ADD CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`clinicId`) REFERENCES `clinic` (`clinicId`),
  ADD CONSTRAINT `appointment_ibfk_3` FOREIGN KEY (`serviceId`) REFERENCES `service` (`serviceId`);

--
-- 資料表的限制式 `clinic_service`
--
ALTER TABLE `clinic_service`
  ADD CONSTRAINT `clinic_service_ibfk_1` FOREIGN KEY (`clinicId`) REFERENCES `clinic` (`clinicId`),
  ADD CONSTRAINT `clinic_service_ibfk_2` FOREIGN KEY (`serviceId`) REFERENCES `service` (`serviceId`);

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
