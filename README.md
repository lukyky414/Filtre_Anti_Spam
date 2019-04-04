# Apprentissage supervisé
# Master 1TP filtre anti-spam
F. Lauer
Le but de ce TP est de construire un filtre anti-spam basé sur le classifieur naïf de Bayes.

### 1  Rendu du TP
—  Ce TP est à réaliser en binôme.\
—  Chaque binôme devra déposer son TP sur ARCHE sous forme d’une archive ZIP (ou TAR.GZ)avant le 8 mai 2019 à 23h55(dépôt impossible passé cette date).\
—  L’archive ZIP devra contenir au minimum :\
   —  Le rapport de TP décrivant la contribution de chaque membre du binôme, la solution mise enœuvre, les choix effectués et les difficultés rencontrées. Ce rapport devra également contenir desexemples d’exécution du programme et une partie sur la validation de la solution proposée : àcombien estimez-vous la performance de votre filtre anti-spam ?\
   —  Les codes sources (incluant tous les fichiers nécessaires au bon fonctionnement du programme).Ces fichiers devront pouvoir être compilés aisément sur une machine Linux standard. L’idéal estde fournir un TP qui fonctionne suite à ces commandes :\
      `unzip TP_NOM1_NOM2.zipcd` \
      `TP_NOM1_NOM2`\
      `javac filtreAntiSpam.java`\
      `(tout autre mode de création de l’exécutable devra être décrit dans le rapport)`\
—  La note du TP prendra en compte les points suivants :\
   —  le nombre d’étapes correctement réalisées et fonctionnelles (voir section 4),\
   —  le respect des consignes de la section 5,\
   —  la qualité du rapport et la pertinence des commentaires,\
   —  la performance du classifieur évaluée sur une base de test d’environ 3000 exemples (non fournie).\
   —  l’implémentation éventuelle des améliorations de la section 6.
  
### 2  Fichiers à disposition
Vous pouvez récupérer sur ARCHE l’archiveTP.zipcontenant les fichiers suivants.\
— dictionnaire1000en.txt: un fichier texte contenant 1000 mots anglais parmi les plus cou-ramment utilisés (un mot par ligne).\
— baseapp/: un répertoirespamcontenant 500 messages de type spam et un répertoirehamconte-nant 2500 messages de type ham. Chaque message est un fichier texte nomméX.txtoùXrepré-sente l’indice du message.\
— basetest/:  un  répertoirespamcontenant  500  messages  de  type  spam  et  un  répertoirehamcontenant 500 messages de type ham.

### 3  Modélisation des messages
—  Nous considérerons les messages comme des sacs de mots, c’est-à-dire des ensembles de mots dontl’ordre n’a pas d’importance.\
—  Vous utiliserez le modèle binomial de textes conduisant à une représentation sous forme de vecteursbinaires de taille fixe où seule la présence des mots est prise en compte.

### 4  Etapes du TP à réaliser
Vous devrez implémenter un filtre anti-spam basé sur le classifieur naïf de Bayes. Les grandes étapes decette implémentation sont les suivantes :\
— Fonctioncharger_dictionnaire: cette fonction doit pouvoir charger un dictionnaire (parexemple dans un tableau de mots) à partir d’un fichier texte.\
— Fonctionlire_message: cette fonction doit pouvoir lire un message (dans un fichier texte) et letraduire en une représentation sous forme de vecteur binairexà partir d’un dictionnaire.\
—Apprentissage: l’apprentissage des paramètres du classifieur naïf de Bayes à partir demspam+mhammessages. Les nombresmspametmhamserons donnés par l’utilisateur, mais l’emplacementde la base d’apprentissage pourra être figée dans le code. Vous pourrez tester votre classifieur avec,par exemple,mspam= 200etmham= 200.\
—Test: le classifieur devra pouvoir être testé sur un ensemble dem′spam+m′hammessages de teststockés  dans  2  sous-répertoiresspamethamd’un  répertoire  passé  en  argument  de  la  ligne  decommande. La programme doit alors calculer le taux d’erreur sur les spams, sur les hams et le tauxd’erreur global sur l’ensemble des exemples.Exemple d’exécution pour une base de test dans le dossier courant contenant 100 messages dansbasetest/spam/et 200 messages dansbasetest/ham:\
  `java filtreAntiSpam basetest 100 200`\
  `Combien de SPAM dans la base d’apprentissage ? 200`\
  `Combien de HAM dans la base d’apprentissage ? 200`
  
  `Apprentissage...`
  
  `Test :`\
  `SPAM numéro 0 identifié comme un SPAM`\
  `SPAM numéro 1 identifié comme un HAM ***erreur***`\
  `SPAM numéro 2 identifié comme un SPAM`\
  `...`\
  `HAM numéro 0 identifié comme un HAM`\
  `HAM numéro 1 identifié comme un HAM`\
  `HAM numéro 2 identifié comme un SPAM ***erreur***`\
  `...`
  
  `Erreur de test sur les 100 SPAM      : 24 %`\
  `Erreur de test sur les 200 HAM       : 12 %`\
  `Erreur de test globale sur 300 mails : 16 %`
  
