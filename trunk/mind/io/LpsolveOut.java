/*
 * Copyright 2004
 * Johan Bengtgsson <johbe496@student.liu.se>
 * Daniel Campos <danca226@student.liu.se>
 * Martin Fagerfj‰ll <marfa233@student.liu.se>
 * Daniel Ferm <danfe666@student.liu.se>
 * Able Mahari <ablma616@student.liu.se>
 * Andreas Remar <andre063@student.liu.se>
 * Haider Shareef <haish292@student.liu.se>
 *
 * Copyright 2005
 * Jonas Saav <js@acesimulation.com>
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
package mind.io;

import java.io.File;

import mind.model.*;
import mind.automate.OptimizationResult;
import java.util.NoSuchElementException;
import java.util.Hashtable;

import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.LinkedList;
import java.io.*;


public class LpsolveOut
    implements ModelFileFormat {

  public LpsolveOut()
  {
  }

  /**
   * Gets the file formats extension.
   * @return The extension as a string.
   */
  public static String getExtension()
  {
      return "opt";
  }

  /**
   * Gets the file formats description.
   * @return The description as a string.
   */
  public static String getDescription()
  {
      return "Optimized information for a model";
  }

  /**
   * Loads the model from the opt format.
   * Borked, don't use.
   *
   * @param model The model to load into.
   * @param filename The file to load from.
   * @throws IOException ...
   * @throws FileInteractionException ...
   */
  public void load(Model model, File filename)
      throws IOException, FileInteractionException
  {

  }

  /**
   * Checks an exsisting result file from lp_solve for errors. The only way to
   * do this is to find abnormalities from normal looks of a typical result file:
   *
   * Value of objective function: -2.20153e+007
   *
   * Actual values of the variables:
   * F1T1                        9.522
   * F1T1Fu2P1                   9.522
   * ...
   * ...
   * @param filename Filename of the opt-file to be checked for errors
   * @return true of OK
   */
  public static boolean ResultFileOK(String filename)
      throws FileNotFoundException
  {
    FileReader fileReader;
    BufferedReader bufferedReader;
    String line;
    boolean isOK = false;

    /* Open the file */
    try {
        fileReader = new FileReader(filename);
    } catch(FileNotFoundException e) {
        isOK = false;
        throw new FileNotFoundException("The result file was not found: " + filename);
    }

    bufferedReader = new BufferedReader(fileReader);

    try {
      /* the first line should be empty and the second line should read:
         Value of objective function: some value */
      line = bufferedReader.readLine();
      line = bufferedReader.readLine();
      if (! (line == null)) {
        if (line.lastIndexOf("Value of objective function:") != -1)
          isOK = true;
        bufferedReader.close();
        fileReader.close();
      }
      else {
        return isOK = false;
      }

    }
    catch (IOException ex) {
      /* something is already wrong here!  */
      isOK = false;
    }

    return isOK;
  }

  /**
   * reMIND 2005<br>
   * Read in a LP_SOLVE outfile and put it in an OptimizationResult.
   * The above function that was supposed to read in a CPLEX outfile
   * is borked.
   *
   * @param  filename Name of the lp_solve result file to load.
   * @return An OptimizationResult containing an internal
   *         representation of the optimal result.
   * @throws IOException Thrown whenever the file is unreadable or if it's
   *         the wrong format.
   * @throws FileNotFoundException Thrown if the file can't be found.
   * @see    OptimizationResult
   * @author  Jonas S‰‰v
   */
  public static OptimizationResult load(String filename)
      throws IOException, FileNotFoundException
  {
    /* A result file from lp_solve looks like this:


     Value of objective function: 41069.2

     Actual values of the variables:
     F100T1                     20.965
     F100T1Fu146P1              20.965
     F102T1                          0
     F102T1Fu133P1                   0
     F102T1Fu133P2                   0
     F103T1                   0.719374
     F103T1Fu28P1             0.719374
     F103T1Fu86P1             0.719374
     F106T1                    35.8515

     */
      OptimizationResult m_result = new OptimizationResult();
      FileReader m_fileReader = null;
      BufferedReader m_reader = null;
      String m_row;
      StringTokenizer m_tokenizer;
      String m_token = null;
      boolean m_store = true;
      Hashtable m_flows = new Hashtable();
      int lineNumber = 0;

      if(filename == null) {
          throw new IllegalArgumentException("Filename is empty (null).");
      }

      /* open the file */
      try {
          m_fileReader = new FileReader(filename);
      } catch(FileNotFoundException e) {
          throw new FileNotFoundException("The result file was not found: " + filename);
      }
      m_reader = new BufferedReader(m_fileReader);

      /* find the objective value */
          try {
              m_row = m_reader.readLine();
              m_row = m_reader.readLine();
              lineNumber++;lineNumber++;
          } catch(IOException e) {
              m_fileReader.close();
              throw new IOException(e.getMessage());
          }

          if(m_row == null) {
              /* EOF found */
              m_fileReader.close();
              throw new IOException("Unexpected end of file found.");
          }

          m_tokenizer = new StringTokenizer(m_row);

          try {
            for (int i=1;i<=4;i++) {
              // parse past "Value of objective function:"
              m_token = m_tokenizer.nextToken();
            }
          }
          catch(NoSuchElementException e) {
              /* unexpected result file format */
          }

      /* get the objective value */
      try {
          m_result.globalOptimum = Double.parseDouble(m_tokenizer.nextToken());
      }
      catch(NumberFormatException e){
          m_fileReader.close();
          throw new IOException("Unable to read objective value.");
      }

      /* locate the string "variables:"   (data comes after this) */
          m_token = "";
          try {
              m_row = m_reader.readLine();
              m_row = m_reader.readLine();
              lineNumber++;lineNumber++;
          } catch(IOException e) {
              m_fileReader.close();
              throw new IOException(e.getMessage());
          }

          if(m_row == null) {
              /* EOF found */
              m_fileReader.close();
              throw new IOException("Unexpected end of file found.");
          }

          m_tokenizer = new StringTokenizer(m_row);

          try {
            for (int i=1;i<=5;i++) {
              // parse past "Value of objective function:"
              m_token = m_tokenizer.nextToken();
            }
          }
          catch(NoSuchElementException e) {
              /* unexpected result file format */
          }


      /* "variables:" has been found, Now start reading data */

      /* here comes the data! */
      while(m_row != null) {
          m_row = m_reader.readLine();
          lineNumber++;

          /* check if we're at EOF */
          if(m_row == null)
              break;

          m_tokenizer = new StringTokenizer(m_row);

          String m_tempId;
          try {
              m_tempId = m_tokenizer.nextToken();
          }
          catch(NoSuchElementException e) {
              System.out.println("Row: " + lineNumber);
              System.out.println(m_row);
              System.out.println("Token: " + m_token);
              m_fileReader.close();
              throw new IOException("Unexpected end of tokens found.");
          }

          double m_value;
          try {
              m_value = Double.parseDouble(m_tokenizer.nextToken());
          }
          catch(NumberFormatException e){
              System.out.println("Row: " + lineNumber);
              System.out.println(m_row);
              System.out.println("Token: " + m_token);
              m_fileReader.close();
              throw new IOException("Unable to read value.");
          }
          catch(NoSuchElementException e) {
              System.out.println("Row: " + lineNumber);
              System.out.println(m_row);
              System.out.println("Token: " + m_token);
              m_fileReader.close();
              throw new IOException("Unexpected end of tokens found.");
          }

          /* parse tempId to extract ID and timestep */
          String m_id = null;
          String m_timestep = null;
          int m_intId;
          int m_intTimestep;

          m_store = true;

          /* check if this tempId is a flow */
          try {
              if(m_tempId.charAt(0) != 'F') {
                  continue;
              }
              else if(!(m_tempId.charAt(1) >= '0'
                        && m_tempId.charAt(1) <= '9')) {
                  continue;
              }
          } catch(Exception e) {
              /* some kind of OOB exception, ignore this id */
              continue;
          }

          /* parse ID in tempId */
          m_id = "";
          int j = 1;
          for(int i = 1;i < m_tempId.length();i++) {
              char m_ch = m_tempId.charAt(i);
              if(m_ch >= '0' && m_ch <= '9')
                  m_id += m_ch;
              else {
                  j = i + 1;
                  break; /* found end of ID */
              }
          }
          try {
              m_intId = Integer.parseInt(m_id);
          }
          catch(NumberFormatException e) {
              System.out.println("m_tempId = " + m_tempId);
              System.out.println("Row: " + lineNumber);
              System.out.println(m_row);
              System.out.println("Token: " + m_token);
              m_fileReader.close();
              throw new IOException("Bad parsing of id number. ("
                                    + m_tempId + ")");
          }

          /* parse timestep */
          m_timestep = "";

          /* find 'T' by parsing backwards */
          for(int k = m_tempId.length()-1;k >= 0;k--) {
              if(m_tempId.charAt(k) == 'T') {
                  /* found it! */
                  j = k + 1;
                  break;
              }
              if(k == 0) {
                  /* bad Id, don't save */
                  m_store = false;
              }
          }

          for(;j < m_tempId.length();j++) {
              char m_ch = m_tempId.charAt(j);
              if(m_ch >= '0' && m_ch <= '9')
                  m_timestep += m_ch;
              else if(m_ch == ' ')
                  break; /* found end of timestep*/
              else {
                  m_store = false; /* found something else, this means
                                  * we found a temp variable */
              }
          }

          if(m_store == false)
              continue;

          try {
              m_intTimestep = Integer.parseInt(m_timestep);
          }
          catch(NumberFormatException e) {
              System.out.println("m_timestep = " + m_timestep);
              System.out.println("Row: " + lineNumber);
              System.out.println(m_row);
              System.out.println("Token: " + m_token);
              m_fileReader.close();
              throw new IOException("Bad parsing of timestep. ("
                                    + m_timestep + ")");
          }

          /* ignore 0 timesteps */
          if(m_intTimestep == 0)
              continue;

          if(m_store) {

              /* get the hashtable for this flow */
              Hashtable m_flow =
                  (Hashtable)m_flows.get(new Integer(m_intId));
              if(m_flow == null) {

                  /* this is a new flow, create a new hashtable and
                   * insert it into the flow hashtable */
                  m_flow = new Hashtable();
                  m_flows.put(new Integer(m_intId), m_flow);
              }
              m_flow.put(new Integer(m_intTimestep), new Double(m_value));
          }
      }

      /* Now everything is stored in a hashtable of hashtables.
       * Convert this to an OptimizationResult. */

      /* the number of flows */
      double m_values[];

      Enumeration enu = m_flows.keys();
      LinkedList keys = new LinkedList();

      /* reverse list of keys */
      while(enu.hasMoreElements()) {
          keys.addLast(enu.nextElement());
      }

      /* iterate over all the flows */
      while(keys.size() > 0) {
          Integer m_flowId = (Integer)keys.removeLast();

          Hashtable m_flow = (Hashtable)m_flows.get(m_flowId);
          m_values = new double[m_flow.size()];
          m_values[0] = 0.0;

          /* iterate through all the timesteps */
          for(int j = 1;j <= m_values.length;j++) {
              m_values[j - 1] =
                  ((Double)m_flow.get(new Integer(j))).doubleValue();
          }
          m_result.addFlow(new ID(new Long(m_flowId.intValue()), "F"), m_values);
      }

      m_fileReader.close();

      return m_result;
  }

  /**
   * Reads a resultfile to a string.
   * @param filename
   * @return a string representation of the file
   * @throws IOException
   */
  public static String readFile(String filename) throws IOException {
   //you can define 'lineSep' elsewhere in the class so that it isn't
   //recreated every time the method is called
   String lineSep = System.getProperty("line.separator");
   BufferedReader br = new BufferedReader(new FileReader(filename));
   String nextLine = "";
   StringBuffer sb = new StringBuffer();
   while ((nextLine = br.readLine()) != null) {
     sb.append(nextLine);
     //BufferedReader strips the EOL character so we add it back
     sb.append(lineSep);
   }
   return sb.toString();
}


  /**
   * It is not possible to save a LpsolveOut file.
   * @param model Looking good.
   * @param filename Name of file.
   */
  public void save( Model model, File filename )
  {
  }

}