package org.uet.dse.rtlplus.actions;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.tzi.use.gui.main.MainWindow;
import org.tzi.use.gui.main.ViewFrame;
import org.tzi.use.runtime.gui.IPluginAction;
import org.tzi.use.runtime.gui.IPluginActionDelegate;
import org.uet.dse.rtlplus.Main;
import org.uet.dse.rtlplus.objectdiagram.RtlObjectDiagramView;

public class ActionShowObjectDiagram implements IPluginActionDelegate {

	@Override
	public void performAction(IPluginAction pluginAction) {
		MainWindow mainWindow = pluginAction.getParent();
		if (Main.getTggRuleCollection().getRuleList().size() == 0) {
			JOptionPane.showMessageDialog(mainWindow, "Plese load some TGG rules first.", "No transformation rules",  JOptionPane.WARNING_MESSAGE);
		}
		else {
			URL url = Main.class.getResource("/resources/diagram.png");
			ViewFrame vf = new ViewFrame("RTL object diagram", null, "");
			vf.setFrameIcon(new ImageIcon(url));
			RtlObjectDiagramView odv = new RtlObjectDiagramView(mainWindow, Main.getTggRuleCollection().getClassMap(), pluginAction.getSession().system());
			vf.setContentPane(odv);
			vf.pack();
			mainWindow.addNewViewFrame(vf);
		}
	}

}