/*******************************************************************************
 * Copyright (c) 2009 Daniel Murygin <dm@sernet.de>.
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 *     This program is distributed in the hope that it will be useful,    
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *     You should have received a copy of the GNU General Public 
 * License along with this program. 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel <dm@sernet.de> - initial API and implementation
 ******************************************************************************/
package sernet.verinice.iso27k.rcp;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import sernet.gs.ui.rcp.main.Activator;
import sernet.gs.ui.rcp.main.ExceptionUtil;
import sernet.gs.ui.rcp.main.ImageCache;
import sernet.gs.ui.rcp.main.bsi.model.Attachment;
import sernet.gs.ui.rcp.main.bsi.model.AttachmentFile;
import sernet.gs.ui.rcp.main.bsi.model.BSIModel;
import sernet.gs.ui.rcp.main.bsi.views.Messages;
import sernet.gs.ui.rcp.main.common.model.CnAElementFactory;
import sernet.gs.ui.rcp.main.common.model.IModelLoadListener;
import sernet.gs.ui.rcp.main.service.ICommandService;
import sernet.gs.ui.rcp.main.service.ServiceFactory;
import sernet.gs.ui.rcp.main.service.commands.CommandException;
import sernet.gs.ui.rcp.main.service.crudcommands.DeleteNote;
import sernet.gs.ui.rcp.main.service.crudcommands.LoadAttachmentFile;
import sernet.gs.ui.rcp.main.service.crudcommands.LoadAttachments;
import sernet.gs.ui.rcp.main.service.crudcommands.LoadBSIModel;
import sernet.gs.ui.rcp.main.service.crudcommands.SaveAttachment;
import sernet.gs.ui.rcp.main.service.crudcommands.SaveNote;
import sernet.verinice.iso27k.rcp.action.ControlDragListener;
import sernet.verinice.iso27k.service.IItem;
import sernet.verinice.iso27k.service.Item;
import sernet.verinice.iso27k.service.commands.CsvFile;
import sernet.verinice.iso27k.service.commands.ImportCatalog;
import sernet.verinice.rcp.IAttachedToPerspective;

/**
 * @author Daniel <dm@sernet.de>
 * 
 */
public class CatalogView extends ViewPart implements IAttachedToPerspective  {

	private static final Logger LOG = Logger.getLogger(CatalogView.class);

	public static final String ID = "sernet.verinice.iso27k.rcp.CatalogView"; //$NON-NLS-1$
	
	public static final DateFormat DATE_TIME_FORMAT_SHORT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

	private Action addCatalogAction;
	
	private Action deleteCatalogAction;
	
	private Action filterAction;
	
	private Action expandAllAction;

	private Action collapseAllAction;
	
	private DragSourceListener dragListener;

	private ICommandService commandService;

	private TreeViewer viewer;
	
	private Label labelCatalog;
	
	private Combo comboCatalog;
	
	private ComboModel<Attachment> comboModel;
	
	private Label labelFilter;
	
	private Text filter;
	
	private BSIModel bsiModel;
	
	private CatalogTextFilter textFilter;
	
