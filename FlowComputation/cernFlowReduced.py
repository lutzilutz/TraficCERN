EntranceList=["N3","N4","N5","N6","N8","N9"]  # name of entrances
ExitList=["M3","M4","M5","M6","M8","M9"]  # name of exits

# exits associated with each entrance
Sortie={"N3":["M4","M5","M6","M8","M9"],
        "N4":["M3","M5","M6","M8","M9"],
        "N5":["M3","M4","M6","M8","M9"],
        "N6":["M3","M4","M5","M8","M9"],
        "N8":["M4","M5","M6","M9"],
        "N9":["M4","M5","M6","M8"]
       }

# Build the inverse relation: entrances associated with each exit
Entrance={}
for sortie in ExitList:
    Entrance[sortie]=[]
    for k in Sortie:
        if sortie in Sortie[k]: Entrance[sortie].append(k)

for k in Entrance:
    Entrance[k].sort()
    print k, Entrance[k]


def computeCoeffAlpha(EntranceList, Sortie, beta):
    alpha={}
    for i in EntranceList:
        alpha[i]={}
        denominator=0.
        for j in Sortie[i]:
            denominator+=beta[j]

        for j in Sortie[i]:
            alpha[i][j]=beta[j]/denominator
    return alpha

# compute the M[j]'s compatible with the chosen beta and N[i]'s
def initExit(ExitList, Entrance, N, beta, alpha):
    m={}
    for j in ExitList:
        m[j]=0.
        for i in Entrance[j]:
            m[j]+=N[i]*alpha[i][j]
    return m

def nextBetaValues(beta, Entrance, Sortie):

    gamma={}
    for i in Sortie:
        gamma[i]=0
        for k in Sortie[i]:
            gamma[i]+=beta[k]

    for j in Entrance:
        s=0
        for i in Entrance[j]:
            s=s+N[i]/gamma[i]

        beta[j]=M[j]/s

#------------------- begin program

#-- define the number of cars for each entries. This is a test. Enter CERN data

# number of car for each entrance
N={"N3":0, "N4":192, "N5":200, "N6":189, "N8":245, "N9":0}

# number of car for each exit
M={"M3":0,  "M4":389, "M5":206, "M6":99, "M8":132, "M9":0}

# inital guess of attractivity
beta={"M3":2.,  "M4":1., "M5":.5, "M6":1., "M8":1., "M9":1.}
alpha=computeCoeffAlpha(EntranceList, Sortie, beta)

#M=initExit(ExitList, Entrance, N, beta, alpha)
#for j in ExitList:
#    print j,M[j]


count=0;
while count<10:
    for j in ExitList:
        print beta[j],
    print "\n"
    nextBetaValues(beta, Entrance, Sortie)
    count+=1

alpha=computeCoeffAlpha(EntranceList, Sortie, beta)

# ---- results

# print M as a function of N
st="\t"
for i in EntranceList:
    st+="%s\t"
print st % tuple(EntranceList)


for j in ExitList:
    st=j
    tmp=[]
    s=0.
    for i in EntranceList:
        st+="\t%1.2f"
        tmp.append(alpha[i].get(j,0))
        s+=N[i]*alpha[i].get(j,0)
    st+="\t%4.2f"
    tmp.append(s)
    print st % tuple(tmp)

st="\t"
tmp=[]
for i in EntranceList:
    st+="%1.2f\t"
    s=0.
    for j in ExitList:
        s+=alpha[i].get(j,0)
    tmp.append(s)
print st % tuple(tmp)
