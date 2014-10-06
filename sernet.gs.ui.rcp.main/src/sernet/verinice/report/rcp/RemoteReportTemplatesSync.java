/*******************************************************************************
 * Copyright (c) 2014 Benjamin Weißenfels.
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
 *     Benjamin Weißenfels <bw[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package sernet.verinice.report.rcp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import sernet.gs.ui.rcp.main.CnAWorkspace;
import sernet.gs.ui.rcp.main.service.ServiceFactory;
import sernet.verinice.interfaces.IReportDepositService;
import sernet.verinice.model.report.PropertyFileExistsException;
import sernet.verinice.model.report.ReportMetaDataException;
import sernet.verinice.model.report.ReportTemplate;
import sernet.verinice.model.report.ReportTemplateMetaData;

/**
 * Copies the remote templates into a the local server report templates folder.
 *
 * @author Benjamin Weißenfels <bw[at]sernet[dot]de>
 *
 */
public class RemoteReportTemplatesSync {

    public void syncReportTemplates() throws IOException, ReportMetaDataException, PropertyFileExistsException {

        String[] fileNames = getLocalServerReportTemplateFileNames();
        Set<ReportTemplateMetaData> localServerTemplates = getIReportDepositService().getReportTemplates(fileNames);
        Set<ReportTemplateMetaData> remoteSeverTemplates = getIReportDepositService().getServerReportTemplates();

        for (ReportTemplateMetaData remoteTemplateMetaData : remoteSeverTemplates) {
            if (!localServerTemplates.contains(remoteTemplateMetaData)) {
                syncTemplate(remoteTemplateMetaData);
            }
        }
    }

    private IReportDepositService getIReportDepositService() {
        return ServiceFactory.lookupReportDepositService();
    }

    @SuppressWarnings("rawtypes")
    private String[] getLocalServerReportTemplateFileNames() {
        List<String> list = new ArrayList<String>();
        IOFileFilter filter = new SuffixFileFilter("rptdesign", IOCase.INSENSITIVE);
        Iterator<File> iter = FileUtils.iterateFiles(new File(CnAWorkspace.getInstance().getRemoteReportTemplateDir()), filter, null);
        while (iter.hasNext()) {
            list.add(iter.next().getAbsolutePath());
        }
        return list.toArray(new String[list.size()]);
    }

    private void syncTemplate(ReportTemplateMetaData metadata) throws IOException {
        ReportTemplate template = getIReportDepositService().getReportTemplate(metadata);
        String directory = CnAWorkspace.getInstance().getRemoteReportTemplateDir();
        File rptdesignTemplate = new File(FilenameUtils.concat(directory, template.getMetaData().getFilename()));
        FileUtils.writeByteArrayToFile(rptdesignTemplate, template.getRptdesignFile());
    }

}