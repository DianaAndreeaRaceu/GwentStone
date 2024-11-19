
Clase :	-Card
	-Minion (extensie a clasei Card)
	-Diciple (extensie a clasei Minion)
	-Miraj (extensie a clasei Minion)
	-TheCursedOne (extensie a clasei Minion)
	-TheRipper (extensie a clasei Minion)
	-Hero (extensie a clasei Card)
	-EmpressThorina (extensie a clasei Hero)
	-General Kocioraw (extensie a clasei Hero)
	-KingMudface (extensie a clasei Hero)
	-LordRoyce (extensie a clasei Hero)
	-Player
	-Board
	-Statistics
	-Game
	-Main

1. CLASA CARD

	Contine: -campurile private	a)name (de tip String)
					b)mana (de tip int)
					c)description (de tip String)
					d)colors (o lista de Stringuri)
					
		 -constructorul, care initializeaza fiecare camp cu valorie
		  primite ca parametrii
		 
		 -functii get/set pentru fiecare camp
		 
		 
		 

2. CLASA MINION

	Este o extensie a clasei "Card".
	
	Contine: - campurile private 	a)health (de tip int) ->retine health-ul minionului
					
					b)attackDamage (de tip int) ->retine punctele pentru damage
					
					c)hasAttacked (de tip int) -> retine daca atacul/abilitatea
					 			      minionului a fost folosita
					
					d)isFrozen (de tip int) -> retine daca minionul este sau nu
								   inghetat
		
		 -constructorul, care initializeaza campurile din clasa Card 
		 (clasa pe care o extinde) si campurile locale health si attackDamage 
		 cu valorile primite ca paramterii
		 
		 -functii get/set pentru fiecare camp
		 
		 -urmatoarele metode:
		 	
		 	a)unfreeze() - Seteaza variabila resposabila de inghetul 
		 	  minionului la 0 (dezgheata minionul).
		 	
		 	b)freeze() - Seteaza variabila responsabila de inghetul 
		 	  minionului la 1 (ingheata minionul).
		 	
		 	c)attack() - Scade din health-ul minionului primit ca paramteru 
		 	  damage-ul minionului curent si retine atacul (seteaza variabila 
		 	  hasAttacked cu 1).
		 	
		 	d)isTank() - Verifica daca numele minionului curent este "Goliath"
		 	  sau "Warden". In caz afirmativ functia returneaza 1 (minionul 
		 	  este de tip tank), altfel returneaza 0 (minionul nu este de tip tank).
		 	
		 	e)isBackRow() - Verifica daca numele minionului curent este 
		 	  "Sentinel", "Berserker", "The Cursed One" sau "Disciple". 
		 	  In caz afirmativ, retuneaza 1 (minionul trebuie plasat pe randul 
		 	  din spate al jucatorului), altfel returneaza 0 (minionul nu trebuie 
		 	  plasat pe randul din spate al jucatorului)
		 	
		 	g)useAbility() - Metoda destinata suprascrierii pentru utilizarea abilitatii
		 	
		 	h)createMinion() - Creaza o copie a minionului detinator de abilitate in
		 	  functie de numele pe care il poarta acesta.
		 	

3. CLASA DISCIPLE	

	Este o extensie a clasei "Minion".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Minion(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Minion, aplicand abilitatea
		   minionului Disciple (mareste cu 2 health-ul minionului target)



4. CLASA MIRAJ	

	Este o extensie a clasei "Minion".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Minion(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Minion, aplicand abilitatea
		   minionului Miraj (interschimba health-ul minionului care ataca, cu cel al 
		   minionului atacat)
		   
		   
5. CLASA TheCursedOne	

	Este o extensie a clasei "Minion".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Minion(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Minion, aplicand abilitatea
		   minionului The Cursed One (interschimba health-ul minionului atacat, cu attack
		   damage-ul acestuia)
		   
		   
6. CLASA TheRipper	

	Este o extensie a clasei "Minion".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Minion(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Minion, aplicand abilitatea
		   minionului The Ripper (scade cu 2 attack damage-ul minionului atacat, sau il face 0
		   daca numarul rezultat prin scadere este negativ)


7. CLASA HERO

	Este o extensie a clasei "Card".
	
	Contine: - campurile private 	a)health (de tip int) ->retine health-ul eroului
	
					b)abilityUsed (de tip int) -> retine daca abilitatea
					 			      eroului a fost folosita
					 			      
					c)DEFAULT_HEALTH (de tip int) -> contine health-ul
					 				 initial al eroului (30)
		
		 - constructorul, care initializeaza campurile din clasa Card (clasa pe care
		   o extinde) cu valorile primite ca paramterii si campul local health cu
		   DEFAULT_HEALTH
		 
		 -functii get/set pentru health si abilityUsed
		 
		 -urmatoarele metode:
		 
		 	a)useAbility() - Metoda destinata suprascrierii pentru utilizarea abilitatii
		 	
		 	b)createHero() - Creaza o copie a eroului detinator de abilitate in
		 	  functie de numele pe care il poarta acesta.
		 	
		
		
		 	
