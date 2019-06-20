package com.dgx.inevaup.app.cartesianplane;

// This Class is in Spanish my native language

public class Fraccion {
    private int num;
    private int den;

    private double dounu;
    private double doude;

    public Fraccion() {
        num=0;
        den=1;
    }
    public Fraccion(int x, int y) {
        num=x;
        den=y;
    }
    public Fraccion(int x){
        num=x;
        den=1;
    }

    public Fraccion(double x, double y){
        dounu=x;
        doude=y;
    }

    public Fraccion(double x){
        dounu=x;
        doude=1;
    }

    public static Fraccion sumar(Fraccion a, Fraccion b){
        a.simplificar();
        b.simplificar();
        Fraccion c=new Fraccion();
        c.num=a.num*b.den+b.num*a.den;
        c.den=a.den*b.den;
        return c.simplificar();
    }
    public static Fraccion restar(Fraccion a, Fraccion b){
        a.simplificar();
        b.simplificar();
        Fraccion c=new Fraccion();
        c.num=a.num*b.den-b.num*a.den;
        c.den=a.den*b.den;
        return c.simplificar();
    }
    public static Fraccion multiplicar(Fraccion a, Fraccion b){
        a.simplificar();
        b.simplificar();
        return new Fraccion(a.num*b.num, a.den*b.den).simplificar();
    }
    public static Fraccion multiplicar2(Fraccion a, Fraccion b , Fraccion c){
        a.simplificar();
        b.simplificar();
        c.simplificar();
        return new Fraccion(a.num*b.num*c.num, a.den*b.den*c.den).simplificar();
    }
    public static Fraccion inversa(Fraccion a){
        return new Fraccion(a.den, a.num).simplificar();
    }
    public static Fraccion dividir(Fraccion a, Fraccion b){
        return multiplicar(a, inversa(b)).simplificar();
    }
    private int mcd(){
        int u= Math.abs(num);
        int v= Math.abs(den);
        if(v==0){
            return u;
        }
        int r;
        while(v!=0){
            r=u%v;
            u=v;
            v=r;
        }
        return u;
    }
    public Fraccion simplificar(){
        int dividir=mcd();
        num/=dividir;
        den/=dividir;
        return this;
    }

    public String totext(){
        String texto;
        if(num%den==0){
            texto= String.valueOf(num/den);
        }else{
            if(den<0){
                texto=num*-1+"/"+ Math.abs(den);
            }else if(den<0 && num<0){
                texto=num*-1+"/"+den*-1;
            }else{
                texto=num+"/"+den;
            }

        }

        return texto;
    }

    public int getden(){
        return den;
    }

    public int getnum(){
        return num;
    }

    public double dividir(){
        return (double)num/den;
    }

    public float dividirf(){
        return (float)num/den;
    }

    public Fraccion abs(){
        return new Fraccion(Math.abs(num), Math.abs(den));
    }


    public String all3(){
        String signo;
        String fra = new Fraccion(num,den).abs().simplificar().totext();
        if(dividir()<0){
            signo = " - ";
        }else{
            signo = " + ";
        }
        return signo+fra;
    }

    public String all2(){
        String signo;
        String fra = new Fraccion(num,den).abs().simplificar().totext();
        if(dividir()<0){
            signo = " - ";
        }else{
            signo = " + ";
        }
        return signo+fra;
    }

    public String all(){
        return new Fraccion(num,den).simplificar().totext();
    }

    public Fraccion simred(){
        Fraccion f;
        if(num<=0 && den<=0){
            f = new Fraccion(num,den).abs().simplificar();
        }else{
            f = new Fraccion(num,den).simplificar();
        }
        return f;
    }

    public int FraInt(){
        return num/den;
    }


    public boolean si_divi(){
        boolean valor;
        valor = num % den == 0;
        return valor;
    }

    public Fraccion Xredundancy() {

        if (num <= 0 && den < 0) {
            return new Fraccion(num * -1, den * -1);
        }else{
            return new Fraccion(num , den );
        }
    }

}
