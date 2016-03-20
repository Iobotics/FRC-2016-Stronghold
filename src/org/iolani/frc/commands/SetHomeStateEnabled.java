package org.iolani.frc.commands;


/**
 *  Simple command to change the home state on the OI from button
 *  triggers. This is needed because the only allowed way to respond to
 *  buttons is by running commands.
 *  
 *  @author jmalins
 */
public class SetHomeStateEnabled extends CommandBase {

	private final boolean _enabled;
  
	/**
	 * Constructor.
	 * 
	 * @param enabled - are we in the home state
	 */
    public SetHomeStateEnabled(boolean enabled) {
    	_enabled = enabled;
    }

    // set gunner mode on OI //
    protected void initialize() {
    	oi.setHomeStateEnabled(_enabled);
    }

    protected void execute() {
    }

    // terminates immediately //
    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    	this.end();
    }
}
