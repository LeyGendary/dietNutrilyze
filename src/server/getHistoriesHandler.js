const { Firestore, Timestamp } = require('@google-cloud/firestore');

async function getHistoriesHandler(request, h) {
  try {
    const db = new Firestore();
    const predictCollection = db.collection('predictions');
    const snapshot = await predictCollection.get();

    const histories = [];
    snapshot.forEach((doc) => {
      histories.push({
        id: doc.id,
        history: {
          result: doc.data().result,
          Calories: doc.data().Calories,
          Carbohydrates: doc.data().Carbohydrates,
          Protein: doc.data().Protein,
          Fat: doc.data().Fat,
          Fiber: doc.data().Fiber,
          VitaminC: doc.data().VitaminC,
          Zinc: doc.data().Zinc,
          Potassium: doc.data().Potassium,
          Iron: doc.data().Iron,
          Calcium: doc.data().Calcium,
          confidenceScore: doc.data().confidenceScore,
          createdAt: doc.data().createdAt instanceof Timestamp ? doc.data().createdAt.toDate().toISOString() : doc.data().createdAt,
          id: doc.id
        }
      });
    });

    return {
      status: 'success',
      data: histories
    };
  } catch (error) {
    console.error('Error fetching prediction histories:', error);
    return h.response({ status: 'error', message: 'Failed to fetch prediction histories' }).code(500);
  }
}

module.exports = getHistoriesHandler;
