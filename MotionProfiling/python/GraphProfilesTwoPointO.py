import os
import matplotlib
import matplotlib.pyplot as plt

path = 'profileGraphs/'
os.mkdir(path)

for x in range(100):
    profileFile = open('profiles/prof' + str(x) + '.txt','r')
    #keyFile = open('motor-key.txt','r')

    points = [word.strip() for line in profileFile.readlines() for word in line.split(',') if word.strip()]
    seperateTypes = [word.strip() for line in points for word in line.split('-') if word.strip()]
    positions = []
    velocities = []
    for index, item in enumerate(seperateTypes, start=0):
        if index % 2 == 0:
            positions.append(seperateTypes[index])
        else:
            velocities.append(seperateTypes[index])

    seperatePositions = [word.strip() for line in positions for word in line.split(':') if word.strip()]
    seperateVelocities = [word.strip() for line in velocities for word in line.split(':') if word.strip()]
    desiredPositions = []
    simulatedPositions = []
    desiredVelocities = []
    simulatedVelocities = []
    for index, item in enumerate(seperatePositions, start=0):
        if index % 2 == 0:
            desiredPositions.append(seperatePositions[index])
            #desiredVelocities.append(seperateVelocities[index])
        else:
            simulatedPositions.append(seperatePositions[index])
            #simulatedVelocities.append(seperateVelocities[index])
    for index, item in enumerate(seperateVelocities, start=0):
        if index % 2 == 0:
            #desiredPositions.append(seperatePositions[index])
            desiredVelocities.append(seperateVelocities[index])
        else:
            #simulatedPositions.append(seperatePositions[index])
            simulatedVelocities.append(seperateVelocities[index])

    figPos, axPos = plt.subplots()
    figVel, axVel = plt.subplots()
    tpDP = list(map(float, desiredPositions)) #Converts array of string numbers into doubles
    tpSP = list(map(float, simulatedPositions)) #Converts array of string numbers into doubles
    tpDV = list(map(float, desiredVelocities)) #Converts array of string numbers into doubles
    tpSV = list(map(float, simulatedVelocities)) #Converts array of string numbers into doubles

    timeValues = []
    #For every velocity value there should be a corresponding time value for when that velocity should be reached
    for index, w in enumerate(tpDP):
        timeValues.append(index * 0.1)
    axPos.plot(timeValues, tpDP)
    axPos.plot(timeValues, tpSP)
    axVel.plot(timeValues, tpDV)
    axVel.plot(timeValues, tpSV)

    axPos.set(xlabel='time (s)', ylabel='position (ft)', title='Trapezoidal Profile')
    axVel.set(xlabel='time (s)', ylabel='velocity (ft/s)', title='Trapezoidal Profile')
    axPos.grid()
    axVel.grid()

    figPos.savefig('profileGraphs/prof' + str(x) + '-pos.png')
    figVel.savefig('profileGraphs/prof' + str(x) + '-vel.png')