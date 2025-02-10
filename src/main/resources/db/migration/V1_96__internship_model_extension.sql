alter table internship_week
rename column number_of_hours to working_hours;

alter table internship
    add column if not exists weekly_hours integer,
    add column if not exists foreign_company boolean default false,
    add column if not exists company_token varchar(255),
    add column if not exists company_token_expiration timestamp with time zone;


