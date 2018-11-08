import os
import matplotlib
import matplotlib.pyplot as plt

path = 'profileGraphs/'
os.mkdir(path)

for x in range(100):
    f1 = open('profiles/prof' + str(x) + '-velocity.txt','r') #Opens data file
    f2 = open('profiles/prof' + str(x) + '-position.txt','r')
    t1 = [word.strip() for line in f1.readlines() for word in line.split(',') if word.strip()] #Splits data into array by breaking apart at any comma
    t2 = [word.strip() for line in f2.readlines() for word in line.split(',') if word.strip()] #Splits data into array by breaking apart at any comma

    fig, ax = plt.subplots()
    tp1 = list(map(float, t1)) #Converts array of string numbers into doubles
    tp2 = list(map(float, t2))
    timeValues = []
    goalDistance = []
    maxVelocity = []

    f3 = open('profiles/keys/prof' + str(x) + '-key.txt','r')
    t3 = [word.strip() for line in f3.readlines() for word in line.split(',') if word.strip()] #Splits data into array by breaking apart at any comma
    tp3 = list(map(float, t3))

    #For every velocity value there should be a corresponding time value for when that velocity should be reached
    for index, w in enumerate(tp1):
        timeValues.append(index * 0.1)
        goalDistance.append(tp3[0])
        maxVelocity.append(tp3[1])
    ax.plot(timeValues, tp1)
    ax.plot(timeValues, tp2)
    ax.plot(timeValues, goalDistance)
    ax.plot(timeValues, maxVelocity)

    ax.set(xlabel='time (s)', ylabel='Blue: velocity (ft/s) Orange: distance (ft)', title='Trapezoidal Profile')
    ax.grid()

    fig.savefig('profileGraphs/profile' + str(x) + '.png')