--liquibase formatted sql

--changeset application:1 dbms:postgresql splitStatements
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:f SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'cinema_hall');
CREATE TABLE IF NOT EXISTS cinema_hall (
    id              integer PRIMARY KEY,
    name            VARCHAR(25) NOT NULL
);


--changeset application:2 dbms:postgresql splitStatements:false
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.sequences where sequence_name='hall_id_sequence' and sequence_schema='public';
CREATE SEQUENCE hall_id_sequence;

insert into cinema_hall
values
       (nextval('hall_id_sequence'), 'first_hall'),
       (nextval('hall_id_sequence'), 'second_hall'),
       (nextval('hall_id_sequence'), 'third_hall');

