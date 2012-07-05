/*******************************************************************************
 * Copyright (c) 2012 Daniel Murygin.
 *
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,    
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murygin <dm[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package sernet.verinice.bpm;

import org.apache.log4j.Logger;
import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

import sernet.gs.server.security.DummyAuthentication;
import sernet.gs.service.ServerInitializer;

/**
 *
 *
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class Reminder implements EventListener  {

    private static final Logger LOG = Logger.getLogger(Reminder.class);
    
    private DummyAuthentication authentication = new DummyAuthentication(); 
    
    String taskType;
    String assignee;
    String uuid; 
    
    public void sendEmail(String taskType, String assignee, String uuid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sendEmail called...");           
        }  
        
        ServerInitializer.inheritVeriniceContextState();      
        
        // NotificationJob can not do a real login
        // authentication is a fake instance to run secured commands and dao actions
        // without a login
        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(authentication);
             
        IEmailHandler handler = EmailHandlerFactory.getHandler(taskType);
        handler.send(assignee, uuid);
    }
    
    /* (non-Javadoc)
     * @see org.jbpm.api.listener.EventListener#notify(org.jbpm.api.listener.EventListenerExecution)
     */
    @Override
    public void notify(EventListenerExecution execution) throws Exception {
        sendEmail(taskType, assignee, uuid);   
    }

}