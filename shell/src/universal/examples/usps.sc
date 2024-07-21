// Benchmark on USPS zip code hand writing data
import smile._
import smile.util._
import smile.base.rbf.RBF;
import smile.base.mlp.{Layer, OutputFunction}
import smile.clustering._
import smile.data.formula._
import smile.data.`type`._
import smile.classification._
import smile.math._
import smile.math.distance._
import smile.math.kernel._
import smile.math.rbf._
import smile.validation._
import smile.validation.metric._

val fields = new java.util.ArrayList[StructField]
fields.add(new StructField("class", DataTypes.ByteType))
(1 to 256).foreach(i => fields.add(new StructField("V" + i, DataTypes.DoubleType)))
val schema = DataTypes.struct(fields)

val formula: Formula = "class" ~ "."
val zipTrain = read.csv(Paths.getTestData("usps/zip.train").toString, delimiter = " ", header = false, schema = schema)
val zipTest = read.csv(Paths.getTestData("usps/zip.test").toString, delimiter = " ", header = false, schema = schema)
val x = formula.x(zipTrain).toArray()
val y = formula.y(zipTrain).toIntArray()
val testx = formula.x(zipTest).toArray()
val testy = formula.y(zipTest).toIntArray()

val n = x.length
val k = 10

// Random Forest
println("Training Random Forest of 200 trees...")
val metrics = validate.classification(formula, zipTrain, zipTest) { (formula, data) =>
  randomForest(formula, data, ntrees = 200)
}
println(metrics)

// Gradient Tree Boost
println("Training Gradient Tree Boost of 200 trees...")
validate.classification(formula, zipTrain, zipTest) { (formula, data) =>
  gbm(formula, data, ntrees = 200)
}

// SVM
println("Training SVM, one epoch...")
val kernel = new GaussianKernel(8.0)
validate.classification(x, y, testx, testy) { (x, y) =>
  ovo(x, y) { (x, y) =>
    SVM.fit(x, y, kernel, 5, 1E-3)
  }
}

// RBF Network
println("Training RBF Network...")
val kmeans = KMeans.fit(x, 200)
val distance = new EuclideanDistance
val neurons = RBF.of(kmeans.centroids, new GaussianRadialBasis(8.0), distance)
validate.classification(x, y, testx, testy) { (x, y) =>
  rbfnet(x, y, neurons, false)
}

// Logistic Regression
println("Training Logistic regression...")
validate.classification(x, y, testx, testy) { (x, y) =>
  logit(x, y, 0.3, 1E-3, 1000)
}

// Neural Network
println("Training Neural Network, 10 epoch...")
val net = new MLP(Layer.input(256),
  Layer.sigmoid(768),
  Layer.sigmoid(192),
  Layer.sigmoid(30),
  Layer.mle(10, OutputFunction.SIGMOID)
)

net.setLearningRate(TimeFunction.linear(0.01, 20000, 0.001));

(0 until 10).foreach(epoch => {
  println("----- epoch %d -----" format epoch)
  MathEx.permutate(x.length).foreach(i =>
    net.update(x(i), y(i))
  )
  val prediction = net.predict(testx)
  println("Accuracy = %.2f%%" format (100.0 * Accuracy.of(testy, prediction)))
})
