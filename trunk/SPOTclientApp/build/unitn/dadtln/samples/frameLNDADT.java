package unitn.dadtln.samples;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


import org.sunspotworld.demo.util.PacketTypes;

public class frameLNDADT extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static frameLNDADTDataListener listener = null;
    public static ClientNode clientNode;
    
    
    /**
     * Creates a new TelemetryFrame window.
     */
    public frameLNDADT() {
        init(null);
        setVisible(true);
    }
    
    public void setMasterApp(ClientNode app){
    	clientNode = app;
    }
    
    /**
     * Create an initial new window and display it.
     *
     * @param args the command line arguments (ignored)
     */
    public static void main(String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TelemetryFrame");

    }

    /**
     * Initialize the new TelemetryFrame
     */
    private void init(File file) {
        if (listener == null) {
            listener = new frameLNDADTDataListener();         // only need one
            listener.start();
        }
        initComponents();
        
        listener.doSendData(PacketTypes.PING_REQ); 
    }
    

    /**
     * Display the current connection status to a remote SPOT. 
     * Called by the AccelerometerListener whenever the radio connection status changes.
     *
     * @param conn true if now connected to a remote SPOT
     * @param msg the String message to display, includes the 
     */
    public void setConnectionStatus(boolean conn, String msg) {
        txtRequestData.setText(msg);
    	btnSendRequest.setEnabled(conn);
    }
     
    public void updateReceiveText(String src, double data){
    	txtReplyOutput.append(src + "\n");
    	txtReplyOutput.append(data + "\n");
    }
 
    /**
     * Cleanly exit.
     */
    private void doQuit() {
        System.exit(0);
    }
    
    // GUI code generated using NetBeans GUI editor:

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        getContentPane().setLayout(new java.awt.BorderLayout(0, 5));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("LNDADT Demo");
        setName("spotTelemetry");
        getContentPane().add(getJPanel1(), BorderLayout.CENTER);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;

        btnSendRequest.setText("ds.average");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;

      
        pack();
        this.setSize(562, 399);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        listener.setGUI(this);
        

    }//GEN-LAST:event_formWindowActivated

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        listener.setGUI(null);
    }//GEN-LAST:event_formWindowDeactivated

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
            doQuit();
    }//GEN-LAST:event_formWindowClosed


    private void btnSendRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
    	txtReplyOutput.append("Client node run \n");    
    	
    	clientNode.runRequest();
    	// hostSendData()
    	
    	
    }
 
    public void hostSendData(){
    	listener.doSendData(PacketTypes.SEND_TEMP_DATA_REQ);       
    }
    
    private JPanel getJPanel1() {
    	if(jPanel1 == null) {
    		jPanel1 = new JPanel();
    		FlowLayout jPanel1Layout = new FlowLayout();
    		jPanel1.setLayout(jPanel1Layout);
    		jPanel1.setPreferredSize(new java.awt.Dimension(569, 327));
    		jPanel1.add(getBtnSendRequest());
    		jPanel1.add(getTxtRequestData());
    		jPanel1.add(getLblReplyOutput());
    		jPanel1.add(getTxtReplyOutput());
    	}
    	return jPanel1;
    }
    
    private JButton getBtnSendRequest() {
    	if(btnSendRequest == null) {
    		btnSendRequest = new JButton();
    		btnSendRequest.setText("Send req");

    		btnSendRequest.setVerticalAlignment(SwingConstants.TOP);
    		btnSendRequest.setVerticalTextPosition(SwingConstants.TOP);

    		btnSendRequest.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	btnSendRequestActionPerformed(evt);
                }
            });
    	}
    	return btnSendRequest;
    }
    
    private JTextField getTxtRequestData() {
    	if(txtRequestData == null) {
    		txtRequestData = new JTextField();
    		
    		txtRequestData.setEditable(false);
    		txtRequestData.setPreferredSize(new java.awt.Dimension(401, 22));
    	}
    	return txtRequestData;
    }
    
    private JLabel getLblReplyOutput() {
    	if(lblReplyOutput == null) {
    		lblReplyOutput = new JLabel();
    		lblReplyOutput.setText("Reply output:");
    		lblReplyOutput.setPreferredSize(new java.awt.Dimension(534, 15));
    		lblReplyOutput.setHorizontalAlignment(SwingConstants.LEFT);
    		lblReplyOutput.setVerticalAlignment(SwingConstants.TOP);
    		lblReplyOutput.setVerticalTextPosition(SwingConstants.TOP);
    	}
    	return lblReplyOutput;
    }
    
    private JTextArea getTxtReplyOutput() {
    	if(txtReplyOutput == null) {
    		txtReplyOutput = new JTextArea();
    		
    		txtReplyOutput.setEditable(false);
     		txtReplyOutput.setText("<empty> \n");
    		txtReplyOutput.setPreferredSize(new java.awt.Dimension(539, 304));
    	
           
    	}
    	return txtReplyOutput;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables


    private JTextField txtRequestData;
    private JTextArea txtReplyOutput;
    private JLabel lblReplyOutput;
    private JButton btnSendRequest;
    private JPanel jPanel1;

    
}
