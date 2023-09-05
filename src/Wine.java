public class Wine {


    public double[] getWineAttributes() {
        return wineAttributes;
    }

    private final double[] wineAttributes;
    private int CLASS;

    public Wine(double[] attributes, int CLASS){
        this.wineAttributes = attributes;
        this.CLASS = CLASS;
    }

    public void setWineAttribute(int i, double value){
        this.wineAttributes[i] = value;
    }
    public int getCLASS() {
        return CLASS;
    }

    public void attributesToString(){
        System.out.println("-------t------");
        for (int i = 0;i<13;i++){
            System.out.println(this.wineAttributes[i]);

        }
        System.out.println("-------b------");

    }
    public void setCLASS(int CLASS) {
        this.CLASS = CLASS;
    }
}
