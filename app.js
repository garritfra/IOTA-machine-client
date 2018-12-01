const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");

const app = express();
app.use(bodyParser.json());
app.use(cors());

app.post("/", (req, res) => {
  const address = req.body.address;
  const data = req.body.data;
  console.log(req.body);
  res.json(req.body);
});

app.get("/new", (req, res) => {});

app.listen(8080, err => {
  if (err) console.log(err);
  console.log("Listening on 8080");
});
