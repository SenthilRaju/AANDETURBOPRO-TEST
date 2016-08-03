-- Script to change all "Rejected" jobs to "Booked" jobs
-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
UPDATE joMaster SET ChangedOn=CURDATE(), JobStatus = 3 WHERE jobStatus = -2;
-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
