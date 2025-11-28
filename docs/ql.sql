CREATE DATABASE  IF NOT EXISTS `task_manager_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `task_manager_db`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: task_manager_db
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
-- Table structure for table `checklist_items`
--

DROP TABLE IF EXISTS `checklist_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklist_items` (
  `item_id` varchar(20) NOT NULL,
  `checklist_id` char(36) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `is_done` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `checklist_id` (`checklist_id`),
  CONSTRAINT `checklist_items_ibfk_1` FOREIGN KEY (`checklist_id`) REFERENCES `checklists` (`checklist_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklist_items`
--

LOCK TABLES `checklist_items` WRITE;
/*!40000 ALTER TABLE `checklist_items` DISABLE KEYS */;
INSERT INTO `checklist_items` VALUES ('CLI5','CL2','Validate email',1),('CLI6','CL2','Hash password',1),('CLI7','CL2','Generate JWT',0),('CLI8','CL2','Unit test',0);
/*!40000 ALTER TABLE `checklist_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checklists`
--

DROP TABLE IF EXISTS `checklists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklists` (
  `checklist_id` char(36) NOT NULL,
  `task_id` char(36) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`checklist_id`),
  KEY `task_id` (`task_id`),
  CONSTRAINT `checklists_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklists`
--

LOCK TABLES `checklists` WRITE;
/*!40000 ALTER TABLE `checklists` DISABLE KEYS */;
INSERT INTO `checklists` VALUES ('CL2','TK13','Checklist API Login');
/*!40000 ALTER TABLE `checklists` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `comments` VALUES ('c130c3ab-16da-4368-9a08-9b93124942c3','02ce5134-cb0a-4e4d-9080-b2fb5b348915','a53508eb-813c-4f67-bde7-de0eaef69bcb','cu the thoi','2025-11-26 08:21:30'),('CM1762799105676','TK10','U_ADMIN','hoàn thiện đầy đủ thông tin nhé','2025-11-10 11:25:06'),('CM3','TK15','U18','Sử dụng GitLab CI/CD hay Jenkins?','2025-11-10 08:33:15');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labels`
--

DROP TABLE IF EXISTS `labels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `labels` (
  `label_id` char(36) NOT NULL,
  `team_id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `color` char(7) NOT NULL,
  PRIMARY KEY (`label_id`),
  KEY `team_id` (`team_id`),
  CONSTRAINT `labels_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labels`
--

LOCK TABLES `labels` WRITE;
/*!40000 ALTER TABLE `labels` DISABLE KEYS */;
/*!40000 ALTER TABLE `labels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `permission_id` char(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text,
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
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
  CONSTRAINT `project_members_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `project_members_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `project_members_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_members`
--

LOCK TABLES `project_members` WRITE;
/*!40000 ALTER TABLE `project_members` DISABLE KEYS */;
INSERT INTO `project_members` VALUES ('P2','a53508eb-813c-4f67-bde7-de0eaef69bcb','R1'),('P3','a53508eb-813c-4f67-bde7-de0eaef69bcb','R1'),('P4','a53508eb-813c-4f67-bde7-de0eaef69bcb','R1'),('P4','U_ADMIN','R1'),('5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','a53508eb-813c-4f67-bde7-de0eaef69bcb','R2'),('5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','U1762907651539','R2'),('753297ca-260b-45ea-ad83-261601da33f7','a53508eb-813c-4f67-bde7-de0eaef69bcb','R2'),('753297ca-260b-45ea-ad83-261601da33f7','U_ADMIN','R2'),('753297ca-260b-45ea-ad83-261601da33f7','U10','R2'),('f6dffa43-7d6a-456e-964c-5b09dd74a5f1','U_ADMIN','R2'),('P1','U12','R2'),('P2','U1','R2'),('P2','U14','R2'),('P2','U2','R2'),('P2','U5','R2'),('P3','U5','R2'),('P4','U1','R2'),('P5','U14','R2'),('P1','U1','R3'),('P1','U13','R3'),('P1','U15','R3'),('P1','U16','R3'),('P1','U18','R3'),('P1','U2','R3'),('P1','U5','R3'),('P1','U6','R3'),('P1','U9','R3'),('P2','U10','R3'),('P2','U12','R3'),('P2','U17','R3'),('P2','U19','R3'),('P2','U3','R3'),('P2','U4','R3'),('P2','U7','R3'),('P2','U8','R3'),('P3','U1','R3'),('P3','U16','R3'),('P3','U18','R3'),('P3','U20','R3'),('P3','U6','R3'),('P3','U7','R3'),('P4','U11','R3'),('P4','U13','R3'),('P4','U15','R3'),('P4','U2','R3'),('P4','U3','R3'),('P4','U9','R3'),('P5','U17','R3'),('P5','U19','R3'),('P5','U20','R3'),('P5','U7','R3'),('P5','U8','R3');
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
  `team_id` char(36) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`project_id`),
  KEY `team_id` (`team_id`),
  CONSTRAINT `projects_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects`
