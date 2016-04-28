package com.vilyever.popupcontroller.util;

/**
 * StateFlow
 * AndroidPopupController <com.vilyever.popupcontroller.util>
 * Created by vilyever on 2016/4/28.
 * Feature:
 */
public class StateFlow {
    final StateFlow self = this;
    
    
    /* Constructors */
    
    
    /* Public Methods */


    /* Properties */
    private State state = State.Detached;
    public StateFlow setState(State state) {
        this.state = state;
        return this;
    }
    public State getState() {
        return this.state;
    }

    
    /* Overrides */
    
    
    /* Delegates */
    
    
    /* Private Methods */

    /* Enums */
    public enum State {
        WillAttach, Attached, WillAppear, Appeared,
        WillDisappear, Disappeared, WillDetach, Detached
    }
}