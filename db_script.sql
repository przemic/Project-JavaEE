SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `java` ;
CREATE SCHEMA IF NOT EXISTS `java` DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci ;
USE `java` ;

-- -----------------------------------------------------
-- Table `java`.`Place`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `java`.`Place` ;

CREATE  TABLE IF NOT EXISTS `java`.`Place` (
  `id` INT NOT NULL ,
  `name` TEXT NULL ,
  `longitude` TEXT NULL ,
  `latitude` TEXT NULL ,
  `street_name` TEXT NULL ,
  `building_number` INT NULL ,
  `flat_number` INT NULL ,
  `city` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `java`.`Event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `java`.`Event` ;

CREATE  TABLE IF NOT EXISTS `java`.`Event` (
  `id` INT NOT NULL ,
  `name` TEXT NULL ,
  `date` DATETIME NULL ,
  `description_text` TEXT NULL ,
  `Place_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Event_Place` (`Place_id` ASC) ,
  CONSTRAINT `fk_Event_Place`
    FOREIGN KEY (`Place_id` )
    REFERENCES `java`.`Place` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `java`.`Group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `java`.`Group` ;

CREATE  TABLE IF NOT EXISTS `java`.`Group` (
  `id` INT NOT NULL ,
  `name` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `java`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `java`.`User` ;

CREATE  TABLE IF NOT EXISTS `java`.`User` (
  `id` INT NOT NULL ,
  `name` TEXT NULL ,
  `surname` TEXT NULL ,
  `birth_date` DATE NULL ,
  `password` TEXT NULL ,
  `login` TEXT NULL ,
  `Group_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_User_Group` (`Group_id` ASC) ,
  CONSTRAINT `fk_User_Group`
    FOREIGN KEY (`Group_id` )
    REFERENCES `java`.`Group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `java`.`Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `java`.`Comment` ;

CREATE  TABLE IF NOT EXISTS `java`.`Comment` (
  `id` INT NOT NULL ,
  `description_text` TEXT NULL ,
  `Event_id` INT NOT NULL ,
  `User_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Comment_Event` (`Event_id` ASC) ,
  INDEX `fk_Comment_User` (`User_id` ASC) ,
  CONSTRAINT `fk_Comment_Event`
    FOREIGN KEY (`Event_id` )
    REFERENCES `java`.`Event` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comment_User`
    FOREIGN KEY (`User_id` )
    REFERENCES `java`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `java`.`Event_To_User_Asoc`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `java`.`Event_To_User_Asoc` ;

CREATE  TABLE IF NOT EXISTS `java`.`Event_To_User_Asoc` (
  `attendance` INT NULL ,
  `id` INT NOT NULL ,
  `Event_id` INT NOT NULL ,
  `User_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Event_To_User_Asoc_Event` (`Event_id` ASC) ,
  INDEX `fk_Event_To_User_Asoc_User` (`User_id` ASC) ,
  CONSTRAINT `fk_Event_To_User_Asoc_Event`
    FOREIGN KEY (`Event_id` )
    REFERENCES `java`.`Event` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Event_To_User_Asoc_User`
    FOREIGN KEY (`User_id` )
    REFERENCES `java`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
