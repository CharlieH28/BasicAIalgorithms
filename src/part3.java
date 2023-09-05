import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class part3 {

    private final ArrayList<Radar> radarList = new ArrayList<>();
    private final ArrayList<Double> weights = new ArrayList<>();

    private final static int MAX = 100, featureSize = 34;
    private double bias = (Math.random());


    public part3(String filename) throws FileNotFoundException {
        readDataFiles("src/"+filename);
        Perceptron();
        //example();
    }

    public void readDataFiles(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner din = new Scanner(file);
        din.nextLine();
        while (din.hasNext()) {
            Radar radar = new Radar();
            int i = 0;
            while (i < featureSize) {
                radar.addValue(Double.parseDouble(din.next()));
                i++;

            }
            radar.setOutput(1);
            radar.setCLASS(din.next());
            radarList.add(radar);

        }

        int f = 0;
        while (f < featureSize) {
            weights.add(Math.random());
            f++;
        }
        System.out.println("there are " + radarList.size() + " instances");

    }

    public void Perceptron() {
        double localError, globalError;
        int output, iter = 0, cor;

        while (iter < MAX) {
            cor = 0;
            globalError = 0;
            int count = 0;

            for (Radar radar : radarList) {
                count++;
                //System.out.println("---------");
                //System.out.println("Radar "+count);
                output = outputCalculation(radar);
                //System.out.println("output: "+output);
                localError = radar.getCLASSNum() - output;
                //System.out.println("le: "+localError);
                //System.out.println("---------");

                double LR = 0.05;
                for (int c = 0; c < (this.weights.size()); c++) {
                    double newValue = this.weights.get(c) + (LR * localError * radar.getValues().get(c));
                    //System.out.println("pre "+this.weights.get(c));
                    //System.out.println(LR * localError * radar.getValues().get(c) );
                    //System.out.println("ones with error: "+count);
                    //System.out.println("new "+newValue);
                    this.weights.set(c, newValue);
                }
                this.bias += LR * localError;
                globalError += (localError * localError);

//            System.out.println("--------------");
//            System.out.println("output: "+output);
//            System.out.println("actual training output: "+radar.getCLASSNum());
                if (output == radar.getCLASSNum()) {
                    cor++;
                    //System.out.println("Correct!!");
                }
            }

            iter++;
            System.out.println("----------------------------");
            System.out.println("Iteration: " + iter);
            System.out.println("global error: " + globalError);
            System.out.println("RSME: " + Math.sqrt(globalError / radarList.size()));
            System.out.println("Accuracy: "+((double)cor/radarList.size()*100)+"%");
            System.out.println("----------------------------");
        }
        for (Double weight:this.weights){
            System.out.println(weight);
        }
    }

    private int outputCalculation(Radar radar) {

        double sum = 0;
        for (int v = 0; v < radar.getValues().size(); v++) {
            sum += radar.getValues().get(v) * this.weights.get(v);
        }
        sum += bias;
        //System.out.println("output sum: "+sum);
        double theta = 0;
        return (sum >= theta) ? 1 : 0;
    }

    private static class Radar {

        public ArrayList<Double> getValues() {
            return values;
        }

        private final ArrayList<Double> values = new ArrayList<>();

        public int getCLASSNum() {
            return (this.CLASS.equals("g")) ? 1 : 0;
        }

        private String CLASS;

        public Radar() {
        }

        public void addValue(double d) {
            this.values.add(d);
        }

        public void setCLASS(String c) {
            this.CLASS = c;
        }

        public void setOutput(int i) {
        }
    }
}
