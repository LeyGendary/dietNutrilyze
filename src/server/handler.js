const predictClassification = require('../services/inferenceService');
const storeData = require('../services/storeData');
const crypto = require('crypto');

async function postPredictHandler(request, h) {
  const { image } = request.payload;
  const { model } = request.server.app;
  
  // Extract confidenceScore, label, and nutritionInfo from the predictClassification function
  const { confidenceScore, label, nutritionInfo } = await predictClassification(model, image);
  
  const id = crypto.randomUUID();
  const createdAt = new Date().toISOString();

  const data = {
    id: id,
    result: label,
    Calories: nutritionInfo['Calories (per 100g)'],
    Carbohydrates: nutritionInfo['Carbohydrates (g)'],
    Protein: nutritionInfo['Protein (g)'],
    Fat: nutritionInfo['Fat (g)'],
    Fiber: nutritionInfo['Fiber (g)'],
    VitaminC: nutritionInfo['Vitamin C (mg)'],
    Zinc: nutritionInfo['Zinc (mg)'],
    Potassium: nutritionInfo['Potassium (mg)'],
    Iron: nutritionInfo['Iron (mg)'],
    Calcium: nutritionInfo['Calcium (mg)'],
    confidenceScore: confidenceScore,
    createdAt: createdAt
  };

  // await storeData(id, data);

  const response = h.response({
    status: 'success',
    message: confidenceScore > 99 ? 'Model is predicted successfully.' : 'Model is predicted successfully but under threshold. Please use the correct picture',
    data
  });
  
  response.code(201);
  return response;
}

module.exports = postPredictHandler;
