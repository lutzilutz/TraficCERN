EntranceList=["A","B","C","D","E1","E3","E4","F"]  # name of entrances
ExitList=["G","H","I","J","K","L1","L3","L4"]  # name of exits

# exits associated with each entrance
Sortie={"A":["J","H","I","K","L1","L3","L4"],
        "B":["G","J","I","K","L1","L3","L4"],
        "C":["G","H","J","K","L1","L3","L4"],
        "D":["G","H","I","J","L1","L3","L4"],
        "E1":["K","L3","G","H","I"],
        "E3":["G","H","I","K","L1","L4"],
        "E4":["G","H","I","K","L3"],
        "F":["G","H","I","K"]
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
N={"A":1828, "B":671, "C":717, "D":213, "E1":75, "E3":195, "E4":43, "F":0}

# number of car for each exit
M={"G":704, "H":142, "I":583, "J":663, "K":141, "L1":226, "L3":1172, "L4":111}

# inital guess of attractivity
beta={"G":1., "H":1., "I":1., "J":1., "K":1., "L1":1., "L3":1., "L4":1.}
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
#st="\t"
#for i in EntranceList:
    #st+="%s\t"
#print st % tuple(EntranceList)


for j in ExitList:
    #st=j
    st="{"
    tmp=[]
    s=0.
    for i in EntranceList:
        st+="%1.3f"
        if (EntranceList.index(i) == len(EntranceList)-1):
            if (ExitList.index(j) == len(ExitList)-1):
                st += "}"
            else:
                st += "},"
        else:
            st+=",\t"
        tmp.append(alpha[i].get(j,0))
        s+=N[i]*alpha[i].get(j,0)
    #st+="\t%4.2f"
    #tmp.append(s)
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
