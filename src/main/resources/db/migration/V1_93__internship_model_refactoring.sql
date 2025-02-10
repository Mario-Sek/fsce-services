ALTER TABLE internship
DROP COLUMN description;

ALTER TABLE internship_week
DROP COLUMN description;

ALTER TABLE internship_week
DROP COLUMN company_comment;

ALTER TABLE internship_week
DROP COLUMN coordinator_comment;

ALTER TABLE internship
ADD COLUMN description TEXT;

ALTER TABLE internship_week
ADD COLUMN description TEXT;

ALTER TABLE internship_week
ADD COLUMN company_comment TEXT;

ALTER TABLE internship_week
ADD COLUMN coordinator_comment TEXT;

