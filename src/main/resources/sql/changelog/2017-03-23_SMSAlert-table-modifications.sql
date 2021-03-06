# Create new sms_alerts table

CREATE TABLE sms_alerts
(
  id 						      INTEGER 		        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  citation_number 		VARCHAR(25),
  court_date 				  DATETIME,
  phone_number			  VARCHAR(25),
  date_of_birth 			DATE     						NULL
)ENGINE=InnoDB;

# Add auto_increment to violations
ALTER TABLE violations MODIFY id INTEGER NOT NULL AUTO_INCREMENT;

# Add auto_increment to citations
ALTER TABLE citations MODIFY id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY;