8. CLASA EmpressThorina	

	Este o extensie a clasei "Hero".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Hero(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Hero, aplicand abilitatea
		   eroului Empress Thorina (elimina minionul cu health maxim de pe randul specificat)

		 	
9. CLASA GeneralKocioraw	

	Este o extensie a clasei "Hero".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Hero(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Hero, aplicand abilitatea
		   eroului General Kocioraw (creste cu 1 attack damage-ul tuturor minionilor de pe un rand)



10. CLASA KingMudface	

	Este o extensie a clasei "Hero".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Hero(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Hero, aplicand abilitatea
		   eroului King Mudface (creste cu 1 health-ul tuturor minionilor de pe un rand specificat)


11. CLASA LordRoyce	

	Este o extensie a clasei "Hero".
	
	Contine: - constructorul, care initializeaza campurile din clasa Card si clasa Hero(clasa pe
		   care o extinde) cu valorile primite ca paramterii
		   
		 - metoda useAbility() - suprascrie metoda din clasa Hero, aplicand abilitatea
		   eroului LordRoyce (ingheata toate cartile de pe un rand specificat)

		 	
		 	
12. CLASA PLAYER 

	Contine: - campurile private	a)mana (de tip int) -> retine mana jucatorului
	
					b)increaseMana (de tip int) -> retine cu cat creste mana
					  			       jucatorului in fiecare runda
					  
					c)LIMIT_MANA (de tip int) -> retine limita maxima de crestere
					  			     a manei per runda
					  
					d)hero (de tip Hero) -> retine eroul jucatorului
					
					e)hand (lista de Card) -> retine cartile din mana jucatorului
					
					f)deck (lista de Card) -> retine cartile din pachetul jactorului
					
					g)turnEnded (de tip int) -> retine daca a fost sau nu randul
					  			    jucatorului in runda curenta
					
		 -constructorul, care initializeaza campurile mana, hand, hero si deck cu parmetrii
		  primiti
		
		 - functii get/set pentru campuri
		 
		 - -urmatoarele metode:
		 
		 	a)addCard() - Este metoda de adaugare a primei carti din pachet in mana 
		 	 jucatorului. Daca pachetul de carti al jucatorului exista si nu e gol,
		 	 atunci adauga in lista cartilor din mana jucatorului prima carte din
		 	 pachetul de carti al jucatorului si scoate acea carte din pachet (cu
		 	 ajutorul metodei remove() din clasa Board)
		 	
		 	b)increaseMana() - Este metoda de crestere pentru mana jucatorului.
		 	  Daca increaseMana nu a atins inca limita maxima de crestere, atunci
		 	  creste cu 1, apoi se adauga la mana curenta a jucatorului.
		 	
		 	c)useHero() - Este metoda de scadere a manei jucatorului pentru utilizarea
		 	  eroului. Scade din mana jucatorului mana eroului pe care il detine.
		 	
		 	d)endTurn() - Este metoda care marcheaza incheierea rundei pentru un jucator.
		 	 (seteaza turnEnded cu 1).
		 	
		 	e)resetTurn() - Este metoda care marcheaza inceputul rundei pentru un jucator. 
		 	 (seteaza turnEnded cu 0).
		 	
		 	f)getHeroCard() - 
		 
		 
		 
