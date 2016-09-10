let express = require("express");
let fs = require("fs");
let serveIndex = require('serve-index');

let app = express();

let ROOT = __dirname + '\\';

app.use(express.static(ROOT));
app.use('/data', serveIndex(ROOT));

/*
 * Upload the file to the path
 * Post body - file data
 */
app.post('/upload/:path', (req, res) => {
  
});

app.listen(process.env.PORT || '3000');
console.log("running on port " + (process.env.PORT || '3000'));