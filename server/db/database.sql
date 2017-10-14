create table Users (
    UserName varchar(255) not null primary key,
    Password varchar(255) not null,
    Email varchar(255) not null,
    FirstName varchar(255) not null,
    LastName varchar(255) not null,
    Gender char(1) not null,
    PersonID varchar(255)
);

create table Persons (
    PersonID varchar(255) not null,
    Descendant varchar(255) not null,
    FirstName varchar(255) not null,
    LastName varchar(255) not null,
    Gender char(1) not null,
    Father varchar(255),
    Mother varchar(255),
    Spouse varchar(255)
);

create table Events (
    EventID varchar(255) not null,
    Descendant varchar(255) not null,
    PersonID varchar(255) not null,
    Latitude decimal(7, 4) not null,
    Longitude decimal(7, 4) not null,
    Country varchar(255) not null,
    City varchar(255) not null,
    EventType varchar(255) not null,
    EventYear smallint(4) not null
);

create table AuthTokens (
    AuthToken varchar(255) not null primary key,
    UserName varchar(255) not null,
    DateCreated bigint not null
);