13. CLASA BOARD


	Contine: - campurile private	a)minion (o martrice de minioni) -> retine tabla de joc
	
					b)ROWS (de tip int) -> retine numarul de linii al matricei
					
					c)COLUMNS (de tip int) -> retine numarul de coloane al matricei
					
					
		 -constructorul, care initializeaza matricea cu dimensiunile ROWS si COLUMNS
		
		 - functii get pentru ROWS si COLUMNS
		 
		 -urmatoarele metode: 
		 
		 	a)firstPositionFree() - Este metoda care retuneaza prima pozitie libera din
		 	  randul transmis ca pramaetru al matricii/tablei de joc. Parcurge randul si
		 	  daca nu exista carte pe pozitia curenta, retuneaza coloana pe care se afla.
		 	  Daca dupa pargurgerea intregului rand nu a retunat nimic, retuneaza -1, in
		 	  semn ca nu exista pozitie libera pe randul respectiv.
		 	
		 	b)empltyRow() - Este metoda care verifica daca randul transmis ca parametru
		 	  este sau nu gol. Parcurge randul si daca exista carte pe pozitia respectiva
		 	  returneaza 0, in semn ca randul nu este gol. Daca se incheie parcurgerea si
		 	  nu a gasit nicio carte, returneaza 1, adica randul este gol.
		 	
		 	c)addMinion() - Este metoda care adauga o carte pe masa de joc. Daca exista o
		 	  pozitie libera pe randul transmis ca parametru, adauga pe prima pozitie libera
		 	  a randului cartea data de parametru.
		 	
		 	d)removeMinion() - Este metoda care scoate o carte de pe masa de joc de la o
		 	  pozitie specificata. Egaleaza elementul de pe pozitia transmisa ca parametru
		 	  cu null, parcurge randul pana la penultima pozitie, egaland elementul curent
		 	  cu elementul de pe pozitia urmatoare, astfel shiftand elementele spre stanga.
		 	  Ultimul element este, de asemenea egalat cu null, valoarea lui fiind deja mutata
		 	  cu o pozitie spre stanga.
		 	
		 	e)resetHasAttackedForAllMinions() - Este metoda care reseteaza variabila
		 	  responsabila cu utilizarea atacului/abilitatii tuturor minionilor de pe masa de
		 	  joc. Parcurge toata matricea si pentru fiecare carte, daca exista, se seteaza
		 	  variabila la 0.
		 	
		 	f)getCard() - Este metoda care returneaza cartea de la pozitia transmisa ca
		 	  parametru de pe tabla de joc.
		 	
		 	g)getMaxHealthCard() - Este metoda care retuneaza coloana pe care se afla minionul
		 	  cu cel mai mare health de pe randul specificat de parametru. Daca randul este gol
		 	  returneaza -1 (nu exista minion cu health maxim), altfel parcurge tot randul si
		 	  compara health-ul minionului curent cu maximul gasit pana in momentul respectiv.
		 	  Astfel, daca health-ul este mai mare, maximul gasit se modifica si se memoreaza
		 	  coloana pe care se afla minionul respectiv. 
		 	
		 	h)getCardsOnBoard() - Această funcție generează un obiect JSON care reprezintă starea
		 	  actuală a tablei de joc, incluzând toate cărțile plasate pe aceasta. Fiecare carte
		 	  este descrisă prin atributele sale, cum ar fi mana, atacul, sănătatea, culorile, 
		 	  descrierea și numele. Parcurge tabla de joc si pentru fiecare pozitie, daca exista
		 	  o carte, creaza un nod JSON, care include toate detaliile despre acea carte si adauga 
		 	  nodul in randul din JSON corespunzator.Adauga fiecare rand completat intr-un array 
		 	  (reprezinta board-ul).
		 	
		 	i)getFreezedOnBoard() - Această funcție generează un obiect JSON care reprezintă cartile 
		 	  inghetate de pe board, incluzând toate cărțile plasate pe aceasta. Fiecare carte
		 	  este descrisă prin atributele sale, cum ar fi mana, atacul, sănătatea, culorile, 
		 	  descrierea și numele. Parcurge tabla de joc si pentru fiecare pozitie, daca exista
		 	  o carte inghetata, creaza un nod JSON, care include toate detaliile despre acea carte si adauga 
		 	  nodul in randul din JSON corespunzator.
		 	
		 	
		 	
		 	
