package org.uet.dse.rtlplus.actions;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.tzi.use.gui.main.MainWindow;
import org.tzi.use.gui.main.ViewFrame;
import org.tzi.use.main.Session;
import org.tzi.use.runtime.gui.IPluginAction;
import org.tzi.use.runtime.gui.IPluginActionDelegate;
import org.uet.dse.rtlplus.Main;
import org.uet.dse.rtlplus.mm.MRuleCollection.TransformationType;
import org.uet.dse.rtlplus.sync.SyncWorker;

public class ActionIncrementalUpdate implements IPluginActionDelegate {

	@Override
	public void performAction(IPluginAction pluginAction) {
		MainWindow mainWindow = pluginAction.getParent();
		if (Main.getTggRuleCollection().getType() == TransformationType.SYNCHRONIZATION) {
			Session session = pluginAction.getSession();
			SyncWorker syncWorker = new SyncWorker(mainWindow, session);
			URL url = Main.class.getResource("/resources/delta.png");
			ViewFrame vf = new ViewFrame("Model incremental update", null, "");
			vf.setFrameIcon(new ImageIcon(url));
			vf.addInternalFrameListener(new InternalFrameListener() {
				@Override
				public void internalFrameActivated(InternalFrameEvent arg0) {
				}

				@Override
				public void internalFrameClosed(InternalFrameEvent arg0) {
					syncWorker.unsubscribe();
				}

				@Override
				public void internalFrameClosing(InternalFrameEvent arg0) {
				}

				@Override
				public void internalFrameDeactivated(InternalFrameEvent arg0) {
				}

				@Override
				public void internalFrameDeiconified(InternalFrameEvent arg0) {
				}

				@Override
				public void internalFrameIconified(InternalFrameEvent arg0) {
				}

				@Override
				public void internalFrameOpened(InternalFrameEvent arg0) {
				}
			});
			vf.setContentPane(syncWorker);
			mainWindow.addNewViewFrame(vf);
		}
		else JOptionPane.showMessageDialog(mainWindow, "This feature is only available for the synchronization transformation type.");
	}
}
