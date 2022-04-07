drop table ancestors;

create table ancestors
(
    pNr        varchar(15)  not null
        primary key,
    personName varchar(255) null,
    gender     varchar(10)  null,
    birthYear  int          null,
    birthMonth int          null,
    motherNr   varchar(15)  null,
    fatherNr   varchar(15)  null
);


insert into ancestors
values (1, 'Marie', 'f', 1867, 11, null, null),
       (2, 'Pierre', 'm', 1859, 5, null, null),
       (3, 'Frederic', 'm', 1900, 3, 1, 2),
       (4, 'Irene', 'f', 1897, 9, 1, 2),
       (5, 'Helene', 'f', 1927, 9, 4, 3),
       (6, 'Eve', 'f', 1904, 12, 1, 2),
       (7, 'Henry', 'm', 1904, 2, 1, 2),
       (8, 'Anne', 'f', 1937, 7, 6, 7);
