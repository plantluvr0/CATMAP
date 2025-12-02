import { Pool } from 'pg';
import { Connector } from "@google-cloud/cloud-sql-connector";
import Joi from 'joi';
import express from 'express'

//sets up server
const app = express();
const port = process.env.PORT;
//

//sets up db pool
const connector = new Connector();
const clientops = await connector.getOptions({
    instanceConnectionName: "catmap-474717:us-central1:catmapsql1",
    ipType: "PUBLIC"
})
const pool = await new Pool({
    ...clientops,
    user: process.env.DB_USER,
    password: process.env.DB_PASS,
    database: "postgres",
    max: 5
});
//

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.get('/', async (req, res) => {
    try {
        const { rows } = await pool.query('SELECT * FROM nodes');
        res.status(202).set('Cache-Control', 'no-cache');
        res.json(rows);
    } catch (err) {
        console.error('Database connection failed:', err.stack);
        res.status(404).json({message: 'could not retrieve data'})
    }
});

app.post('/data/new', async (req, res) => {
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
        for (const item of body.nodes) {
            const { rows } = await pool.query('SElECT id and neighbors FROM nodes WHERE nodeId = $1 and nodeId = $2', [item.startNodeId, item.endNodeId]);
            rows.forEach((node) => {
                node.neighbors.forEach( async (neighbor, index) => {
                    if (neighbor.id === item.startNodeId || neighbor.id === item.endNodeId) {
                        const newWeight = updateWeight(neighbor.weight, neighbor.counter, item.time, neighbor.distance, neighbor.slope)
                        const newCounter = neighbor.counter + 1;
                        await pool.query("UPDATE nodes SET neighbors = jsonb_set(jsonb_set(neighbors, '{$1, weight}', '$2'), '{$1, counter}', $3)" +
                            " WHERE nodeId = $4", [index, newWeight, newCounter, node.id]);
                    }
                })
            })
        }
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

