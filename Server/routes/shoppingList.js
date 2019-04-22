const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

router.get("/:idUser", (req, res, next) => {
    const stmt = db.prepare('SELECT * FROM ShoppingList, ShoppingListItem, Product WHERE idUser = ? AND ShoppingList.idShoppingList = ShoppingListItem.idShoppingList AND Product.idProduct = ShoppingListItem.idProduct');
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

router.get("/add/:idUser/:idProduct", (req, res) => {
	//check if user has a shoppinglist
	db.get('SELECT * FROM ShoppingList WHERE idUser = ?', [req.params.idUser], function(err, list){
		if (err) {
			res.status(400).json({ "error": err.message })
			return;
		}
		console.log(list);
		//if not, create one
		if(list == undefined){
			db.run('insert into ShoppingList (idUser) values (?)', [req.params.idUser], function (err) {
				if (err) {
                    res.status(400).json({ "error": err.message })
                    return;
				}
				
				console.log("Shopping list created.")
			})

			//get the shoppingList created
			db.get('select * from ShoppingList where idUser = ?', [req.params.idUser], function(err, row2){
				if (err) {
					res.status(400).json({ "error": err.message })
					return;
				}

				//add product as a shopping list item
				db.run('insert into ShoppingListItem (quantity, idProduct, idShoppingList) values (?,?,?)', [1, req.params.idProduct, row2.idShoppingList], function(err){
					if (err) {
						res.status(400).json({ "error": err.message })
						return;
					}

					res.json({
						"message": "Shopping list created and new product added!",
						"data": row2,
						"id" : this.lastID
					})
				})
			})
		}
		else{
			if(list.idUser == req.params.idUser){
				db.get('select * from ShoppingListItem where idShoppingList = ? AND idProduct = ?', [list.idShoppingList, req.params.idProduct], function(err, row3){
					if (err) {
						res.status(400).json({ "error": err.message })
						return;
					}
					
					if(row3 == undefined){
						db.run('insert into ShoppingListItem (quantity, idProduct, idShoppingList) values (?,?,?)', [1, req.params.idProduct, list.idShoppingList], function(err){
							if (err) {
								res.status(400).json({ "error": err.message })
								return;
							}
		
							res.json({
								"message": "New product added!",
								"data": row3,
								"id" : this.lastID
							})
						})
					}
					else{
						res.json({
							"message": "Product already listed...",
							"data": row3,
							"id" : this.lastID
						})
					}
				})
			}
			else{
				res.json('Error');
			}
		} 

	})
})

router.get("/remove/:idUser/:idProduct", (req, res) => {
	db.get('SELECT * FROM ShoppingList, ShoppingListItem WHERE ShoppingList.idUser = ? AND ShoppingList.idShoppingList = ShoppingListItem.idShoppingList AND ShoppingListItem.idProduct = ?', [req.params.idUser, req.params.idProduct], function (err, list){
		if (err) {
			res.status(400).json({ "error": err.message })
			return;
		}	

		if(list == undefined){
			res.json({
				"message": "Product not found...",
				"data": list,
				"id" : this.lastID
			})
		}
		else{
			db.run('DELETE FROM ShoppingListItem WHERE idProduct = ? AND idShoppingList = ?', [req.params.idProduct, list.idShoppingList], function (err, list2){
				
				if (err) {
					res.status(400).json({ "error": err.message })
					return;
				}


				res.json({
					"message": "Product deleted!",
					"data": list2,
					"id" : this.lastID
				})
			})
		}
	})
})

router.get("/add_quantity/:idUser/:idProduct", (req, res) => {
	db.get('SELECT * FROM ShoppingList, ShoppingListItem WHERE ShoppingList.idUser = ? AND ShoppingList.idShoppingList = ShoppingListItem.idShoppingList AND ShoppingListItem.idProduct = ?', [req.params.idUser, req.params.idProduct], function (err, list){
		if (err) {
			res.status(400).json({ "error": err.message })
			return;
		}	

		if(list == undefined){
			res.json({
				"message": "Product not found...",
				"data": list,
				"id" : this.lastID
			})
		}
		else{
			db.run('UPDATE ShoppingListItem SET quantity = ? WHERE idShoppingList = ? AND idProduct = ?', [list.quantity + 1, list.idShoppingList, req.params.idProduct], function(err, result){
				if (err) {
					res.status(400).json({ "error": err.message })
					return;
				}
				
				res.json({
					"message": "Add another product of the same type!",
					"data": result,
					"id" : this.lastID
				})
				
			})
		}

	})
})

router.get("/subtract_quantity/:idUser/:idProduct", (req, res) => {
	db.get('SELECT * FROM ShoppingList, ShoppingListItem WHERE ShoppingList.idUser = ? AND ShoppingList.idShoppingList = ShoppingListItem.idShoppingList AND ShoppingListItem.idProduct = ?', [req.params.idUser, req.params.idProduct], function (err, list){
		if (err) {
			res.status(400).json({ "error": err.message })
			return;
		}	

		if(list == undefined){
			res.json({
				"message": "Product not found...",
				"data": list,
				"id" : this.lastID
			})
		}
		else if(list.quantity == 1){
			db.run('DELETE FROM ShoppingListItem WHERE idProduct = ? AND idShoppingList = ?', [req.params.idProduct, list.idShoppingList], function (err){
				
				if (err) {
					res.status(400).json({ "error": err.message })
					return;
				}


				res.json({
					"message": "Product deleted!",
					"data": list,
					"id" : this.lastID
				})
			})
		}
		else{
			db.run('UPDATE ShoppingListItem SET quantity = ? WHERE idShoppingList = ? AND idProduct = ?', [list.quantity - 1, list.idShoppingList, req.params.idProduct], function(err, result){
				if (err) {
					res.status(400).json({ "error": err.message })
					return;
				}
				
				res.json({
					"message": "Removed another product of the same type!",
					"data": result,
					"id" : this.lastID
				})
				
			})
		}

	})
})

module.exports = router;

