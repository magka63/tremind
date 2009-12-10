/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terlegård <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter Åstrand <petas096@student.liu.se>
 *
 * This file is part of reMIND.
 *
 * reMIND is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * reMIND is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with reMIND; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/**
 * This class contains all the constants that the user settings file
 * uses.
 *
 * @author Tim Terlegård
 * @version 2001-04-08
 */

package mind.gui;

public interface UserSettingConstants
{
    final static String AUTOSAVE = "Autosave";
    final static String AUTOSAVE_TIMER = "3";
    final static String AUTOSAVE_STATE = "AutosaveState";
    final static String DATE_IN_OPT_FILE = "DateInOPTFile";
    final static String DATE_IN_MPS_FILE = "DateInMPSFile";
    /*Added by PUM5 2007-12-06*/
    final static String DATE_IN_XML_FILE = "DateInXMLFile";
    //End PUM5
    final static String SHOW_OPT_OUT = "ShowOptOut";
    final static String OPTIMIZER = "Optimizer";
    // Added by Nawzad Mardan 090318
    final static String OPENWITH = "OpenWith";
    final static String EXCEL_PATH = "ExcelPath";
    final static String NOTEPAD_PATH = "NotepadPath";
    // END
    final static String CPLEX_PATH = "CplexPath";
    final static String LPSOLVE_PATH = "LPSolvePath";
    final static String RMD_MODEL_FOLDER  = "RmdModelFolder";
    final static String MPS_LOAD_FOLDER = "MpsLoadFolder";
    final static String OPT_LOAD_FOLDER = "OptLoadFolder";
    final static String MPS_EXPORT_FOLDER = "MpsExportFolder";
    /*Added by PUM5 2007-11-30*/
    final static String XML_EXPORT_FOLDER = "XmlExportFolder";
    //end PUM5
    final static String GUI_SETTINGS_SHOW_NODE_LABELS = "ShowNodeLabels";
    final static String GUI_SETTINGS_SHOW_NODE_IDS = "ShowNodeIDs";
    final static String GUI_SETTINGS_SHOW_FLOW_LABELS = "ShowFlowLabels";
    final static String GUI_SETTINGS_SHOW_FLOW_IDS = "ShowFlowIDs";
    final static String MPS_SETTINGS_EXPORT_ALL_TIMESTEPS = "MpsExportAllTimeSteps";
    final static String MPS_INFINITY_DEFINITIION = "MpsInfinityDefinition";
    // Added by Nawzad Mardan 2008-08-25
    final static String MPS_DIMINUTIVE_DEFINITIION = "MpsDiminutiveDefinition";
    final static String HEADER = "reMIND user settings";
    final static String SAVE_FILE = "settings.ini";

    final static String AREA_INC_WIDTH = "AreaIncWidth";
    final static String AREA_INC_HEIGHT = "AreaIncHeight";
}
