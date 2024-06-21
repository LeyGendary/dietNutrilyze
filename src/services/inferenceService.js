const tf = require('@tensorflow/tfjs-node');
const axios = require('axios');
const csv = require('csv-parser');
const InputError = require('../exceptions/InputError');

const classes = [
    'Apple', 'Apricot', 'Banana', 'Beans', 'Beetroot', 'Bell Pepper', 
    'Bitter Gourd', 'Black Berry', 'Black Current', 'Blueberry', 'Bottle Gourd',
    'Broccoli', 'Cabbage', 'Carrot', 'Cauliflower', 'Chilli', 'Corn', 'Cranberry',
    'Cucumber', 'Custard Apple', 'Dates', 'Dragon Fruit', 'Fig', 'Garlic', 
    'Ginger', 'Grapes', 'Guava', 'Jackfruit', 'Kiwi', 'Lemon', 'Litchi', 
    'Mango', 'Okra', 'Orange', 'Passion Fruit', 'Peach', 'Pear', 'Plum', 
    'Raspberry', 'Spinach', 'Strawberry', 'Tomato'
];

// Function to load CSV data from a URL into a dictionary
const loadCSVData = async (url) => {
    const results = {};
    const response = await axios.get(url);
    return new Promise((resolve, reject) => {
        require('stream')
            .Readable
            .from(response.data)
            .pipe(csv())
            .on('data', (data) => {
                results[data.Name] = data;
            })
            .on('end', () => {
                resolve(results);
            })
            .on('error', (error) => {
                reject(error);
            });
    });
};

// Load the dataset
let nutritionData = {};
loadCSVData('https://storage.googleapis.com/model-nutrilyze/nutrition_values.csv')
    .then((data) => {
        nutritionData = data;
    })
    .catch((error) => {
        console.error('Error loading CSV data:', error);
    });

async function predictClassification(model, image) {
    try {
        const tensor = tf.node
            .decodeJpeg(image)
            .resizeNearestNeighbor([100, 100])
            .expandDims()
            .toFloat();

        const prediction = model.predict(tensor);
        const score = await prediction.data();
        const confidenceScore = Math.max(...score) * 100;

        const classResult = tf.argMax(prediction, 1).dataSync()[0];
        const label = classes[classResult];

        // Retrieve nutritional information
        const nutritionInfo = nutritionData[label] || {};

        return { 
            confidenceScore, 
            label, 
            nutritionInfo
        };
    } catch (error) {
        throw new InputError(`Terjadi kesalahan input: ${error.message}`);
    }
}

module.exports = predictClassification;
