CREATE TABLE `project` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(256) NOT NULL,
    `end_state` VARCHAR(45) NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uq_name` (`name` ASC));
