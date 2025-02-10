alter table if exists joined_subject
    add column if not exists cycle varchar(255);

update joined_subject js
set cycle = sd.cycle
from subject_details sd
where js.main_subject_id = sd.id;