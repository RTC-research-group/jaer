/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.unizh.ini.jaer.chip.retina;

import java.util.Timer;
import java.util.TimerTask;
import net.sf.jaer.chip.AEChip;
import net.sf.jaer.event.EventPacket;
import net.sf.jaer.eventprocessing.EventFilter2D;
import net.sf.jaer.hardwareinterface.usb.cypressfx2.HasResettablePixelArray;

/**
 * Periodically resets pixel array to get a recalciatrant retina going.
 * @author tobi
 */
public class BadRetKicker extends EventFilter2D {

    public static String getDescription(){ return "Resets pixel array when no events are received for some time";}
    
    private int noActivityKickDelayMs = 1000;
    Timer timer = new Timer("BadRetKickerMonitor");
    KickTask kickTask=null;
    long lastEventSysTime = System.currentTimeMillis();

    public BadRetKicker(AEChip chip) {
        super(chip);
        setPropertyTooltip("noActivityKickDelayMs", "how long to wait in ms with no activity to reset pixel array");
    }

    @Override
    public EventPacket<?> filterPacket(EventPacket<?> in) {
        if (in != null && in.getSize() > 0) {
            lastEventSysTime = System.currentTimeMillis();
        }
        return in;
    }

    @Override
    public void resetFilter() {
    }

    @Override
    public void initFilter() {
    }

    @Override
    public synchronized void setFilterEnabled(boolean yes) {
        super.setFilterEnabled(yes);
        if(yes){
            if(kickTask!=null) kickTask.cancel();
            timer.schedule(kickTask=new KickTask(), noActivityKickDelayMs, noActivityKickDelayMs/10);
        }else{
            if(kickTask!=null) kickTask.cancel();
        }

    }

    /**
     * @return the noActivityKickDelayMs
     */
    public int getNoActivityKickDelayMs() {
        return noActivityKickDelayMs;
    }

    /**
     * @param noActivityKickDelayMs the noActivityKickDelayMs to set
     */
    public void setNoActivityKickDelayMs(int noActivityKickDelayMs) {
        this.noActivityKickDelayMs = noActivityKickDelayMs;
    }

    private class KickTask extends TimerTask {

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            if (now - lastEventSysTime > noActivityKickDelayMs) {
                log.info("resetting array");
                if (chip != null && chip.getHardwareInterface() != null && chip.getHardwareInterface() instanceof HasResettablePixelArray) {
                    ((HasResettablePixelArray) chip.getHardwareInterface()).resetPixelArray();
                }
            }
        }
    }
}
