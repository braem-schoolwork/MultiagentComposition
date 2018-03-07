//Beliefs

//Goal
!place.

//place note action
+!place:	prevNote(X) & position(Y) & pastNotes(Z)
<-			.print("previous note: ",X,", position: ",Y,"past notes: ",Z)
			placeNote;
			!place
.

//cant find past note nor vertical note
+!place:	not vert_note(_) & not horiz_note(_)
<-			.println("Can't find past or vertical note")
.

//cant find past note
+!place:	not horiz_note(_) & vert_note(X)
<-			.println("Can't find past note, but found vert note")
.

//cant find vertical note
+!place:	not vert_note(_) & horiz_note(X)
<- 			.println("Can't find vert note, but found past note")
.

//found both
+!place:	vert_note(X) & horiz_note(Y)
<-			.println("Found both vert and past note")
.

//failure to place note
-!place:	true
<-			.print("failed to place note")
.