import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn import datasets
from sklearn.cross_validation import train_test_split
from sklearn import linear_model
from sklearn.metrics import precision_score,accuracy_score,confusion_matrix,roc_auc_score,roc_curve
import sklearn.preprocessing as pp

"""
returns accuracy, precision, and Area under the ROC curve classification metrics
"""
def PRINT_METRICS(y_val, y_pred ,y_probs):
    #print("SVM (SVC) classification")
    acc = accuracy_score(y_val, y_pred)
    #print('Accurracy\t',acc)
    prec = precision_score(y_val, y_pred)
    #print('Precision\t',prec)
    prob_c1 = [el[1] for el in y_probs]
    auroc = roc_auc_score(y_val,prob_c1)
    #print('AUROC\t\t',auroc,'\n')
    return acc,prec,auroc
"""
displays a graphical ROC Curve
"""
def ROC_CURVE(y_val, y_probs):
    prob_c1 = [el[1] for el in y_probs]
    fpr, tpr, thresholds = roc_curve(y_val,prob_c1)
    auroc = roc_auc_score(y_val,prob_c1)
    plt.figure()
    plt.plot(fpr, tpr,lw=2,color='darkorange',label='ROC curve (area = %0.2f)' % auroc)
    plt.plot([0, 1], [0, 1], color='navy', lw=2, linestyle='--')
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.title('Reciever Operating Characteristic')
    plt.legend(loc="lower right")
    plt.show()

"""
opens and reads a CSV file and returns the data as a matrix (2D-array)
"""
def CSV_TO_MATRIX(fname):
    data = pd.read_csv(fname,header=None)
    return data.as_matrix()

"""
opens @fname, returns a trained DecisionTreeClassifier
"""
def train(fname):
    td = CSV_TO_MATRIX(fname)
    X = pp.scale(td[:,:-1])
    y = td[:,-1]
    C = linear_model.LogisticRegressionCV(refit='true')
    C.fit(X, y)
    return C

"""
returns a trained Decision Tree Classifier for @sub_X (a subset of X)
and @sub_y (the corresponding subset of y)
"""
def train_sub(sub_X, sub_y, p):
    C = linear_model.LogisticRegressionCV(refit='true')
    C.fit(sub_X, sub_y)
    return C

"""
using classifier @C, returns a class prediction for @row
"""
def predict(C,row):
    return C.predict(row)

"""
using classifier @C, returns a class prediction confidences for @row
"""
def predict_prob(C,row):
    return C.predict_proba(row)


#################################   MAIN   ####################################

###  IMPORT TRAINING DATA
training_matrix=CSV_TO_MATRIX('train.csv')
X = training_matrix[:,:-1]
y = training_matrix[:,-1]

###  SELECT CLASSIFIER & QUALIFY
acc_max = 0;prec_max = 0;auroc_max = 0
ts = 0.33
numseeds = 20
tota = 0;totp = 0;totr = 0
print("Evaluating classifier quality...")
for rs in range(0,numseeds):
    X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=ts, random_state=rs)
    t_class = train_sub(X_train,y_train,2)
    y_pred = []
    y_probs = []
    for X_vector in X_val:
        y_pred.append(predict(t_class,X_vector.reshape(1,-1)))
        y_probs.append(t_class.predict_proba(X_vector.reshape(1,-1)).tolist()[0])

    acc, prec, auroc = PRINT_METRICS(y_val,y_pred,y_probs)
    tota+=acc
    totp+=prec
    totr+=auroc

print("Averages over 20 random seeds with size",ts*100,"%", "\nacc:",tota/numseeds, "\nprec:", totp/numseeds, "\nauroc:",totr/numseeds)
ROC_CURVE(y_val, y_probs)

#PREDICT ON TEST DATA
probabilities_output = 'prob_4.csv'
predictions_output = 'pred_4.csv'
trainfile = 'train.csv'
testfile = 'ts4.csv'

classifier = train(trainfile)
X = CSV_TO_MATRIX(testfile)

y_pred = [];y_probs = []
for X_vector in X:
    y_pred.append(predict(classifier,X_vector.reshape(1,-1)))
    y_probs.append(predict_prob(classifier,X_vector.reshape(1,-1)).tolist()[0])

np.savetxt(predictions_output,y_pred,delimiter=',')
np.savetxt(probabilities_output,y_probs,delimiter=',')
