let express = require("express");
let fs = require("fs");
let serveIndex = require('serve-index');
var busboy = require('connect-busboy');

let app = express();

let ROOT = __dirname + '\\';

app.use(express.static(ROOT));
app.use('/data', serveIndex(ROOT));
app.use(busboy());

/*
 * Upload the file to the path
 */
app.post('/fileupload', function(req, res) {
  var fstream;
  req.pipe(req.busboy);
  req.busboy.on('file', function (fieldname, file, filename) {
    console.log("Uploading: " + filename);
    fstream = fs.createWriteStream(ROOT + filename);
    file.pipe(fstream);
    fstream.on('close', function () {
      res.redirect('back');
    });
  });
});

app.listen(process.env.PORT || '3000');
console.log("running on port " + (process.env.PORT || '3000'));