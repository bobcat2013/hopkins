#!/usr/bin/python
import string
import sys
import pickle


# saves a object to a file
# found this function from 
# http://stackoverflow.com/questions/
#                  4529815/how-to-save-an-object-in-python
def saveobject(obj, filename):
    with open(filename, 'wb') as output:
        pickle.dump(obj, output, pickle.HIGHEST_PROTOCOL)

# returns the object saved at filename
def loadobject(filename):
    return pickle.load(open(filename,'rb'))

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


def buildInvFile(dictionary,invertedFile,wordArray,docId):
    wordsSeen = []
    for word in wordArray:
        # this is for whether to incriment the 
        # number of documents seen count
        if word in wordsSeen:
            firstTimeSeeingWordThisDoc = False
        else:
            firstTimeSeeingWordThisDoc = True
            wordsSeen.append(word)
        if word in dictionary:
            if firstTimeSeeingWordThisDoc:
                dictionary[word] += 1
                invertedFile[word].append([docId,1])
            else:
                invertedFile[word][-1][1] += 1 
        else:
            dictionary[word] = 1
            invertedFile[word] = [[docId,1]]
    return [dictionary,invertedFile]

# the actual program
# first check for an argumrnt
if len(sys.argv) != 2:
    print "Incorrect number of arguments"
    print "Correct use:"
    print "./hw2.py fileToRead.txt"
    sys.exit()

# open 
tagedFile = open(sys.argv[1])

# read in the file while building the hashmap
paragraph = getAParagrah(tagedFile)
paragraphCount = 0
dictionary = {}
invertedFile = {}
while(paragraph):
    paragraphCount += 1
    [dictionary,invertedFile] = buildInvFile(dictionary,invertedFile, normalizeText(paragraph[1]),paragraph[0])
    paragraph = getAParagrah(tagedFile)
print str(paragraphCount)+" paragraphs read in"

print "saving the inverted file and dictonary"
saveobject(dictionary, "dict.bin")
saveobject(invertedFile, "invtfile.bin")
print "deleting the inverted file and dictonary"
del dictionary
del invertedFile
print "reloading the inverted file and dictonary"
dictionary = loadobject("dict.bin")
invertedFile = loadobject("invtfile.bin")

print str(len(dictionary.keys()))+" many terms"
totalcount =0
for word in dictionary.keys():
	for pair in invertedFile[word]:
		totalcount += pair[1]

print str(totalcount)+" total terms read"

# for these print the document frequency and # of times in each document
listToPrintFreqsAndOccurances = ["archer", "blameless","rubies","iphone"]

# for these only print the frequency
onlyFreqs = ["bread", "lovingkindness", "sarah", "sing"]

# print out all the info
for word in listToPrintFreqsAndOccurances:
    if word in dictionary: 
        print word+ " is in " + str(dictionary[word])+" documents"
        for pair in invertedFile[word]:
            print word+ " is in document #" + str(pair[0])+" "+ str(pair[1])+" time"
    else:
        print word+ " is in 0 documents"

for word in onlyFreqs:
    if word in dictionary:
        print word+ " is in " + str(dictionary[word])+ " documents"
    else:
        print word+ " is in 0 documents"