—  Le programme doit aussi afficher pour chaque exemple de test les probabilités a posteriori des deuxcatégories, par exemple :\
  `SPAM numéro 0 : P(Y=SPAM | X=x) = 9.956780e-01, P(Y=HAM | X=x) = 4.321999e-03`\
      `=>  identifié comme un SPAM`\
  `SPAM numéro 1 :  P(Y=SPAM | X=x) = 4.956780e-01, P(Y=HAM | X=x) = 5.043220e-01`\
      `=>  identifié comme un HAM ***erreur***`
      
### 5  Consignes et indices
—  Les consignes au niveau de l’interface ne sont données qu’à titre indicatif. L’utilisation de votreprogramme peut être différente si elle est soit évidente soit documentée.\
—  Le chargement du dictionnaire doit exclure les mots de moins de 3 lettres (typiquement des mots"outils" inutiles pour la classification).\
—  Le classifieur naïf de Bayes ne nécessite pas de retenir toute la base d’apprentissage en mémoire.Vous pouvez donc parcourir la base d’apprentissage en traitant chaque mail les uns après les autreset indépendamment (en n’ouvrant qu’un fichier à la fois).\
—  La comparaison des mots du mail avec ceux du dictionnaire doit être insensible à la casse.\
—  Vous pouvez vous contenter dans un premier temps d’une version simple de la fonctionlire_messagedans laquelle les mots sont supposés tous être séparés par des espaces. Même si cela implique queles mots suivis d’un signe de ponctuation comme "house," ne seront pas reconnus comme faisantpartie du dictionnaire et seront ignorés.\
—  Utilisez des tests "if" plutôt que les formes du type(bjSP AM)xj(1−bjSP AM)1−xj(impliquant despuissances 0 et 1) pour calculer la prédiction du classifieur plus efficacement.\
—  Appliquez le lissage des paramètres avec= 1.\
—  Faites attention aux problèmes numériques lors de l’étiquetage d’un nouvel email de test. Par exemple,évitez les tests du type if (10−354<10−342) . . .\
—  Essayez de limiter l’influence de la précision de la machine sur le calcul des probabilitésa posteriori.

### 6  Améliorations (bonus)
—  Améliorez l’interface : le classifieur doit être enregistré dans un fichier pour permettre de tester unnouveau message rapidement. Par exemple :\
  `java apprend_filtre mon_classifieur baseapp 500 1000`
  
  `Apprentissage sur 500 spams et 1000 hams...`\
  `... c’est très long ...`\
  `Classifieur enregistré dans ’mon_classifieur’.`
  
  `java filtre_mail mon_classifieur message.txt`\
  `D’après ’mon_classifieur’, le message ’message.txt’ est un SPAM !`
  
—  Implémentez un apprentissage en ligne pour permettre d’améliorer l’estimation des paramètres avecun nouveau message sans devoir réaliser à nouveau un apprentissage complet. Par exemple :\
  `java apprend_filtre mon_classifieur baseapp 500 1000`
  
  `Apprentissage sur 500 spams et 1000 hams...`\
  `...`\
  `Classifieur enregistré dans ’mon_classifieur’.`
  
  `java apprend_filtre_enligne mon_classifieur newMsg.txt SPAM`
  
  `Modification du filtre ’mon_classifieur’ par apprentissagesur le SPAM ’newMsg.txt’.`
  
  `java apprend_filtre_enligne mon_classifieur newMsg2.txt HAM`
  
  `Modification du filtre ’mon_classifieur’ par apprentissagesur le HAM ’newMsg2.txt’.`
  
Attention au lissage : \
  bjSP AM(mspam) =njSP AM+mspam+ 2\
oùnjSP AMest le nombre de spams contenant le motjparmimspamau cours de l’apprentissageprécédent, et, avec un exemplexen plus :\
  bjSP AM(mspam+ 1) =njSP AM+xj+mspam+ 1 + 23\
