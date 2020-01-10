public class Fraction {
    public int x;
    public int y;
    public byte exist;
    public double u=0; //потенциал

    public Fraction(int arg1, int arg2, byte arg3){
        this.x =     arg1;
        this.y =     arg2;
        this.exist = arg3;
    }
    @Override
    public String toString(){
        return String.valueOf(this.x) + " " + String.valueOf(this.y) ;
    }

}
