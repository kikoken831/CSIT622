import sys
import random
import math

def main():
    
    # Initial Input
    e = []
    s = []

    try:
        f1 = open(sys.argv[1], "r")
    except:
        print(f"Cannot open file {sys.argv[1]}")
        exit()

    try:
        f2 = open(sys.argv[2], "r")
    except:
        print(f"Cannot open file {sys.argv[2]}")
        exit()

    try: 
        numOfDay = int(sys.argv[3])
    except:
        print("Please enter an integer")
        exit()
    else:
        print(f"Your file {sys.argv[1]} is opened for reading")
            
        for line in f1:
            e.append(line.split(':'))

        f1.close()
        print(f"Your file has been closed")

        print(f"Your file {sys.argv[2]} is opened for reading")
        for line in f2:
            s.append(line.split(':'))
        f2.close()
        print(f"Your file has been closed\n")
        print('=============================================\n')


    events = []
    eventName = []
    eventCD = []
    eventMin = []
    eventMax = []
    eventWeight = []

    for line in e:
        for word in line:
            if word == '\n':
                continue
            else:
                events.append(word.replace('\n', ''))

    numOfEvent = int(events[0])

    events.pop(0)

    for i in range(0, len(events)):
        if i%5==0:
            eventName.append(events[i])
            eventCD.append(events[i+1])
            eventMin.append(events[i+2])
            eventMax.append(events[i+3])
            eventWeight.append(events[i+4])

    # Stats
    stats = []
    statsName = []
    mean = []
    sd = []

    for line in s:
        for word in line:
            if word == '\n':
                continue
            else:
                stats.append(word.replace('\n', ''))
    
    numOfStats = int(stats[0])

    stats.pop(0)

    for i in range(0, len(stats)):
        if i%3==0:
            statsName.append(stats[i])
            mean.append(stats[i+1])
            sd.append(stats[i+2])
    
    print('---Inconsistencies---\n')
    #check time online
    if float(eventMax[1])<0 and float(mean[1])<0:
        print('Time cannot be less than 0')

    
    if int(eventMax[1]) > 1440:
        print('Time online should not be more than 1440 minutes as there is only 1440 minutes in a day')

    
    if float(mean[1]) > float(eventMax[1]):
        print('Mean should not be more than the max value')


    for i in range (0, len(eventName)):
        if(int(eventWeight[i]) < 0):
            print(f'"{eventName[i]}" weight should be positive')

        if(eventCD[i] == "D"):
            try:
                checkInt = int(mean[i])

            except:
                print(f'"{eventName[i]}" is discrete and should contain integer value')

        elif(eventCD[i] == "C"):
            if(len(mean[i][mean[i].rfind('.')+1:])) != 2:
                print(f'"{eventName[i]}" is continuous and value should be in 2 decimal places')

    if(numOfEvent != numOfStats):
        print(f"Number of events do not tally in {sys.argv[1]} and {sys.argv[2]}")


    print('\n=============================================\n')

    # Activity Simulation Engine and the Logs
    print('---Simulation Engine and the Logs---\n')
    newdata = []

    for i in range(1, numOfDay+1):
        data = []
        filename = 'Day'+str(i)+'_log.txt'
        f = open(filename, 'w')
        print(f'{filename} has been opened for creation')
        for j in range(0, numOfEvent):
            f.write(eventName[j])
            f.write(':')
            rand = GenerateEvents(eventMax[j], eventCD[j], eventMin[j], float(mean[j])+float(sd[j]))
            data.append(rand)
            f.write(str(rand)+'\n')
        newdata.append(data)
        
        print('File created')
        f.close()

    print(f"Event generation is completed. Analysis will begin.")
    # Analysis Engine
    statsFile = "NEWSTATS.txt"
    f2 = open(statsFile, 'w')
    print(f'{statsFile} has been opened for creation')
    f2.write("Sum total of event each day" + '\n')
    for i in range (0, len(newdata)):
        sum = 0
        for j in range(len(newdata[i])):
            sum += newdata[i][j]
        string = f"Day {i+1}: {round(sum,2)}"  
        f2.write(string + '\n')
    
    baseline = []
    for i in range(numOfEvent):
        # data is grouped accordingly to event names
        baseline.append(Group(i, newdata, numOfDay, numOfEvent))

    baselineMean = []
    for i in range(len(baseline)):
        result = 0
        for j in range(numOfEvent):
            result = result + baseline[i][j]
        baselineMean.append(result/numOfEvent)

    baselineSD = []
    for i in range(len(baseline)):
        totalsum = 0
        for j in range(numOfEvent):
            totalsum = totalsum + (baseline[i][j]-baselineMean[i])**2
        variance = totalsum/numOfEvent
        baselineSD.append(math.sqrt(variance))
    
    f2.write('\n')
    f2.write("Mean of each event" + '\n')
    for i in range(0, numOfEvent):
        f2.write(eventName[i] + ': ' + str(round(baselineMean[i],2)) + '\n')

    f2.write('\n')
    f2.write("Standard deviation of each event" + '\n')
    for i in range(0, numOfEvent):
        f2.write(eventName[i] + ': ' + str(round(baselineSD[i],2)) + '\n')

    print('File created')
    f2.close()

    # Alert Engine
    total = 0
    for i in range(0, numOfEvent):
        total = total + int(eventWeight[i])
    threshold = 2*total
    print('\n=============================================\n')
    print('Threshold: ' + str(threshold))

    
    print('\n=============================================\n')
    print('---Alert Engine---\n')

    newStats = []
    loopStatsFile = True
    while loopStatsFile == True:
        newStatsFile = input("Please insert new stats file: ")
        try:
            f3 = open(newStatsFile, "r")
        except IOError:
            print ("Could not open file")
            exit()
        else:
            numOfDaysNew = int(input("Please enter number of days: "))

        for line in f3:
                newStats.append(line.split(':'))

        newS = []
        newSName = []
        newMean = []
        newSD = []

        for line in s:
            for word in line:
                if word == '\n':
                    continue
                else:
                    newS.append(word.replace('\n', ''))
        
        numOfNewS = int(newS[0])

        newS.pop(0)

        for i in range(0, len(newS)):
            if i%3==0:
                newSName.append(stats[i])
                newMean.append(stats[i+1])
                newSD.append(stats[i+2])

        newSarr = []
        for i in range(1, numOfDaysNew+1):
            count = 0
            newSdata = []
            print('Day '+str(i))
            print('Threshold: ' + str(threshold))
            for j in range(0, numOfNewS):
                newrand = GenerateEvents(eventMax[j], eventCD[j], eventMin[j], float(newMean[j])+float(newSD[j]))
                anomalyCounter = abs(((float(newrand)-float(baselineMean[j]))/float(baselineSD[j]))*int(eventWeight[j]))
                print(f'{newSName[j]:30}: (Anomaly Counter: {anomalyCounter:.2f})')
                count += anomalyCounter
                newSdata.append(newrand)
            newSarr.append(newSdata)
            print('%-30s: %.2f' % ('Total for Anomaly Counter', count))

            if (count >= threshold):
                print(f"\n>>>>>>>>>> Anomaly Detected <<<<<<<<<<")
            print('\n*********************************************\n')

        checkYN = True
        while checkYN == True:
            checkQuit = input("Do you want to quit the program? (Y/N): ")
            if(checkQuit.lower() == "y"):
                loopStatsFile = False
                checkYN = False
            elif(checkQuit.lower() == "n"):
                checkYN = False
            else:
                print("Please enter Y/N only")

def GenerateEvents(eventMax, eventCD, min, max):
    if eventMax=='':
        if eventCD=='D':
            rand = random.randint(int(min), int(max))
        else:
            rand = round(random.uniform(int(min), int(max)), 2)
    else:
        if eventCD=='D':
            rand = random.randint(int(min), int(max))
        else:
            rand = round(random.uniform(float(min), float(max)), 2)
    return rand

def Group(value, newdata, numOfDay, numOfEvent):
    result = []
    for i in range(0, numOfDay):
        for j in range(0, numOfEvent):
            if j%numOfEvent==value:
                result.append(newdata[i][j])
    return result

if __name__ == '__main__':
    main()