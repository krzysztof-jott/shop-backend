--liquibase formatted sql
--changeset kjakub:9
alter table review add moderated boolean default false;