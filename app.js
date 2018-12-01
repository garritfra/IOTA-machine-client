const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
const { composeAPI } = require("@iota/core");
const converter = require("@iota/converter");

const app = express();
app.use(bodyParser.json());
app.use(cors());

const provider = "https://testnet140.tangle.works";
const iota = composeAPI({
  provider: provider
});

app.post("/", (req, res) => {
  const address = req.body.address;
  const data = req.body.data;
  console.log(req.body);
  res.json(req.body);
});

app.get("/", (req, res) => {
  iota
    .getNodeInfo()
    .then(info => res.send(info))
    .catch(err => {
      res.send(err);
    });
});

app.get("/new", (req, res) => {
  // must be truly random & 81-trytes long

  let seed = "9";
  for (let i = 0; i < 80; i++) {
    seed += "9";
  }

  const trytes = converter.asciiToTrytes(
    JSON.stringify({ data: { station: "New Product Created" } })
  );

  // Array of transfers which defines transfer recipients and value transferred in IOTAs.
  const transfers = [
    {
      address:
        "OEQFZPGYVLGUDPUNWZZORRIBS9LAGMHJEWWHLLRKIE9Z9VHHXGBPLCDIKRBMKXCTPJWZ9BEGMJMIKHMD99R9OKBUAY",
      value: 0, // 1Ki
      tag: "", // optional tag of `0-27` trytes
      message: trytes // optional message in trytes
    }
  ];

  // Depth or how far to go for tip selection entry point
  const depth = 3;

  // Difficulty of Proof-of-Work required to attach transaction to tangle.
  // Minimum value on mainnet & spamnet is `14`, `9` on devnet and other testnets.
  const minWeightMagnitude = 14;

  // Prepare a bundle and signs it
  iota
    .prepareTransfers(seed, transfers)
    .then(trytes => {
      // Persist trytes locally before sending to network.
      // This allows for reattachments and prevents key reuse if trytes can't
      // be recovered by querying the network after broadcasting.

      // Does tip selection, attaches to tangle by doing PoW and broadcasts.
      return iota.sendTrytes(trytes, depth, minWeightMagnitude);
    })
    .then(bundle => {
      res.send(
        `Published transaction with tail hash: ${bundle[0].hash}\n` +
          JSON.stringify(bundle[0])
      );
    })
    .catch(err => {
      // catch any errors
    });
});

app.listen(8080, err => {
  if (err) console.log(err);
  console.log("Listening on 8080");
});
