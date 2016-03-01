/*******************************************************************************
 * Copyright (c) 2012 Sebastian Hagedorn <sh@sernet.de>.
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
 *     Sebastian Hagedorn <sh@sernet.de> - initial API and implementation
 ******************************************************************************/
package sernet.gs.ui.rcp.main.bsi.dnd.transfer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;

import org.apache.log4j.Logger;
import org.eclipse.swt.dnd.TransferData;

import sernet.verinice.model.iso27k.IISO27kElement;

/**
 *
 */
public final class ISO27kElementTransfer extends VeriniceElementTransfer {
    
    private static final String TYPENAME_ISOELEMENT = "isoElement";
    private static final int TYPEID_ISOELEMENT = registerType(TYPENAME_ISOELEMENT);
    
    private static ISO27kElementTransfer instance = new ISO27kElementTransfer();
    
    public static ISO27kElementTransfer getInstance(){
        return instance;
    }
    
    private ISO27kElementTransfer(){}

    /* (non-Javadoc)
     * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
     */
    @Override
    protected String[] getTypeNames() {
        return new String[]{TYPENAME_ISOELEMENT};
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
     */
    @Override
    protected int[] getTypeIds() {
        return new int[]{TYPEID_ISOELEMENT};
    }
    
    public void javaToNative (Object data, TransferData transferData){
        TransferUtil.iSO27KtoNative(getInstance(), data, transferData);
    }
    
    public Object nativeToJava(TransferData transferData){
        Object o = null;
        if(isSupportedType(transferData)){
            byte[] bs = (byte[]) super.nativeToJava(transferData);
            ByteArrayInputStream bis = new ByteArrayInputStream(bs);
            ObjectInput in;
            try {
                in = new ObjectInputStream(bis);
                o = in.readObject();
                bis.close();
                in.close();
            } catch (OptionalDataException e){
                getLog().error("Wrong data", e);
            } catch (IOException e) {
                getLog().error("Error while transfering dnd object back to java", e);
            } catch (ClassNotFoundException e) {
                getLog().error("Error while transfering dnd object back to java", e);
            }
        }
        return o;
    }
    
    @Override
    protected Logger getLog() {
        if(log == null){
            log = Logger.getLogger(ISO27kElementTransfer.class);
        }
        return log;
    }

    /*
     * (non-Javadoc)
     * 
     * @see sernet.gs.ui.rcp.main.bsi.dnd.transfer.VeriniceElementTransfer#
     * validateData(java.lang.Object)
     */
    @Override
    public boolean validateData(Object data) {
        return data instanceof IISO27kElement[] ||
                data instanceof IISO27kElement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see sernet.gs.ui.rcp.main.bsi.dnd.transfer.VeriniceElementTransfer#
     * doJavaToNative(byte[], org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public void doJavaToNative(byte[] byteArray, TransferData transferData) {

        super.javaToNative(byteArray, transferData);

    }

}
