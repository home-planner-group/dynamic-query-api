drop table TA;
drop table Takes;
drop table Class;
drop table Professor;
drop table Student;


create table Professor
(
    pName  varchar(15) not null primary key,
    salary int         null
);

create table Class
(
    classNr varchar(9) primary key,
    room    int,
    day     varchar(10),
    pName   varchar(15),
    foreign key (pName) references Professor (pName)
        on delete set null on update cascade
);

create table Student
(
    matNr int default 0 not null primary key,
    sName varchar(30)   not null
);

create table TA
(
    matNr    int        not null,
    classNr  varchar(9) not null,
    hours    smallint,
    taSalary int,
    primary key (matNr, classNr),
    foreign key (matNr) references Student (matNr)
        on update cascade on delete cascade,
    foreign key (classNr) references Class (classNr)
        on update cascade on delete cascade
);

create table Takes
(
    matNr   int           not null default 0,
    classNr varchar(9)    not null,
    grade   decimal(2, 1) null,
    primary key (matNr, classNr),
    foreign key (matNr) references Student (matNr)
        on update cascade on delete cascade,
    foreign key (classNr) references Class (classNr)
        on update cascade on delete restrict
);

insert into Professor
values ('Langes', 2000),
       ('Mueller', 3000),
       ('Wagner', 2000),
       ('Schmidt', 1000);

insert into Student
values (1234, 'Schmidt'),
       (2345, 'Schmidt'),
       (1000, 'Reinhard'),
       (4000, 'Wagner');

insert into Class
values ('DTB-SS93', 212, 'Monday', 'Langes'),
       ('BSY-SS93', 114, 'Monday', 'Mueller'),
       ('LIA-WS92', 114, 'Friday', 'Wagner'),
       ('STP-WS92', 212, 'Tuesday', 'Mueller');

insert into TA
values (1000, 'LIA-WS92', 40, 1000),
       (1234, 'LIA-WS92', 35, 1000),
       (2345, 'DTB-SS93', 80, 2000),
       (1000, 'STP-WS92', 40, 1000);

insert into Takes
values (1234, 'STP-WS92', 1.0),
       (1234, 'LIA-WS92', 2.0),
       (2345, 'STP-WS92', 3.0),
       (2345, 'LIA-WS92', 4.0),
       (1000, 'STP-WS92', 3.0),
       (1000, 'LIA-WS92', 2.0),
       (4000, 'STP-WS92', 3.0),
       (4000, 'LIA-WS92', 1.0),
       (1000, 'DTB-SS93', NULL),
       (1000, 'BSY-SS93', NULL),
       (1234, 'DTB-SS93', NULL);
