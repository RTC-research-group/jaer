package ch.unizh.ini.jaer.chip.cochlea.gui;

import ch.unizh.ini.jaer.chip.cochlea.CochleaLPChannelConfig;
import ch.unizh.ini.jaer.config.AbstractChipControlPanel;
import ch.unizh.ini.jaer.config.AbstractMultiBitRegisterCP;
import ch.unizh.ini.jaer.config.MultiBitConfigRegister;

/**
 * Control panel for CochleaLP Channel
 *
 * @author Ilya Kiselev
 */
public class CochleaLPChannelCP extends AbstractMultiBitRegisterCP {

	/**
	 * Construct control for one channel
	 *
	 * @param chan,
	 *     the channel, or null to control all channels
	 * @param observingPanel
	 *     the panel which has the observer for the MultiBitRegister
	 */
	public CochleaLPChannelCP(final CochleaLPChannelConfig chan, final AbstractChipControlPanel observingPanel) {
		super((MultiBitConfigRegister) chan, observingPanel, new AbstractMultiBitRegisterCP.ActionsCollection(CochleaLPChannelConfig.getFieldsLengths()));

		label.setText(reg.getName());
		label.setToolTipText("<html>" + reg.toString() + "<br>" + reg.getDescription() + "<br>Enter value or use mouse wheel or arrow keys to change value.");
	}
}
