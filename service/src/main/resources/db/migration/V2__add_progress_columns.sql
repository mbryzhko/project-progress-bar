ALTER TABLE `project`
ADD COLUMN `start_date` DATETIME NULL AFTER `end_state`,
ADD COLUMN `end_date` DATETIME NULL AFTER `start_date`,
ADD COLUMN `progress_state` VARCHAR(45) NULL AFTER `end_date`,
ADD COLUMN `progress_percent` TINYINT NULL AFTER `progress_state`;
