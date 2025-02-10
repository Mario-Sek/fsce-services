alter table if exists company
    add column if not exists password varchar(255),
    add column if not exists token    varchar(255),
    add column if not exists valid_to timestamp;


alter table if exists website_posting
    add column if not exists status varchar(255);