/*******************************************************************************
 * Copyright (c) 2015 Sebastian Hagedorn.
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
 *     Sebastian Hagedorn <sh[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package sernet.gs.ui.rcp.main.service.migrationcommands;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import sernet.hui.common.connect.HUITypeFactory;
import sernet.hui.common.connect.HuiRelation;
import sernet.verinice.interfaces.CommandException;
import sernet.verinice.interfaces.IBaseDao;
import sernet.verinice.model.bsi.BausteinUmsetzung;
import sernet.verinice.model.bsi.ITVerbund;
import sernet.verinice.model.bsi.MassnahmenUmsetzung;
import sernet.verinice.model.bsi.NetzKomponente;
import sernet.verinice.model.bsi.SonstIT;
import sernet.verinice.model.bsi.TelefonKomponente;
import sernet.verinice.model.bsi.risikoanalyse.FinishedRiskAnalysis;
import sernet.verinice.model.bsi.risikoanalyse.GefaehrdungsUmsetzung;
import sernet.verinice.model.common.CnALink;
import sernet.verinice.model.ds.StellungnahmeDSB;
import sernet.verinice.model.ds.VerantwortlicheStelle;
import sernet.verinice.model.iso27k.IncidentGroup;
import sernet.verinice.service.commands.CreateLink;

/**
 * @author Sebastian Hagedorn <sh[at]sernet[dot]de>
 */
public class MigrateDbTo1_03D extends DbMigration {
    
    private final static String HQL_ALL_LINKTYPES = "select source.objectType, target.objectType,"
            + " source.uuid, target.uuid, link"
            + " from CnALink link, CnATreeElement source, CnATreeElement target" 
            + " where link.id.dependantId = source.dbId"
            + " and link.id.dependencyId = target.dbId";
    private transient Logger log;

    @Override
    public void execute() {
        
        IBaseDao<CnALink, Serializable> linkDao = getDaoFactory().getDAO(CnALink.class);
        
//        Object o = linkDao.findByCallback(new HibernateCallback() {
//            
//            @Override
//            public Object doInHibernate(Session session) throws HibernateException, SQLException {
//                return session.createSQLQuery(SQL_ALL_LINKTYPES).list();
//            }
//        });
        List<Object[]> hqlResultList = linkDao.findByQuery(HQL_ALL_LINKTYPES, new Object[] {});
        
        for(Object[] result : hqlResultList) {
            String sourceEntityType = ensureTypeIDisUsed((String)result[0]);
            String targetEntityType = ensureTypeIDisUsed((String)result[1]);
            String sourceUuid = (String)result[2];
            String targetUuid = (String)result[3];
            CnALink link = (CnALink)result[4];
            String relationId = link.getRelationId();
            if(StringUtils.isNotEmpty(relationId) && !isExistantId(sourceEntityType, targetEntityType, relationId)){
                if(getLog().isDebugEnabled()) {
                    getLog().debug("RelationId: <" + relationId + "> is not defined for [" + sourceEntityType + "]=>[" + targetEntityType + "]");
                    getLog().debug("repairing relation");
                }
                try {
                    // create new, corrected, link
                    // note that target and source are switched here on purpose
                    CreateLink createLinkCommand = new CreateLink(targetUuid, sourceUuid, relationId);
                    CnALink repairedLink = getCommandService().executeCommand(createLinkCommand).getLink();
                    if(repairedLink != null && getLog().isDebugEnabled()) {
                        getLog().debug("Operation succeeded. Link repaired and alive!");
                    }

                    // delete broken link
                    linkDao.delete(link);
                } catch (CommandException e) {
                    getLog().error("Error occured while trying to repair a broken (wrong relationId) link", e);
                }

            } else {
                if(getLog().isDebugEnabled()) {
                    getLog().debug("RelationId: <" + relationId + "> is well defined for [" + sourceEntityType + "]=>[" + targetEntityType + "]");
                }
            }
        }

//        super.updateVersion();

    }
    
    private String ensureTypeIDisUsed(String typeId) {
        if(BausteinUmsetzung.HIBERNATE_TYPE_ID.equals(typeId)) {
            return BausteinUmsetzung.TYPE_ID;
        } else if(MassnahmenUmsetzung.HIBERNATE_TYPE_ID.equals(typeId) || "risiko-massnahmen-umsetzung".equals(typeId)) {
            return MassnahmenUmsetzung.TYPE_ID;
        } else if("gefaehrdungs-umsetzung".equals(typeId)) {
            return GefaehrdungsUmsetzung.TYPE_ID;
        } else if("finished-risk-analysis".equals(typeId)) {
            return FinishedRiskAnalysis.TYPE_ID;
        } else if("sonst-it".equals(typeId)) {
            return SonstIT.TYPE_ID;
        } else if("netz-komponente".equals(typeId)) {
            return NetzKomponente.TYPE_ID;
        } else if("telefon-komponente".equals(typeId)) {
            return TelefonKomponente.TYPE_ID;
        } else if("it-verbund".equals(typeId)) {
            return ITVerbund.TYPE_ID;
        } else if("verantwortliche-stelle".equals(typeId)) {
            return VerantwortlicheStelle.TYPE_ID;
        } else if("stellungnahme-dsb".equals(typeId)) {
            return StellungnahmeDSB.TYPE_ID;
        } else if("incidentgroup".equals(typeId)) {
            return IncidentGroup.TYPE_ID;
        }
        return typeId;
    }
    
    private boolean isExistantId(String sourceEntityType, String targetEntityType, String relationId) {
        for(HuiRelation relation : HUITypeFactory.getInstance().getPossibleRelations(sourceEntityType, targetEntityType)) {
            if(relation.getId().equals(relationId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getVersion() {
        return 1.03D;
    }
    
    private Logger getLog() {
        if (log == null)
            log = Logger.getLogger(MigrateDbTo1_02D.class);
        return log;
    }

}
