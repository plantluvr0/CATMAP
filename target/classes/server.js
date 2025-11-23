import { Pool } from 'pg'

const Joi = require('joi');
const express = require('express');
const app = express();
const port = process.env.PORT;

//sets up db pool
const pool = new Pool({
    host: "localhost",
    user: process.env.DB_USER,
    password: process.env.DB_PASS,
    port: process.env.DB_PORT,
    database: process.env.DB_NAME,
    max: 20
});

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.get('/map', (req, res) => {
    const json = {};
    json.nodes = pool.query('SELECT * FROM nodes').rows;
    res.status(202).set('Cache-Control', 'no-cache')
    res.json(json);
});

app.get('/path', (req, res) => {
    const { start, end } = req.query;
    const path = pool.query('SELECT * FROM paths WHERE start_node = $1 and end_node = $2', [start, end]);
    res.status(202).set('Cache-Control', 'no-cache');
    res.json(path.rows);
});

app.post('/data/new', (req, res) => {
    const { body } = req;
    const schema = Joi.object({
        nodes: Joi.array()
            .items( Joi.object({
                startNodeId: Joi.number()
                    .integer(),
                endNodeId: Joi.number()
                    .integer(),
                time: Joi.number()
        }) )
    });

    //input val
    const { error } = schema.validate(body);
    if (!error) {
        // actual work
        body.nodes.forEach((item) => {
            const { rows } = pool.query('SElECT id and neighbors FROM nodes WHERE nodeId = $1 and nodeId = $2', [item.startNodeId, item.endNodeId]);
            rows.forEach((node) => {
                node.neighbors.forEach((neighbor, index) => {
                    if (neighbor.id === item.startNodeId || neighbor.id === item.endNodeId) {
                        const newWeight = updateWeight(neighbor.weight, neighbor.counter, item.time, neighbor.distance, neighbor.slope)
                        const newCounter = neighbor.counter + 1;
                        pool.query("UPDATE nodes SET neighbors = jsonb_set(jsonb_set(neighbors, '{$1, weight}', '$2'), '{$1, counter}', $3)" +
                            " WHERE nodeId = $4", [index, newWeight, newCounter, node.id]);
                    }
                })
            })
        })
    } else {
        res.status(400).json({ message: error.details[0].message });
    }
});

app.listen(port, () => {
    console.log(`listening on port ${port}`);
});

function updateWeight(curr, counter, time, distance, slope) {
    let newWeight = (time * distance) / slope;
    return ((curr * counter) + newWeight) / (counter + 1);
}

