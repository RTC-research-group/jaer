/*
 * BiasgenPanel.java
 *
 * Created on September 24, 2005, 10:05 PM
 */

package ch.unizh.ini.jaer.projects.opticalflow.graphics;

import net.sf.jaer.biasgen.Biasgen;
import net.sf.jaer.biasgen.BiasgenFrame;
import net.sf.jaer.biasgen.MasterbiasPanel;
import net.sf.jaer.biasgen.PotPanel;
import ch.unizh.ini.jaer.projects.opticalflow.mdc2d.MDC2D.MDC2DBiasgen;

/**
 * A panel for controlling a bias generator, 
 * with a Masterbias and an IPotArray. 
 * This is added to the content panel of BiasgenFrame. 
 * It builds the PotPanel, the MasterbiasPanel, and the 
 * extra control panel if there is one.
 *
 * @author  tobi
 */
public class BiasgenPanelMDC2D extends javax.swing.JPanel {
    public Biasgen biasgen;
    MasterbiasPanel masterbiasPanel;
    PotPanel iPotPanel;
    PotPanel vPotPanel;
    BiasgenFrame frame;
    
    /** Creates new form BiasgenPanel
     * @param biasgen the source of the parameters
     * @param frame the parent enclosing frame
     */
    public BiasgenPanelMDC2D(Biasgen biasgen, BiasgenFrame frame) {
        this.biasgen=biasgen;
        this.frame=frame;
        if(biasgen==null) throw new RuntimeException("null biasgen while trying to construct BiasgenPanel");
        vPotPanel=new PotPanel(((MDC2DBiasgen)biasgen).vpots);
        iPotPanel=new PotPanel(((MDC2DBiasgen)biasgen).ipots);
        masterbiasPanel=new MasterbiasPanel(biasgen.getMasterbias());

        initComponents();
        add(jTabbedPane1);
        jTabbedPane1.addTab("On Chip Biasgenerator", iPotPanel);
        jTabbedPane1.addTab("Master Bias",masterbiasPanel);
        jTabbedPane1.addTab("external DAC",vPotPanel);


        masterbiasPanel.masterbias.setPowerDownEnabled(false);

    }

   

   

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
       
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
    }//GEN-LAST:event_formMouseMoved
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    
    

}
