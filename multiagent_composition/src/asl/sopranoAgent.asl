//Beliefs
~myturn.

//Goal
!ask.

//place note action
+!ask:		myturn
<-			.send(bassAgent, achieve, gimme);
			//.print("Asking to gimme!"); 
			wait;
.

+!ask:		~myturn <- .print("not my turn!"); !ask;.
-!ask<-		//.print("asking again..");
			!ask
.

+!place:	prevNote(X) & position(Y) & pastNotes(Z) 
				& vertNotes(W, L)[source(bassAgent)] & myturn
<-			//.print("previous note: ",X,", position: ",Y,
			//			"past notes: ",Z, "vert notes: ",W,
			//			"vert note positions: ", L);
			placeNote(W, L, Y);
			//.print("placed!");
			-vertNotes(W, L)[source(bassAgent)];
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
			//.send(bassAgent, achieve, ask);
.

+!gimme:	pastNotes(X) & pastPositions(Y)
<-			//.print("Giving my notes away...");
			.send(bassAgent, tell, vertNotes(X, Y));
			.send(bassAgent, achieve, place);
			//.print("Now my turn");
			-+myturn;
.

