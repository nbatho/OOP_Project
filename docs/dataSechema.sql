-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: task_manager_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` char(36) NOT NULL,
  `task_id` char(36) DEFAULT NULL,
  `user_id` char(36) DEFAULT NULL,
  `body` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  KEY `task_id` (`task_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES ('CM1762799105676','TK10','U_ADMIN','hoàn thiện đầy đủ thông tin nhé','2025-11-10 11:25:06'),('CM3','TK15','U18','Sử dụng GitLab CI/CD hay Jenkins?','2025-11-10 08:33:15'),('f8a7b754-0d60-4921-96ea-96f8ce95e15a','63cf2210-607f-495e-a5d8-c0b7bab61e7e','a53508eb-813c-4f67-bde7-de0eaef69bcb','helo','2025-11-29 13:11:04');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_members`
--

DROP TABLE IF EXISTS `project_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_members` (
  `project_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `role_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`project_id`,`user_id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `project_members_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `project_members_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `project_members_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_members`
--

LOCK TABLES `project_members` WRITE;
/*!40000 ALTER TABLE `project_members` DISABLE KEYS */;
INSERT INTO `project_members` VALUES ('P2','a53508eb-813c-4f67-bde7-de0eaef69bcb','R1'),('P3','a53508eb-813c-4f67-bde7-de0eaef69bcb','R1'),('P4','a53508eb-813c-4f67-bde7-de0eaef69bcb','R1'),('P4','U_ADMIN','R1'),('4826f8f7-f519-48e1-a593-f7cf59d501b6','U16','R2'),('5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','6d4354c0-f27a-4768-8e2f-55e202b013f7','R2'),('5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','U1762907651539','R2'),('5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','U1762909759378','R2'),('f6dffa43-7d6a-456e-964c-5b09dd74a5f1','U_ADMIN','R2'),('P1','U12','R2'),('P2','U1','R2'),('P2','U14','R2'),('P2','U2','R2'),('P2','U5','R2'),('P3','U5','R2'),('P4','U1','R2'),('P5','U14','R2'),('P1','U1','R3'),('P1','U13','R3'),('P1','U15','R3'),('P1','U16','R3'),('P1','U18','R3'),('P1','U2','R3'),('P1','U5','R3'),('P1','U6','R3'),('P1','U9','R3'),('P2','U10','R3'),('P2','U12','R3'),('P2','U17','R3'),('P2','U19','R3'),('P2','U3','R3'),('P2','U4','R3'),('P2','U7','R3'),('P2','U8','R3'),('P3','U1','R3'),('P3','U16','R3'),('P3','U18','R3'),('P3','U20','R3'),('P3','U6','R3'),('P3','U7','R3'),('P4','U11','R3'),('P4','U13','R3'),('P4','U15','R3'),('P4','U2','R3'),('P4','U3','R3'),('P4','U9','R3'),('P5','U17','R3'),('P5','U19','R3'),('P5','U20','R3'),('P5','U7','R3'),('P5','U8','R3');
/*!40000 ALTER TABLE `project_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projects` (
  `project_id` char(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects`
--

LOCK TABLES `projects` WRITE;
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
INSERT INTO `projects` VALUES ('15012acc-e1f4-41e9-9181-d938e1332238','test','123','2025-11-24 03:17:43'),('4826f8f7-f519-48e1-a593-f7cf59d501b6','123123123123213','','2025-11-28 08:33:43'),('5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','TestNewProject','123','2025-11-24 03:19:11'),('f6dffa43-7d6a-456e-964c-5b09dd74a5f1','Xây dựng content marketing app','Xây dựng content marketing app','2025-11-10 04:07:21'),('P_demo','P_test','test create p','2025-11-23 08:57:40'),('P1','Website Thương Mại Điện Tử (E-Commerce Web)','Xây dựng website bán hàng đa ngành tích hợp thanh toán và vận chuyển.','2025-11-10 08:33:15'),('P1762877363077','Team Design Click clock','xây dựng giao diện trang CMS ','2025-11-11 09:09:23'),('P1762909923538','test db','csdl','2025-11-11 18:12:04'),('P2','Mobile App Đặt Lịch Dịch Vụ (Booking App)','Ứng dụng di động cho phép người dùng đặt lịch hẹn dịch vụ (salon, gym, khám bệnh).','2025-11-10 08:33:15'),('P3','Hệ thống Authentication + Authorization (Auth Service)','Xây dựng Microservice quản lý đăng nhập, đăng ký, JWT và OAuth.','2025-11-10 08:33:15'),('P4','Dashboard Quản Lý Nội Bộ (Admin Dashboard)','Giao diện quản trị, báo cáo và điều hành hệ thống.','2025-11-10 08:33:15'),('P5','Mini AI Chatbot hỗ trợ khách hàng','Tích hợp mô hình ngôn ngữ lớn để trả lời tự động các câu hỏi của khách hàng.','2025-11-10 08:33:15'),('PROJ001','Advanced E-Commerce Platform','Build an advanced e-commerce platform with AI features','2025-11-19 08:28:23');
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` char(36) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('R1','Admin'),('R2','Manager'),('R3','Member'),('ROLE001','Manager'),('ROLE003','Quality Assurance');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_assignees`
--

DROP TABLE IF EXISTS `task_assignees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_assignees` (
  `task_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  PRIMARY KEY (`task_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `task_assignees_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE,
  CONSTRAINT `task_assignees_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_assignees`
--

LOCK TABLES `task_assignees` WRITE;
/*!40000 ALTER TABLE `task_assignees` DISABLE KEYS */;
INSERT INTO `task_assignees` VALUES ('63cf2210-607f-495e-a5d8-c0b7bab61e7e','a53508eb-813c-4f67-bde7-de0eaef69bcb'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','a53508eb-813c-4f67-bde7-de0eaef69bcb'),('fedf492d-0828-436f-8a60-bebef9bd60c8','a53508eb-813c-4f67-bde7-de0eaef69bcb'),('f76ef839-4a2d-46b0-a73f-56dc9c7a6dfc','U_ADMIN'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','U1'),('TK17','U1'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','U10'),('TK7','U12'),('8f8b0367-f5e8-44b0-9be3-9fa1b24a0afd','U13'),('TK19','U13'),('TK12','U14'),('TK24','U14'),('TK20','U15'),('TK6','U15'),('b15d0a1b-4364-4989-bf87-095c9be55ee5','U16'),('TK15','U16'),('TK16','U16'),('TK6','U16'),('TK11','U17'),('TK23','U17'),('d39b66ba-7f0b-4f6b-ad87-ad4e43bb78fa','U1762907651539'),('fedf492d-0828-436f-8a60-bebef9bd60c8','U1762907651539'),('TK22','U19'),('TK2','U2'),('4e4a753d-a865-4da3-8c8a-acbe06f8a77a','U20'),('TK15','U20'),('TK8','U3'),('TK3','U5'),('TK13','U6'),('TK4','U6'),('TK14','U7'),('TK9','U7'),('TK21','U8'),('TK18','U9');
/*!40000 ALTER TABLE `task_assignees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasks` (
  `task_id` char(36) NOT NULL,
  `project_id` char(36) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `start_date` date DEFAULT NULL,
  `status` enum('TODO','IN_PROGRESS','DONE','CANCELLED') DEFAULT 'TODO',
  `priority` enum('HIGH','MEDIUM','LOW') DEFAULT 'LOW',
  `due_date` date DEFAULT NULL,
  `created_by` char(36) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`),
  KEY `project_id` (`project_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES ('4e4a753d-a865-4da3-8c8a-acbe06f8a77a','P3','User tao tho','THO','2025-11-28','TODO','HIGH','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 08:26:11'),('63cf2210-607f-495e-a5d8-c0b7bab61e7e','P2','Test Delete','demo','2025-11-28','TODO','HIGH','2025-11-26','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 08:12:29'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','P2','hehe','hehe','2025-11-28','TODO','HIGH','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 08:46:02'),('8f8b0367-f5e8-44b0-9be3-9fa1b24a0afd','P4','Nghich thu','','2025-11-28','TODO','LOW','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-25 12:32:08'),('b15d0a1b-4364-4989-bf87-095c9be55ee5','4826f8f7-f519-48e1-a593-f7cf59d501b6','ọc ọc 123','','2025-11-28','TODO','HIGH','2025-11-26','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-29 03:37:22'),('d39b66ba-7f0b-4f6b-ad87-ad4e43bb78fa','5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','123','123','2025-11-28','DONE','HIGH','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-28 08:11:15'),('f76ef839-4a2d-46b0-a73f-56dc9c7a6dfc','P4','demo','omed','2025-11-28','IN_PROGRESS','HIGH','2025-11-28','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-25 12:41:53'),('fedf492d-0828-436f-8a60-bebef9bd60c8','5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','123','123','2025-11-28','TODO','HIGH','2025-11-28','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-27 07:14:00'),('TK10','P2','Prototype mobile screens','Tạo bản mẫu (prototype) chi tiết cho các màn hình di động.','2025-11-28','TODO','HIGH','2025-11-25','U14','2025-11-10 08:33:15'),('TK11','P2','Test mobile app trên 3 thiết bị','Kiểm thử khả năng tương thích của ứng dụng trên iOS, Android và Tablet.','2025-11-28','TODO','HIGH','2025-12-20','U12','2025-11-10 08:33:15'),('TK12','P2','Requirement gathering','Thu thập và phân tích yêu cầu nghiệp vụ chi tiết.','2025-11-28','CANCELLED','LOW','2025-11-29','U14','2025-11-10 08:33:15'),('TK13','P3','JWT Access/Refresh token','Phát triển logic tạo, kiểm tra và làm mới JWT.','2025-11-28','TODO','HIGH','2025-11-28','U5','2025-11-10 08:33:15'),('TK14','P3','OAuth2 Google Login','Tích hợp đăng nhập thông qua Google OAuth2.','2025-11-28','TODO','LOW','2025-12-05','U5','2025-11-10 08:33:15'),('TK15','P3','CI/CD pipeline','Xây dựng pipeline tự động hóa triển khai (Deployment) cho Auth Service.','2025-11-28','TODO','HIGH','2025-12-10','U5','2025-11-10 08:33:15'),('TK16','P3','Test login & token expiration','Kiểm thử bảo mật đăng nhập và cơ chế hết hạn token.','2025-11-28','TODO','LOW','2025-12-12','U7','2025-11-10 08:33:15'),('TK17','P4','Admin table UI + pagination','Xây dựng giao diện bảng quản trị với chức năng phân trang.','2025-11-28','TODO','LOW','2025-11-30','U13','2025-11-10 08:33:15'),('TK18','P4','Dashboard charts','Thiết kế và triển khai các biểu đồ hiển thị dữ liệu trên Dashboard.','2025-11-28','TODO','LOW','2025-12-05','U13','2025-11-10 08:33:15'),('TK19','P4','Functional specification','Viết tài liệu đặc tả chức năng chi tiết cho toàn bộ Dashboard.','2025-11-28','DONE','LOW','2025-11-18','U13','2025-11-10 08:33:15'),('TK2','P1','Product Detail UI + Responsive','Phát triển trang chi tiết sản phẩm, đảm bảo hiển thị tốt trên di động.','2025-11-28','TODO','HIGH','2025-12-10','U13','2025-11-10 08:33:15'),('TK20','P4','Regression test','Thực hiện kiểm thử hồi quy cho toàn bộ chức năng hiện có.','2025-11-28','TODO','LOW','2025-12-10','U1','2025-11-10 08:33:15'),('TK21','P5','LLM API integration','Tích hợp API của mô hình ngôn ngữ lớn (LLM) vào Backend.','2025-11-28','TODO','HIGH','2025-12-15','U14','2025-11-10 08:33:15'),('TK22','P5','Deploy inference server','Triển khai máy chủ suy luận (Inference Server) cho mô hình AI.','2025-11-28','TODO','HIGH','2025-12-20','U14','2025-11-10 08:33:15'),('TK23','P5','Test conversation flows','Kiểm thử các kịch bản hội thoại phức tạp của Chatbot.','2025-11-28','TODO','LOW','2025-12-22','U14','2025-11-10 08:33:15'),('TK24','P5','Business flow diagrams','Thiết kế sơ đồ luồng nghiệp vụ của Chatbot.','2025-11-28','DONE','LOW','2025-11-15','U14','2025-11-10 08:33:15'),('TK3','P1','API Products (CRUD)','Xây dựng API cho phép Thêm, Đọc, Cập nhật, Xóa sản phẩm.','2025-11-28','TODO','HIGH','2025-11-25','U12','2025-11-10 08:33:15'),('TK4','P1','Payment Integration','Tích hợp cổng thanh toán bên thứ ba (ví dụ: VNPAY/Momo).','2025-11-28','TODO','HIGH','2025-12-15','U12','2025-11-10 08:33:15'),('TK6','P1','Test payment flow','Xây dựng kịch bản và thực hiện kiểm thử end-to-end luồng thanh toán.','2025-11-28','TODO','HIGH','2025-12-16','U12','2025-11-10 08:33:15'),('TK7','P1','Viết SRS + Use Case','Tài liệu đặc tả yêu cầu phần mềm và các trường hợp sử dụng.','2025-11-28','DONE','LOW','2025-11-15','U12','2025-11-10 08:33:15'),('TK8','P2','Write booking UI on React Native','Xây dựng giao diện đặt lịch trên ứng dụng di động.','2025-11-28','TODO','LOW','2025-12-05','U14','2025-11-10 08:33:15'),('TK9','P2','API schedule / booking','Xây dựng các API quản lý lịch hẹn và đặt chỗ.','2025-11-28','TODO','HIGH','2025-12-10','U14','2025-11-10 08:33:15');
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` char(36) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('6d4354c0-f27a-4768-8e2f-55e202b013f7','tho','tho12@gmail.com','63bc9e3734391984de9dab53b4a53bf881871b9a36e62b99afd5635fe6fd58c4','2025-11-21 07:56:40'),('7666c99f-f694-4aad-a809-bd45f151fe9b','hehe','hehe@gmail.com','529ca8050a00180790cf88b63468826a','2025-11-23 02:22:01'),('a53508eb-813c-4f67-bde7-de0eaef69bcb','tho','tho@gmail.com','db33f30c28364cd44195ed6105b82c29','2025-11-21 07:51:50'),('f974dd49-dc0e-49ea-b65f-47bcffd4027e','Tran Gia Nam','nam@gmail.com','c4ca4238a0b923820dcc509a6f75849b','2025-11-29 12:27:04'),('U_ADMIN','Super Admin Test','admin.test@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U1','Nguyễn Minh An','an.nguyen@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U10','Đỗ Vũ Mai Lan','lan.do@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U11','Phạm Hải Vy','vy.pham@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U12','Lê Mỹ Duyên','duyen.le@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U13','Huỳnh Ngọc Hà','ha.huynh@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U14','Trần Anh Quân','quan.tran@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U15','Nguyễn Hữu Long','long.nguyen@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U16','Dương Thanh Nhã','nha.duong@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U17','Trịnh Phương Thảo','thao.trinh@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U1762875299790','Lê Nguyễn Ngọc Trang','ancung@gmail.com','d3482746a8442294e57f78034b5d3451','2025-11-11 08:35:00'),('U1762907651539','Trần Gia Mon','mon@gmail.com','786e6c297b3cc01349178c4c361fd9fa','2025-11-11 17:34:12'),('U1762909759378','lehaison','lhs@gmail.com','e10adc3949ba59abbe56e057f20f883e','2025-11-11 18:09:19'),('U18','Bùi Hoàng Quý','quy.bui@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U19','Phạm Trung Kiên','kien.pham@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U2','Trần Khánh Duy','duy.tran@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U20','Đinh Công Hoàng','hoang.dinh@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U3','Lê Thu Hằng','hang.le@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U4','Đỗ Kim Ngân','ngan.do@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U5','Phạm Quốc Huy','huy.pham@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U6','Hồ Minh Tuấn','tuan.ho@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U7','Nguyễn Chí Bảo','bao.nguyen@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U8','Lê Thanh Đức','duc.le@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U9','Hoàng Gia Khang','khang.hoang@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-30  9:56:15
