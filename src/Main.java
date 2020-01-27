import java.util.List;
import java.util.ArrayList;

public class Main {

    public static final double EPS = 1;
    public static final double SIGM = 3;
    public static final double T = 0.5;
    public static double energySys = 0;
    public static double delta;
    public static int r;
    public static final double TAO = 1.0;
    public static final double chemPot = -0.2;

    public static void main(String args[]) {

        byte matrix[][] = new  byte[40][40];
        //Fraction[] fractions = new Fraction[100];// массив частиц
        List<Fraction> fractions = new ArrayList<>(100);
        int[] masX =       new int[1100];
        double[] masY = new double[1100];
        int random;
        int num = 0;

        for (int i = 0; i < 40; i++) {          //заполнение решетки частицами
            for (int j = 0; j < 40; j++) {
                random = (int) (Math.random() * 13);
                if (random == 3 && num < 100) {
                    matrix[i][j] = 1;
                    fractions.add(new Fraction(i,j,(byte)1));
                    num++;
                } else
                    matrix[i][j] = 0;

            }
        }

        for(int i = 0; i < fractions.size(); i++){           //расчет начальной энергии
            energy(fractions, fractions.get(i));
            energySys += fractions.get(i).u;
        }
        energySys = (energySys/2) ;

        showMatrix(matrix);
        System.out.println(num);
        long startTime = System.currentTimeMillis();

        for(int i = 0, n = 0; i < 10000000; i++) {
            int randomp = (int)(Math.random()*4);
            if(randomp <=1) move(matrix, fractions);
            if(randomp ==2) creation(matrix, fractions);
            if(randomp ==3) annihilation(matrix, fractions);

             if(i%1000 == 0 && (i < 100000 && i>=5000 ) ){
                    n =  (i/100) -50;
                    masX[n] =  i;
                    masY[n] = energySys;
             }
            if(i%10000==0 && i >=100000 ){
                    n = 90 + i/10000;
                    masX[n] =  i;
                    masY[n] = energySys;
                System.out.println(energySys + " " + fractions.size());
            }
            //System.out.println(energySys + " " + delta + " " + r);
        }
        System.out.println("Result energy" + " " + energySys);

        for(int i = 0; i < fractions.size(); i++){
            energy(fractions, fractions.get(i));
            energySys += fractions.get(i).u;
        }
        energySys = (energySys/2) ;
        System.out.println("Result energy 1" + " " + energySys);


        new Plot().makePlot(masX, masY);
        showMatrix(matrix);
        long stopTime =      System.currentTimeMillis();
        System.out.println((stopTime - startTime)/1000);

    }

    public static List<Fraction> move(byte[][] arg, List<Fraction> zap) {

        int random = (int)(Math.random()*zap.size()); //выбор частицы
        int random1 = (int)(Math.random()*8); //выбор соседней ячейки
        int a = zap.get(random).x;
        int b = zap.get(random).y;
        Fraction coord[] = new Fraction[8];
        double energy1 =        energySys ;  //n
        double energy2;                      //n+1
        double deltaEnergy;
        int k =  0;
        r = random;

        for(int t = 0, i = (a == 0)?39:a-1; t < 3; t++, i++){     //выбор соседних ячеек
            if(i>=40) i -= 40;
            for(int p =0,  j = (b == 0)?39:b-1 ; p<3 ;p++,j++){
                if(j>=40) j -= 40;
                if(!(i ==a &&j==b)){
                    coord[k] = new Fraction(i,j,(byte)arg[i][j]);
                    k++;
                }
            }
        }


        if(coord[random1].exist == 0 ){                           //перемещение
            Fraction bufer = zap.get(random);
            arg[a][b] = 0;
            arg[coord[random1].x][coord[random1].y] = 1;
            zap.set( random,coord[random1] );

            energy(zap,zap.get(random));
            energy2 = energySys - (bufer.u/2) + (zap.get(random).u/2) ;
            if(energy1 >= energy2 ){
                energySys = energy2;
                delta = 0;
                return zap;
            }else{
                deltaEnergy = Math.exp((energy1 - energy2)/T);
                delta = deltaEnergy;
                if(Math.random() <= deltaEnergy){
                    energySys = energy2;
                    return zap;
                }else{
                    delta = 1;
                    zap.set( random,bufer );
                    arg[a][b] = 1;
                    arg[coord[random1].x][coord[random1].y] = 0;
                    return zap;
                }
            }
        }
        return zap;
    }

