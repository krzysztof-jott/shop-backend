--liquibase formatted sql
--changeset kjakub:23
alter table users add hash varchar(120);
--changeset kjakub:24
alter table users add hash_date datetime;