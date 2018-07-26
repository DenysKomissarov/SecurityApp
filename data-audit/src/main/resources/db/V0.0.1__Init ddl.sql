CREATE TABLE if not exists comp_user
(
 id                 UUID primary key default gen_random_uuid(),
 created_time        TIMESTAMP without time zone not null ,
 updated_time       TIMESTAMP without time zone not null ,
 name               VARCHAR (255),
 password           VARCHAR (255),
 email              VARCHAR (255) unique,
 username           VARCHAR (255),
 roles              VARCHAR (355)
);

CREATE table if not exists files(
  id                 UUID primary key default gen_random_uuid(),
  created_time       TIMESTAMP without time zone not null ,
  updated_time       TIMESTAMP without time zone not null ,
  owner              UUID references comp_user(id),
  file_name          VARCHAR (255)

);