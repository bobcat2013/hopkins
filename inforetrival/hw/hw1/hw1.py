#!/usr/bin/python
import string
import sys

# makes a list holding two lists from file
def loadDoubleList(fileName):
    lines  = open(fileName,'r').readlines()
    out = [[],[]]
    for line in lines:
        line = line.strip()
        temp = line.split(',')
        if(len(temp)==2):
            out[0].append(temp[0])
            out[1].append(temp[1])
        else:
            print "ERROR: Incorrect Format"
    return out

# pulls a taged paragraph's text
# and the id 
# it is in the format [id,text]
def getAParagrah(inFile):
    firstLine = inFile.readline()
    if not firstLine:
        return False
    while(not firstLine.startswith('<P ID=')):
        firstLine = inFile.readline()
        if not firstLine:
            return False
    parts = firstLine.split('=')
    idnum = int(parts[1].split('>')[0])
    newLine = inFile.readline().strip()
    if not newLine:
        return False
    outText = ""
    while(not newLine.startswith('</P>')):
        outText += " "+newLine 
        newLine = inFile.readline()
        if not newLine:
            break;
        newLine = newLine.strip()
    return [idnum,outText]


# Perform some normalization of the text.  
# For example, remove punctuation and lower-case words. 
# Be sure to describe how you determine what constitutes a word.
def normalizeText(text):
    # make everything lowercase
    text = text.lower()
    # remove contractions by looping through a list of english contractions 
    # and their replacements
    listsOfcontractionTranslations = loadDoubleList("listOfContractions.txt")
    for trans in listsOfcontractionTranslations:
        string.replace(text,trans[0],trans[1])
    # remove the 's
    text = string.replace(text,"'s"," ")
    # remove any remaining punctionation
    for punct in string.punctuation:
        text = string.replace(text,punct," ")
    # split on whitespace into an array
    words = text.split()
    # return the array
    return words





#Report the number of 'paragraphs' processed;
# we'll consider each paragraph to be a 'document',
# even though all paragraphs are in a single file. 
# Also report the number of unique words observed (vocabulary size),
# and the total number of words encountered (collection size, in words). 

#o The program should calculate both the total number of times each word is seen (collection frequency of the
#word) and the number of documents which the word occurs in (document frequency of the word).

def calculateText(wordChart,wordArray):
    wordsSeen = []
    for word in wordArray:
        if word in wordsSeen:
            firstTimeSeeingWordThisDoc = False
        else:
            firstTimeSeeingWordThisDoc = True
            wordsSeen.append(word)
        if word in wordChart:
            wordChart[word][0] += 1
            if firstTimeSeeingWordThisDoc:
                wordChart[word][1] += 1
        else:
            wordChart[word]=[1,1]
    return wordChart

def printFormated(sortedList,number):
    if number < len(sortedList):
         print sortedList[number][0]+"\t\t"+str(number)+"\t\t"+str(sortedList[number][1][0])+"\t\t"+str(sortedList[number][1][1])

def makeColData(sortedList,number):
    data = []
    data.append(sortedList[number][0])
    data.append(str(number))
    data.append(str(sortedList[number][1][0]))
    data.append(str(sortedList[number][1][1]))
    return data

def makeColDataRange(sortedList,number):
    data = []
    for ii in xrange(number):
        temp =[]
        temp.append(sortedList[ii][0])
        temp.append(str(ii))
        temp.append(str(sortedList[ii][1][0]))
        temp.append(str(sortedList[ii][1][1]))
        data.append(temp)
    return data

def printCols(data):
    col_width = max(len(word) for row in data for word in row) + 2  # padding
    for row in data:
        print "".join(word.ljust(col_width) for word in row)

# Identify the 30 most frequent words (by total count, also known as collection frequency) and report both the 
# collection frequency and the document frequency.

# Also print the 100th, 500th, and 1000th
# most-frequent words and their frequencies of occurrence. (But please 
# do not turn in a printout with the top 1000.)

# Compute and print the number of words that occur in exactly one document. (For Sense, I believe surplice 
# and simpering are such words.) What percentage of the dictionary terms occur in just one document?
def sortLists(inlist):
    ii = 0
    outList = []
    while ii < len(inlist):
        jj = ii
        best = jj
        while jj < len(inlist):
            if inlist[jj][1][0] > inlist[best][1][0]:
                best = jj
            jj += 1
        outList.append(inlist[best])
        inlist[best] =  inlist[ii]
        ii += 1
    return outList

def sort(x):
    out = []
    first = True
    keys = x.keys()
    if len(keys) == 0:
        return []
    if len(keys) == 1:
        return [[keys[0],x[keys[0]]]]
    out.append([keys[0],x[keys.pop(0)]])
    for key in keys:
        notBeenAdded = True
        for ii in xrange(len(out)):
            if x[keys[ii]][0] > out[ii][1][0]:
                notBeenAdded = False
                out.insert(ii,[keys[ii],x[keys[ii]]])
        if notBeenAdded:
            out.append([keys[ii],x[keys[ii]]])
    return out

def combineList(olddata,newdata):
    for ii in xrange(len(newdata[0])):
        olddata[0].append(newdata[0][ii])
        olddata[1].append(newdata[1][ii])
        olddata[2].append(newdata[2][ii])
        olddata[3].append(newdata[3][ii])
    return olddata

if len(sys.argv) != 2:
    print "Incorrect number of arguments"
    print "Correct use:"
    print "./hw1.py fileToRead.txt"
    sys.exit()

tagedFile = open(sys.argv[1])
paragraph = getAParagrah(tagedFile)
paragraphCount = 0
wordDict = {}
while(paragraph):
    if paragraph:
        wordArray  = normalizeText(paragraph[1])
        paragraphCount += 1
        wordDict = calculateText(wordDict, wordArray)
    paragraph = getAParagrah(tagedFile)
templist = []
for key in wordDict.keys():
     templist.append([key,wordDict[key]])
sortedList = sortLists(templist)
#sortedList = sort(wordDict)
totalwords = 0
#wordsThatAppearOnlyOnce = []
numberWordsThatAppearOnlyOnce = 0
for wordpair in sortedList:
    totalwords += wordpair[1][0]
    if wordpair[1][1] == 1:
        numberWordsThatAppearOnlyOnce += 1
#        wordsThatAppearOnlyOnce.append(wordpair[0])

print "Nunber of documents " + str(paragraphCount)
print "Number of unique words seen " + str( len(wordDict.keys()))
print "Number of words seen " + str(totalwords)
print "Number of words that only appear in one document " + str(numberWordsThatAppearOnlyOnce)
print ("Percent of words that only appear in one document " +
str((numberWordsThatAppearOnlyOnce*1.0/(1.0*len(wordDict.keys())))*100.0)+" %")
data = [["Word","Rank","Frequency","# of Docs"]]
data.extend(makeColDataRange(sortedList,30))
data.append(makeColData(sortedList,100))
data.append(makeColData(sortedList,500))
data.append(makeColData(sortedList,1000))
printCols(data)
#for wordThatAppearOnlyOnce in wordsThatAppearOnlyOnce:
#    print wordThatAppearOnlyOnce