	private IModelLoadListener modelLoadListener;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		try {
			initView(parent);
			startInitDataJob();		
		} catch (Exception e) {
			LOG.error("Error while creating catalog view", e);
			ExceptionUtil.log(e, "Error while opening Catalog-View.");
		}	
	}



	/**
	 * 
	 */
	protected void startInitDataJob() {
		WorkspaceJob initDataJob = new WorkspaceJob(Messages.ISMView_InitData) {
			public IStatus runInWorkspace(final IProgressMonitor monitor) {
				IStatus status = Status.OK_STATUS;
				try {
					monitor.beginTask(Messages.ISMView_InitData, IProgressMonitor.UNKNOWN);
					loadCatalogAttachmets();
				} catch (Exception e) {
					LOG.error("Error while loading data.", e);
					status= new Status(Status.ERROR, "sernet.gs.ui.rcp.main", "Error while loading data.",e); //$NON-NLS-1$
				} finally {
					monitor.done();
				}
				return status;
			}
		};
		JobScheduler.scheduleInitJob(initDataJob);
	}



	private void initView(Composite parent) {
		GridLayout gl = new GridLayout(1, true);
		parent.setLayout(gl);
		
		Composite compForm = new Composite(parent,SWT.NONE);
		GridLayout glForm = new GridLayout(2, false);
		compForm.setLayout(glForm);
		labelCatalog = new Label(compForm,SWT.NONE);
		labelCatalog.setText("Katalog");
		comboCatalog = new Combo(compForm, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboCatalog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboCatalog.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		    	  comboModel.setSelectedIndex(comboCatalog.getSelectionIndex());
		    	  openCatalog();
		    	  deleteCatalogAction.setEnabled(true);
		      }
		    });
		comboModel = new ComboModel<Attachment>(new ComboModelLabelProvider<Attachment>() {
			@Override
			public String getLabel(Attachment attachment) {
				StringBuilder sb = new StringBuilder();
				sb.append(attachment.getFileName());
				sb.append(" (").append(DATE_TIME_FORMAT_SHORT.format(attachment.getDate())).append(")");
				return sb.toString();
			}		
		});
		
		labelFilter = new Label(compForm,SWT.NONE);
		labelFilter.setText("Filter");
		filter = new Text(compForm, SWT.BORDER);
		filter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filter.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {				
			}
			public void keyReleased(KeyEvent e) {
				textFilter.setPattern(filter.getText());
			}		
		});
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.setSorter(new KapitelSorter());
			
		getSite().setSelectionProvider(viewer);
		
		makeActions();
		hookActions();
		hookDNDListeners();
		fillLocalToolBar();
	}
	


	/**
	 * 
	 */
	private void loadCatalogAttachmets() {
		try {
			Activator.inheritVeriniceContextState();
			if(getBsiModel()!=null) {
				// model is loaded: load data
				LoadAttachments command = new LoadAttachments(getBsiModel().getDbId());		
				command = getCommandService().executeCommand(command);		
				List<Attachment> attachmentList = command.getAttachmentList();
				comboModel.clear();
				for (Attachment attachment : attachmentList) {
					comboModel.add(attachment);
				}
				Display.getDefault().syncExec(new Runnable(){
					public void run() {
						comboCatalog.setItems(comboModel.getLabelArray());
					}
				});
			} else if(modelLoadListener==null) {
				// model is not loaded yet: add a listener to load data when it's laoded
				modelLoadListener = new IModelLoadListener() {

					public void closed(BSIModel model) {
						// nothing to do
					}

					public void loaded(BSIModel model) {
						startInitDataJob();
					}
					
				};
				CnAElementFactory.getInstance().addLoadListener(modelLoadListener);
			}
			
			
		} catch(Exception e) {
			LOG.error("Error while loading catalogs", e);
			ExceptionUtil.log(e, "Error while loading catalogs");
		}
	}
	
	/**
	 * @return 
	 * @throws CommandException 
	 * 
	 */
	private Attachment saveFile(ImportCatalog importCatalog) throws CommandException {
		CsvFile csvFile = importCatalog.getCsvFile();
		Attachment attachment = null;
		if(csvFile!=null) {			
			attachment = new Attachment();
			attachment.setCnATreeElementId(getBsiModel().getDbId());
			attachment.setCnAElementTitel(getBsiModel().getTitle());
			Date now = Calendar.getInstance().getTime();
			attachment.setDate(now);
			attachment.setFilePath(csvFile.getFilePath());
			attachment.setTitel(attachment.getFileName());
			attachment.setText("Control / threat catalog imported at: " + DateFormat.getDateTimeInstance().format(now));
			SaveNote command = new SaveNote(attachment);	
			command = getCommandService().executeCommand(command);
			attachment = (Attachment) command.getAddition();
			
			AttachmentFile attachmentFile = new AttachmentFile();
			attachmentFile.setDbId(attachment.getDbId());
			attachmentFile.setFileData(csvFile.getFileContent());
			SaveAttachment saveAttachmentFile = new SaveAttachment(attachmentFile);
			saveAttachmentFile = getCommandService().executeCommand(saveAttachmentFile);
			attachmentFile = saveAttachmentFile.getElement();
			saveAttachmentFile.clear();
		}
		return attachment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void makeActions() {
		addCatalogAction = new Action() {
			public void run() {
				importCatalog();
			}
		};
		addCatalogAction.setText("Katalog importieren...");
		addCatalogAction.setImageDescriptor(ImageCache.getInstance().getImageDescriptor(ImageCache.NOTE_NEW));
		addCatalogAction.setEnabled(true);
		
		deleteCatalogAction = new Action() {
			public void run() {
				boolean confirm = MessageDialog.openConfirm(viewer.getControl().getShell(), "Wirklich löschen?", "Diesen Katalog wirklich löschen?");
				if (confirm)
					deleteCatalog();
			}
		};
		deleteCatalogAction.setText("Katalog löschen...");
		deleteCatalogAction.setImageDescriptor(ImageCache.getInstance().getImageDescriptor(ImageCache.DELETE));
		deleteCatalogAction.setEnabled(false);
		
		textFilter = new CatalogTextFilter(viewer);
		filterAction = new CatalogViewFilterAction(viewer, this.textFilter);
		
		expandAllAction = new Action() {
			@Override
			public void run() {
				viewer.expandAll();
			}
		};
		expandAllAction.setText("Expand All");
		expandAllAction.setImageDescriptor(ImageCache.getInstance().getImageDescriptor(ImageCache.EXPANDALL));

		collapseAllAction = new Action() {
			@Override
			public void run() {
				viewer.collapseAll();
			}
		};
		collapseAllAction.setText("Collapse All");
		collapseAllAction.setImageDescriptor(ImageCache.getInstance().getImageDescriptor(ImageCache.COLLAPSEALL));

		
		dragListener = new ControlDragListener(viewer);
	}

	/**
	 * 
	 */
	private void hookActions() {
	}
	
	private void hookDNDListeners() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance(),FileTransfer.getInstance() };
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		viewer.addDragSupport(operations, types, dragListener);
	}
	/**
	 * 
	 */
	private void fillLocalToolBar() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager manager = bars.getToolBarManager();
		manager.add(this.addCatalogAction);
		manager.add(this.deleteCatalogAction);
		manager.add(this.expandAllAction);
		manager.add(this.collapseAllAction);
	}

	public ICommandService getCommandService() {
		if (commandService == null) {
			commandService = createCommandServive();
		}
		return commandService;
	}

	private ICommandService createCommandServive() {
		return ServiceFactory.lookupCommandService();
	}

	static class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

		public void dispose() {
		}

		public Object[] getChildren(Object parent) {
			return ((IItem) parent).getItems().toArray();		
		}

		public Object[] getElements(Object parent) {
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			return null;
		}

		public boolean hasChildren(Object parent) {
			return ((IItem) parent).getItems().size()>0;
		}

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
	}
	
	static class ViewLabelProvider extends LabelProvider {

		public Image getImage(Object obj) {
			IItem item = (IItem) obj;
			String image = ImageCache.UNKNOWN;
			
			if(item.getDescription()!=null && item.getItems().size()>0) {
				image = ImageCache.BAUSTEIN;
			}
			else if(item.getDescription()!=null && item.getTypeId()==IItem.CONTROL) {
				image = ImageCache.STUFE_NONE;
			}
			else if(item.getDescription()!=null && item.getTypeId()==IItem.THREAT) {
				image = ImageCache.GEFAEHRDUNG;
			}
			return ImageCache.getInstance().getImage(image);	
		}

		public String getText(Object obj) {
			IItem item = ((IItem)obj);
			StringBuilder sb = new StringBuilder();
			// add the number to the titel
			if(item.getName()==null || !item.getName().startsWith(item.getNumberString())) {
				sb.append(item.getNumberString()).append(" ");
			}
			return sb.append(item.getName()).toString();
		}
	}

	public BSIModel getBsiModel() {
		if(bsiModel==null) {
			try {
				bsiModel = CnAElementFactory.getLoadedModel();
			} catch (Exception e) {
				LOG.error("Error while creating BSI-Model", e);
			}
		}
		return bsiModel;
	}

	public void setBsiModel(BSIModel bsiModel) {
		this.bsiModel = bsiModel;
	}
	
	public BSIModel loadBsiModel() {
		LoadBSIModel loadBSIModel = new LoadBSIModel();
		try {
			loadBSIModel = getCommandService().executeCommand(loadBSIModel);
		} catch (CommandException e) {
			LOG.error("Error while loading BSI-Model.", e);
		}
		bsiModel = loadBSIModel.getModel();
		return bsiModel;
	}

	private void openCatalog() {
		try {
			Attachment selected = comboModel.getSelectedObject();
			if(selected!=null) {
				LoadAttachmentFile loadAttachmentFile = new LoadAttachmentFile(selected.getDbId());
				loadAttachmentFile = getCommandService().executeCommand(loadAttachmentFile);
				if(loadAttachmentFile!=null && loadAttachmentFile.getAttachmentFile()!=null && loadAttachmentFile.getAttachmentFile().getFileData()!=null) {
					ImportCatalog importCatalog = new ImportCatalog(loadAttachmentFile.getAttachmentFile().getFileData());
					importCatalog = getCommandService().executeCommand(importCatalog);
					if(importCatalog.getCatalog()!=null) {
						viewer.setInput(importCatalog.getCatalog().getRoot());
					}
				}
			}
		} catch(Exception e) {
			LOG.error("Error while loading catalog", e);
			ExceptionUtil.log(e, "Error while loading catalog");
		}
	}

	private void importCatalog() {
		FileDialog fd = new FileDialog(CatalogView.this.getSite().getShell());
		fd.setText("Katalog auswählen...");
		fd.setFilterPath("~");
		fd.setFilterExtensions(new String[] { "*.csv" });
		String selected = fd.open();
		if (selected != null && selected.length() > 0) {
			try {
				ImportCatalog importCatalog = new ImportCatalog(selected);
				importCatalog = getCommandService().executeCommand(importCatalog);
				Attachment attachment = saveFile(importCatalog);
				if(importCatalog.getCatalog()!=null) {
					viewer.setInput(importCatalog.getCatalog().getRoot());
				}
				comboModel.add(attachment);
				comboCatalog.setItems(comboModel.getLabelArray());
				selectComboItem(attachment);
			} catch (Exception e) {
				LOG.error("Error while reading file data", e);
				ExceptionUtil.log(e, "Fehler beim Lesen der Datei.");
			}
		}
	}
	
	/**
	 * @param attachment
	 */
	private void selectComboItem(Attachment attachment) {
		comboModel.setSelectedObject(attachment);
		// indexes that are out of range are ignored in Combo
		comboCatalog.select(comboModel.getSelectedIndex());
	}

	/**
	 * 
	 */
	protected void deleteCatalog() {
		try {
			Attachment selected = comboModel.getSelectedObject();
			DeleteNote command = new DeleteNote(selected);		
			command = getCommandService().executeCommand(command);
			comboModel.removeSelected();
			openCatalog();
			comboCatalog.setItems(comboModel.getLabelArray());
			comboCatalog.select(comboModel.getSelectedIndex());
			if(comboModel.getSelectedIndex()<0) {
				deleteCatalogAction.setEnabled(false);
				viewer.setInput(new Item());
			}
		} catch(Exception e) {
			LOG.error("Error while deleting catalog", e);
			ExceptionUtil.log(e, "Fehler beim Löschen des Katalogs");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see sernet.verinice.rcp.IAttachedToPerspective#getPerspectiveId()
	 */
	public String getPerspectiveId() {
		return Iso27kPerspective.ID;
	}
}
