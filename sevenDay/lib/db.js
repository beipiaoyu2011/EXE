const MongoClient = require('mongodb').MongoClient;
const url = 'mongodb://localhost:27017';
const dbname = 'movie';
const test = require('assert');
const collectionName = 'myMovie';

const insertDocument = (db, data, callback) => {
    const doc = db.collection(collectionName);
    doc.insertMany(data, (err, result) => {
        callback(result);
    });
};

const findDocument = (db, callback) => {
    const doc = db.collection(collectionName);
    doc.find({}).toArray((err, cont) => {
        console.log(cont);
        callback(cont);
    });
};

const indexCollection = (db, callback) => {
    const doc = db.collection(collectionName);
    doc.createIndex({ a: 1 }, null, (err, result) => {
        callback();
    });
};

const createCollection = (db, callback) => {
    db.createCollection(collectionName, { "capped": true, "size": 100000, "max": 5000 }, (err, result) => {
        callback();
    });
};

module.exports = (data) => {
    MongoClient.connect(url, (err, client) => {
        if (err) return console.err(err);
        var db = client.db(dbname);
        createCollection(db, () => {
            insertDocument(db, data, () => {
                indexCollection(db, () => {
                    findDocument(db, () => {
                        client.close();
                    });
                });
            });
        });

    });
};

