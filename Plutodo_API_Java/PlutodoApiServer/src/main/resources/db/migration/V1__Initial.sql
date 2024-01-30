CREATE EXTENSION pgcrypto;

-- Tables

CREATE TABLE public.user_account
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(30) NOT NULL UNIQUE,
    email_address VARCHAR(50) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    date_created TIMESTAMP NOT NULL DEFAULT NOW(),
    date_last_login TIMESTAMP NULL
);

CREATE TABLE public.category
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(30) NOT NULL,
    color varchar(7) NULL,
    date_created TIMESTAMP NOT NULL DEFAULT NOW(),
    user_account_id uuid NOT NULL,
    FOREIGN KEY (user_account_id) REFERENCES "user_account"(id),
    UNIQUE(user_account_id, name)
);

CREATE TABLE public.task_item
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(30) NOT NULL,
    description VARCHAR(8192),
    is_finished BOOLEAN,
    date_created TIMESTAMP NOT NULL DEFAULT NOW(),
    date_due TIMESTAMP NULL,
    category_id uuid NOT NULL,
    FOREIGN KEY (category_id) REFERENCES "category"(id)
);

CREATE TABLE public.calendar_event
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(30) NOT NULL,
    description VARCHAR(8192),
    date_created TIMESTAMP NOT NULL DEFAULT NOW(),
    date_from TIMESTAMP NOT NULL,
    date_to TIMESTAMP NULL,
    is_whole_day BOOLEAN NOT NULL,
    category_id uuid NOT NULL,
    FOREIGN KEY (category_id) REFERENCES "category"(id)
);

CREATE TABLE public.deleted_items
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    table_name TEXT NOT NULL,
    content TEXT NOT NULL,
    date_created TIMESTAMP NULL,
    date_deleted TIMESTAMP NOT NULL DEFAULT NOW()
);

------- User authentication
CREATE PROCEDURE create_user(
    p_email TEXT,
    p_username TEXT,
    p_password TEXT
)
    LANGUAGE PLPGSQL
AS
$$
BEGIN
INSERT INTO public."user_account"(username, email_address, "password")
VALUES(p_username, p_email, crypt(p_password, gen_salt('bf')));
END; $$;

CREATE FUNCTION authenticate_user_email(p_email TEXT, p_password TEXT)
    RETURNS uuid
    LANGUAGE PLPGSQL
AS
$$
DECLARE uuid_result uuid;
BEGIN
SELECT Id
INTO uuid_result
FROM public."user_account"
WHERE email_address = p_email AND crypt(p_password, "password");

RETURN uuid_result;
END; $$;

CREATE FUNCTION authenticate_user_name(p_name TEXT, p_password TEXT)
    RETURNS uuid
    LANGUAGE PLPGSQL
AS
$$
DECLARE uuid_result uuid;
BEGIN
SELECT Id
INTO uuid_result
FROM public."user_account"
WHERE name = p_name AND crypt(p_password, "password");

RETURN uuid_result;
END; $$;


------------------
------ Item deletion
------------------

-- Calendar Event
CREATE FUNCTION log_deleted_calendar_event()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS
$$
BEGIN
INSERT INTO deleted_items(name, table_name, "content", date_created)
VALUES (OLD.name, 'calendar_event', (SELECT row_to_json(OLD) FROM OLD), OLD.date_created);
RETURN OLD;
END; $$;

CREATE TRIGGER calendar_event_delete
    AFTER DELETE
    ON calendar_event
    FOR EACH ROW
    EXECUTE FUNCTION log_deleted_calendar_event();

-- Task Item
CREATE FUNCTION log_deleted_task_item()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS
$$
BEGIN
INSERT INTO deleted_items(name, table_name, "content", date_created)
VALUES (OLD.name, 'task_item', (SELECT row_to_json(OLD) FROM OLD), OLD.date_created);
RETURN OLD;
END; $$;

CREATE TRIGGER task_item_delete
    AFTER DELETE
    ON task_item
    FOR EACH ROW
    EXECUTE FUNCTION log_deleted_task_item();

-- Category
CREATE FUNCTION log_deleted_category()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS
$$
BEGIN
DELETE FROM calendar_event
WHERE category_id = OLD.Id;

DELETE FROM task_item
WHERE category_id = OLD.Id;

INSERT INTO deleted_items(name, table_name, "content", date_created)
VALUES (OLD.name, 'category', (SELECT row_to_json(OLD) FROM OLD), OLD.date_created);
RETURN OLD;
END; $$;

CREATE TRIGGER category_delete
    AFTER DELETE
    ON category
    FOR EACH ROW
    EXECUTE FUNCTION log_deleted_category();

-- UserAccount
CREATE FUNCTION log_deleted_user_account()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS
$$
BEGIN
DELETE FROM category
WHERE owner_id = OLD.Id;

INSERT INTO deleted_items(name, table_name, "content", date_created)
VALUES (OLD.name, 'user_account', (SELECT row_to_json(OLD) FROM OLD), OLD.date_created);
RETURN OLD;
END; $$;

CREATE TRIGGER user_account_delete
    AFTER DELETE
    ON user_account
    FOR EACH ROW
    EXECUTE FUNCTION log_deleted_user_account();