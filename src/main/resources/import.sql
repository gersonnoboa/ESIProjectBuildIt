create table if not exists users (username varchar(50) not null primary key, password varchar(50) not null, enabled boolean not null)
create table if not exists authorities (username varchar(50) not null, authority varchar(50) not null, constraint FK_AUTHORITIES_USERS foreign key(username) references users(username))

insert into users (username, password, enabled) values ('admin', 'admin', true);
insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN');
insert into users (username, password, enabled) values ('siteengr', 'siteengr', true);
insert into users (username, password, enabled) values ('worksengr', 'worksengr', true);
insert into users (username, password, enabled) values ('supervisor', 'supervisor', true);
insert into authorities (username, authority) values ('siteengr', 'ROLE_SITE_ENGINEER');
insert into authorities (username, authority) values ('worksengr', 'ROLE_WORKS_ENGINEER');
insert into authorities (username, authority) values ('supervisor', 'ROLE_SITE_ENGINEER');
insert into authorities (username, authority) values ('supervisor', 'ROLE_WORKS_ENGINEER');

INSERT INTO EMPLOYEE VALUES('1', 'Luciano', 'Site Engineer')
INSERT INTO PLANT_HIRE_REQUEST VALUES('1', '', '', 'PENDING', 100, 'Excavator', '', 100, '2018-04-30', '2018-03-30', '', '1', 'OPEN', '', '1')