14. CLASA STATISTICS


	Contine: - campurile private	a)wins1 (de tip int) -> retine castigurile
							        jucatorului 1
							        
					b)wins2 (de tip int) -> retine castigurile
							 	jucatorului 2
							 	
					c)games (de tip int) -> retine numarul de jocuri
					
					
		 -constructorul, care initializeaza toate cele trei campuri cu 0
		 
		 -urmatoarele metode: 
		 
		 	a)increaseGames() - Metoda pentru cresterea cu 1 a variabilei care se ocupa
		 	  de retinerea numarului de jocuri.
		 	
		 	b)increaseWins() - Metoda care se ocupa de cresterea scorului jucatorului
		 	  castigator. Daca indexul primit ca parametru este 1, punctul se acorda
		 	  jucatorului 1, altfel se acorda jucatorului 2.
		 	
		 	c)getTotalGamesPlayedJson() - Această funcție generează un obiect JSON 
		 	  care indică numărul total de jocuri jucate. JSON-ul include comanda 
		 	  "getTotalGamesPlayed" și numărul total de jocuri ca valoare sub cheia 
		 	  "output".
		 	
		 	d)getPlayerWinsJson() - Aceasta functie creeaza un obiect JSON care afiseaza 
		 	  numarul total de victorii ale unui jucător specific. Pe baza indexului 
		 	  jucatorului (playerIdx), functia returnează comanda "getPlayerOneWins" sau 
		 	  "getPlayerTwoWins", in functie de cine este castigatorul, impreună cu numarul 
		 	  de victorii al jucatorului respectiv in cheia "output".
		 	
		 	
		 	
