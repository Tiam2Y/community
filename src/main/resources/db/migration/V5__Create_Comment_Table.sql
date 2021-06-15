create table comment
(
	id Bigint auto_increment,
	parent_id BIGINT not null,
	type int not null,
	commentator BIGINT not null,
	gmt_create BIGINT not null,
	gmt_modified BIGINT not null,
	like_count bigint default 0,
	constraint COMMENT_PK
		primary key (id)
);