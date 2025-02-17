

-- New table
CREATE TABLE public.nextcloud_user_info
(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL,
    password TEXT NOT NULL,
    server_url TEXT NOT NULL,
    date_created TIMESTAMP NOT NULL DEFAULT NOW(),
    date_last_login TIMESTAMP NULL,
    user_account_id uuid NOT NULL,
    FOREIGN KEY (user_account_id) REFERENCES "user_account"(id)
);

-- Add reference to 'user_account' table
ALTER TABLE public.user_account ADD COLUMN nextcloud_user_id uuid NULL;
ALTER TABLE public.user_account ADD FOREIGN KEY (nextcloud_user_id) REFERENCES "nextcloud_user_info"(id);

-- Nextcloud user info creation procedure
CREATE OR REPLACE PROCEDURE create_nextcloud_login(
    p_username TEXT,
    p_password TEXT,
    p_server TEXT
    )
    LANGUAGE PLPGSQL
AS
$$
BEGIN
INSERT INTO public."nextcloud_user_info"(username, "password", "server_url")
VALUES(p_username, crypt(p_password, gen_salt('bf')), p_server);
END; $$;
