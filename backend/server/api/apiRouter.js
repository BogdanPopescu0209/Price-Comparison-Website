const express = require('express');
const router = express.Router();
const mysql = require('mysql')
const url = require('url');

let connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "citizen5859",
    database: "bikes",
    debug: false
});

//REST API Configuration
//Search
router.get('/search/*', handleProductGet);
router.get('/bike-model/*', handleProductGet);
router.get('/bike-instance/*', handleProductGet);
router.get('/bike-comparison/*', handleProductGet);

//Function to handle query's
async function handleProductGet(request, response){

    let handleUrl = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    let queries = handleUrl.query;

    //Get the pagination properties if they have been set. Will be undefined if not set.
    let numItems = queries['num_items'];
    let offset = queries['offset'];
    
    let pathArray = handleUrl.pathname.split("/");

    let pathEnd = pathArray[pathArray.length - 1];

    try {
        //If pathArray[1] equals search return all bikes found
        if (pathArray[1] === 'search') {

            let productCount = await getProductCount(pathEnd);

            let searchResults = await search(pathEnd, numItems, offset);

            let returnObj = {
                count: productCount,
                data: searchResults
            }
            response.json(returnObj);
        }

        //If pathArray[1] equals to bike-model return model
        if(pathArray[1] === 'bike-model'){

            let model = await getBikeModel(pathEnd);
            response.json(model);

        }

        let regex = new RegExp('^[0-9]+$');
        //If pathArray[1] equals to bike-instance and pathEnd is a number return instance
        if(pathArray[1] === 'bike-instance'){

            if(regex.test(pathEnd)){
                let instance = await getInstance(pathEnd);
                response.json(instance);
            }
        }

        //If pathArray[1] equals to bike-comparison and pathEnd is a number return comparison
        if(pathArray[1] === 'bike-comparison'){

            if(regex.test(pathEnd)){
                let comparison = await getComparison(pathEnd);
                response.json(comparison);
            }
        }

    }
    catch (ex){
        response.status(HTTP_STATUS.NOT_FOUND);
        response.send("error: " + JSON.stringify(ex) + "', url: " + request.url + "}");
    }
}

//Function that returns count of bikes
async function getProductCount(pathEnd){

    let sql ="SELECT COUNT(*) FROM bike_instance WHERE description LIKE" + "'%" + pathEnd + "%'";

    let result = await executeQuery(sql);

    return result[0]["COUNT(*)"];
}

//Function that searches for bikes
async function search(pathEnd, numItems, offset){

    let sql = "SELECT * FROM bike_instance WHERE description LIKE" + "'%" + pathEnd.replaceAll("%20", " ") + "%'";

    if (numItems !== undefined && offset !== undefined) {

        sql += "ORDER BY bike_instance.id LIMIT " + numItems + " OFFSET " + offset;

    }

    return executeQuery(sql);
}

//Function that returns bike model
async function getBikeModel(pathEnd){

    let sql = "SELECT * FROM bike_models WHERE model LIKE" + "'%" + pathEnd.replaceAll("%20", " ") + "%'";

    return executeQuery(sql);
}

//Function that returns bike instance
async function getInstance(pathEnd){

    let sql = "SELECT * FROM bike_instance WHERE id=" + pathEnd;

    return executeQuery(sql);
}

//Function that returns bike comparison
async function getComparison(pathEnd){

    let sql = "SELECT * FROM bike_comparison WHERE bike_instance_id=" + pathEnd + " ORDER BY price ASC";

    return executeQuery(sql);
}

//Function that executes query's
async function executeQuery(sql){

    let queryPromise = new Promise((resolve, reject) =>{

        connectionPool.query(sql, function(err, result){

            if(err){
                reject(err)
            }
            resolve(result)
        })
    })
    return queryPromise;
}

module.exports = router;