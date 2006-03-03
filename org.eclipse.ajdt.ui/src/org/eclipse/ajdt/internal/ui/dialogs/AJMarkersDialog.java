/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Sian January - initial implementation
 *******************************************************************************/
package org.eclipse.ajdt.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ajdt.core.javaelements.AJCompilationUnit;
import org.eclipse.ajdt.core.javaelements.AJCompilationUnitManager;
import org.eclipse.ajdt.core.javaelements.AspectElement;
import org.eclipse.ajdt.internal.builder.CustomMarkerImageProvider;
import org.eclipse.ajdt.internal.builder.MarkerUpdating;
import org.eclipse.ajdt.internal.ui.preferences.AspectJPreferences;
import org.eclipse.ajdt.internal.ui.resources.AspectJImages;
import org.eclipse.ajdt.internal.ui.text.UIMessages;
import org.eclipse.ajdt.internal.utils.AJDTUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * Dialog used to configure advice marker icons for a project
 *
 */
public class AJMarkersDialog extends Dialog {

	private static final String SAMPLE = "SAMPLE"; //$NON-NLS-1$
	private static final String DEFAULT_MARKERS = UIMessages.AJMarkersPropertyPage_default;
	public static final String NO_MARKERS = UIMessages.AJMarkersPropertyPage_none;

	private Table table;
	private boolean pageChanged = false;
	private IProject project;
	private Map tableItemsToAspects = new HashMap();
	private List aspectNames = new ArrayList();
	
	private Map sampleImageDescriptorsTo16x16 = new HashMap();
	
	private static final ImageDescriptor[] sampleImageDescriptors16x16 = new ImageDescriptor[] {
		AspectJImages.ARROW_SAMPLE_16.getImageDescriptor(),
		AspectJImages.BULB_SAMPLE_16.getImageDescriptor(),
		AspectJImages.CIRCLE_SAMPLE_16.getImageDescriptor(),
		AspectJImages.CLOCK_SAMPLE_16.getImageDescriptor(),
		AspectJImages.COG_SAMPLE_16.getImageDescriptor(),
		AspectJImages.CROSS_SAMPLE_16.getImageDescriptor(),
		AspectJImages.DEBUG_SAMPLE_16.getImageDescriptor(),
		AspectJImages.DOCUMENT_SAMPLE_16.getImageDescriptor(),
		AspectJImages.EXCLAMATION_SAMPLE_16.getImageDescriptor(),
		AspectJImages.KEY_SAMPLE_16.getImageDescriptor(),
		AspectJImages.PLUS_SAMPLE_16.getImageDescriptor(),
		AspectJImages.READWRITE_SAMPLE_16.getImageDescriptor(),
		AspectJImages.TICK_SAMPLE_16.getImageDescriptor(),
		AspectJImages.TRACE_SAMPLE_16.getImageDescriptor()
	};
	
