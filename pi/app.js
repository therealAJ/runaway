var express = require("express");
var fs = require("fs");
var serveIndex = require('serve-index');
var serveStatic = require('serve-static');
var busboy = require('connect-busboy');

var app = express();

var ROOT = __dirname + '/';

//app.use(express.static(ROOT));
app.use('/data', serveIndex(ROOT, {
  'template': function(locals, callback) {
    files = locals.fileList.map(function(item) {
      return {
        name: item.name,
        isDirectory: !(item.stat.nlink == 1)
      };
    });
    callback(null, JSON.stringify(files));
  }
}));
app.use('/data', serveStatic(ROOT))
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
      res.send('Upload successful: ' + filename);
    });
  });
});

app.listen(process.env.PORT || '3000');
console.log("running on port " + (process.env.PORT || '3000'));