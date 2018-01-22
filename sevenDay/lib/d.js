const MongoClient = require('mongodb').MongoClient;
const url = "mongodb://localhost:27017";
const long = require('mongodb').Long;
const decimalValue = require('mongodb').Decimal128;

MongoClient.connect(url, (err, client) => {
    console.log('sucess');
    const db = client.db('movie');
    const cols = db.collection('inserts');
    // const a_long = long("2");
    // const b_decim = decimalValue.fromString("27.555");
    // cols.insertMany([{ a: 1 }, { a: 2 }, { a: 3 }], (e, r) => {
    //     cols.updateOne({ a: 1 }, { $set: { b: 1 } }, (err, r) => {
    //         cols.updateMany({ a: 2 }, { $set: { b: 666 } }, (err, r) => {
    //             cols.updateMany({ a: 3 }, { $set: { b: 888 } }, { upsert: true }, (err, r) => {
    //                 client.close();
    //             });
    //         });
    //     });
    // });

    // cols.deleteMany({}, (err, r)=>{
    //     client.close();
    // });

    // cols.findOneAndUpdate({ a: 1 }, { $set: { b: 9999 } }, {
    //     returnOriginal: false,
    //     sort: [['a', 1]],
    //     upsert: true
    // }, (err, r) => {
    //     client.close();
    // });

    // cols.bulkWrite([
    //     {
    //         insertOne: { document: { a: 7888 } }
    //     },
    //     {
    //         updateOne: { filter: { a: 2 }, update: { $set: { a: 2999 } }, upsert: true }
    //     }
    // ], { ordered: true }, (err, r) => {
    //     client.close();
    // })


    // cols.insertOne(
    //     {
    //         a: 1,
    //         b: function (params) {
    //             return 'hello'
    //         }
    //     },
    //     {
    //         w: 'majority',
    //         wtimeout: 10000,
    //         serializeFunctions: true
    //     },
    //     (err, r) => {
    //         client.close();
    //     }
    // );

    const bulk = cols.initializeOrderedBulkOp();
    for (let i = 0; i < 10; i++) {
        bulk.insert({ a: i });
    }
    for (let i = 0; i < 10; i++) {
        bulk.find({ b: i }).upsert().updateOne({ b: 1 });
    }
    bulk.find({ b: 1 }).deleteOne();

    bulk.execute((err, r) => {
        client.close();
    });



});
