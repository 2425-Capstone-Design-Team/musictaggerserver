Create Table If Not Exists playtimeHistoryTable(
    email Varchar(50),
    musicId INT,
    totalPlaytime LONG,
    tagInfo JSON,
    PRIMARY KEY (email, musicId)
);

Create Table If Not Exists playlistTable(
    email Varchar(50) Primary Key,
    musicIdList Json
);

Create Table If Not Exists playlistShareTable(
    email Varchar(50),
    name Varchar(20),
    musicIdList Json,
    description Varchar(100),
    likeCount Int,
    copyCount Int,
    PRIMARY KEY (email, name)
);

Drop Table playlistShareTable;