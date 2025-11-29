CREATE DATABASE IF NOT EXISTS `task_manager_db`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE `task_manager_db`;

-- ----------------------------
-- Table: users
-- ----------------------------
CREATE TABLE `users` (
  `user_id` char(36) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table: roles
-- ----------------------------
CREATE TABLE `roles` (
  `role_id` char(36) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table: projects
-- ----------------------------
CREATE TABLE `projects` (
  `project_id` char(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table: tasks
-- ----------------------------
CREATE TABLE `tasks` (
  `task_id` char(36) NOT NULL,
  `project_id` char(36) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `status` enum('TODO','IN_PROGRESS','DONE','CANCELLED') DEFAULT 'TODO',
  `priority` enum('HIGH','MEDIUM','LOW') DEFAULT 'LOW',
  `due_date` date DEFAULT NULL,
  `created_by` char(36) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`),
  KEY `project_id` (`project_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`project_id`)
        REFERENCES `projects` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_ibfk_2` FOREIGN KEY (`created_by`)
        REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table: comments
-- ----------------------------
CREATE TABLE `comments` (
  `comment_id` char(36) NOT NULL,
  `task_id` char(36) DEFAULT NULL,
  `user_id` char(36) DEFAULT NULL,
  `body` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  KEY `task_id` (`task_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`task_id`)
        REFERENCES `tasks` (`task_id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table: project_members
-- ----------------------------
CREATE TABLE `project_members` (
  `project_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `role_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`project_id`, `user_id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `project_members_ibfk_1` FOREIGN KEY (`project_id`)
        REFERENCES `projects` (`project_id`) ON DELETE CASCADE,
  CONSTRAINT `project_members_ibfk_2` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`user_id`),
  CONSTRAINT `project_members_ibfk_3` FOREIGN KEY (`role_id`)
        REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table: task_assignees
-- ----------------------------
CREATE TABLE `task_assignees` (
  `task_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  PRIMARY KEY (`task_id`, `user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `task_assignees_ibfk_1` FOREIGN KEY (`task_id`)
        REFERENCES `tasks` (`task_id`) ON DELETE CASCADE,
  CONSTRAINT `task_assignees_ibfk_2` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
