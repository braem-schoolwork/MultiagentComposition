//Beliefs
myturn.

//Goal
!ask.

//place note action
+!ask:	myturn
<-		.send(sopranoAgent, achieve, gimme); 
		//.print("Telling them to gimme!"); 
		wait;
.

+!ask:	~myturn <- //.print("not my turn!");
		!ask
.

-!ask<-		//.print("asking again..");
			!ask
.

+!place:	prevNote(X) & position(Y) & pastNotes(Z) 
				& vertNotes(W, L)[source(sopranoAgent)] & myturn
<-			//.print("previous note: ",X,", position: ",Y,
			//			"past notes: ",Z, "vert notes: ",W,
			//			"vert note positions: ", L);
			placeNote(W, L, Y);
			//.print("placed!");
			//wait(1000);
			-vertNotes(W, L)[source(sopranoAgent)];
			-+~myturn;
.

+!place:	myturn
<-			!place;
.

+!place:	~myturn
<-			.print("Its not my turn though");
.

-!place<-	-+~myturn;
			//.print("I failed, no longer my turn");
			//.send(sopranoAgent, achieve, ask);
.

+!gimme:	pastNotes(X) & pastPositions(Y)
<-			//.print("Giving..");
			.send(sopranoAgent, tell, vertNotes(X, Y));
			.send(sopranoAgent, achieve, place);
			//.print("Now my turn");
			-+myturn;
.

