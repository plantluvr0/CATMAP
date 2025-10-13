import { Pool } from 'pg'

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
    json.map = pool.query('SELECT * FROM map').rows;
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
        username: Joi.string().required(),
        steps: Joi.array(),
        time: Joi.number().integer(),
        rate: Joi.number().integer()
    });

    //input val
    const { error } = schema.validate(body);
    if (error) {
        res.status(400).json({ message: error.details[0].message });
    } else {


    }
});

app.listen(port, () => {
    console.log(`listening on port ${port}`)
});

