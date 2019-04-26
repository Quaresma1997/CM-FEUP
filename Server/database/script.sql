DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS CreditCard;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS ShoppingList;
DROP TABLE IF EXISTS ShoppingListItem;
DROP TABLE IF EXISTS Transactions;
DROP TABLE IF EXISTS TransactionItem;

CREATE TABLE User(
    idUser INTEGER PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    address TEXT NOT NULL,
    password TEXT NOT NULL,
    fiscalNumber TEXT NOT NULL,
    publicKey TEXT NOT NULL
);

CREATE Table CreditCard(
    idCreditCard INTEGER PRIMARY KEY,
    type TEXT NOT NULL,
    number TEXT NOT NULL,
    validity TEXT NOT NULL,
    idUser INTEGER REFERENCES User(idUser)
);

CREATE TABLE Product(
    idProduct INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    price FLOAT NOT NULL,
    barcode INTEGER NOT NULL
); 

CREATE TABLE ShoppingList(
    idShoppingList INTEGER PRIMARY KEY,
    idUser INTEGER REFERENCES User(idUser)
);

CREATE TABLE ShoppingListItem(
    idShoppingListItem INTEGER PRIMARY KEY,
    quantity INTEGER NOT NULL,
    barcode INTEGER REFERENCES Product(barcode),
    idShoppingList INTEGER REFERENCES ShoppingList(idShoppingList)
);

CREATE TABLE Transactions(
    idTransaction TEXT PRIMARY KEY,
    day datetime NOT NULL,
    idUser INTEGER REFERENCES User(idUser),
    total FLOAT NOT NULL,
    token TEXT NOT NULL

);

CREATE TABLE TransactionItem(
    idTransactionItem INTEGER PRIMARY KEY,
    quantity INTEGER NOT NULL,
    barcode INTEGER REFERENCES Product(idProduct),
    idTransaction TEXT REFERENCES Transactions(idTransaction)
);

INSERT INTO User (email, name, address, password, fiscalNumber, publicKey) VALUES('carlos@gmail.com', 'Carlos Marques', 'Street 1234', '25d55ad283aa400af464c76d713c07ad', '123456789', 'OpenSSLRSAPublicKey{modulus=b2793dc5472b4928df549105133e1110e2c987c75c9c5ec81b7007de370a6268bd48e4aadb1266b8abb8f9e5ff21,publicExponent=10001}');
INSERT INTO CreditCard (type, number, validity, idUser) VALUES('Visa', 123456789012, '11/20', 1);
INSERT INTO Product (name, price, barcode) VALUES ('Smartphone', 120, 612345678907);
INSERT INTO Product (name, price, barcode) VALUES ('Printer', 45, 128534783579);
INSERT INTO Product (name, price, barcode) VALUES ('Coffee Machine', 24, 832487098238);
INSERT INTO Product (name, price, barcode) VALUES ('Flashdrive 1TB', 449, 347778542586);
INSERT INTO Product (name, price, barcode) VALUES ('Flashdrive 128GB', 52, 121245625879);
INSERT INTO Product (name, price, barcode) VALUES ('Hardrive 2TB', 125, 365252145238);
INSERT INTO Product (name, price, barcode) VALUES ('Hardrive 1TB', 62, 222333444591);
INSERT INTO Product (name, price, barcode) VALUES ('JoyStick', 50, 987456123029);
INSERT INTO Product (name, price, barcode) VALUES ('Keyboard', 22, 808052525362);
INSERT INTO ShoppingList (idUser) VALUES (1);
INSERT INTO ShoppingListItem (quantity, barcode, idShoppingList) VALUES (10, 612345678907, 1);


INSERT INTO Transactions (day, idUser, total, token) VALUES ('2019-04-21 11:20:44', 1, 285, 'e73e0ec0-d7d0-47f7-9377-3bd53e11815a');
INSERT INTO Transactions (day, idUser, total, token) VALUES ('2019-04-25 18:55:44', 1, 125, 'e9a14039-3ec2-41b4-ab43-1377e8c7933d');
INSERT INTO TransactionItem (quantity, barcode, idTransaction) VALUES (2, 612345678907, 1);
INSERT INTO TransactionItem (quantity, barcode, idTransaction) VALUES (1, 128534783579, 1);
INSERT INTO TransactionItem (quantity, barcode, idTransaction) VALUES (1, 365252145238, 2);