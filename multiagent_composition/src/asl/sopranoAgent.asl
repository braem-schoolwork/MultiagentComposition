//Beliefs
~myturn.

//Goal
!place.

//place note action
+!place:	not vertNotes(_, _) & myturn
<-			.send(bassAgent, achieve, gimme);
.

+!place:	prevNote(X) & position(Y) & pastNotes(Z) 
				& vertNotes(W, L)[source(bassAgent)] & myturn
<-			//.print("previous note: ",X,", position: ",Y,
			//			"past notes: ",Z, "vert notes: ",W,
			//			"vert note positions: ", L);
			placeNote(W, L, Y);
			-vertNotes(W, L)[source(bassAgent)];
			-+~myturn;
			!place
.

+!place:	~myturn & vertNotes(W, L)[source(bassAgent)]
<-			-vertNotes(W, L)[source(bassAgent)];
			!place;
.

-!place<-	-+~myturn;
			.send(bassAgent, unachieve, place);
			!tellnotes;
			.send(bassAgent, tell, myturn);
			.send(bassAgent, achieve, place);
			!gimme;
.

+!tellnotes:pastNotes(X) & pastPositions(Y) & vertNotes(W, L)[source(bassAgent)]
<-			-vertNotes(W, L)[source(bassAgent)];
			.send(bassAgent, tell, vertNotes(X, Y));
.

+!tellnotes:pastNotes(X) & pastPositions(Y) 
<-
			.send(bassAgent, tell, vertNotes(X, Y));
.


+!gimme:	pastNotes(X) & pastPositions(Y)
<-			//.print(X);
			.send(bassAgent, tell, vertNotes(X, Y));
			-+myturn;
			.send(bassAgent, achieve, place);
			!place
.

-!gimme<-	.print("Failed gimme somehow");
.
