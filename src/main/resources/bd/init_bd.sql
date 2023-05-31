drop table if exists priority cascade;
drop table if exists stat cascade;
drop table if exists category cascade;
drop table if exists task cascade;

set time zone 'UTC';

create table priority
(
    id    bigserial primary key,
    title varchar(45) not null unique ,
    color varchar(45) not null
);

create table stat
(
    id                bigserial primary key,
    completed_total   bigint default (0) check ( completed_total >= 0 ),
    uncompleted_total bigint default (0) check ( uncompleted_total >= 0 )
);

create table category
(
    id                bigserial primary key,
    title             varchar(45) not null unique,
    completed_count   bigint default (0) check ( completed_count >= 0 ),
    uncompleted_count bigint default (0) check ( uncompleted_count >= 0 )
);

create table task
(
    id          bigserial primary key,
    title       varchar(100) not null unique ,
    completed   int default (0),
    date        date,
    priority_id bigint          references priority (id) on DELETE set null,
    category_id bigint          references category (id) on DELETE set null
);

create index idx_task_completed on task (completed);

create or replace function on_task_insert() returns trigger as
$task_after_insert$
declare
    new_cat_id int;
    new_compl  int;
begin

    select coalesce(c.id, 0)
    from category c
    where c.id = NEW.category_id
    into new_cat_id;

    select coalesce(NEW.completed, 0) into new_compl;

    if (new_cat_id > 0 and new_compl = 1) then
        update category
        set completed_count = ((select coalesce(c.completed_count, 0)
                                from category c
                                where c.id = new_cat_id) + 1)
        where id = new_cat_id;
    end if;

    if (new_cat_id > 0 and new_compl = 1) then
        update category
        set uncompleted_count = ((select coalesce(c.uncompleted_count, 0)
                                  from category c
                                  where c.id = new_cat_id) + 1)
        where id = new_cat_id;
    end if;

    if new_compl = 1 then
        update stat
        set completed_total = ((select coalesce(s.completed_total, 0)
                                from stat s) + 1);
    else
        update stat
        set uncompleted_total = ((select coalesce(s.uncompleted_total, 0)
                                  from stat s) + 1);
    end if;
    return NEW;
end
$task_after_insert$
    LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER task_after_insert
    AFTER INSERT
    ON task
    FOR EACH ROW
execute function on_task_insert();

create or replace function on_task_update() returns trigger as
$task_after_update$
declare
    new_cat_id int;
    old_cat_id int;
    new_compl  smallint;
    old_compl  smallint;
begin

    select coalesce(NEW.category_id, 0) into new_cat_id;
    select coalesce(OLD.category_id, 0) into old_cat_id;
    select coalesce(NEW.completed, 0) into new_compl;
    select coalesce(OLD.completed, 0) into old_compl;

    if ((old_compl <> new_compl) and (old_cat_id = new_cat_id)) then
        if (new_compl = 1) then
            update category
            set uncompleted_count = (
                    (select coalesce(c.uncompleted_count, 0)
                     from category c
                     where c.id = old_cat_id) - 1),
                completed_count   = (
                        (select coalesce(c.completed_count, 0)
                         from category c
                         where c.id = old_cat_id) + 1)
            where id = old_cat_id;

            update stat
            set uncompleted_total = ((select coalesce(s.uncompleted_total, 0) from stat s) - 1),
                completed_total   = ((select coalesce(s.completed_total, 0) from stat s) + 1);
        else
            update category
            set uncompleted_count = (
                    (select coalesce(c.uncompleted_count, 0)
                     from category c
                     where c.id = old_cat_id) + 1),
                completed_count   = (
                        (select coalesce(c.completed_count, 0)
                         from category c
                         where c.id = old_cat_id) - 1)
            where id = old_cat_id;

            update stat
            set uncompleted_total = ((select coalesce(s.uncompleted_total, 0) from stat s) + 1),
                completed_total   = ((select coalesce(s.completed_total, 0) from stat s) - 1);
        end if;
    end if;

    if ((old_compl = new_compl) and (old_cat_id <> new_cat_id)) then
        if (new_compl = 1) then
            update category
            set completed_count = (
                    (select coalesce(c.completed_count, 0) from category c where c.id = old_cat_id) -
                    1)
            where id = old_cat_id;

            update category
            set completed_count = (
                    (select coalesce(c.completed_count, 0) from category c where c.id = new_cat_id) +
                    1)
            where id = new_cat_id;
        else
            update category
            set uncompleted_count = (
                    (select coalesce(c.uncompleted_count, 0) from category c where c.id = OLD.category_id) -
                    1)
            where id = OLD.category_id;

            update category
            set uncompleted_count = (
                    (select coalesce(c.uncompleted_count, 0) from category c where c.id = new_cat_id) +
                    1)
            where id = new_cat_id;
        end if;
    end if;

    if ((old_compl <> new_compl) and (old_cat_id <> new_cat_id)) then
        if (new_compl = 0) then
            update category
            set completed_count = (
                    (select coalesce(c.completed_count, 0) from category c where c.id = old_cat_id) -
                    1)
            where id = old_cat_id;

            update category
            set uncompleted_count = (
                    (select coalesce(c.uncompleted_count, 0) from category c where c.id = new_cat_id) + 1)
            where id = new_cat_id;

            update stat
            set uncompleted_total = ((select coalesce(s.uncompleted_total, 0) from stat s) + 1),
                completed_total   = ((select coalesce(s.completed_total, 0) from stat s) - 1);
        else
            update category
            set uncompleted_count = (
                    (select coalesce(c.uncompleted_count, 0) from category c where c.id = old_cat_id) - 1)
            where id = old_cat_id;

            update category
            set completed_count = (
                    (select coalesce(c.completed_count, 0) from category c where c.id = new_cat_id) +
                    1)
            where id = new_cat_id;

            update stat
            set uncompleted_total = ((select coalesce(s.uncompleted_total, 0) from stat s) - 1),
                completed_total   = ((select coalesce(s.completed_total, 0) from stat s) + 1)
            where id = 1;
        end if;
    end if;

    return NEW;
end
$task_after_update$
    LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER task_after_update
    AFTER UPDATE
    ON task
    FOR EACH ROW
execute function on_task_update();

create or replace function on_task_delete() returns trigger as
$task_after_delete$
declare
    old_cat_id int;
    old_compl  smallint;
begin
    select coalesce(OLD.category_id, 0) into old_cat_id;
    select coalesce(OLD.completed, 0) into old_compl;

    if ((old_cat_id > 0) and (old_compl = 1)) then
        update category
        set completed_count = ((select coalesce(c.completed_count, 0) from category c where c.id = old_cat_id) - 1)
        where id = old_cat_id;
    end if;

    if ((old_cat_id > 0) and (old_compl = 0)) then
        update category
        set uncompleted_count = (
                (select coalesce(c.uncompleted_count, 0) from category c where c.id = old_cat_id) - 1)
        where id = old_cat_id;
    end if;

    if (old_compl = 1) then
        update stat set completed_total = ((select coalesce(completed_total, 0)) - 1);
    else
        update stat set uncompleted_total = ((select coalesce(uncompleted_total, 0)) - 1);
    end if;

    return NEW;
end
$task_after_delete$
    LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER task_after_delete
    AFTER DELETE
    ON task
    FOR EACH ROW
execute function on_task_delete();
