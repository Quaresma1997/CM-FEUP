const express = require('express');
const router = express.Router();
const async = require('async');
const uuidv4 = require('uuid/v4');
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');


router.post('/', function(req, res){
    const idUser = req.body.idUser;

    var stmt = db.prepare('SELECT * FROM ShoppingList, ShoppingListItem, Product WHERE idUser = ? AND ShoppingList.idShoppingList = ShoppingListItem.idShoppingList AND Product.idProduct = ShoppingListItem.idProduct')
    stmt.all(idUser, (err, shoppingList) => {
        var uuid = uuidv4();
        stmt.get([uuid, idUser], (err, t) => {
            async.each(shoppingList, (c, callback) => {
				stmt = db.prepare('INSERT INTO TransactionItem (quantity, idProduct, idTransaction) VALUES (?, ?, ?)');
				stmt.get([c.quantity, c.idProduct, uuid], (err, t1) => {
					console.log("async: " + err);
					callback();
				});
			}, (err) => {
				if (err)
					console.log(err);
				else res.json(uuid);
            });
        });
    })
})

router.get('/:uuid', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Transactions, TransactionItem, Product WHERE Transactions.idTransaction = TransactionItem.idTransaction AND Product.idProduct = TransactionItem.idProduct AND Transactions.idTransaction = ?');
	stmt.get(req.params.uuid, (err, info) => {
		if (info.idOrder == req.params.uuid) {
			res.send(info);
		} else {
			console.log(info);
			res.send('Error');
		}
	});
});

router.get('/printer/:uuid', function (req, res) {
	var result = {
		day: '',
		idUser: 0,
		email: '',
		name: '',
		fiscalNumber: '',
		address: '',
		products: [
		],
	};
	var stmt = db.prepare('SELECT * FROM transactions WHERE idOrder = ?');
	stmt.get(req.params.uuid, (err, order) => {
		result.day = order.day;
		stmt = db.prepare('SELECT * FROM User WHERE idUser = ?');
		stmt.get(order.idUser, (err, user) => {
			result.idUser = order.idUser;
			result.address = user.address;
			result.email = user.email;
			result.name = user.name;
			result.nif = user.NIF;
			stmt = db.prepare('SELECT * FROM OrderItem WHERE idOrder = ?');
			stmt.all(req.params.uuid, (err, info) => {
				async.each(info, (i, callback) => {
					var stmt1 = db.prepare('SELECT * FROM Product WHERE idProduct = ?');
					stmt1.get(i.idProduct, (err, product) => {
						console.log(product);
						result.products.push({
							idProduct: i.idProduct,
							maker: product.maker,
							model: product.model,
							price: product.price,
							description: product.description,
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
	const stmt = db.prepare('SELECT * FROM Transactions, TransactionItem, Product WHERE idUser = ? AND Transactions.idTransaction = TransactionItem.idTransaction AND Product.idProduct = TransactionItem.idProduct');
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
					maker: transactions[i].maker,
					model: transactions[i].model,
					price: transactions[i].price,
					description: transactions[i].description,
				});
			} else {
				j = j + 1;
				temp = transactions[i].idOrder;
				result.push({
					idOrder: transactions[i].idOrder,
					day: transactions[i].day,
					products: [],
				});
				result[j].products.push({
					idProduct: transactions[i].idProduct,
					quantity: transactions[i].quantity,
					maker: transactions[i].maker,
					model: transactions[i].model,
					price: transactions[i].price,
					description: transactions[i].description,
				});
			}
		}
		if (transactions.length > 0) {
			res.send(result);
		} else {
			console.log(transactions);
			res.send('No transactions');
		}
	});
});

module.exports = router;