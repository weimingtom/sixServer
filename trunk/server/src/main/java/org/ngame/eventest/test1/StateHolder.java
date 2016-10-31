package org.ngame.eventest.test1;

import java.util.HashSet;
import java.util.Set;

public class StateHolder {
  private int state;
 
  public int getState() {
    return state;
  }
 
  public void setState( int state ) {
	  int oldState = this.state;
	  this.state = state;
	  if( oldState != state ) {
	    broadcast( new StateEvent( oldState, state ) );
	  }
  }
  private void broadcast( StateEvent stateEvent ) {
	  for( StateListener listener : listeners ) {
	    listener.stateChanged( stateEvent );
	  }
	}
  
  
  
  private final Set<StateListener> listeners = new HashSet<>();
  public void addStateListener( StateListener listener ) {
    listeners.add( listener );
  }
  public void removeStateListener( StateListener listener ) {
    listeners.remove( listener );
  }
}