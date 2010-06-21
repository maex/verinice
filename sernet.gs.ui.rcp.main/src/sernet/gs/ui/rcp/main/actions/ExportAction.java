/*******************************************************************************
 * Copyright (c) 2010 Andreas Becker <andreas[at]becker[dot]name>.
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
 *     Andreas Becker <andreas[at]becker[dot]name> - initial API and implementation
 ******************************************************************************/

package sernet.gs.ui.rcp.main.actions;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.w3c.dom.Document;

import sernet.gs.ui.rcp.main.Activator;
import sernet.gs.ui.rcp.main.bsi.dialogs.EncryptionDialog;
import sernet.gs.ui.rcp.main.bsi.dialogs.ExportDialog;
import sernet.gs.ui.rcp.main.bsi.dialogs.EncryptionDialog.EncryptionMethod;
import sernet.gs.ui.rcp.main.common.model.CnATreeElement;
import sernet.gs.ui.rcp.main.service.ServiceFactory;
import sernet.gs.ui.rcp.main.service.commands.CommandException;
import sernet.gs.ui.rcp.main.service.taskcommands.ExportCommand;
import sernet.verinice.encryption.IEncryptionService;

/**
 * {@link Action} that exports selected objects from the
 * database to an XML file at the selected path. This uses
 * {@link ExportDialog} to retrieve user selections.
 */
public class ExportAction extends Action
{
	public static final String ID = "sernet.gs.ui.rcp.main.export"; //$NON-NLS-1$
	
	private IWorkbenchWindow window;

	public ExportAction(IWorkbenchWindow window, String label)
	{
		this.window = window;
		setText(label);
		setId(ID);
	}
	
	/*
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public void run()
	{
		ExportDialog dialog = new ExportDialog(Display.getCurrent().getActiveShell());
		if( dialog.open() == Dialog.OK )
		{
			LinkedList<CnATreeElement> exportElements = new LinkedList<CnATreeElement>();
			exportElements.add(dialog.getSelectedITNetwork());
			ExportCommand exportCommand;
			
			if( dialog.isRestrictedToEntityTypes() )
			{
				exportCommand = new ExportCommand(exportElements, dialog.getSourceId(), dialog.getEntityTypesToBeExported());
			}
			else
			{
				exportCommand = new ExportCommand(exportElements, dialog.getSourceId());
			}
				
			
			try
			{
				exportCommand = ServiceFactory.lookupCommandService().executeCommand(exportCommand);
			}
			catch(CommandException ex)
			{
				ex.printStackTrace();
			}
			
			Document doc = exportCommand.getExportDocument();
			writeDocumentToFile(doc, dialog.getStorageLocation(), dialog.getEncryptOutput());
		}
	}
	
	/*********************************************************************
	 * Writes a Dom-tree {@code doc} to a (newly created) file given
	 * by its {@code path}. If {@code encryptOutput} is true, a
	 * decryption dialog is opened, which lets the user choose an
	 * encryption method. In this case, the bytestream will be encrypted
	 * appropriately before being written to the file.
	 * 
	 * @param doc
	 * @param uri
	 *********************************************************************/
	public void writeDocumentToFile( Document doc, String path, boolean encryptOutput )
	{
		try
		{
			OutputStream os = new FileOutputStream( path );
			
			if (encryptOutput) {
				EncryptionDialog encDialog = new EncryptionDialog(Display.getDefault().getActiveShell());
				if (encDialog.open() == Dialog.OK) {
					IEncryptionService service = Activator.getDefault().getEncryptionService();
					
					EncryptionMethod encMethod = encDialog.getSelectedEncryptionMethod();
					if (encMethod == EncryptionMethod.PASSWORD) {
						os = service.encrypt(os, encDialog.getEnteredPassword());
					} else if (encMethod == EncryptionMethod.X509_CERTIFICATE) {
						os = service.encrypt(os, encDialog.getSelectedX509CertificateFile());
					}
				}
			}
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform( new DOMSource( doc ), new StreamResult( os ) );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			return;
		}
	}
}