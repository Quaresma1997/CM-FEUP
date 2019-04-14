const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');
var md5 = require("md5")

router.get("/users", (req, res, next) => {
    var sql = "select * from user"
    var params = []
    db.all(sql, params, (err, rows) => {
        if (err) {
            res.status(400).json({ "error": err.message });
            return;
        }
        res.json({
            "message": "success",
            "data": rows
        })
    });
});

router.get("/cards", (req, res, next) => {
    var sql = "select * from CreditCard"
    var params = []
    db.all(sql, params, (err, rows) => {
        if (err) {
            res.status(400).json({ "error": err.message });
            return;
        }
        res.json({
            "message": "success",
            "data": rows
        })
    });
});

router.post("/login", (req, res) => {
    var data = {
        email: req.body.email,
        password: md5(req.body.password)
    }

    db.get('SELECT idUser FROM User WHERE email = ? AND password = ?', [data.email, data.password], function (err, user) {
        if (err) {
            res.status(400).json({ "error": err.message });
            return;
        }    
        
        if(user == undefined){
            res.json({
                "message": "No user found...",
                "data": user    
            })
        }
        else{
            res.json({
                "message": "Success",
                "data": user    
            })  
        }

    })
})

router.post("/register", (req, res, next) => {
    var errors = []
    if (!req.body.password) {
        errors.push("No password specified");
    }
    if (!req.body.email) {
        errors.push("No email specified");
    }
    if (errors.length) {
        res.status(400).json({ "error": errors.join(",") });
        return;
    }
    var data = {
        name: req.body.name,
        email: req.body.email,
        password: md5(req.body.password),
        address: req.body.address,
        fiscalNumber: req.body.fiscalNumber,
        publicKey: req.body.publicKey,
        cardType: req.body.cardType,
        cardNumber: req.body.cardNumber,
        cardExpiration: req.body.cardExpiration
    }

    db.serialize(() => {
        var sql = 'INSERT INTO User (name, email, address, password, fiscalNumber, publicKey) VALUES (?,?,?,?,?,?)'
        var params = [data.name, data.email, data.address, data.password, data.fiscalNumber, data.publicKey]
        db.run(sql, params, function (err, result) {

            if (err) {
                res.status(400).json({ "error": err.message })
                return;
            }
        });

        db.get('SELECT idUser FROM User WHERE email = ?', [req.body.email], function (err, row) {
            if (err) {
                return console.error(err.message);
            }
            console.log(row.idUser);
            
            var cardParams = [data.cardType, data.cardNumber, data.cardExpiration, row.idUser]
            db.run('INSERT INTO CreditCard (type, number, validity, idUser) VALUES (?,?,?,?)', cardParams, function (err, result) {
                if (err) {
                    res.status(400).json({ "error": err.message })
                    return;
                }
                res.json({
                    "message": "User and card successfully added",
                    "data": data,
                    "id": this.lastID
                })
            });
        });

       
       
    });

})


module.exports = router;