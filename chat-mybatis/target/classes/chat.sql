

create table if not exists `users` (
	`id` varchar(64) not null comment '唯一id',
	`username` varchar(20) not null comment '用户名',
	`password` varchar(64) not null comment '加密后的密码',
	`face_image` varchar(255) not null comment '用户头像，小头像',
	`face_image_big` varchar(255) not null comment '用户头像，大头像',
	`nickname` varchar(20) not null comment '昵称',
	`qrcode` varchar(255) not null comment '二维码',
	`cid` varchar(64) comment '设备id',
	primary key (`id`)
) engine = innodb default charset = utf8 comment = '用户表';

create table if not exists `friends_request` (
	`id` varchar(64) not null comment '唯一id',
	`send_user_id` varchar(64) not null comment '发送用户的id',
	`accept_user_id` varchar(64) not null comment '接收用户的id',
	`request_date_time` datetime not null comment '发送请求的时间'
) engine = innodb default charset = utf8 comment '用户请求表';

create table if not exists `my_friends` (
	`id` varchar(64) not null comment '唯一id',
	`my_user_id` varchar(64) not null comment '用户的id',
	`my_friend_user_id` varchar(64) not null comment '用户B的id',
	primary key (`id`)
) engine = innodb default charset = utf8 comment '朋友表';

create table if not exists `chat_msg` (
	`id` varchar(64) not null comment '唯一id',
	`send_user_id` varchar(64) not null comment '发送用户的id',
	`accept_user_id` varchar(64) not null comment '接收用户的id',
	`msg` varchar(255) not null comment '消息',
	`sign_flag` int(1) not null comment '消息状态',
	`create_time` datetime not null comment '创建时间',
	primary key (`id`)
) engine = innodb default charset = utf8 comment '聊天记录表';
