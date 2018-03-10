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

+!place:	~myturn
<-			//wait(100);
			!place;
.

-!place<-	-+~myturn;
			!place;
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
