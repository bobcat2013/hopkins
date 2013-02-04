#!/usr/bin/python
import string
import sys

# makes a list holding two lists from file
# used to load the list of contractions
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
# returns it is in the format [id,text]
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


# This fuction perform some normalization of the text.  
# everything is lowercased then contractions are broken up
# then remaining 's are removed
# any more punctuation is replaced with a space
# then words are split on shite space
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

# the calculate text metod builds up a
# dict (what python calls a hashmap) for each 
# paragraph by looping through an area of words and 
# incrementing their counts in the hash table 
def calculateText(wordChart,wordArray):
    wordsSeen = []
    for word in wordArray:
		# this is for whether to incriment the 
		# number of documents seen count
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

# make an list of data to print
def makeColData(sortedList,number):
    data = []
    data.append(sortedList[number][0]) # the word
    data.append(str(number)) # how it ranks in terms of frequency
    data.append(str(sortedList[number][1][0])) # how often it appeared
    data.append(str(sortedList[number][1][1])) # how many documents it is in
    return data

# same as makeColData but for a range of numbers
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

# this nice print in colums trick I looked up online at
# http://stackoverflow.com/questions/9989334/create-nice-column-output-in-python
def printCols(data):
    col_width = max(len(word) for row in data for word in row) + 2  # padding
    for row in data:
        print "".join(word.ljust(col_width) for word in row)

# takes the list of data and puts it in order from greatest to
# least using insertion sort
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

# the actual program
# first check for an argumrnt
if len(sys.argv) != 2:
    print "Incorrect number of arguments"
    print "Correct use:"
    print "./hw1.py fileToRead.txt"
    sys.exit()

# open 
tagedFile = open(sys.argv[1])

# read in the file while building the hashmap
paragraph = getAParagrah(tagedFile)
paragraphCount = 0
wordDict = {}
while(paragraph):
    if paragraph:
        wordArray  = normalizeText(paragraph[1])
        paragraphCount += 1
        wordDict = calculateText(wordDict, wordArray)
    paragraph = getAParagrah(tagedFile)

# convert the hashmap to a list
templist = []
for key in wordDict.keys():
     templist.append([key,wordDict[key]])

# sort the list
sortedList = sortLists(templist)

# Calculate the total number of words and how many 
# words only appear in one document
totalwords = 0
numberWordsThatAppearOnlyOnce = 0
for wordpair in sortedList:
    totalwords += wordpair[1][0]
    if wordpair[1][1] == 1:
        numberWordsThatAppearOnlyOnce += 1

# print out all the info
print "Nunber of documents " + str(paragraphCount)
print "Number of unique words seen " + str( len(wordDict.keys()))
print "Number of words seen " + str(totalwords)
print ("Number of words that only appear in one document " 
+ str(numberWordsThatAppearOnlyOnce))
print ("Percent of words that only appear in one document " +
str((numberWordsThatAppearOnlyOnce*1.0/(1.0*len(wordDict.keys())))*100.0)+" %")
data = [["Word","Rank","Frequency","# of Docs"]]
data.extend(makeColDataRange(sortedList,30))
data.append(makeColData(sortedList,100))
data.append(makeColData(sortedList,500))
data.append(makeColData(sortedList,1000))
printCols(data)
