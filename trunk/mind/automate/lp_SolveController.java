/*
 * Copyright 2005:
 *  Jonas S‰‰v <js@acesimulation.com>
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

package mind.automate;

import mind.automate.Pair;
import mind.io.OptimizationCommandFile;
import mind.io.LpsolveOut;
import mind.GlobalStringConstants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JOptionPane;

/**
 * Class for automatic optimization using the external program lp_solve.
 *
 * @author  Jonas Saav
 * @version $Revision: 1.2 $ $Date: 2005/05/06 10:22:45 $
 */

public class lp_SolveController extends OptimizeController {

  /**
   * Perform automatic optimization of the MPS file using Runtime.
   *
   * @param filename The MPS file to optimize.
   * @return A pair containing stdout from LP_Solve and an OptimizationResult.
   *         If the OptimizationResult is null, there was an error.
   */

   public Pair optimize(String filename, String lpsolvePath)
       throws IOException, FileNotFoundException
   {
       boolean optimizationSuccessful = true;
       OptimizationResult result = null;
       String lpsolveOutput = "";

       if(filename == null) {
           throw new IllegalArgumentException("Filename is empty (null).");
       }
       if(lpsolvePath == null) {
           throw new IllegalArgumentException("Path to LPSOLVE is"
                                              + " empty (null).");
       }

       /* fix double backslash for MS-DOG */
  //     filename = filename.replaceAll("\\\\", "\\\\\\\\");
  //     lpsolvePath = lpsolvePath.replaceAll("\\\\", "\\\\\\\\");


       /* Construct the name of the result file */
        String fileWithoutExtension = getFilenameWithoutExtension(filename);
        String resultFile = fileWithoutExtension + "." + LpsolveOut.getExtension();

       /* read in LPSOLVE command options from an OCF file */
       String commands = null;
       try {
           commands = OptimizationCommandFile.load(GlobalStringConstants.LPSOLVE_COMMAND_FILE);
           /* get rid of line separators. commands is not really commands, but flags and must not
              be separated by line separators */
           java.util.StringTokenizer tkn = new java.util.StringTokenizer(commands);
           commands = "";
           while (tkn.hasMoreTokens()) {
             commands += tkn.nextToken() + " ";
           }
       }
       catch(FileNotFoundException e) {
           throw new FileNotFoundException("Can't find " + GlobalStringConstants.LPSOLVE_COMMAND_FILE);
       }
       catch(IOException e) {
           throw new IOException("Couldn't read command file.");
       }


       /* run LPSOLVE with Runtime */
       /* Create a temporary bat file */

       Runtime runtime = Runtime.getRuntime();
       Process lpsolve_proc = null;
       try {
         FileWriter batfile = new FileWriter("run_lpsolve.bat");
         String execstring = lpsolvePath + " " + commands + " < " + filename + " > " + resultFile;
         batfile.write(execstring);
         batfile.close();

         lpsolve_proc = runtime.exec("run_lpsolve.bat");

       } catch(SecurityException e) {
           throw new IOException(e.getMessage());
       } catch(IOException e) {
           throw new IOException(e.getMessage());
       } catch(NullPointerException e) {
           throw new IllegalArgumentException(e.getMessage());
       } catch(IllegalArgumentException e) {
           throw new IllegalArgumentException(e.getMessage());
       }


      /* wait for lp_solve to finnish. The main thread is locked during execution */
      try {
        lpsolve_proc.waitFor();
      }
      catch (InterruptedException ex) {
        JOptionPane.showMessageDialog(null, "Optimizer was interupted: " + ex.getMessage());
        optimizationSuccessful = false;
      }

       if(optimizationSuccessful && LpsolveOut.ResultFileOK(resultFile)) {
           /* load result with LpsolveOut */
           try {
               result = LpsolveOut.load(resultFile);
           }
           catch(FileNotFoundException e) {
               throw new FileNotFoundException(e.getMessage());
           }
           catch(IOException e) {
               throw new IOException("Unable to parse " + resultFile + " "
                                     + e.getMessage());
           }
       }

      try {
        lpsolveOutput = LpsolveOut.readFile(resultFile);
      }
      catch (FileNotFoundException ex) {
        throw new FileNotFoundException(ex.getMessage());
      }

       /* pair stdout from CPLEX with return value from CplexOut.load */
       Pair p = new Pair();
       p.left = lpsolveOutput;
       p.right = result;

       /* return this Pair */
       return p;
  }
  public Pair load(String filename) throws FileNotFoundException, IOException {
    /* load result with LPSolveOut */
    OptimizationResult result = null;
    try {
        result = LpsolveOut.load(filename);
    } catch(FileNotFoundException e) {
        throw new FileNotFoundException(e.getMessage());
    }catch(IOException e) {
        throw new IOException(e.getMessage());
    }

    /* pair "Optimizer not called" with return value from CplexOut.load */
    Pair p = new Pair();
    p.left = "Optimizer not called";
    p.right = result;

    /* return this Pair */
    return p;
  }

}