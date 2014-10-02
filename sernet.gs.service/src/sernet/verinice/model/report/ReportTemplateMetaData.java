/*******************************************************************************
 * Copyright (c) 2014 Sebastian Hagedorn <sh@sernet.de>.
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
package sernet.verinice.model.report;

import java.io.Serializable;

import sernet.verinice.interfaces.IReportDepositService.OutputFormat;



/**
 *
 */
public class ReportTemplateMetaData implements Serializable{
    
    private static final long serialVersionUID = 201410011436L;

    private String filename;
    
    private OutputFormat[] outputFormat;
    
    private String outputname;
    
    private boolean isServerTemplate;
    
    public ReportTemplateMetaData(String filename, String outputname, OutputFormat[] outputFormats, boolean serverTemplate){
        this.filename = filename;
        this.outputname = outputname;
        this.outputFormat = outputFormats;
        this.isServerTemplate = serverTemplate;
    }

    public String getFilename() {
        return filename;
    }

    public OutputFormat[] getOutputFormats() {
        return outputFormat;
    }

    public String getOutputname() {
        return outputname;
    }
    
    public boolean isServerTemplate() {
        return isServerTemplate;
    }
    
   

}