15. CLASA GAME


	Contine: - campurile private	a)player1 (de tip Player) -> retine jucatorul 1
	
					b)player2 (de tip Player) -> retine jucatorul 2
					
					c)currentPlayer (de tip int) -> retine jucatorul curent
					
					d)board (de tip Board) -> retine tabla de joc
					
					e)gameStatistics(de tip Statistics)-> retine statisticile
		
		 -constructorul, care initializeaza player1, player2, board si gameStatistics
		  cu valorile primite ca parametrii
		 
		 -urmatoarele metode: 
		 
		 	a)setCurrentPlayer() - Setter pentru variabila currentPlayer
		 	
		 	b)getCurrentPlayer() - Metoda care returneaza jucatorul curent in functie
		 	  de numarul continut de currentPlayer(1/2).
		 	
		 	c)unfreezeCardsPlayer1() - Este metoda care dezgheata cartile jucatorului 1.
		 	  Parcurge a doua jumatate a tablei de joc (cea care apartine jucatorului 1)
		 	  si, daca la pozitia respectiva gaseste o carte inghetata, o dezgheata folosind
		 	  functia unfreeze() din clasa Minion.
		 	
		 	d)unfreezeCardsPlayer2() - Este metoda care dezgheata cartile jucatorului 2.
		 	  Parcurge prima jumatate a tablei de joc (cea care apartine jucatorului 2) si,
		 	  daca la pozitia respectiva gaseste o carte inghetata, o dezgheata folosind 
		 	  functia unfreeze() din clasa Minion.	
		 	
		 	e)switchPlayer() - Este metoda pentru schimbarea jucatorului curent. Daca 
		 	  jucatorul curent este 1, acesta va fi schimbat cu 2, altfel va fi schimbat cu 1.
		 	
		 	f)startNextRound() - Este metoda care pregateste urmatoarea runda. Reseteaza 
		 	  pentru ambii jucatori variabila responsabila pentru tura fiecaruia (o egaleaza
		 	  cu 0, cu ajutorul functiei resetTurn() din clasa Player), creste mana fiecarui 
		 	  jucator (cu ajutorul functiei increaseMana() din clasa Player), adauga cate o 
		 	  carte in mana fiecarui jucator (cu ajutorul functiei addCard() din clasa Player), 
		 	  reseteaza variabila responsabila pentru retinerea utilizarii atacului/abilitatii 
		 	  unui minion pentru toate cartile aflate pe masa de joc (cu ajutorul functiei 
		 	  resetHasAttackedForAllMinions() din clasa Board), apoi seteaza utilizarea 
		 	  abilitatii eroului fiecarui jucator la 0, adica nefolosita (cu ajutorul metodei 
		 	  setAbilityUsed() din clasa Hero).
		 	
		 	g)endTurn() - Este metoda care se ocupa finalizarea rundei pentru jucatorul curent. 
		 	  Marcheaza aceasta finalizare apeland functia endTurn() din clasa Player pentru 
		 	  jucatorul curent. Dezgheata cartile de pe tabla care apartin jucatorului curent, 
		 	  utilizand unfreezeCardsPlayer1()/unfreezeCardsPlayer2(). Daca s-a incheiat deja 
		 	  runda si pentru celalat jucator, incepe o noua runda, apeland functia startNextRound(). 
		 	  In final se schimba jucatorul curent cu ajutorul metodei switchPlayer().
		 	
		 	h)detRowForCard() - Este metoda folosita pentru a determina randul pe care trebuie 
		 	  plasata o anumita carte a unui jucator pe tabla de joc. Daca jucatorul care detine 
		 	  cartea este jucatorul 1, atunci daca minionul primit ca parametru trebuie plasat pe 
		 	  randul din spate (verificare facuta cu ajutorul metodei isBackRow() din clasa Minion) 
		 	  va returna randul 3 (randul din spate al jucatorului 1), altfel va returna 2 (randul 
		 	  din fata al jucatorului 2). Daca jucatorul dat de parametru nu este jucatorul 1 (ci 
		 	  este jucatorul 2), in cazul in care minionul trebuie plasat pe randul din spte, va 
		 	  returna 0 (randul din spate al jucatorului 2), altfel va returna 1 (randul din fata 
		 	  al jucatorului 2).
		 	
		 	i)placeCard() - Aceasta metoda plaseaza o carte pe tabla de joc, verificand validitatea 
		 	  operației. Functia primeste indexul cartii din mana si verifica daca indexul este 
		 	  valid, daca jucatorul are suficienta mana si daca exista spatiu pe randul corespunzator. 
		 	  In caz de eroare, functia returneaza un obiect JSON care contine comanda executata, 
		 	  indexul cartii si un mesaj de eroare descriptiv. Daca toate verificarile sunt trecute, 
		 	  functia actualizeaza starea jocului, scade mana corespunzatoare, plaseaza cartea pe 
		 	  randul specificat (prin functia addMinion() din clasa Board) si o elimina din lista 
		 	  cu cartile din mana jucatorului (cu ajutorul functiei remove()).
		 	
		 	j)tankInEnemy() - Este metoda care verifica daca exista o carte de tipul tank pe 
		 	  randurile adversarului. Daca randul vizat al adversarului primit ca parametru se 
		 	  afla in prima jumatate a matricei (partea celui de-al diolea jucator), parcurge acea 
		 	  jumatate si verifica pentru fiecare pozitie daca exista o carte si daca este de tip 
		 	  tank (cu ajutorul metodei isTank() din clasa Minion). In caz afirmativ returneaza 1 
		 	  (exista carti/carte de tip tank). In schimb, daca randul vizat se afla in a doua 
		 	  jumatate a matricii (partea primului jucator), parcurge a doua jumatate a tablei de 
		 	  joc si se fac aceleasi verificari ca si pentru prima jumatate. Daca nu se gaseste nicio 
		 	  carte de tip tank in parcurgere, se va returna 0.
		 	
		 	k)cardAttack() -
		 	
		 	l)useCardAbility() -
		 	
		 	m)attackHero() - 
		 	
		 	n)gameOver() - Este metoda care marcheaza sfarsitul jocului si se ocupa de statistici, 
		 	  primind ca parametru jucatorul castigator. Creste numarul de jocuri jucate si in functie 
		 	  de cine este jucatorul castigator, creste si variabila care contorizeaza castigurile unui 
		 	  anumit jucator cu 1.
		 	
		 	o)enemyRow() - Este metoda care verifica daca un anumit rand este un rand inamic pentru 
		 	  un jucator primit ca parametru. Daca jucatorul este jucatorul 1 si randul este 0 sau 1 
		 	  (randurile jucatorului 2), atunci returneaza 1 (este un rand inamic), altfel, retuneaza 
		 	  0 (nu este un rand inamic). Insa, daca jucatirul nu este jucatorul 1 si randul este 2 
		 	  sau 3 (randurile jucatorului 1), atunci returneaza 1 (este rand inamic), altfel retuneaza 
		 	  0 (nu e rand inamic).
		 	
		 	p)useHeroAbility() - 
		 	
		 	q)getCardsInHand() -
		 	
		 	r)getPlayerDeck() - 
		 	
		 	s)getCardsOnTable() - 
		 	
		 	t)getPlayerTurn() -
		 	
		 	u)getPlayerHero() - 
		 	
		 	v)getCardAtPosition() - 
		 	
		 	w)getPlayerMana() -
		 	
		 	x)getFrozenCardsOnTable() - 
		 	

16. CLASA MAIN


	Metode:	-action() - 
	
		-buildDeck() - 		 	
		 	