	private Image defaultImage = AspectJImages.instance().getRegistry().get(AspectJImages.ADVICE.getImageDescriptor());
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(UIMessages.AJMarkersDialog_configure_title);
	}
	
	public AJMarkersDialog(Shell parentShell, IProject project) {
		super(parentShell);
		for (int i = 0; i < CustomMarkerImageProvider.sampleImageDescriptors.length; i++) {
			sampleImageDescriptorsTo16x16.put(CustomMarkerImageProvider.sampleImageDescriptors[i], sampleImageDescriptors16x16[i]);
		}
		this.project = project;
	}

	public AJMarkersDialog(IShellProvider parentShell, IProject project) {
		super(parentShell);
		this.project = project;
	}
	
	

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 400;
		composite.setLayoutData(gd);
		GridLayout gl = new GridLayout(2, false);
		gl.marginHeight = 15;
		gl.marginWidth = 15;
		composite.setLayout(gl);
		Label label = new Label(composite, SWT.NONE);
		label.setText(UIMessages.AJMarkersPropertyPage_configure_icons);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		Label spacer = new Label(composite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		spacer.setLayoutData(gd);
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gd);
		TableColumn aspectsColumn = new TableColumn(table, SWT.LEFT);
		TableColumn iconsColumn = new TableColumn(table, SWT.LEFT);
		aspectsColumn.setText(UIMessages.AJMarkersPropertyPage_aspect);
		iconsColumn.setText(UIMessages.AJMarkersPropertyPage_icon_for_markers);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		Composite buttonsComposite = new Composite(composite, SWT.NONE);
		buttonsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL));
		buttonsComposite.setLayout(new GridLayout(1, false));
		final Button editButton = new Button(buttonsComposite, SWT.PUSH);
		editButton.setText(UIMessages.AJMarkersPropertyPage_edit);
		editButton.setEnabled(false);
		editButton.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent e) {
				edit(table);
			}

		});
		table.addSelectionListener(new SelectionAdapter(){
			public void widgetDefaultSelected(SelectionEvent e) {
				edit(table);
			};
			
			public void widgetSelected(SelectionEvent e) {
				editButton.setEnabled(true);
			}
		});
		try {
			addAspects(table);
		} catch (CoreException e1) {
		}
		iconsColumn.pack();
		aspectsColumn.pack();
		aspectsColumn.setWidth(aspectsColumn.getWidth() + 20);
		return composite;
	}
	
	private void addAspects(Table table) throws CoreException {
		List ajcus = AJCompilationUnitManager.INSTANCE.getAJCompilationUnits(JavaCore.create(project));
		for (Iterator iter = ajcus.iterator(); iter.hasNext();) {
			AJCompilationUnit unit = (AJCompilationUnit) iter.next();
			IType[] types = unit.getAllTypes();
			for (int j = 0; j < types.length; j++) {
				IType type = types[j];
				if (type instanceof AspectElement) {
					String fullyQualifiedAspectName = getFullyQualifiedAspectName(type);
					String aspectName = getAspectDisplayName(type);
					aspectNames.add(aspectName);
					TableItem tableItem = new TableItem(table, SWT.None);
					tableItemsToAspects.put(tableItem, type);
					tableItem.setText(0, aspectName);
					tableItem.setImage(getAspectImage(type));
					String savedValue = AspectJPreferences.getSavedIcon(project, fullyQualifiedAspectName);
					if(savedValue == null || savedValue.trim().equals("")) { //$NON-NLS-1$
						tableItem.setText(1, DEFAULT_MARKERS);
						tableItem.setImage(1, defaultImage);
					} else if (savedValue.equals(NO_MARKERS)) {
						tableItem.setText(1, NO_MARKERS);
					} else if (savedValue.startsWith(SAMPLE)) {
						int index = getSampleIndex(savedValue);
						tableItem.setImage(1, AspectJImages.instance().getRegistry().get(sampleImageDescriptors16x16[index]));
						tableItem.setText(1, CustomMarkerImageProvider.sampleImageNames[index]);
						tableItem.setData(CustomMarkerImageProvider.sampleImageLocations[index]);							
					} else {
						Image image = CustomMarkerImageProvider.getImage(savedValue); 
						if(image == null) {
							tableItem.setText(1, DEFAULT_MARKERS);
							tableItem.setImage(1, defaultImage);
						} else {
							tableItem.setImage(1, image);
							tableItem.setText(1, savedValue.substring(savedValue.lastIndexOf('/') + 1));
							tableItem.setData(savedValue);
						}
					}
				}
			}
		}		
	}
	
	private Image getAspectImage(IType type) {
		int flags = Flags.AccPublic;
		try {
			flags = type.getFlags();
		} catch (JavaModelException e) {
		}
		if (Flags.isPublic(flags)) {
			return AspectJImages.instance().getRegistry().get(AspectJImages.ASPECT_PUBLIC.getImageDescriptor());
		} else if (Flags.isPrivate(flags)) {
			return AspectJImages.instance().getRegistry().get(AspectJImages.ASPECT_PRIVATE.getImageDescriptor());
		} else if (Flags.isProtected(flags)) {
			return AspectJImages.instance().getRegistry().get(AspectJImages.ASPECT_PROTECTED.getImageDescriptor());
		} else {
			return AspectJImages.instance().getRegistry().get(AspectJImages.ASPECT_PACKAGE.getImageDescriptor());
		}
	}

	private String getAspectDisplayName(IType type) {
		char[][] enclosingTypes = AJDTUtils.getEnclosingTypes(type);
		String name = type.getPackageFragment().getElementName();
		for (int i = 0; i < enclosingTypes.length; i++) {
			name += "."; //$NON-NLS-1$
			name += new String(enclosingTypes[i]);
		}
		return type.getElementName() + " - " + name; //$NON-NLS-1$
	}

	public static String getFullyQualifiedAspectName(IType type) {
		char[][] enclosingTypes = AJDTUtils.getEnclosingTypes(type);
		String name = type.getPackageFragment().getElementName();
		name += "."; //$NON-NLS-1$
		for (int i = 0; i < enclosingTypes.length; i++) {
			name += new String(enclosingTypes[i]);
			name += "."; //$NON-NLS-1$
		}
		name += type.getElementName();
		return name;
	}
    
       
    /**
     * Restore Defaults
     */
	protected void performDefaults() {
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			item.setText(1, DEFAULT_MARKERS);
			item.setImage(1, defaultImage);
			item.setData(null);
		}
		pageChanged = true;
	}
    
	protected void okPressed() {
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			AspectElement aspectEl = (AspectElement) tableItemsToAspects.get(item);
			if (aspectEl != null) {
				String fullyQualifiedName = getFullyQualifiedAspectName(aspectEl);
				String text = item.getText(1);
				if (text == DEFAULT_MARKERS) {
					AspectJPreferences.setSavedIcon(project, fullyQualifiedName, null);
				} else if (text == NO_MARKERS) {
					AspectJPreferences.setSavedIcon(project, fullyQualifiedName, NO_MARKERS);
				} else {
					AspectJPreferences.setSavedIcon(project, fullyQualifiedName, (String)item.getData());
				}
			}
		}
		if(pageChanged) {
			MarkerUpdating.deleteAllMarkers(project);
			MarkerUpdating.addNewMarkers(project);
		}
		super.okPressed();
	} 
	
	protected void cancelPressed() {
		super.cancelPressed();
	}


    
    private void edit(final Table table) {
		MarkerSelectionDialog dialog = new MarkerSelectionDialog(getShell(), project, (String) table.getSelection()[0].getData(), table.getSelection()[0].getText(0), aspectNames);
		dialog.create();
		if(dialog.open() == Window.OK) {
			pageChanged = true;
			String selection = dialog.getSelection();
			String aspectName = dialog.getAspectName();
			if(!(table.getSelection()[0]).getText(0).equals(aspectName)) {
				Set items = tableItemsToAspects.keySet();
				for (Iterator iter = items.iterator(); iter.hasNext();) {
					TableItem item = (TableItem) iter.next();
					if(item.getText(0).equals(aspectName)) {
						table.setSelection(item);
						break;
					}
				}
			}
			if(selection == null) {
				table.getSelection()[0].setText(1, DEFAULT_MARKERS);
				table.getSelection()[0].setImage(1, defaultImage);
				table.getSelection()[0].setData(null);
			} else if (selection == NO_MARKERS) {
				table.getSelection()[0].setText(1, NO_MARKERS);
				table.getSelection()[0].setImage(1, null);
				table.getSelection()[0].setData(NO_MARKERS);
			} else if(selection.startsWith(SAMPLE)) { 
				int index = getSampleIndex(selection);
				table.getSelection()[0].setText(1, CustomMarkerImageProvider.sampleImageNames[index]);
				table.getSelection()[0].setImage(1, AspectJImages.instance().getRegistry().get(sampleImageDescriptors16x16[index]));
				table.getSelection()[0].setData(CustomMarkerImageProvider.sampleImageLocations[index]);
			} else {
				table.getSelection()[0].setText(1, selection.substring(selection.lastIndexOf('/') + 1));
				table.getSelection()[0].setImage(1, CustomMarkerImageProvider.getImage(selection));
				table.getSelection()[0].setData(selection);			
			}
			table.layout();
		}
	}

	private int getSampleIndex(String selection) {
		String[] split = selection.split("_"); //$NON-NLS-1$
		int index = Integer.parseInt(split[1]);
		return index;
	}

	/**
	 * Dialog used to select the icon used for markers relating to
	 * a partcular aspect.
	 */
	public class MarkerSelectionDialog extends Dialog {
		
    	/**
    	 * Override to set the title
    	 */
    	protected void configureShell(Shell shell) {
    	   super.configureShell(shell);
    	   shell.setText(UIMessages.AJMarkersPropertyPage_select_icon);
    	}	
    	
		private String selection;
		private String aspectName;
		private IProject project;
		private List aspects;
		
		protected MarkerSelectionDialog(Shell shell, IProject project, String selection, String aspectName, List aspects) {
			super(shell);
			this.project = project;
			this.selection = selection;
			this.aspectName = aspectName;
			this.aspects = aspects;
		}

		protected Control createDialogArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL));
			GridLayout gl = new GridLayout(3, false);
			gl.marginHeight = 10;
			gl.marginWidth = 10;
			composite.setLayout(gl);
			Label label = new Label(composite, SWT.NONE);
			label.setText(UIMessages.AJMarkersPropertyPage_select_an_icon);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 3;
			label.setLayoutData(gd);
			Label aspectLabel = new Label(composite, SWT.NONE);
			aspectLabel.setText(UIMessages.AJMarkersDialog_aspect);
			final Combo aspectCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
			for (Iterator iter = aspects.iterator(); iter.hasNext();) {
				String aspectNameStr = (String) iter.next();
				aspectCombo.add(aspectNameStr);
			}
			aspectCombo.select(aspectCombo.indexOf(aspectName));
			gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL);
			gd.widthHint = 250;
			aspectCombo.setLayoutData(gd);
			new Label(composite, SWT.NONE);
			aspectCombo.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					aspectName = aspectCombo.getItem(aspectCombo.getSelectionIndex());
				}
			});
			Label iconLabel = new Label(composite, SWT.NONE);
			iconLabel.setText(UIMessages.AJMarkersDialog_icon);
			iconLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
			final Table table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
			final TableColumn column = new TableColumn(table, SWT.LEFT);
			TableItem noneItem = new TableItem(table, SWT.NONE);
			noneItem.setText(NO_MARKERS);
			noneItem.setData(NO_MARKERS);
			if(NO_MARKERS.equals(selection)) {
				table.setSelection(noneItem);
			}
			TableItem defaultItem = new TableItem(table, SWT.NONE);
			defaultItem.setText(DEFAULT_MARKERS);
			if(selection == null) {
				table.setSelection(defaultItem);
			}
			defaultItem.setImage(defaultImage);
			if(selection != NO_MARKERS && selection != null && !(selection.startsWith(SAMPLE))) {
				TableItem customItem = new TableItem(table, SWT.NONE);
				customItem.setText(selection.substring(selection.lastIndexOf('/') + 1));
				customItem.setImage(CustomMarkerImageProvider.getImage(selection));
				customItem.setData(selection);
				table.setSelection(customItem);
			}
			for (int i = 0; i < sampleImageDescriptors16x16.length; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setImage(AspectJImages.instance().getRegistry().get(sampleImageDescriptors16x16[i]));
				item.setText(CustomMarkerImageProvider.sampleImageNames[i]);
				item.setData(CustomMarkerImageProvider.sampleImageLocations[i]);
				if(CustomMarkerImageProvider.sampleImageLocations[i].equals(selection)) {
					table.setSelection(item);
				}
			}		
			column.pack();
			table.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					int selected = table.getSelectionIndex();
					TableItem[] items = table.getItems();
					selection = (String) items[selected].getData();
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			});		
			table.setLayoutData(new GridData(GridData.FILL_BOTH));
			Button browseIconButton = new Button(composite, SWT.PUSH);
			browseIconButton.setText(UIMessages.AJMarkersPropertyPage_browse);
			browseIconButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
			browseIconButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					MarkerImageSelectionDialog dialog = new MarkerImageSelectionDialog(getShell(), project);
					dialog.create();
					if(dialog.open() == Window.OK) {
						selection = dialog.getFileLocation();
						boolean alreadyThere = false;
						TableItem[] items = table.getItems();
						for (int i = 0; i < items.length; i++) {
							if(selection.equals(items[i].getData())) {
								alreadyThere = true;
								table.setSelection(items[i]);
								break;
							}
						}
						if (!alreadyThere) {
							TableItem tableItem = new TableItem(table, SWT.NONE, 2);
							tableItem.setText(dialog.getFileName());
							tableItem.setData(selection);
							tableItem.setImage(CustomMarkerImageProvider.getImage(selection));
							table.setSelection(tableItem);
							column.pack();
						}
						getButton(IDialogConstants.OK_ID).setEnabled(true);
					}
				}	
			});	
			return composite;
		}
		
		public String getSelection() {
			return selection;
		}

		public String getAspectName() {
			return aspectName;
		}
	}
	
	/**
	 * Dialog used to select gif image files from the project
	 */
	public class MarkerImageSelectionDialog extends Dialog {
		
    	private String selection;
    	private String fileName;
		private IProject project;
		
		protected MarkerImageSelectionDialog(Shell parentShell, IProject project) {
			super(parentShell);
			this.project = project;
		}

		public String getFileName() {
			return fileName;
		}

		public String getFileLocation() {
			return selection;
		}

		/**
    	 * Override to set the title
    	 */
    	protected void configureShell(Shell shell) {
    	   super.configureShell(shell);
    	   shell.setText(UIMessages.AJMarkersPropertyPage_select_icon);
    	}	
    	
		protected Control createDialogArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL));
			GridLayout gl = new GridLayout(1, false);
			gl.marginHeight = 10;
			gl.marginWidth = 10;
			composite.setLayout(gl);		
			final Tree tree = new Tree(composite, SWT.BORDER | SWT.SINGLE);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL | GridData.FILL_VERTICAL | GridData.GRAB_VERTICAL);
			gd.heightHint = 150;
			gd.widthHint = 200;
			tree.setLayoutData(gd);
			fillTree(project, tree);
			tree.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					selection = getSelectionForItem(tree.getSelection()[0]);
					fileName = getNameForItem(tree.getSelection()[0]);
					getButton(IDialogConstants.OK_ID).setEnabled(selection != null);
				}
				
			});
			return composite;
		}
		
		private String getNameForItem(TreeItem item) {
			if(item.getData() instanceof IFile) {
				return ((IFile)item.getData()).getName();
			}
			return null;
		}
		
		private String getSelectionForItem(TreeItem item) {
			if(item.getData() instanceof IFile) {
				return ((IFile)item.getData()).getFullPath().toString();
			}
			return null;
		}
		
		private void fillTree(IProject project, Tree tree) {
			TreeViewer viewer = new TreeViewer(tree);
			viewer.setContentProvider(new ITreeContentProvider(){

				public Object[] getChildren(Object parentElement) {
					if(parentElement instanceof IContainer) {
						try {
							IResource[] members = ((IContainer)parentElement).members();
							List children = new ArrayList();
							for (int i = 0; i < members.length; i++) {
								IResource resource = members[i];
								if(resource instanceof IContainer || resource.getFileExtension().equals("gif")) { //$NON-NLS-1$
									children.add(resource);
								}
							}
							return children.toArray();
						} catch (CoreException e) {
						}
					}
					return new Object[0];
				}

				public Object getParent(Object element) {
					if(element instanceof IResource) {
						return ((IResource)element).getParent();
					}
					return null;
				}

				public boolean hasChildren(Object element) {
					return element instanceof IContainer;
				}

				public Object[] getElements(Object inputElement) {
					if(inputElement instanceof Object[]) {
						return (Object[]) inputElement;
					} else return (getChildren(inputElement));
				}

				public void dispose() {					
				}

				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {				
				}

			});
			viewer.setLabelProvider(new WorkbenchLabelProvider());
			viewer.setInput(new Object[] {project});
		}

		protected String getSelection() {
			return selection;
		}    	
	} 

}
