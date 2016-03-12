package org.iolani.frc.commands;


/**
 *  Simple command to change the gunner mode state on the OI from button
 *  triggers. This is needed because the only allowed way to respond to
 *  buttons is by running commands.
 *  
 *  @author jmalins
 */
public class SetGunnerControlEnabled extends CommandBase {

	private final boolean _enabled;
  
	/**
	 * Constructor.
	 * 
	 * @param enabled - should gunner mode be enabled
	 */
    public SetGunnerControlEnabled(boolean enabled) {
    	_enabled = enabled;
    }

    // set gunner mode on OI //
    protected void initialize() {
    	oi.setGunnerControlEnabled(_enabled);
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
