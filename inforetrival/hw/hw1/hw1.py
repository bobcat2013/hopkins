#!/usr/bin/python
import string
import system

# makes a list holding two lists from file
def loadDoubleList(fileName):
    lines  = open(fileName,'r').readlines()
    out = [[],[]]
    for line in lines:
        temp = line.split(',')
        if(len(temp)==2):
            out[0].append(temp[0])
            out[1].append(temp[1])
        else:
            print "ERROR: Incorrect Format"
    return out

# Perform some normalization of the text.  
# For example, remove punctuation and lower-case words. 
# Be sure to describe how you determine what constitutes a word.
def normalizeText(text):
    
    # make everything lowercase
    text = text.lower()

    # split on whitespace into an array
    words = text.split()

    # remove punction
    for ii in xrange(len(words)):
        words[ii] = words[ii].translate(None, string.punctuation)

    return words
#Report the number of ‘paragraphs’ processed;
# we'll consider each paragraph to be a 'document',
# even though all paragraphs are in a single file. 
# Also report the number of unique words observed (vocabulary size),
# and the total number of words encountered (collection size, in words). 

#o The program should calculate both the total number of times each word is seen (collection frequency of the
#word) and the number of documents which the word occurs in (document frequency of the word).

#o Identify the 30 most frequent words (by total count, also known as collection frequency) and report both the 
#collection frequency and the document frequency.

#o Also print the 100th, 500th, and 1000th
# most-frequent words and their frequencies of occurrence. (But please 
#do not turn in a printout with the top 1000.)

#o Compute and print the number of words that occur in exactly one document. (For Sense, I believe surplice 
#and simpering are such words.) What percentage of the dictionary terms occur in just one document?
