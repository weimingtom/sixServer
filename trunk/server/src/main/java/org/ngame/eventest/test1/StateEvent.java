package org.ngame.eventest.test1;
// change event to broadcast
public class StateEvent {
 
  public final int oldState;
  public final int newState;
  StateEvent( int oldState, int newState ) {
    this.oldState = oldState;
    this.newState = newState;
  }
}
