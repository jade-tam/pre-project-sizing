ALTER TABLE users
    ADD COLUMN avatar_url VARCHAR(255),
    ADD COLUMN display_name VARCHAR(100),
    ADD COLUMN pronouns VARCHAR(50),
    ADD COLUMN bio VARCHAR(500);

-- Populate existing rows
UPDATE users
SET
    display_name = full_name,
    pronouns = '',
    bio = ''
WHERE display_name IS NULL;