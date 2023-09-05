import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;


public class part1 {

    /*
    Part 1: k-Nearest Neighbour Method (30 Marks for COMP307, and 37 Marks for AIML420

    The wine data set is taken from the UCI Machine Learning Repository (https://archive.ics.uci.edu/ml/
    datasets/wine). The data set contains 178 instances in 3 classes, having 59, 71 and 48 instances, respectively.
    Each instance has 13 attributes: Alcohol, Malic acid, Ash, Alcalinity of ash, Magnesium, Total phenols, Flavanoids,
    Nonflavanoid phenols, Proanthocyanins, Color intensity, Hue, OD280/OD315 of diluted wines, and
    Proline. We have split the dataset into two subsets: one for training and the other for testing.

    code:

     */
    private int K;

    private final ArrayList<Wine> trainingList = new ArrayList<>();
    private final ArrayList<Wine> testList = new ArrayList<>();
    private final ArrayList<Result> resultList = new ArrayList<>();

    private final String trainingName;
    private final String testName;

    public part1(String training,String test) {
        this.trainingName = training;
        this.testName = test;
    }

    /** The run() -> program runs/ reads training and test files
     *
     *
     * */
    public void run() throws IOException {

        System.out.println("| PART 1: KNN Algorithm |");
        wineFileReader("src/"+testName, this.testList);
        wineFileReader("src/"+trainingName, this.trainingList);
        this.K = 3;

        KNN(K);


    }

    /**
     * normalising a wine list - the array of data the wine contains is normalised
     **/
    public static void normaliseList(ArrayList<Wine> winelist) {

        for (int i = 0; i < 13; i++) {
            double[] arrayToNormalize = new double[winelist.size()];
            int x = 0;
            int y = 0;
            for (Wine wine : winelist) {
                arrayToNormalize[x] = wine.getWineAttributes()[i];
                x++;
            }
            double min = getMinValue(arrayToNormalize);
            double max = getMaxValue(arrayToNormalize);

            // put back the normalized value into the list
            for (Wine wine : winelist) {
                arrayToNormalize[y] = (arrayToNormalize[y] - min) / (max - min);
                wine.setWineAttribute(i, arrayToNormalize[y]);
                y++;
            }
        }
    }

    /**
     * get Max value out of an array of numbers
     **/
    public static double getMaxValue(double[] numbers) {
        double maxValue = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > maxValue) {
                maxValue = numbers[i];
            }
        }
        return maxValue;
    }

    /**
     * get Min value out of an array of numbers
     **/
    public static double getMinValue(double[] numbers) {
        double minValue = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < minValue) {
                minValue = numbers[i];
            }
        }
        return minValue;
    }

    /**
     * reads a wine file and puts the data into the array lists
     **/
    public void wineFileReader(String filename, ArrayList<Wine> list) throws FileNotFoundException {


        File file = new File(filename);
        Scanner sc = new Scanner(file);

        sc.nextLine();
        while (sc.hasNext()) {

            double[] attributes = new double[13];
            int i = 0;

            while (i < 13) {
                attributes[i] = Double.parseDouble(sc.next());
                i++;
            }
            int d = Integer.parseInt(sc.next());
            Wine wine = new Wine(attributes, d);
            list.add(wine);
        }

        // normalise the attributes for a selected wine list
        normaliseList(list);
    }

    /**
     * K-Nearest Neighbour Search Algorithm
     **/
    public void KNN(int k) {

        int[] testClasses = new int[3];
        int[] trainingClasses = new int[3];
        int acc = 0;
        // iterate through the test wines
        for (Wine testWine : testList) {

            //count amount of wine in each class in the test
            if (testWine.getCLASS() == 1) {
                testClasses[0]++;
            } else if (testWine.getCLASS() == 2) {
                testClasses[1]++;
            } else {
                testClasses[2]++;
            }

            // iterate through the training wines
            for (Wine trainWine : trainingList) {
                // start of the distance calculation
                double d = 0.0;

                for (int j = 0; j < trainWine.getWineAttributes().length; j++) {
                    d += Math.pow(Math.abs(trainWine.getWineAttributes()[j] - testWine.getWineAttributes()[j]), 2);

                }
                // calculate the distance from the k number of training wine from the wine instance - adds to result list (distance, training wine list)
                double distance = Math.sqrt(d);
                resultList.add(new Result(distance, trainWine.getCLASS()));
            }

            // sort result with nearest values first
            resultList.sort(new DistanceComparator());

            // adds k amount of nearest neighbours to an array
            int[] classArray = new int[k];
            for (int p = 0; p < k; p++) {
                System.out.print(resultList.get(p).getCLASS() + "----" + resultList.get(p).distance);
                classArray[p] = resultList.get(p).getCLASS();
            }

            // find the modal of the k nearest neighbours
            int majorClass = findClass(classArray);
            System.out.print("  Class of new instance is: " + majorClass);
            System.out.println("    Class of test instance is: " + testWine.getCLASS());

            if (1 == majorClass) {
                trainingClasses[0]++;
            } else if (2 == majorClass) {
                trainingClasses[1]++;
            } else {
                trainingClasses[2]++;
            }

            // finds accuracy
            if (testWine.getCLASS() == majorClass) {
                acc++;
            }
            else{
                System.out.println("guessed: "+majorClass+" Actual: "+testWine.getCLASS());
            }
            this.resultList.clear();
        }
        double sumacc = (1.0* acc) / testList.size() * 100;
        System.out.println(sumacc);
        System.out.println("--------Training cases--------");
        System.out.println("total: " + trainingList.size());
        System.out.println("class 1: " + trainingClasses[0]);
        System.out.println("class 2: " + trainingClasses[1]);
        System.out.println("class 3: " + trainingClasses[2]);
        System.out.println("------------------------------");
        System.out.println("----------Test cases----------");
        System.out.println("total: " + testList.size());
        System.out.println("class 1: " + testClasses[0]);
        System.out.println("class 2: " + testClasses[1]);
        System.out.println("class 3: " + testClasses[2]);
        System.out.println("------------------------------");
        System.out.println("-----------Accuracy ----------");
        System.out.println("correct: " + acc);
        System.out.println("total: " + testList.size());
        System.out.println("Acurracy: " + sumacc + "%");
        System.out.println("------------------------------");
    }

    /**
     * find mode of the classes
     **/
    private int findClass(int[] array) {

        HashMap<Integer, Integer> HM = new HashMap<>();
        int max = 1;
        int temp = 0;
        for (int j : array) {

            if (HM.get(j) != null) {
                int count = HM.get(j);
                count++;
                HM.put(j, count);

                if (count > max) {
                    max = count;
                    temp = j;
                }
            } else {
                HM.put(j, 1);
            }
        }
        if (this.K == 1) {
            temp = array[0];
        } else if (temp == 0) {
            temp = (int) (Math.random() * 3 + 1);
        }
        return temp;

    }
}

/***
 *  creates a distance result from a training instance - stores the training instance class and distance from test instance
 * */
class Result {

    double distance;
    int Class;

    public int getCLASS() {
        return Class;
    }
    public Result(double distance, int Class) {
        this.distance = distance;
        this.Class = Class;

    }
}

/**
 * compares two distances together
 **/
class DistanceComparator implements Comparator<Result> {

    @Override
    public int compare(Result a, Result b) {
        return Double.compare(a.distance, b.distance);

    }
}