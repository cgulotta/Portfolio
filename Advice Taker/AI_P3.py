import re



#Full name: Charles Gulotta
#UID: 111846337
#Directory ID: cgulotta



continue_var = True
KB_singleA = []
KB_pluralA = []
SP_pairs = []
KB = []
lastFact = ("NA",0)
vowels = ('a','e','i','o','u','A','E','I','O','U')


def to_single(pstring):
    for (s,p) in SP_pairs:
        if p == pstring:
            return s
    return None


def to_plural(sstring):
    for (s,p) in SP_pairs:
        if s == sstring:
            return p
    return None

def append_to_KB(X,Y,T):
    KB1 = [i[0] for i in KB]
    idx1 = KB1.index(X)
    idx2 = KB1.index(Y)
    if (X,Y,T) not in KB[idx1][1]:
        KB[idx1][1].append((X,Y,T))
    if (X,Y,T) not in KB[idx2][1]:
        KB[idx2][1].append((X,Y,T))

while(continue_var):
    input = raw_input('=> ')
    if (input == "Bye."):
        continue_var = False
        break
    notFlag = True
    firstItem = ""
    secondItem = ""
    newItems = False
    #REGEX MATCHING
    matches = re.findall(r"(\w+)",input,flags=0)
    if "not" in matches:
        notFlag = False

    #SINGULAR
    if "is" in matches or "Is" in matches:
        if matches[0] == "Is":
            #SINGULAR QUESTION
            if (matches[1].lower() == "a") or (matches[1].lower() == "an"):
                firstItem = matches[2]
            else:
                firstItem = matches[1].title()
            secondItem = matches[-1]
            
            if (firstItem,secondItem,True) in KB_singleA:
                print "Yes."
            elif (firstItem,secondItem,False) in KB_singleA:
                print "No."
            else:
                newfact = raw_input("I'm not sure, is it? ")
                if firstItem not in (a[0] for a in SP_pairs):
                    pluralinput1 = raw_input("What's the plural form of "+firstItem+"? ")
                    SP_pairs.append((firstItem,pluralinput1))
                    KB.append((firstItem,[]))
                    KB.append((pluralinput1,[]))
                    newItems = True
                if secondItem not in (a[0] for a in SP_pairs):
                    pluralinput2 = raw_input("What's the plural form of "+secondItem+"? ")
                    SP_pairs.append((secondItem,pluralinput2))
                    KB.append((secondItem,[]))
                    KB.append((pluralinput2,[]))
                    newItems = True
                if not newItems:
                    print "Ok."
                if newfact == "yes":
                    KB_singleA.append((firstItem,secondItem,True))
                    append_to_KB(firstItem,secondItem,True)
                else:
                    KB_singleA.append((firstItem,secondItem,False))
                    append_to_KB(firstItem,secondItem,False)
        else:
            #SINGULAR STATEMENT
            if (matches[0].lower() == "a") or (matches[0].lower() == "an"):
                firstItem = matches[1]
            else:
                firstItem = matches[0].title()
            secondItem = matches[-1]
            if firstItem not in (a[0] for a in SP_pairs):
                pluralinput1 = raw_input("What's the plural form of "+firstItem+"? ")
                SP_pairs.append((firstItem,pluralinput1))
                KB.append((firstItem,[]))
                KB.append((pluralinput1,[]))
                newItems = True
            if secondItem not in (a[0] for a in SP_pairs):
                pluralinput2 = raw_input("What's the plural form of "+secondItem+"? ")
                SP_pairs.append((secondItem,pluralinput2))
                KB.append((secondItem,[]))
                KB.append((pluralinput2,[]))
                newItems = True
            if not newItems:
                print "Ok."
            if (firstItem,secondItem,notFlag) in KB_singleA:
                print "I know."
            else:
                KB_singleA.append((firstItem,secondItem,notFlag))
                append_to_KB(firstItem,secondItem,notFlag)
    #PLURAL
    elif "are" in matches or "Are" in matches:
        if matches[0].lower() == "are":
            #PLURAL QUESTION
            firstItem = matches[1].lower()
            secondItem = matches[-1].lower()
            if (firstItem,secondItem,True) in KB_pluralA:
                print "Yes."
            elif (firstItem,secondItem,False) in KB_pluralA:
                print "No."
            else:
                newfact = raw_input("I'm not sure, are they? ")
                if firstItem not in (a[1] for a in SP_pairs):
                    singleinput1 = raw_input("What's the singular form of "+firstItem+"? ")
                    SP_pairs.append((singleinput,firstItem))
                    KB.append((firstItem,[]))
                    KB.append((singleinput1,[]))
                    newItems = True
                if secondItem not in (a[1] for a in SP_pairs):
                    singleinput2 = raw_input("What's the singular form of "+secondItem+"? ")
                    SP_pairs.append((singleinput2,secondItem))
                    KB.append((secondItem,[]))
                    KB.append((singleinput2,[]))
                    newItems = True
                if not newItems:
                    print "Ok."
                if newfact == "yes":
                    KB_pluralA.append((firstItem,secondItem,True))
                    append_to_KB(firstItem,secondItem,True)
                else:
                    KB_pluralA.append((firstItem,secondItem,False))
                    append_to_KB(firstItem,secondItem,False)
        else :
            #PLURAL STATEMENT
            firstItem = matches[0].lower()
            secondItem = matches[-1].lower()
            if firstItem not in (a[1] for a in SP_pairs):
                singleinput1 = raw_input("What's the singular form of "+firstItem+"? ")
                SP_pairs.append((singleinput1,firstItem))
                KB.append((firstItem,[]))
                KB.append((singleinput1,[]))
                newItems = True
            if secondItem not in (a[1] for a in SP_pairs):
                singleinput2 = raw_input("What's the singular form of "+secondItem+"? ")
                SP_pairs.append((singleinput2,secondItem))
                KB.append((secondItem,[]))
                KB.append((singleinput2,[]))
                newItems = True
            if not newItems:
                print "Ok."
            if ((firstItem,secondItem,notFlag) in KB_pluralA):
                print "I know."
            else:
                KB_pluralA.append((firstItem,secondItem,notFlag))
                append_to_KB(firstItem,secondItem,notFlag)
                    
    elif "about" in matches:
        #INFORMATION
        item = matches[-1]
        if lastFact[0] == item:
            lastFact = (lastFact[0],lastFact[1]+1)
        else:
            lastFact=(item,0)
        KB1 = [i[0] for i in KB]
        if item in KB1:
            idx = KB1.index(item)
        else:
            print "I don't know anything about "+item+"."
            continue
        if lastFact[1] < len(KB[idx][1]):
            currFact = KB[idx][1][lastFact[1]]
            notflag = ""
            if not currFact[2]:
                notflag="not "
            if to_plural(currFact[0]) == None:
                #PLURAL
                print currFact[0].title()+" are "+notflag+currFact[1]+"."
            else:
                #SINGULAR
                if currFact[0][0].islower():
                    if currFact[0][0] in vowels:
                        s1 = "An "+currFact[0]
                    else:
                        s1 = "A "+currFact[0]
                else:
                    s1 = currFact[0]
                if currFact[1][0].islower():
                    if currFact[1][0] in vowels:
                        s2 = "an "+currFact[1]
                    else:
                        s2 = "a "+currFact[1]
                else:
                    s2 = currFact[1]
                print s1+" is "+notflag+s2+"."

        else:
            print "I don't know anything else about "+lastFact[0]+"."
                
    elif input == "Anything else?":
        #INFORMATION
        lastFact = (lastFact[0],lastFact[1]+1)
        item = lastFact[0]
        KB1 = [i[0] for i in KB]
        if item in KB1:
            idx = KB1.index(item)
        else:
            print "I don't know anything about "+item+"."
            continue
        if lastFact[1] < len(KB[idx][1]):
            currFact = KB[idx][1][lastFact[1]]
            notflag = ""
            if not currFact[2]:
                notflag="not "
            if to_plural(currFact[0]) == None:
                #PLURAL
                print currFact[0].title()+" are "+notflag+currFact[1]+"."
            else:
                #SINGULAR
                if currFact[0][0].islower():
                    if currFact[0][0] in vowels:
                        s1 = "An "+currFact[0]
                    else:
                        s1 = "A "+currFact[0]
                else:
                    s1 = currFact[0]
                if currFact[1][0].islower():
                    if currFact[1][0] in vowels:
                        s2 = "an "+currFact[1]
                    else:
                        s2 = "a "+currFact[1]
                else:
                    s2 = currFact[1]
                print s1+" is "+notflag+s2+"."
        else:
            print "I don't know anything else about "+lastFact[0]+"."
    
    #LOGIC
    #S -> P direct inference
    for (x,y,t) in KB_singleA:
        if (to_plural(x),to_plural(y),t) not in KB_pluralA:
            KB_pluralA.append((to_plural(x),to_plural(y),t))
            append_to_KB(to_plural(x),to_plural(y),t)
    #P -> S direct inference
    for (x,y,t) in KB_pluralA:
        if (to_plural(x),to_single(y),t) not in KB_singleA:
            KB_singleA.append((to_single(x),to_single(y),t))
            append_to_KB(to_single(x),to_single(y),t)

    #P -> P Transitivity
    for A in KB_pluralA:
        for B in KB_pluralA:
            if A!=B and A[1]==B[0] and A[2]==B[2] and A[2]==True:
                new = ((str(A[0]),str(B[1]),True))
                if new not in KB_pluralA:
                    KB_pluralA.append(new)
                    append_to_KB(str(A[0]),str(B[1]),True)
    #P -> S Transitivity
    for A in KB_pluralA:
        for B in KB_singleA:
            if to_single(str(A[0]))==str(B[1]) and B[2] == True:
                new = (str(B[0]),to_single(str(A[1])),A[2])
                if new not in KB_singleA:
                    KB_singleA.append(new)
                    append_to_KB(str(B[0]),to_single(str(A[1])),A[2])
        
