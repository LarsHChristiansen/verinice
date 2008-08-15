package sernet.gs.ui.rcp.main.bsi.risikoanalyse.wizard;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.jface.viewers.TableViewer;

import sernet.gs.ui.rcp.main.bsi.dnd.DNDItems;
import sernet.gs.ui.rcp.main.bsi.model.MassnahmenUmsetzung;
import sernet.gs.ui.rcp.main.bsi.risikoanalyse.model.RisikoMassnahmenUmsetzung;
import sernet.gs.ui.rcp.main.common.model.CnATreeElement;

public class RisikoMassnahmenUmsetzungDragListener implements DragSourceListener {
	
	private TableViewer viewer;
	private CnATreeElement cnaElement;

	public RisikoMassnahmenUmsetzungDragListener(TableViewer newViewer, CnATreeElement newCnaElement) {
		viewer = newViewer;
		cnaElement = newCnaElement;
	}

	/**
	 *  nothing to do after drag completed
	 */
	public void dragFinished(DragSourceEvent event) {
		// nothing to do
		Logger.getLogger(this.getClass()).debug("drag start - dragFinished()");
	}

	public void dragSetData(DragSourceEvent event) {
		event.data = DNDItems.RISIKOMASSNAHMENUMSETZUNG;
		Logger.getLogger(this.getClass()).debug("drag start - dragSetData()");
	}

	/**
	 * starts drag if necessary
	 */
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = ((IStructuredSelection)viewer.getSelection());
		ArrayList<RisikoMassnahmenUmsetzung> risikoMassnahmenUmsetzung =
			new ArrayList<RisikoMassnahmenUmsetzung>();
		
		/* leave, if selcetion is empty */
		if (selection.size() != 1) {
			event.doit = false;
			return;
		}
			
		/* process RisikoMassnahmenUmsetzungen
		 * cast MassnahmenUmsetzungen to RisikoMassnahmenUmsetzungen */
		for (Iterator iter = selection.iterator(); iter.hasNext();) {
			Object object = iter.next();
			if (!(object instanceof RisikoMassnahmenUmsetzung
					|| object instanceof MassnahmenUmsetzung)) {
				event.doit = false;
				return;	
			} else if (object instanceof MassnahmenUmsetzung) {
				/* object is of type MassnahmenUmsetzung - convert to
				 * RisikoMassnahmenUmsetzung
				 */
				// TODO warum wird das nicht bemängelt?
				// der cast dürfte doch gar nicht erlabut sein !?
				// RisikoMassnahmenUmsetzung foo = (RisikoMassnahmenUmsetzung) object;
				RisikoMassnahmenUmsetzung umsetzung = new RisikoMassnahmenUmsetzung(cnaElement, null);
				umsetzung.setName(((MassnahmenUmsetzung) object).getTitle());
				// umsetzung.setName(((MassnahmenUmsetzung) object).getDecription());
				risikoMassnahmenUmsetzung.add(umsetzung);
				Logger.getLogger(this.getClass()).debug("drag start - MassnahmenUmsetzung " + ((MassnahmenUmsetzung) object).getTitle());
			} else {
				/* object is of type RisikoMassnahmenUmsetzung - add it */
				risikoMassnahmenUmsetzung.add((RisikoMassnahmenUmsetzung) object);
				Logger.getLogger(this.getClass()).debug("drag start - RisikoMassnahmenUmsetzung " + ((RisikoMassnahmenUmsetzung) object).getTitle());
			}
				
		}
		event.doit = true;
		DNDItems.setItems(risikoMassnahmenUmsetzung);
	}
}
