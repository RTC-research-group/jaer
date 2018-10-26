/*
 * Copyright (C) 2018 jpdominguez.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package es.us.atc.jaer.filters;

/**
 *
 * @author jpdominguez
 */
import net.sf.jaer.eventprocessing.EventFilter2D;
import net.sf.jaer.chip.AEChip;
import net.sf.jaer.event.EventPacket;
import net.sf.jaer.Description;
import net.sf.jaer.event.BasicEvent;
import net.sf.jaer.event.OutputEventIterator;
import static net.sf.jaer.eventprocessing.EventFilter.log;

@Description("This filter performs a real-time segmentation over the Neuromorphic Auditory Sensor output.")
public class NAS_RT_segmentation extends EventFilter2D {

    private int threshold = getPrefs().getInt("NAS_RT_segmentation.threshold", 1000);
    private int channels = getPrefs().getInt("NAS_RT_segmentation.channels", 32);
    private int binWidth = getPrefs().getInt("NAS_RT_segmentation.binWidth", 20);

    private final int[] buff_th_addr;
    private final int[] buff_th_ts;
    private int first_time;
    private int spikes_processed;

    private int shift;

    public NAS_RT_segmentation(AEChip chip) {
        super(chip);

        //final String parameters = "Parameters";
        setPropertyTooltip("Parameters", "channels", "Select the number of channels of the NAS.");
        setPropertyTooltip("Parameters", "binWidth", "Select the bin_width to be used in the segmentation.");
        setPropertyTooltip("Parameters", "threshold", "Select the threshold to be used in the segmentation.");

        buff_th_addr = new int[threshold];
        buff_th_ts = new int[threshold];
        first_time = 1;
        spikes_processed = 0;
    }

    @Override
    synchronized public EventPacket<?> filterPacket(EventPacket<?> in) {
        checkOutputPacketEventType(in);
        OutputEventIterator outItr = out.outputIterator();
        for (BasicEvent e : in) {
            
            try {
                BasicEvent i = (BasicEvent) e;
                   
                int ts = i.timestamp;
                int addr = i.address;

                shift = buff_th_addr[0];
                System.arraycopy(buff_th_addr, 1, buff_th_addr, 0, threshold - 1);
                buff_th_addr[threshold - 1] = shift;

                shift = buff_th_ts[0];
                System.arraycopy(buff_th_ts, 1, buff_th_ts, 0, threshold - 1);
                buff_th_ts[threshold - 1] = shift;

                buff_th_addr[threshold - 1] = addr;
                buff_th_ts[threshold - 1] = ts;

                spikes_processed++;

                if (((ts - buff_th_ts[0]) <= binWidth * 1000) && (spikes_processed >= threshold)) {
                    if (first_time == 1) {
                        for (int spk = 1; spk < threshold; spk++) {
                            i.setTimestamp(ts + binWidth * 1000);
                            BasicEvent o = (BasicEvent) outItr.nextOutput();                
                            o.copyFrom(i);
                        }
                        first_time = 0;
                    } else {
                        i.setTimestamp(ts + binWidth * 1000);
                        BasicEvent o = (BasicEvent) outItr.nextOutput();                
                        o.copyFrom(i);
                    }
                } else {
                    first_time = 1;
                    //spikes_processed = 0; <-- maybe it's interesting to add this.
                }


            } catch (Exception e1) {
                log.warning("In for-loop in filterPacket caught exception " + e1);
                e1.printStackTrace();
            }
        }
        
        return out;
    }    

     @Override
     public void resetFilter() {
     //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     @Override
     public void initFilter() {
     //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
     }

     /**
     * @return the threshold
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * @param threshold the Threshold to set
     */
    public void setThreshold(int threshold) {
        int oldthreshold = this.threshold;
        this.threshold = threshold;
        getPrefs().putInt("NAS_RT_segmentation.threshold", threshold);
        getSupport().firePropertyChange("threshold", oldthreshold, this.threshold);
    }

    /**
     * @return the number of channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * @param channels the number of channels to set
     */
    public void setChannels(int channels) {
        int oldchannels = this.channels;
        this.channels = channels;
        getPrefs().putInt("NAS_RT_segmentation.channels", channels);
        getSupport().firePropertyChange("channels", oldchannels, this.channels);
    }

    /**
     * @return the bin_width
     */
    public int getBinWidth() {
        return binWidth;
    }

    /**
     * @param binWidth the number of binWidth to set
     */
    public void setBinWidth(int binWidth) {
        int oldbinWidth = this.binWidth;
        this.binWidth = binWidth;
        getPrefs().putInt("NAS_RT_segmentation.binWidth", binWidth);
        getSupport().firePropertyChange("binWidth", oldbinWidth, this.binWidth);
    }

}
