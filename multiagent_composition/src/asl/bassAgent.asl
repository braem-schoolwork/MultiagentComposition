//Beliefs
myturn.

//Goal
!place.

//place note action
+!place:	not vertNotes(_, _) & myturn
<-			.send(sopranoAgent, achieve, gimme);
.

+!place:	prevNote(X) & position(Y) & pastNotes(Z) 
				& vertNotes(W, L)[source(sopranoAgent)] & myturn
<-			//.print("previous note: ",X,", position: ",Y,
			//			"past notes: ",Z, "vert notes: ",W,
			//			"vert note positions: ", L);
			placeNote(W, L, Y);
			-vertNotes(W, L)[source(sopranoAgent)];
			-+~myturn;
			!place
.

+!place:	~myturn & vertNotes(W, L)[source(sopranoAgent)]
<-			-vertNotes(W, L)[source(sopranoAgent)];
			!place;
.

-!place<-	-+~myturn;
			.send(sopranoAgent, unachieve, place);
			!forgetVertNotes;
			!tellnotes;
			.send(sopranoAgent, tell, myturn);
			.send(sopranoAgent, achieve, place);
			!gimme;
.

+!forgetVertNotes: 	vertNotes(W, L)[source(sopranoAgent)]
<- 					-vertNotes(W, L)[source(sopranoAgent)];
.

+!tellnotes:pastNotes(X) & pastPositions(Y) 
<-
			.send(sopranoAgent, tell, vertNotes(X, Y));
.

+!gimme:	pastNotes(X) & pastPositions(Y)
<-			//.print(X);
			.send(sopranoAgent, tell, vertNotes(X, Y));
			-+myturn;
			.send(sopranoAgent, achieve, place);
			!place
.

-!gimme<-	.print("Failed gimme somehow");
.
