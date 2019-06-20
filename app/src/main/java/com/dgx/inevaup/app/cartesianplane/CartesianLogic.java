package com.dgx.inevaup.app.cartesianplane;

import android.util.Log;

public class CartesianLogic {

    int GetFirstTextNumber(int maxnum,int minnum, int ScFactor){
        int StNumber = 65;

        for(int i = maxnum;i<minnum;i++){
            if(i%ScFactor==0){
                StNumber = i;
                break;
            }
        }

        return StNumber;

    }

    float[] getMaxandMinPoints(int ScaleMultiplier, int ScFactor, int corner, Fraccion n1, Fraccion n2, Fraccion n3, float MaxX, float MaxY){
        float[] Numbers = new float[2];

        MaxY=MaxY*-1;

        float nu1 = n1.dividirf();
        float nu2 = n2.dividirf();
        float nu3 = n3.dividirf();

        float m = (nu1*-1)/nu2;
        float b = (nu3/nu2)/ScaleMultiplier;

        //int cx1 = MaxX;
        float cy1 = (m*MaxX)+b;

        float cx2 = (MaxY+(b*-1))/m;
        //int cy2 = MaxY;

        if(corner==1 && m>0 || corner==2 && m<0){
            if(cy1<=MaxY){
                Numbers[0] = MaxX*ScFactor;
                Numbers[1] = cy1*ScFactor*-1;
            }else{
                Numbers[0] = cx2*ScFactor;
                Numbers[1] = MaxY*ScFactor*-1;
            }
        }else if(corner==1 && m<0 || corner==2 && m>0){
            if(cy1>=MaxY){
                Numbers[0] = MaxX*ScFactor;
                Numbers[1] = cy1*ScFactor*-1;
            }else{
                Numbers[0] = cx2*ScFactor;
                Numbers[1] = MaxY*ScFactor*-1;
            }
        }

        return Numbers;
    }

    float[] Solve2x2(Fraccion nu1, Fraccion nu2, Fraccion nu3, Fraccion nu4, Fraccion nu5, Fraccion nu6){
        float[] Solve = new float[2];
        //determinate
        Fraccion desho1 = Fraccion.multiplicar(nu1,nu5);
        Fraccion desho2 = Fraccion.multiplicar(nu4,nu2);
        Fraccion det = Fraccion.restar(desho1, desho2);

        //Search for x
        Fraccion xsho1 = Fraccion.multiplicar(nu3,nu5);
        Fraccion xsho2 = Fraccion.multiplicar(nu6,nu2);
        Fraccion xsho3 = Fraccion.restar(xsho1,xsho2);

        Fraccion x = Fraccion.dividir(xsho3,det);

        //Search for y
        Fraccion ysho1 = Fraccion.multiplicar(nu1,nu6);
        Fraccion ysho2 = Fraccion.multiplicar(nu4,nu3);
        Fraccion ysho3 = Fraccion.restar(ysho1,ysho2);

        Fraccion y = Fraccion.dividir(ysho3,det);

        Solve[0] = x.dividirf();
        Solve[1] = y.dividirf();

        return Solve;
    }

    public boolean isparallel(Fraccion nu1, Fraccion nu2, Fraccion nu3, Fraccion nu4, Fraccion nu5, Fraccion nu6){
        boolean isp;
        //float m = (nu1*-1)/nu2;
        Fraccion mm1 = Fraccion.multiplicar(nu1,new Fraccion(-1));
        Fraccion m1 = Fraccion.dividir(mm1,nu2);

        Fraccion mm2 = Fraccion.multiplicar(nu4,new Fraccion(-1));
        Fraccion m2 = Fraccion.dividir(mm2,nu5);

        isp = m1.dividir() == m2.dividir();

        return isp;
    }
}
