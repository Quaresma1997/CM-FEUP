const express = require('express');
const router = express.Router();
const async = require('async');
const uuidv4 = require('uuid/v4');
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');
const moment = require('moment');


router.post('/', function(req, res){


	var mYear = parseInt(moment().format('YY'));
	var mMonth = parseInt(moment().format('MM'));


    const idUser = req.body.idUser;
	const totalPrice = req.body.total;
	db.get('SELECT validity FROM CreditCard WHERE idUser = ?', [idUser], function(err, c){
		
		vDate = c.validity.split("/");
		var vMonth = parseInt(vDate[0]);
		var vYear = vDate[1];

		if(mYear > vYear){
			res.send("Invalid card");
		}
		else if(mYear = vYear && mMonth > vMonth){
			res.send("Invalid card");
		}
	})

	console.log(totalPrice);
    var stmt = db.prepare('SELECT * FROM ShoppingList, ShoppingListItem, Product WHERE idUser = ? AND ShoppingList.idShoppingList = ShoppingListItem.idShoppingList AND Product.barcode = ShoppingListItem.barcode')
    stmt.all(idUser, (err, shoppingList) => {
		var uuid = uuidv4();
		stmt = db.prepare('INSERT INTO Transactions (day, idUser, total, token) VALUES (CURRENT_TIMESTAMP, ?, ?, ?)');
        stmt.get([idUser, totalPrice, uuid], (err, t) => {
            async.each(shoppingList, (c, callback) => {
				stmt = db.prepare('INSERT INTO TransactionItem (quantity, barcode, idTransaction) VALUES (?, ?, ?)');
				stmt.get([c.quantity, c.barcode, uuid], (err, t1) => {
					console.log("async: " + err);
					callback();
				});
			}, (err) => {
				if (err)
					console.log(err);
				else res.json({"uuid" : uuid });
            });
        });
	})
})
/*

router.get("/:idUser", (req, res, next) => {
    const stmt = db.prepare('SELECT * FROM Transactions, TransactionItem, Product WHERE idUser = ? AND Transactions.token = TransactionItem.idTransaction AND Product.barcode = TransactionItem.barcode');
	console.log(req.params.idUser);
	stmt.all(req.params.idUser, (err, rows) => {
		if (rows != undefined && rows != null) {
			res.json({"products" : rows}); // id is valid
		} else {
			console.log(rows);
			res.json('No Cart'); // id doesn't exist
    }
    });
})

**/
router.get('/:uuid', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Transactions, TransactionItem, Product WHERE Transactions.token = TransactionItem.idTransaction AND Product.barcode = TransactionItem.barcode AND Transactions.token = ?');
	stmt.get(req.params.uuid, (err, info) => {
		if (info.token == req.params.uuid) {
			res.send(info);
		} else {
			console.log(info);
			res.send('Error');
		}
	});
});

router.get('/printer/:uuid', function (req, res) {
	var result = {
		token: '',
		day: '',
		idUser: 0,
		email: '',
		name: '',
		fiscalNumber: '',
		address: '',
		products: [
		],
	};
	var stmt = db.prepare('SELECT * FROM Transactions WHERE token = ?');
	stmt.get(req.params.uuid, (err, transaction) => {
		result.day = transaction.day;
		result.token = transaction.token;
		stmt = db.prepare('SELECT * FROM User WHERE idUser = ?');
		stmt.get(transaction.idUser, (err, user) => {
			result.idUser = transaction.idUser;
			result.address = user.address;
			result.email = user.email;
			result.name = user.name;
			result.fiscalNumber = user.fiscalNumber;
			stmt = db.prepare('SELECT * FROM TransactionItem WHERE idTransaction = ?');
			stmt.all(req.params.uuid, (err, info) => {
				async.each(info, (i, callback) => {
					var stmt1 = db.prepare('SELECT * FROM Product WHERE barcode = ?');
					stmt1.get(i.barcode, (err, product) => {
						console.log(product);
						result.products.push({
							barcode: i.barcode,
							name: product.name,
							price: product.price,
							quantity: i.quantity,
						});
						callback();
					});
				}, (err) => {
					console.log(result);
					res.json(result);
				});
			});
		});
	});
});

// retrieve all transactions of a given customer
router.get('/previous/:idUser', function (req, res) {
	var result = [];
	const stmt = db.prepare('SELECT * FROM Transactions, TransactionItem, Product WHERE idUser = ? AND Transactions.token = TransactionItem.idTransaction AND Product.barcode = TransactionItem.barcode');
	stmt.all(req.params.idUser, (err, transactions) => {
		console.log(transactions);
		transactions.sort(function (a, b) {
			return a.idOrder > b.idOrder;
		});
		var temp = '';
		var j = -1;
		for (var i = 0; i < transactions.length; i++) {
			if (temp == transactions[i].idOrder) {
				result[j].products.push({
					idProduct: transactions[i].idProduct,
					quantity: transactions[i].quantity,
					name: transactions[i].name,
					price: transactions[i].price,
				});
			} else {
				j = j + 1;
				temp = transactions[i].idOrder;
				result.push({
					idOrder: transactions[i].token,
					day: transactions[i].day,
					products: [],
				});
				result[j].products.push({
					idProduct: transactions[i].idProduct,
					quantity: transactions[i].quantity,
					name: transactions[i].name,
					price: transactions[i].price,
				});
			}
		}
		if (transactions.length > 0) {
			res.send({"result" : result});
		} else {
			console.log(transactions);
			res.send('No transactions');
		}
	});
});

module.exports = router;
