--liquibase formatted sql
--changeset kjakub:3
alter table product add slug varchar(255) after image;
alter table product add constraint ui_product_slug unique key(slug);