Create Table If Not Exists table1(
    email Varchar(50) Primary Key,
    password Binary(60),
    nickname Varchar(20)
);

Create Table If Not Exists playtimeHistoryTable(
    email Varchar(50),
    musicId INT,
    tagInfo JSON,
    PRIMARY KEY (email, musicId)
)