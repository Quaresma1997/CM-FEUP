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
    model TEXT NOT NULL,
    maker TEXT NOT NULL,
    color TEXT NOT NULL,
    price FLOAT NOT NULL,
    barcode INTEGER NOT NULL,
    description TEXT
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
    day DATE NOT NULL,
    idUser INTEGER REFERENCES User(idUser),
    total FLOAT NOT NULL,
    token TEXT NOT NULL

);

CREATE TABLE TransactionItem(
    idTransactionItem INTEGER PRIMARY KEY,
    quantity INTEGER NOT NULL,
    idProduct INTEGER REFERENCES Product(idProduct),
    idTransaction TEXT REFERENCES Transactions(idTransaction)
);

INSERT INTO User (email, name, address, password, fiscalNumber, publicKey) VALUES('teste1234@gmail.com', 'Carlos Marques', '1234', '123456789', 'Rua Teste', 'chave_publica');
INSERT INTO Product (model, maker, color, price, barcode, description) VALUES ('model', 'maker', 'red', 10, 612345678907, 'description');
INSERT INTO Product (model, maker, color, price, barcode, description) VALUES ('model2', 'maker2', 'yellow', 70, 128534783579, 'description2');
INSERT INTO ShoppingList (idUser) VALUES (1);
INSERT INTO ShoppingListItem (quantity, barcode, idShoppingList) VALUES (10, 612345678907, 1);


INSERT INTO Transactions (day, idUser, total, token) VALUES ('2019-03-21', 1, 10, 'e73e0ec0-d7d0-47f7-9377-3bd53e11815a');
INSERT INTO Transactions (day, idUser, total, token) VALUES ('2019-03-25', 1, 20, 'e9a14039-3ec2-41b4-ab43-1377e8c7933d');
INSERT INTO TransactionItem (quantity, idProduct, idTransaction) VALUES (10, 1, 1);
INSERT INTO TransactionItem (quantity, idProduct, idTransaction) VALUES (7, 2, 1);
INSERT INTO TransactionItem (quantity, idProduct, idTransaction) VALUES (2, 2, 2);