--liquibase formatted sql
--changeset kjakub:2
alter table product add image varchar(128) after currency;