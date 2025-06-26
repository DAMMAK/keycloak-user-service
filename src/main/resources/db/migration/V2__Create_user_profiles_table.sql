-- V2__Create_user_profiles_table.sql
CREATE TABLE user_profiles (
                               id BIGSERIAL PRIMARY KEY,
                               user_id VARCHAR(36) NOT NULL,
                               profile_picture_url VARCHAR(500),
                               bio TEXT,
                               date_of_birth TIMESTAMP,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_user_profiles_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes
CREATE UNIQUE INDEX idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX idx_user_profiles_created_at ON user_profiles(created_at);

-- Create updated_at trigger for user_profiles
CREATE TRIGGER update_user_profiles_updated_at BEFORE UPDATE
    ON user_profiles FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();