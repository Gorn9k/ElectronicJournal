CREATE TABLE "teacher"
(
    "teacher_id"     BIGSERIAL PRIMARY KEY,
    "surname"        varchar,
    "name"           varchar,
    "patronymic"     varchar,
    "id_from_source" bigint,
    "created"        date,
    "updated"        date,
    "status"         varchar
);

CREATE TABLE "discipline"
(
    "discipline_id"  BIGSERIAL PRIMARY KEY,
    "short_name"     varchar,
    "id_from_source" bigint,
    "group_id"       bigint,
    "created"        date,
    "updated"        date,
    "status"         varchar
);

CREATE TABLE "student_group"
(
    "group_id"       BIGSERIAL PRIMARY KEY,
    "name"           varchar,
    "id_from_source" bigint,
    "created"        date,
    "updated"        date,
    "status"         varchar
);

CREATE TABLE "student"
(
    "student_id"     BIGSERIAL PRIMARY KEY,
    "group_id"       bigint,
    "surname"        varchar,
    "name"           varchar,
    "patronymic"     varchar,
    "sub_group"      integer,
    "id_from_source" bigint,
    "created"        date,
    "updated"        date,
    "status"         varchar
);

CREATE TABLE "type_class"
(
    "type_class_id"  BIGSERIAL PRIMARY KEY,
    "name"           varchar,
    "id_from_source" bigint,
    "created"        date,
    "updated"        date,
    "status"         varchar
);

CREATE TABLE "journal_header"
(
    "journal_header_id"      BIGSERIAL PRIMARY KEY,
    "type_class_id"          bigint,
    "journal_site_id"        bigint,
    "sub_group"              integer,
    "class_topic"            varchar,
    "replacement_teacher_id" bigint,
    "discription"            varchar,
    "date_of_lesson"         date,
    "hours_count"            integer,
    "created"                date,
    "updated"                date,
    "status"                 varchar
);

CREATE TABLE "journal_site"
(
    "journal_site_id" BIGSERIAL PRIMARY KEY,
    "discipline_id"   bigint,
    "teacher_id"      bigint,
    "group_id"        bigint,
    "stream_id"       bigint,
    "created"         date,
    "updated"         date,
    "status"          varchar
);

CREATE TABLE "journal_content"
(
    "journal_content_id" BIGSERIAL PRIMARY KEY,
    "journal_header_id"  bigint,
    "student_id"         bigint,
    "presence"           boolean,
    "grade"              integer,
    "discription"        varchar,
    "created"            date,
    "updated"            date,
    "status"             varchar
);

CREATE TABLE "discipline_classes"
(
    "discipline_id" BIGSERIAL,
    "type_class_id" bigint,
    "teacher_id"    bigint,
    "group_id"      bigint,
    "stream_id"     bigint,
    "hours_count"   integer,
    "created"       date,
    "updated"       date,
    "status"        varchar,
    PRIMARY KEY ("discipline_id", "type_class_id", "teacher_id", "group_id")
);

CREATE TABLE "discipline_content"
(
    "discipline_id" BIGSERIAL,
    "type_class_id" bigint,
    "class_topic"   varchar,
    "hours_count"   integer,
    "created"       date,
    "updated"       date,
    "status"        varchar,
    PRIMARY KEY ("discipline_id", "type_class_id")
);

ALTER TABLE "journal_header"
    ADD FOREIGN KEY ("replacement_teacher_id") REFERENCES "teacher" ("teacher_id");

ALTER TABLE "journal_header"
    ADD FOREIGN KEY ("type_class_id") REFERENCES "type_class" ("type_class_id");

ALTER TABLE "journal_header"
    ADD FOREIGN KEY ("journal_site_id") REFERENCES "journal_site" ("journal_site_id");

ALTER TABLE "journal_site"
    ADD FOREIGN KEY ("group_id") REFERENCES "student_group" ("group_id");

ALTER TABLE "journal_site"
    ADD FOREIGN KEY ("teacher_id") REFERENCES "teacher" ("teacher_id");

ALTER TABLE "journal_site"
    ADD FOREIGN KEY ("discipline_id") REFERENCES "discipline" ("discipline_id");

ALTER TABLE "journal_content"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("student_id");

ALTER TABLE "journal_content"
    ADD FOREIGN KEY ("journal_header_id") REFERENCES "journal_header" ("journal_header_id");

ALTER TABLE "discipline_classes"
    ADD FOREIGN KEY ("group_id") REFERENCES "student_group" ("group_id");

ALTER TABLE "discipline_classes"
    ADD FOREIGN KEY ("type_class_id") REFERENCES "type_class" ("type_class_id");

ALTER TABLE "discipline_classes"
    ADD FOREIGN KEY ("teacher_id") REFERENCES "teacher" ("teacher_id");

ALTER TABLE "discipline_classes"
    ADD FOREIGN KEY ("discipline_id") REFERENCES "discipline" ("discipline_id");

ALTER TABLE "discipline_content"
    ADD FOREIGN KEY ("type_class_id") REFERENCES "type_class" ("type_class_id");

ALTER TABLE "discipline_content"
    ADD FOREIGN KEY ("discipline_id") REFERENCES "discipline" ("discipline_id");

ALTER TABLE "student"
    ADD FOREIGN KEY ("group_id") REFERENCES "student_group" ("group_id");

