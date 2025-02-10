alter table if exists master_thesis
    add constraint fk_master_thesis_location_room foreign key (location) references room (name);