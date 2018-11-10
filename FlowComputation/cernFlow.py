def beta1(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M1):
    a= b1+b2+b4+b5+b6+b7
    r=float(N7)/a
    return float(M1)/r

def beta2(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M2):
    a=b1+b2+b4+b5+b6+b7
    b=b2+b3+b4+b5+b6+b7
    r=float(N4)/b + float(N5)/b + float(N6)/b + float(N7)/a
    return float(M2)/r

def beta3(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M3):
    b=b2+b3+b4+b5+b6+b7
    r=float(N4)/b + float(N5)/b + float(N6)/b
    return float(M3)/r

def beta4(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M4):
    a=b1+b2+b4+b5+b6+b7
    b=b2+b3+b4+b5+b6+b7
    c=       b4+b5+b6
    d=       b4   +b6+b7
    r=float(N2)/d + float(N3)/c + float(N4)/b + float(N5)/b + float(N6)/b + float(N7)/a
    return float(M4)/r

def beta5(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M5):
    a=b1+b2+b4+b5+b6+b7
    b=b2+b3+b4+b5+b6+b7
    c=       b4+b5+b6
    r=float(N3)/c + float(N4)/b + float(N5)/b + float(N6)/b + float(N7)/a
    return float(M5)/r

def beta6(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M6):
    a=b1+b2+b4+b5+b6+b7
    b=b2+b3+b4+b5+b6+b7
    c=       b4+b5+b6
    d=       b4   +b6+b7
    r=float(N2)/d + float(N3)/c + float(N4)/b + float(N5)/b + float(N6)/b + float(N7)/a
    return float(M6)/r

def beta7(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M7):
    a=b1+b2+b4+b5+b6+b7
    b=   b2+b3+b4+b5+b6+b7
    c=   b2   +b4+b5+b6+b7
    d=         b4   +b6+b7
    r=float(N1) + float(N2)/d + float(N4)/b + float(N5)/b + float(N6)/b + float(N7)/a
    return float(M7)/r

def initExit(N1,N2,N3,N4,N5,N6,N7):
    br1=.8;  br2=.2;  br3=.4
    br4=.6;  br5=.2;  br6=3.
    br7=1.

    a=br1+br2    +br4+br5+br6+br7
    b=    br2+br3+br4+br5+br6+br7
    c=            br4+br5+br6
    d=            br4    +br6+br7


    M1=                                                  br1*N7/a
    M2=                               br2*(N4+N5+N6)/b + br2*N7/a
    M3=                               br3*(N4+N5+N6)/b
    M4=         br4*N2/d + br4*N3/c + br4*(N4+N5+N6)/b + br4*N7/a
    M5=                    br5*N3/c + br5*(N4+N5+N6)/b + br5*N7/a
    M6=         br6*N2/d + br6*N3/c + br6*(N4+N5+N6)/b + br6*N7/a
    M7=    N1 + br7*N2/d +          + br7*(N4+N5+N6)/b + br7*N7/a
    return (M1,M2,M3,M4,M5,M6,M7)

#------------------- begin program ----------------------------------------------------------------

#-- define the number of cars for each entries. This is a test. Enter CERN data

N1=100
N2=100
N3=0
N4=963
N5=150
N6=435
N7=647

#-- define the number of cars for each exit. Enter CERN data

M1=100
M2=100
M3=0
M4=1212
M5=139
M6=448
M7=396


# this a test initilization. To be commented if M_i are known
# (M1,M2,M3,M4,M5,M6,M7)=initExit(N1,N2,N3,N4,N5,N6,N7)

# initial values for the iteration
b1=1.;  b2=1.;  b3=1.
b4=1.;  b5=1.;  b6=1.
b7=1.

count=0; delta=9999.
while delta>0.00001:
    bb1=beta1(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M1)
    bb2=beta2(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M2)
    bb3=beta3(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M3)
    bb4=beta4(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M4)
    bb5=beta5(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M5)
    bb6=beta6(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M6)
    bb7=beta7(b1,b2,b3,b4,b5,b6,b7, N1,N2,N3,N4,N5,N6,N7, M7)

    delta =(bb1-b1)**2 +(bb2-b2)**2 +(bb3-b3)**2 +(bb4-b4)**2
    delta+=(bb5-b5)**2 +(bb6-b6)**2 +(bb7-b7)**2
    print(delta)

    b1=bb1; b2=bb2; b3=bb3; b4=bb4; b5=bb5; b6=bb6; b7=bb7;
    count+=1

# ---- results
print ("number of iterations to converge=",count)
print (b1,b2,b3,b4,b5,b6,b7)

a=b1+b2    +b4+b5+b6+b7
b=   b2+b3+b4+b5+b6+b7
c=         b4+b5+b6
d=         b4   +b6+b7

print ("\n      Fraction of car from each entry to each exit\n")
print ("\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t" % ("M1","M2","M3","M4","M5","M6","M7"))
print ("%s\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t" % ("N1",0,0,0,0,0,0,1))
print ("%s\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t" % ("N2",0,0,0,b4/d,0,b6/d,b7/d))
print ("%s\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t" % ("N3",0,0,0,b4/c,b5/c,b6/c,b7/c))
print ("%s\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t" % ("N4",0,b2/b,b3/b,b4/b,b5/b,b6/b,b7/b))
print ("%s\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t" % ("N5",0,b2/b,b3/b,b4/b,b5/b,b6/b,b7/b))
print ("%s\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t" % ("N6",0,b2/b,b3/b,b4/b,b5/b,b6/b,b7/b))
print ("%s\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t%1.2f\t" % ("N7",b1/a,b2/a,0,b4/a,b5/a,b6/a,b7/a))
