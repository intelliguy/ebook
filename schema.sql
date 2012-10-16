create table file (
	fid int primary key,
	directory varchar(200),
	filename varchar(100),
	length double,
	bid int
);

create table book (
	bid int primary key,	
	author varchar(20),
	nauthor int,
	title varchar(100),
	sequence int
);

create table author (
	bid int,
	aid int,
	name varchar(20)
);

select max(id) from textf;

insert into textf values(0,"d:/","test.txt",0,"",0,"",1);