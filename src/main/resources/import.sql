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
