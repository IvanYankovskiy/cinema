--liquibase formatted sql

--changeset application:1 dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:f SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'seats');
create table if not exists seats (
    id          integer PRIMARY KEY,
    hall_id     integer not null,
    row         integer not null,
    seat        integer not null,
    state       varchar(1) not null default 'f',
    UNIQUE (hall_id, row, seat)
);

--changeset application:2 dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT count(*) from information_schema.table_constraints WHERE constraint_name = 'seats_fk_hall' AND table_name = 'seats';
ALTER TABLE seats
    ADD CONSTRAINT seats_fk_hall FOREIGN KEY (hall_id) REFERENCES cinema_hall (id);

--changeset application:3 dbms:postgresql splitStatements:false
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.sequences where sequence_name='seat_id_sequence' and sequence_schema='public';
CREATE SEQUENCE seat_id_sequence;


--changeset application:4 dbms:postgresql
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(indexname) FROM pg_indexes WHERE schemaname = 'public' AND tablename = 'seats' AND indexname = 'idx_srch_hall';
CREATE INDEX idx_srch_hall ON seats (hall_id);