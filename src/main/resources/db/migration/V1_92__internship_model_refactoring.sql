alter table internship
add column company_id varchar(255) references company(id);