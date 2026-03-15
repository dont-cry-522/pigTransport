create table disinfection_record
(
    disinfection_id   int auto_increment comment '消毒记录ID'
        primary key,
    task_id           int                                                         null comment '任务ID',
    driver_id         int                                                         null comment '司机ID',
    car_id            int                                                         null comment '车辆ID',
    disinfection_type varchar(50)                                                 null comment '消毒类型（整车/车厢/轮胎/场地等）',
    location          varchar(255)                                                null comment '消毒位置',
    disinfectant      varchar(100)                                                null comment '消毒剂名称',
    disinfection_time datetime                                                    null comment '消毒时间',
    operator          varchar(50)                                                 null comment '操作人员',
    remark            text                                                        null comment '备注信息',
    result            enum ('qualified', 'unqualified') default 'qualified'       null comment '消毒结果（合格/不合格）',
    photo_url         varchar(255)                                                null comment '消毒照片URL',
    create_time       datetime                          default CURRENT_TIMESTAMP null comment '创建时间',
    deleted           tinyint                           default 0                 null comment '逻辑删除标志（0-正常 1-删除）'
)
    comment '车辆消毒记录表' engine = InnoDB
                             collate = utf8mb4_general_ci;

create index idx_car_id
    on disinfection_record (car_id);

create index idx_create_time
    on disinfection_record (create_time);

create index idx_disinfection_time
    on disinfection_record (disinfection_time);

create index idx_driver_id
    on disinfection_record (driver_id);

create index idx_result
    on disinfection_record (result);

create index idx_task_id
    on disinfection_record (task_id);




create table location_sign
(
    sign_id   int auto_increment
        primary key,
    task_id   int                                not null comment '任务ID',
    driver_id int                                not null comment '司机ID',
    location  varchar(200)                       not null comment '签到位置',
    sign_time datetime default CURRENT_TIMESTAMP null comment '签到时间',
    remark    varchar(255)                       null comment '备注信息',
    deleted   tinyint  default 0                 null comment '逻辑删除标记',
    latitude  decimal(10, 8)                     null comment '纬度',
    longitude decimal(11, 8)                     null comment '经度'
);

create index idx_driver
    on location_sign (driver_id);

create index idx_task
    on location_sign (task_id);


create table pig_batch
(
    batch_id    int auto_increment
        primary key,
    breeder_id  int                                    not null comment '养殖户ID',
    variety     varchar(50)                            not null comment '品种',
    source      varchar(100) default '未指定来源'      null,
    quantity    int                                    not null comment '数量',
    create_time datetime     default CURRENT_TIMESTAMP null,
    deleted     tinyint      default 0                 null comment '逻辑删除标记',
    status      varchar(50)                            null comment '状态',
    batch_no    varchar(50)                            null comment '批次编号',
    remark      text                                   null comment '备注'
);

create index idx_breeder
    on pig_batch (breeder_id);



create table sys_user
(
    user_id     int auto_increment
        primary key,
    username    varchar(50)                           not null,
    real_name   varchar(50)                           null comment '真实姓名',
    password    varchar(100)                          not null,
    role        enum ('farmer', 'admin', 'driver')    not null comment '养殖户/物流管理员/司机',
    status      varchar(10) default '1'               null comment '账号状态：1启用，0禁用',
    remark      varchar(500)                          null comment '备注信息',
    phone       varchar(20)                           null,
    create_time datetime    default CURRENT_TIMESTAMP null,
    deleted     tinyint     default 0                 null comment '逻辑删除：0-未删除，1-已删除',
    constraint username
        unique (username)
);

create index idx_role
    on sys_user (role);

create index idx_username
    on sys_user (username);



create table transport_task
(
    task_id     int auto_increment
        primary key,
    batch_id    int                                                                     not null comment '生猪批次ID',
    vehicle_id  int                                                                     not null comment '车辆ID',
    start_place varchar(255)                                                            null,
    end_place   varchar(255)                                                            null,
    status      enum ('pending', 'transporting', 'completed') default 'pending'         null comment '待分配/运输中/已完成',
    assign_time datetime                                                                null comment '分配时间',
    finish_time datetime                                                                null comment '完成时间',
    create_time datetime                                      default CURRENT_TIMESTAMP null,
    deleted     tinyint                                       default 0                 null comment '逻辑删除标记',
    task_code   varchar(50)                                                             null comment '任务编号',
    driver_id   int                                                                     null comment '司机ID',
    remark      text                                                                    null comment '备注'
);

create index idx_batch
    on transport_task (batch_id);

create index idx_status
    on transport_task (status);

create index idx_vehicle
    on transport_task (vehicle_id);



create table vehicle
(
    vehicle_id    int auto_increment
        primary key,
    license_plate varchar(20)                                                            not null comment '车牌号',
    model         varchar(50)                                                            null comment '车型',
    driver_id     int                                                                    null comment '当前司机ID',
    status        enum ('free', 'transporting', 'maintenance') default 'free'            null comment '空闲/运输中/维修',
    create_time   datetime                                     default CURRENT_TIMESTAMP null,
    deleted       tinyint(1)                                   default 0                 null comment '逻辑删除标记：0未删除，1已删除',
    constraint license_plate
        unique (license_plate)
);

create index idx_driver
    on vehicle (driver_id);

create index idx_status
    on vehicle (status);



