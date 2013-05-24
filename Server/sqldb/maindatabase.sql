PRAGMA foreign_keys = ON;

create table users (
    id integer primary key not null,
    realname varchar not null,
    email varchar unique not null,
    password varchar not null
);

create table walks (
    id integer primary key not null,
    name varchar not null,
    date integer not null,
    distance real not null,
    time integer not null,
    elevation real not null,
    avgspeed real not null
);

create table userswalks (
    userid integer references users(id),
    walkid integer references walks(id),
    primary key(userid, walkid)
);

create table waypoints (
    latitude real not null,
    longitude real not null,
    altitude real not null,
    speed real not null,
    walkid integer references walks(id)
);