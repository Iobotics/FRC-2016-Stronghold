package org.iolani.frc.util;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Button class to implement conditional triggering. We want certain buttons (the gunner's control)
 * to only function when the system is in gunner mode. Otherwise, the gunner's buttons need to do
 * nothing, as they could disrupt the driver.
 *
 * This class allows that behavior by filtering the action of one button by another.
 * 
 * @author jmalins
 */
public class ConditionalButton extends Button {

	private final Button _filterButton;
	private final Button _controlButton;
	
	public ConditionalButton(Button filterButton, Button controlButton) {
		_filterButton  = filterButton;
		_controlButton = controlButton;
	}
	
	@Override
	public boolean get() {
		return _filterButton.get() && _controlButton.get();
	}
}
