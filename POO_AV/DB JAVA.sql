create table student (
	first_name varchar2(10),
	last_name varchar2(10),
	cin number(8),
	primary key (cin),
	email varchar2(30));

create table professor (
	first_name varchar2(10),
	last_name varchar2(10),
	cin number(8),
	primary key (cin),
	email varchar2(30));

create table admin (
	first_name varchar2(10),
	last_name varchar2(10),
	cin number(8),
	primary key (cin),
	email varchar2(30));

create table project (
	subject varchar2(20),
	enterprise varchar2(20),
	student1 number(8) not null,
	student2 number(8),
	supervisor number(8),
	reporter number(8),
	status varchar2(30),
	primary key (subject,enterprise),
	unique (student1,student2),
	foreign key (student1) references student(cin),
	foreign key (student2) references student(cin),
	foreign key (supervisor) references professor(cin),
	foreign key (reporter) references professor(cin),
	check (student1!=student2 and supervisor!=reporter));

insert into student values ('JIHEN','ARIDHI',12345678,'jihenaridhi@gmail.com');
insert into student values ('ROUDEINA','HAMDI',14785236,'roudeinahamdi@gmail.com');
insert into student values ('ONS','BOUAZIZI',14523698,'onsbouazizi@gmail.com');

insert into professor values ('FOULEN','BEN FOULEN',09876543,'foulenbenfoulen@gmail.com');
insert into professor values ('FALTEN','BEN FALTEN',09514753,'faltenbenfalten@gmail.com');

insert into admin values ('ZAYD','BEN AAMR',09632145,'zaydbenaamr@gmail.com');
