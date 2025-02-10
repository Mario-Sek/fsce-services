DO
$$
    BEGIN
        update study_program_details set coordinator_id='dejan.gjorgjevikj' where id = 'SI23_1';
        update study_program_details set coordinator_id='bojan.ilijoski' where id = 'SAP23_1';
        update study_program_details set coordinator_id='ljupcho.antovski' where id = 'ITM23_1';
        update study_program_details set coordinator_id='vesna.dimitrova' where id = 'BKK23_2';
        update study_program_details set coordinator_id='slobodan.kalajdziski' where id = 'BI23_2';
        update study_program_details set coordinator_id='magdalena.kostoska' where id = 'CC23_2';
        update study_program_details set coordinator_id='bojan.ilijoski' where id = 'SAP23_2';
        update study_program_details set coordinator_id='ljupcho.antovski' where id = 'ITM23_2';
        update study_program_details set coordinator_id='dejan.gjorgjevikj' where id = 'SI23_2';
        update study_program_details set coordinator_id='riste.stojanov' where id = 'DS23_1';
        update study_program_details set coordinator_id='vesna.dimitrova' where id = 'BKK23_1';
        update study_program_details set coordinator_id='slobodan.kalajdziski' where id = 'BI23_1';
        update study_program_details set coordinator_id='ana.madevska' where id = 'EDU23_1';
        update study_program_details set coordinator_id='kosta.mitreski' where id = 'EI23_1';
        update study_program_details set coordinator_id='ana.madevska' where id = 'IS23_1';
        update study_program_details set coordinator_id='sonja.filiposka' where id = 'ITCS23_1';
        update study_program_details set coordinator_id='vesna.dimitrievska' where id = 'KN23_1';
        update study_program_details set coordinator_id='magdalena.kostoska' where id = 'CC23_1';
        update study_program_details set coordinator_id='petre.lameski' where id = 'SVS23_1';
    EXCEPTION
        WHEN undefined_column THEN raise notice 'existing datga';
    END;
$$;


update study_program_subject
set mandatory= true, "order"=3
where id = 'KN23_1-SDP-I-13';
update study_program_subject
set mandatory= false, "order" = 100
where id = 'KN23_1-KN-Z-03';

update study_program_subject
set subject_group='факултет-з'
where id in ('KN23_1-студиска програма-з-9', 'KN23_1-факултет-з-9');


update study_program_subject
set subject_group='факултет-л'
where id in ('KN23_1-факултет-л-10');


update subject_details
set dependencies=null
where dependencies='NULL';