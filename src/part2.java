import java.io.File;
import java.io.IOException;
import java.util.*;

public class part2 {

    /*
     *   Decision Trees
     *   Algorithm
     *   impurity
     *
     *
     * */
    private ArrayList<String> attributeNames;
    private final ArrayList<String> temp;
    private final Set<Instance> trainingInstances;
    private int correct;

    public part2(String trainingName,String testName) {
        this.trainingInstances = readDataFile("src/part2/"+trainingName);
        this.temp = this.attributeNames;
        Set<Instance> testInstances = readDataFile("src/part2/"+testName);
        Node root = DecisionTree(this.trainingInstances, this.attributeNames);
        outputDT(root);

        for (Instance instance : testInstances) {
            DTClassifier(root, instance);
        }
        System.out.println("-----------------------");
        System.out.println("Correct cases: " + this.correct);
        System.out.println("Accuracy: " + ((double) correct / testInstances.size()) * 100);
        System.out.println("-----------------------");
    }


    /**
     * Read the data files for part 2 - helper-code
     **/
    private Set<Instance> readDataFile(String fname) {
        /* format of names file:
         * names of categories, separated by spaces
         * names of attributes
         * category followed by true's and false's for each instance
         */
        System.out.println("Reading data from file " + fname);
        try {
            Scanner din = new Scanner(new File(fname));
            this.attributeNames = new ArrayList<>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext(); ) this.attributeNames.add(s.next());
            this.attributeNames.remove(0);
            int numCategories = this.attributeNames.size();
            System.out.println(numCategories + " attribute names");

            return new HashSet<>(readInstances(din));
        } catch (IOException e) {
            throw new RuntimeException("Data File caused IO exception");
        }
    }

    /**
     * read instances for part 2 - helper-code
     **/
    private List<Instance> readInstances(Scanner din) {
        /* instance = classname and space separated attribute values */
        List<Instance> instances = new ArrayList<>();
        while (din.hasNext()) {
            Scanner line = new Scanner(din.nextLine());
            instances.add(new Instance(line.next(), line));
        }
        System.out.println("Read " + instances.size() + " instances");
        return instances;
    }
    public String findMode(Set<Instance> instances){
        int l = 0, d = 0;
        double p;
        String mode;
        for (Instance inst : instances) {
            if (inst.CLASS.equals("live")) {
                l++;
            } else {
                d++;
            }
        }


        if (d > l) {
            p = (double) d / (d + l);
            mode = "die";

        } else if (l > d) {
            p = (double) l / (d + l);
            mode = "live";
        } else {
            p = (0.5);
            mode = (Math.random() * 2 + 1 > 1) ? "live" : "die";
        }


        return mode;
    }
    /**
     * Decision Tree method
     **/
    public Node DecisionTree(Set<Instance> instances, List<String> attributes) {

        if (instances.size() == 0) {
            int l = 0, d = 0;
            double p;
            String mode;

            for (Instance inst : trainingInstances) {
                if (inst.CLASS.equals("live")) {
                    l++;
                } else {
                    d++;
                }
            }
            if (d > l) {
                p = (double) d / (d + l);
                mode = "die";

            } else if (l > d) {
                p = (double) l / (d + l);
                mode = "live";
            } else {
                p = (0.5);
                mode = (Math.random() * 2 + 1 > 1) ? "live" : "die";
            }
            Node node = new Node(findMode(trainingInstances), true, instances);
            node.setProb(p);
            return node; // contains the name and probility of the most probable class across the whole training set

        } else if (impurity(instances) <= 0) { // all are

            Node node;
            for (Instance inst : instances) {

                node = new Node(inst.CLASS, true, instances);
                node.setProb(1);
                return node;
            }

            return new Node(null, true, instances);
        } else if (attributes.size() == 0) {
            int l = 0, d = 0;
            double p;
            String mode;

            for (Instance inst : instances) {
                if (inst.CLASS.equals("live")) {
                    l++;
                } else {
                    d++;
                }
            }


            if (d > l) {
                p = (double) d / (d + l);
                mode = "die";

            } else if (l > d) {
                p = (double) l / (d + l);
                mode = "live";
            } else {
                p = (0.5);
                mode = (Math.random() * 2 + 1 > 1) ? "live" : "die";
            }

            Node node = new Node(mode, true, instances);
            node.setProb(p);
            return node;
        } else {

            double bestWeightedAvImp = 1;
            String bestAtt = null;
            Set<Instance> bestInstTrue = new HashSet<>();
            Set<Instance> bestInstFalse = new HashSet<>();


            for (String attribute : attributes) {
                // set 1 true
                Set<Instance> setTrue = new HashSet<>();
                // set 2 false
                Set<Instance> setFalse = new HashSet<>();
                for (Instance instance : instances) {
                    if (instance.getAtt(this.temp.indexOf(attribute))) {
                        // add to set 1
                        setTrue.add(instance);
                    } else {
                        // add to set 2
                        setFalse.add(instance);
                    }

                }
                double truePurity = impurity(setTrue);
                double falsePurity = impurity(setFalse);
                double weightedAvImp = (truePurity * setTrue.size() / instances.size()) + (falsePurity * setFalse.size() / instances.size());

                if (weightedAvImp < bestWeightedAvImp) {

                    bestWeightedAvImp = weightedAvImp;
                    bestAtt = attribute;
                    bestInstTrue = setTrue;
                    bestInstFalse = setFalse;
                }
            }

            System.out.println(bestAtt);
            attributes.remove(bestAtt);


            System.out.println(attributes);
            if (bestAtt ==null){return new Node(findMode(instances),true, instances);}

            return new Node(bestAtt, false, DecisionTree(bestInstTrue, attributes), DecisionTree(bestInstFalse, attributes),
                    bestInstTrue, bestInstFalse);
        }
    }

    public void DTClassifier(Node node, Instance inst) {

        if (!node.isLeaf) {
            if (inst.getAtt(temp.indexOf(node.att))) {
                DTClassifier(node.left, inst);
            } else {
                DTClassifier(node.right, inst);
            }

        } else {
            System.out.println("----------------------");
            System.out.print("Expected Class: " + inst.CLASS+" || ");
            System.out.println("Actual Class: " + node.Classname);
            System.out.println("----------------------");
            if (inst.CLASS.equals(node.Classname)) {
                correct++;
            }
        }


    }

    public void outputDT(Node node) {

        if (node.isLeaf) {
            node.reportLeaf("\t");

        } else {
            node.report("\t");
        }


    }

    /**
     * Impurity method to check for purity of a split
     **/
    public double impurity(Set<Instance> instances) {

        int sum = instances.size();
        int l = 0, d = 0;


        for (Instance inst : instances) {
            if (inst.CLASS.equals("live")) {
                l++;
            } else {
                d++;
            }
        }
        return (double) l / sum * (double) d / sum;
    }


    /**
     * Private class Node - creates a Node object for the Decision Tree
     **/

    private static class Node {

        private Node left, right;
        double prob;
        boolean isLeaf;
        String att;
        String Classname;
        Set<Instance> nodeTrueInst;
        Set<Instance> nodeFalseInst;

        public Node(String string, boolean isLeaf, Node left, Node right, Set<Instance> trueInst, Set<Instance> falseInst) {
            this.isLeaf = isLeaf;
            this.left = left;
            this.right = right;
            this.att = string;
            this.nodeTrueInst = trueInst;
            this.nodeFalseInst = falseInst;
        }

        public Node(String string, boolean isLeaf, Set<Instance> inst) {
            this.isLeaf = isLeaf;
            this.Classname = string;
            this.nodeTrueInst = inst;
            this.nodeFalseInst = inst;
        }

        public void setProb(double prob) {
            this.prob = prob;
        }

        public void report(String indent) {
            if (this.left == null || this.right == null || this.att==null) {

                this.reportLeaf(indent + "\t");
            } else {

                System.out.printf("%s%s = True:%n", indent, this.att);
                System.out.printf("%sTrue Instances= %s%n", indent, nodeTrueInst.size());
                this.left.report(indent + "\t");
                System.out.printf("%s%s = False:%n", indent, this.att);
                System.out.printf("%sFalse Instances= %s%n", indent, nodeFalseInst.size());
                this.right.report(indent + "\t");
            }
        }

        public void reportLeaf(String indent) {
            if (prob == 0) { //Error-checking
                System.out.printf("%sUnknown%n", indent);
            } else {
                System.out.printf("%sClass %s, prob=%.2f%n", indent, this.Classname, prob);
            }
        }

    }

    /**
     * Private class Instance - creates an instance object - helper-code
     **/
    private static class Instance {

        private int category;
        private final List<Boolean> vals;
        private final String CLASS;

        public Instance(String cl, Scanner s) {

            CLASS = cl;
            vals = new ArrayList<>();
            while (s.hasNextBoolean()) vals.add(s.nextBoolean());
        }

        public boolean getAtt(int index) {
            return vals.get(index);
        }

        public String toString() {
            StringBuilder ans = new StringBuilder(CLASS);
            ans.append(" ");
            for (Boolean val : vals)
                ans.append(val ? "true  " : "false ");
            return ans.toString();
        }

    }

}

