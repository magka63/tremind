/*
 * Copyright 2005:
 *  Jonas S��v <js@acesimulation.com>
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

package mind;

/**
 * <p>Title: Global String Constants</p>
 * <p>Description: String constants used for various things, e.g. string values
 * in UserSettingConstants or items in comboboxes etc.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version $Revision: 1.3 $ $Date: 2007/12/11 14:23:19 $
 */

public interface GlobalStringConstants {

  final public String reMIND_version = "3.0";

  // Strings used in optimization automation
  final public static String OPT_NONE  = "None";
  final public static String OPT_CPLEX = "CPLEX";
  final public static String OPT_LPSOLVE = "LP_Solve";
  final public static String CPLEX_COMMAND_FILE = "commands.ocf";
  final public static String LPSOLVE_COMMAND_FILE = "lpsolvecommands.ocf";
  // Added by Nawzad Mrdan 20090319
  final public static String OPT_EXCEL  = "Excel";
  final public static String OPT_NOTEPAD  = "Notepad";


}