    public static void showMatrix(byte[][] arg) {
        int num = 0;
        for(int i=0; i<40; i++){
            for(int j=0; j<40; j++){
                num += arg[i][j];
                System.out.print(arg[i][j] + " ");
            }
            System.out.println();
        }
        for(int i =0; i<40;i++){
            System.out.print("-");
        }
        System.out.println(num);
    }

    public static void energy(List<Fraction> arg,Fraction fraction){

        double  r;     //раастояние между частицами
        double r1;     //внутри матрицы
        double r2;     //снаружи матрицы
        int     x;
        int     y;
        double u = 0;  // потенциал 6-12

        for(int i = 0; i<arg.size(); i++){
            if(arg.get(i) != fraction) {
                x = arg.get(i).x;
                y = arg.get(i).y;
                r1 = Math.sqrt(Math.pow((x - fraction.x), 2) +
                        Math.pow((y - fraction.y), 2));

                /* расчет координат для r2 */
                if(fraction.x >= 19 && fraction.y >= 19 && x < 19 && y < 19)        { x+=40; y+=40;}
                if(fraction.x >= 19 && fraction.y >= 19 && x >= 19 && y < 19)       { y+=40;}
                if(fraction.x >= 19 && fraction.y >= 19 && x < 19 && y>= 19)        { x+=40;}

                if(fraction.x < 19 && fraction.y >= 19 && x >= 19 && y < 19)        { x-=40; y+=40;}
                if(fraction.x < 19 && fraction.y >= 19 && x >= 19 && y >= 19)       { x-=40;}
                if(fraction.x < 19 && fraction.y >= 19 && x < 19 && y < 19)         { y+=40;}

                if(fraction.x >= 19 && fraction.y < 19 && x < 19 && y >= 19)        { x+=40; y-=40;}
                if(fraction.x >= 19 && fraction.y < 19 && x < 19 && y <19)          { x+=40;}
                if(fraction.x >= 19 && fraction.y < 19 && x >= 19 && y >= 19)       { y-=40;}

                if(fraction.x < 19 && fraction.y < 19 && x >= 19 && y >= 19)        { x-=40; y-=40;}
                if(fraction.x < 19 && fraction.y < 19 && x >= 19 && y < 19)         { x-=40;}
                if(fraction.x < 19 && fraction.y < 19 && x < 19 && y >= 19)         { y-=40;}


                r2 = Math.sqrt(Math.pow((fraction.x - x), 2) +
                        Math.pow((fraction.y - y), 2));

                r = Math.min(r1, r2);
                u = u + 4 * EPS * (Math.pow((SIGM / r), 12) -
                        Math.pow((SIGM / r), 6));
                //System.out.println( r1 + " " + r2+ " " + r + " " + u);

            }
        }
        fraction.u = u - chemPot*2;
    }
    public static List<Fraction> creation(byte[][] arg, List<Fraction> zap){
        int randomX = (int)(Math.random()*40);
        int randomY = (int)(Math.random()*40);
        double energy1 = energySys;
        double energy2;
        double deltaEnergy;
        if(arg[randomX][randomY] == 0 ){
            arg[randomX][randomY] = (byte) 1;
            Fraction bufer = new Fraction(randomX, randomY, (byte)1);
            zap.add(bufer);
            energy(zap, bufer);
            energy2 = energySys + (bufer.u/2);
            deltaEnergy = ((TAO*1600)/zap.size())*Math.exp((energy1 - energy2)/T);
                if(Math.random() <= deltaEnergy){
                    energySys = energy2;
                    return zap;
                }else{
                    zap.remove(bufer);
                    arg[randomX][randomY] = (byte) 0;
                    return zap;
                }
            }
        return zap;
    }

    public static List<Fraction> annihilation(byte [][] arg, List<Fraction> zap) {
        int random = (int) (Math.random() * zap.size());
        double energy1 = energySys;
        double energy2;
        double deltaEnergy;

        Fraction bufer = zap.get(random);
        arg[bufer.x][bufer.y] = 0;
        zap.remove(random);

        energy2 = energySys - (bufer.u / 2);

        deltaEnergy = (zap.size() / (TAO * 1600)) * Math.exp((energy1 - energy2) / T);
        if (Math.random() <= deltaEnergy) {
            energySys = energy2;
            return zap;
        } else {
            zap.add(bufer);
            arg[bufer.x][bufer.y] = 1;
            return zap;
        }
    }
}