--

LOCK TABLES `projects` WRITE;
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
INSERT INTO `projects` VALUES ('0fe6238e-1da2-4ccb-b738-c495f2303552',NULL,'NewProject','test','2025-11-24 03:15:12'),('15012acc-e1f4-41e9-9181-d938e1332238',NULL,'test','123','2025-11-24 03:17:43'),('5fb21d79-a9fb-4d33-8dc0-5a4656f666cc',NULL,'TestNewProject','123','2025-11-24 03:19:11'),('753297ca-260b-45ea-ad83-261601da33f7',NULL,'Ọc ọc ọc ','ọc ','2025-11-27 00:47:32'),('f6dffa43-7d6a-456e-964c-5b09dd74a5f1','62072c6a-1190-49ee-8ea3-c1e8a0f108f3','Xây dựng content marketing app','Xây dựng content marketing app','2025-11-10 04:07:21'),('P_demo',NULL,'P_test','test create p','2025-11-23 08:57:40'),('P1','T_BA','Website Thương Mại Điện Tử (E-Commerce Web)','Xây dựng website bán hàng đa ngành tích hợp thanh toán và vận chuyển.','2025-11-10 08:33:15'),('P1762877363077','62072c6a-1190-49ee-8ea3-c1e8a0f108f3','Team Design Click clock','xây dựng giao diện trang CMS ','2025-11-11 09:09:23'),('P1762909923538','62072c6a-1190-49ee-8ea3-c1e8a0f108f3','test db','csdl','2025-11-11 18:12:04'),('P2','T_BA','Mobile App Đặt Lịch Dịch Vụ (Booking App)','Ứng dụng di động cho phép người dùng đặt lịch hẹn dịch vụ (salon, gym, khám bệnh).','2025-11-10 08:33:15'),('P3','T_BE','Hệ thống Authentication + Authorization (Auth Service)','Xây dựng Microservice quản lý đăng nhập, đăng ký, JWT và OAuth.','2025-11-10 08:33:15'),('P4','T_BA','Dashboard Quản Lý Nội Bộ (Admin Dashboard)','Giao diện quản trị, báo cáo và điều hành hệ thống.','2025-11-10 08:33:15'),('P5','T_BA','Mini AI Chatbot hỗ trợ khách hàng','Tích hợp mô hình ngôn ngữ lớn để trả lời tự động các câu hỏi của khách hàng.','2025-11-10 08:33:15'),('PROJ001','TEAM001','Advanced E-Commerce Platform','Build an advanced e-commerce platform with AI features','2025-11-19 08:28:23');
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permissions` (
  `role_id` char(36) NOT NULL,
  `permission_id` char(36) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`permission_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
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
INSERT INTO `task_assignees` VALUES ('02ce5134-cb0a-4e4d-9080-b2fb5b348915','a53508eb-813c-4f67-bde7-de0eaef69bcb'),('63cf2210-607f-495e-a5d8-c0b7bab61e7e','a53508eb-813c-4f67-bde7-de0eaef69bcb'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','a53508eb-813c-4f67-bde7-de0eaef69bcb'),('f76ef839-4a2d-46b0-a73f-56dc9c7a6dfc','U_ADMIN'),('5f0443fc-3613-4a4e-aebb-2131a671e27b','U1'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','U1'),('TK17','U1'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','U10'),('5f0443fc-3613-4a4e-aebb-2131a671e27b','U12'),('TK7','U12'),('8f8b0367-f5e8-44b0-9be3-9fa1b24a0afd','U13'),('TK19','U13'),('TK12','U14'),('TK24','U14'),('TK20','U15'),('TK6','U15'),('TK15','U16'),('TK16','U16'),('TK6','U16'),('TK11','U17'),('TK23','U17'),('TK22','U19'),('TK2','U2'),('4e4a753d-a865-4da3-8c8a-acbe06f8a77a','U20'),('TK15','U20'),('TK8','U3'),('TK3','U5'),('TK13','U6'),('TK4','U6'),('TK14','U7'),('TK9','U7'),('TK21','U8'),('TK18','U9');
/*!40000 ALTER TABLE `task_assignees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_labels`
--

DROP TABLE IF EXISTS `task_labels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_labels` (
  `task_id` char(36) NOT NULL,
  `label_id` char(36) NOT NULL,
  PRIMARY KEY (`task_id`,`label_id`),
  KEY `label_id` (`label_id`),
  CONSTRAINT `task_labels_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE,
  CONSTRAINT `task_labels_ibfk_2` FOREIGN KEY (`label_id`) REFERENCES `labels` (`label_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_labels`
--

LOCK TABLES `task_labels` WRITE;
/*!40000 ALTER TABLE `task_labels` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_labels` ENABLE KEYS */;
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
  `status` varchar(50) DEFAULT NULL,
  `priority` varchar(50) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `created_by` char(36) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`),
  KEY `project_id` (`project_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `tasks_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES ('02ce5134-cb0a-4e4d-9080-b2fb5b348915','5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','tho','tho','TODO','HIGH','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 00:55:47'),('4e4a753d-a865-4da3-8c8a-acbe06f8a77a','P3','User tao tho','THO','TODO','HIGH','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 08:26:11'),('5f0443fc-3613-4a4e-aebb-2131a671e27b','P2','demo','123','IN_PROGRESS','HIGH','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 08:44:54'),('63cf2210-607f-495e-a5d8-c0b7bab61e7e','P2','Test Delete','demo','TODO','HIGH','2025-11-26','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 08:12:29'),('7ea2cfdb-9c6d-4623-8289-1e0305f49a5e','P2','hehe','hehe','TODO','HIGH','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-26 08:46:02'),('8f8b0367-f5e8-44b0-9be3-9fa1b24a0afd','P4','Nghich thu','','Todo','Low','2025-11-27','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-25 12:32:08'),('ed32a7bc-1af2-4495-8803-5b3949943c35','5fb21d79-a9fb-4d33-8dc0-5a4656f666cc','123123','123','Todo','Low','2025-11-25','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-25 08:33:42'),('f76ef839-4a2d-46b0-a73f-56dc9c7a6dfc','P4','demo','omed','IN_PROGRESS','HIGH','2025-11-28','a53508eb-813c-4f67-bde7-de0eaef69bcb','2025-11-25 12:41:53'),('TK10','P2','Prototype mobile screens','Tạo bản mẫu (prototype) chi tiết cho các màn hình di động.','TODO','HIGH','2025-11-25','U14','2025-11-10 08:33:15'),('TK11','P2','Test mobile app trên 3 thiết bị','Kiểm thử khả năng tương thích của ứng dụng trên iOS, Android và Tablet.','Todo','High','2025-12-20','U12','2025-11-10 08:33:15'),('TK12','P2','Requirement gathering','Thu thập và phân tích yêu cầu nghiệp vụ chi tiết.','Done','Normal','2025-11-12','U14','2025-11-10 08:33:15'),('TK13','P3','JWT Access/Refresh token','Phát triển logic tạo, kiểm tra và làm mới JWT.','Todo','High','2025-11-28','U5','2025-11-10 08:33:15'),('TK14','P3','OAuth2 Google Login','Tích hợp đăng nhập thông qua Google OAuth2.','Todo','Normal','2025-12-05','U5','2025-11-10 08:33:15'),('TK15','P3','CI/CD pipeline','Xây dựng pipeline tự động hóa triển khai (Deployment) cho Auth Service.','In Progress','High','2025-12-10','U5','2025-11-10 08:33:15'),('TK16','P3','Test login & token expiration','Kiểm thử bảo mật đăng nhập và cơ chế hết hạn token.','Todo','Normal','2025-12-12','U7','2025-11-10 08:33:15'),('TK17','P4','Admin table UI + pagination','Xây dựng giao diện bảng quản trị với chức năng phân trang.','Todo','Normal','2025-11-30','U13','2025-11-10 08:33:15'),('TK18','P4','Dashboard charts','Thiết kế và triển khai các biểu đồ hiển thị dữ liệu trên Dashboard.','In Progress','Normal','2025-12-05','U13','2025-11-10 08:33:15'),('TK19','P4','Functional specification','Viết tài liệu đặc tả chức năng chi tiết cho toàn bộ Dashboard.','Done','Normal','2025-11-18','U13','2025-11-10 08:33:15'),('TK2','P1','Product Detail UI + Responsive','Phát triển trang chi tiết sản phẩm, đảm bảo hiển thị tốt trên di động.','Todo','High','2025-12-10','U13','2025-11-10 08:33:15'),('TK20','P4','Regression test','Thực hiện kiểm thử hồi quy cho toàn bộ chức năng hiện có.','Todo','Low','2025-12-10','U1','2025-11-10 08:33:15'),('TK21','P5','LLM API integration','Tích hợp API của mô hình ngôn ngữ lớn (LLM) vào Backend.','Todo','High','2025-12-15','U14','2025-11-10 08:33:15'),('TK22','P5','Deploy inference server','Triển khai máy chủ suy luận (Inference Server) cho mô hình AI.','Todo','High','2025-12-20','U14','2025-11-10 08:33:15'),('TK23','P5','Test conversation flows','Kiểm thử các kịch bản hội thoại phức tạp của Chatbot.','Todo','Normal','2025-12-22','U14','2025-11-10 08:33:15'),('TK24','P5','Business flow diagrams','Thiết kế sơ đồ luồng nghiệp vụ của Chatbot.','Done','Normal','2025-11-15','U14','2025-11-10 08:33:15'),('TK3','P1','API Products (CRUD)','Xây dựng API cho phép Thêm, Đọc, Cập nhật, Xóa sản phẩm.','Todo','High','2025-11-25','U12','2025-11-10 08:33:15'),('TK4','P1','Payment Integration','Tích hợp cổng thanh toán bên thứ ba (ví dụ: VNPAY/Momo).','Todo','High','2025-12-15','U12','2025-11-10 08:33:15'),('TK6','P1','Test payment flow','Xây dựng kịch bản và thực hiện kiểm thử end-to-end luồng thanh toán.','Todo','High','2025-12-16','U12','2025-11-10 08:33:15'),('TK7','P1','Viết SRS + Use Case','Tài liệu đặc tả yêu cầu phần mềm và các trường hợp sử dụng.','Done','Normal','2025-11-15','U12','2025-11-10 08:33:15'),('TK8','P2','Write booking UI on React Native','Xây dựng giao diện đặt lịch trên ứng dụng di động.','Todo','Normal','2025-12-05','U14','2025-11-10 08:33:15'),('TK9','P2','API schedule / booking','Xây dựng các API quản lý lịch hẹn và đặt chỗ.','Todo','High','2025-12-10','U14','2025-11-10 08:33:15');
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_members`
--

DROP TABLE IF EXISTS `team_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_members` (
  `team_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `role_id` char(36) NOT NULL,
  PRIMARY KEY (`team_id`,`user_id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `team_members_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`) ON DELETE CASCADE,
  CONSTRAINT `team_members_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `team_members_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_members`
--

LOCK TABLES `team_members` WRITE;
/*!40000 ALTER TABLE `team_members` DISABLE KEYS */;
/*!40000 ALTER TABLE `team_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `team_id` char(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` VALUES ('62072c6a-1190-49ee-8ea3-c1e8a0f108f3','Nhóm MAR','2025-11-10 04:07:21'),('T_123123123','Team fe','2025-11-19 06:39:00'),('T_1762875543217','Team Design Clickapp','2025-11-11 08:39:03'),('T_1762875617301','Team Slide ','2025-11-11 08:40:17'),('T_BA','Business Analyst Team','2025-11-10 08:33:15'),('T_BE','Backend Team','2025-11-10 08:33:15'),('T_DEVOPS','DevOps Team','2025-11-10 08:33:15'),('T_FE','Frontend Team','2025-11-10 08:33:15'),('T_QA','Quality Assurance Team','2025-11-10 08:33:15'),('T_UI','UI/UX Team','2025-11-10 08:33:15'),('TEAM001','Backend Development Team','2025-11-19 08:28:23');
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
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
INSERT INTO `users` VALUES ('6d4354c0-f27a-4768-8e2f-55e202b013f7','tho','tho12@gmail.com','63bc9e3734391984de9dab53b4a53bf881871b9a36e62b99afd5635fe6fd58c4','2025-11-21 07:56:40'),('7666c99f-f694-4aad-a809-bd45f151fe9b','hehe','hehe@gmail.com','529ca8050a00180790cf88b63468826a','2025-11-23 02:22:01'),('a53508eb-813c-4f67-bde7-de0eaef69bcb','tho','tho@gmail.com','db33f30c28364cd44195ed6105b82c29','2025-11-21 07:51:50'),('U_ADMIN','Super Admin Test','admin.test@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U1','Nguyễn Minh An','an.nguyen@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U10','Đỗ Vũ Mai Lan','lan.do@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U11','Phạm Hải Vy','vy.pham@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U12','Lê Mỹ Duyên','duyen.le@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U13','Huỳnh Ngọc Hà','ha.huynh@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U14','Trần Anh Quân','quan.tran@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U15','Nguyễn Hữu Long','long.nguyen@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U16','Dương Thanh Nhã','nha.duong@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U17','Trịnh Phương Thảo','thao.trinh@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U1762875299790','Lê Nguyễn Ngọc Trang','ancung@gmail.com','d3482746a8442294e57f78034b5d3451','2025-11-11 08:35:00'),('U1762907651539','Trần Gia Mon','mon@gmail.com','786e6c297b3cc01349178c4c361fd9fa','2025-11-11 17:34:12'),('U1762909759378','lehaison','lhs@gmail.com','e10adc3949ba59abbe56e057f20f883e','2025-11-11 18:09:19'),('U18','Bùi Hoàng Quý','quy.bui@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U19','Phạm Trung Kiên','kien.pham@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U2','Trần Khánh Duy','duy.tran@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U20','Đinh Công Hoàng','hoang.dinh@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U3','Lê Thu Hằng','hang.le@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U4','Đỗ Kim Ngân','ngan.do@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U5','Phạm Quốc Huy','huy.pham@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U6','Hồ Minh Tuấn','tuan.ho@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U7','Nguyễn Chí Bảo','bao.nguyen@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U8','Lê Thanh Đức','duc.le@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15'),('U9','Hoàng Gia Khang','khang.hoang@example.com','81dc9bdb52d04dc20036dbd8313ed055','2025-11-10 08:33:15');
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

-- Dump completed on 2025-11-27  7:48:54
