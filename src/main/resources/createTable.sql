DROP TABLE playtime_history_table;
DROP TABLE user_playlist_table;
DROP TABLE shared_playlist_table;

CREATE TABLE IF NOT EXISTS playtime_history_table(
    userID VARCHAR(50),
    musicID INT,
    total_played_time LONG,
    tag_info JSON,
    PRIMARY KEY (userID, musicID)
);

CREATE TABLE IF NOT EXISTS user_playlist_table(
    userID VARCHAR(50),
    name VARCHAR(20),
    description VARCHAR(200),
    musicID_list JSON,
    PRIMARY KEY (userID, name)
);

CREATE TABLE IF NOT EXISTS shared_playlist_table(
    userID VARCHAR(50),
    name VARCHAR(20),
    description VARCHAR(100),
    musicID_list JSON,
    like_count INT,
    download_count INT,
    PRIMARY KEY (userID, name)
);