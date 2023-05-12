--liquibase formatted sql
--changeset kjakub:25
alter table product add sale_price decimal(9, 2);