CREATE TABLE trades (
    tradeID INT AUTO_INCREMENT PRIMARY KEY,
    price FLOAT,
    quantity INT,
    userID INT,
    ticker VARCHAR(45),
    name VARCHAR(45)
);

CREATE TABLE users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(45),
    password VARCHAR(45),
    email VARCHAR(45),
    balance FLOAT
);
