#!/usr/bin/python
# Matthew Yates
# Information Retrieval
# homework #2
import string
import sys
import pickle
import array
import struct

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

# this builds an inverted file by using an array of strings
# then building a lists of pairs of docids frequencies
# with each list stored in a hash table with the
# the unique terms as the keys
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

# this takes a dictionary which is a hashmap
# of strings to documents that the word appears in
# and the invertedFile which is a hashmap of
# term to postings list and returns a list of postings
# lists sorted by the terms alphabeticaly
# it also returns the dictionary as a hashmap
# of terms to their position in the list of 
# postings lists and the number of postings
def makeBinaryInvertedFile(dictionary,invertedFile):
    words =  dictionary.keys()
    words.sort()
    pointers = []
    dictionaryImpv = {}
    binInvtFile = []
    place =0
    for word in words:
        docLists = invertedFile[word]
        dictionaryImpv[word] = [dictionary[word],place]
        for pair in docLists:
            binInvtFile.append([int(pair[0]),int(pair[1])])
            place += 1
    return [dictionaryImpv,binInvtFile]


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
# loop till a empty paragraph is read
while(paragraph):
    paragraphCount += 1
    [dictionary,invertedFile] = buildInvFile(dictionary,invertedFile,
    normalizeText(paragraph[1]),paragraph[0])
    paragraph = getAParagrah(tagedFile)
print str(paragraphCount)+" paragraphs read in"
# convert the hashmap to a list
[dictionaryImpv,binInvtFile] = makeBinaryInvertedFile(dictionary,invertedFile)
print "deleting the temporary"
del invertedFile
del dictionary
# convert the list of posting lists
# to a single list of 2 byte ints
invertedFile = array.array('H')
for pair in binInvtFile:
    invertedFile.append(pair[0])
    invertedFile.append(pair[1])

print "saving the inverted file and dictonary"
saveobject(dictionaryImpv, "dict.bin")
saveobject(invertedFile, "invtfile.bin")

print "deleting the inverted file and dictonary"
del dictionaryImpv
del binInvtFile
del invertedFile

print "reloading the inverted file and dictonary"
binInvtFile = loadobject("invtfile.bin")
dictionary = loadobject("dict.bin")

print str(len(dictionary.keys()))+" many terms"
totalcount =0
skip = True
for num in binInvtFile:
    if(skip):
        skip = False
    else:
        skip = True
        totalcount += num

print str(totalcount)+" total terms read"

# for these print the document frequency and # of times in each document
listToPrintFreqsAndOccurances = ["archer", "blameless","rubies","iphone"]

# for these only print the frequency
onlyFreqs = ["bread", "lovingkindness", "sarah", "sing"]

# print out all the info
for word in listToPrintFreqsAndOccurances:
    if word in dictionary: 
        print word+ " is in " + str(dictionary[word][0])+" documents"
        for index in xrange(dictionary[word][1]*2,
        dictionary[word][1]*2+dictionary[word][0]*2,2):
            print( word+ " is in document #" + str(binInvtFile[index])
                    +" "+ str(binInvtFile[index+1])+" time")
    else:
        print word+ " is in 0 documents"

for word in onlyFreqs:
    if word in dictionary:
        print word+ " is in " + str(dictionary[word][0])+ " documents"
    else:
        print word+ " is in 0 documents"

