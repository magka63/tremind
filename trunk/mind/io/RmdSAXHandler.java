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
package mind.io;

import java.awt.Point;
import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import java.util.Iterator;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Hashtable;

import mind.model.*;
import mind.gui.*;
import mind.gui.dialog.MainDialog;

/**
 * EventHandler for the SAX parsing of Rmd files
 *
 * @author Johan Trygg
 * @author Hans-Fredric Österlund
 * @version 2003-09-09
 */

public class RmdSAXHandler extends DefaultHandler {
	RmdXmlAppIfc m_AppIfc;

	//Variables for temporary starage
	private Stack data;

	private Hashtable nodes;

	private String charStr;

	// Misc class members
	private int error;

	private Locator locator;

	public RmdSAXHandler() {
		Exception e = new Exception();
		System.out.println("Cannot use this RmdSAXHandler constructor "
				+ "(nice error message?)");
		e.printStackTrace(System.out);
		System.exit(1);
	}

	public RmdSAXHandler(Model m, GraphModel gm) throws SAXException {
		m_AppIfc = new RmdXmlAppIfc();
		m_AppIfc.m_Model = m;
		m_AppIfc.m_GraphModel = gm;
	}

	/**
	 * Parse a node without any model
	 *
	 */
	//public RmdSAXHandler(Node node, ResourceControl rcntl, Timesteplevel topTSL, DiscountedsystemcostControl dcsc)
	public RmdSAXHandler(Node node, ResourceControl rcntl, Timesteplevel topTSL)
        throws SAXException {
		if (node == null) {
			throw new SAXException("The node cannot be null");
		}
		m_AppIfc = new RmdXmlAppIfc();
		m_AppIfc.m_Node = node;
		m_AppIfc.m_ResCntl = rcntl;
		m_AppIfc.m_TopTSL = topTSL;
                //Added by Nawzad Mardan
               // m_AppIfc.c_discountedsystemcostControl = dcsc;
	}

	//===========================================================
	// Helper sub classes
	//===========================================================

	public class RmdXmlAppIfc {
		public RmdXmlAppIfc() {
		}

		//Used when parsing a singe node
		public Node m_Node;

		public ResourceControl m_ResCntl;

		public Timesteplevel m_TopTSL;

		//Used when parsing a model
		public Model m_Model;

		public GraphModel m_GraphModel;

		public Hashtable m_NodeIds;
                
	}

	public class RmdXmlObject extends Object {
		private String m_Tag; // Item Tag: model, node, flow, etc.

		private Vector m_Children; // Sub item vector

		private Vector m_AttrTag; // Matching Tag/Value pair vectors, but since the same tag

		private Vector m_AttrValue; // can occure multiple times a hash can't be used.

		public RmdXmlObject(String tag) {
			m_Tag = tag;
		}

		public String getTag() {
			return m_Tag;
		}

		private void splitToStringVector(Vector v, String s, String r) {
			String sTmp = (s.length() < 2) ? "" : s
					.substring(1, s.length() - 1); // sTmp = s without "[]"
			String[] sVec = sTmp.split(r);
			int nPos = 0;
			while (nPos < sVec.length) {
				v.add(sVec[nPos].trim());
				nPos++;
			}
		}

		private void splitToFloatVector(Vector v, String s, String r) {
			Vector sVec = new Vector();
			int nPos = 0;

			splitToStringVector(sVec, s, r);

			while (nPos < sVec.size()) {
				String str = (String) sVec.get(nPos);
				v.add(new Float(str));
				nPos++;
			}
		}

		public boolean isSingleValue() {
			if (m_Children == null) {
				if (m_AttrTag == null) {
					// Single value with the value == "". For empty value items the callback to
					// characters is not performed and this is a fix for that.
					addAttr(m_Tag, "");
					return true;
				}
				if ((m_AttrTag.size() == 1) && (m_Tag.equals(getAttrTag(0)))) {
					return true;
				}
			}

			return false;
		}

		public int numAttr() {
			int num = 0;
			if (m_AttrTag != null)
				num = m_AttrTag.size();
			return num;
		}

		public int numChildren() {
			int num = 0;
			if (m_Children != null)
				num = m_Children.size();
			return num;
		}

		public void addAttr(String name, String value) {
			if (m_AttrValue == null)
				m_AttrValue = new Vector();
			if (m_AttrTag == null)
				m_AttrTag = new Vector();
			m_AttrTag.add(name);
			m_AttrValue.add(value);
		}

		public String getAttr(int ix) {
			String AttrVal = null;

			if ((0 <= ix) && (ix < numAttr())) {
				AttrVal = (String) m_AttrValue.get(ix);
			}

			return AttrVal;
		}

		public String getAttr(String name) {
			int ix;
			for (ix = 0; ix < numAttr(); ix++) {
				if (name.equals(m_AttrTag.get(ix))) {
					return getAttr(ix);
				}
			}

			return null;
		}

		public String getAttrTag(int ix) {
			String name = null;

			if ((0 <= ix) && (ix < numAttr())) {
				name = (String) m_AttrTag.get(ix);
			}

			return name;
		}

		public void addObject(RmdXmlObject obj) {
			if (obj != null) {
				if (obj.isSingleValue()) {
					addAttr(obj.getAttrTag(0), obj.getAttr(0));
				} else {
					if (m_Children == null) {
						m_Children = new Vector();
					}
					m_Children.add(obj);
				}
			}
		}

		public RmdXmlObject getChild(int ix) {
			RmdXmlObject obj = null;

			if ((0 <= ix) && (ix < numChildren())) {
				obj = (RmdXmlObject) m_Children.get(ix);
			}

			return obj;
		}

		public void addToList(LinkedList l) {
			int ix = 0;

			if (!isSingleValue())
				l.add(getTag());

			for (ix = 0; ix < numAttr(); ix++) {
				l.add(m_AttrTag.get(ix));
				l.add(m_AttrValue.get(ix));
			}

			for (ix = 0; ix < numChildren(); ix++) {
				getChild(ix).addToList(l);
			}
		}

		public void doSetup(RmdXmlObject parent, RmdXmlAppIfc appIfc)
				throws SAXException {
			// Called after completed parsing of the input file.
			int ix;
			if (m_Tag.equals("objectfunction")) {
				String label = "";
				float k1 = 0.0f;
				float k2 = 0.0f;
				Vector bounds = new Vector();
				Vector limits1 = new Vector();
				Vector limits2 = new Vector();
		
				for (ix = 0; ix < numAttr(); ix++) {
					String tag = getAttrTag(ix);
					String value = getAttr(ix);
					if (tag.equals("label"))
						label = value;
					if (tag.equals("k1"))
						k1 = Float.parseFloat(value);
					if (tag.equals("k2"))
						k2 = Float.parseFloat(value);
					if (tag.equals("boundstring"))
						bounds.add(value);
					if (tag.equals("limit1value"))
						limits1.add(new Float(value));
					if (tag.equals("limit2value"))
						limits2.add(new Float(value));
				}
				if ((appIfc != null) && (appIfc.m_Model != null)) {
					if(bounds.size() == limits1.size() && bounds.size() == limits2.size()){
						appIfc.m_Model.addObjectFunction(label, k1, k2, bounds,
							limits1, limits2);
					}else{
						throw new SAXException(
								makeError("Error when reading objectfunction" +
										" whith label '" + label + "'"));
					}
			}
		
			} else if (m_Tag.equals("resource")) {
				String label = "";
				String unit = "";
				String prefix = "";
				String colorname = "Black";
				int colorvalue = java.awt.Color.black.getRGB();

				for (ix = 0; ix < numAttr(); ix++) {
					String tag = getAttrTag(ix);
					String value = getAttr(ix);
					if (tag.equals("type"))
						label = value;
					if (tag.equals("unit"))
						unit = value;
					if (tag.equals("prefix"))
						prefix = value;
					if (tag.equals("colorname"))
						colorname = value;
					if (tag.equals("colorvalue"))
						colorvalue = Integer.parseInt(value);
				}

				if ((appIfc != null) && (appIfc.m_Model != null)) {
					appIfc.m_Model.addResource(label, unit, prefix,
							new ExtendedColor(colorname, colorvalue));
				}
			} else if (m_Tag.equals("node")) {
				String id = "";
				String label = "";
				String note = "";
				String tLevel = "none";
				for (ix = 0; ix < numAttr(); ix++) {
					String tag = getAttrTag(ix);
					String value = getAttr(ix);
					if (tag.equals("id"))
						id = value;
					if (tag.equals("label"))
						label = value;
					if (tag.equals("note"))
						note = value;
					if (tag.equals("tLevel"))
						tLevel = value;
				}

				ID currId;
				if (id.length() == 0) {
					try {
						currId = appIfc.m_Model.addNode("Blank Node");
					} catch (ModelException e) {
						throw new SAXException(makeError(e.toString()));//This should not happen, I think.
					}
				} else {
					try {
						currId = appIfc.m_Model.addNodeWithID(id);
					} catch (ModelException e) {
						throw new SAXException(
								makeError("Could not create node '" + id
										+ "' (" + e.toString() + ")"));
					}
				}
				Node currNode = (Node) appIfc.m_Model.getNode(currId);
				appIfc.m_GraphModel.add(currId); //Coordinates will be added later
				appIfc.m_NodeIds.put(currId.toString(), currId); //Add nodeID to nodes hashtable

				Timesteplevel tsl = appIfc.m_Model.getTimesteplevel(tLevel);
				if (tsl == null) {
					throw new SAXException(makeError("The timesteplevel '"
							+ tLevel + "' is not defined."));
				}

				currNode.setTimesteplevel(tsl);
				currNode.setLabel(label);
				currNode.setNote(note);
			} else if (m_Tag.equals("timesteplevel")) {
				int divide = 1;
				String name = "TOP";
				Vector labelVec = new Vector();
				Vector lengthVec = new Vector();

				for (ix = 0; ix < numAttr(); ix++) {
					String tag = getAttrTag(ix);
					String value = getAttr(ix);
					if (tag.equals("name"))
						name = value;
					if (tag.equals("divide"))
						divide = Integer.parseInt(value);
					if (tag.equals("labelVector"))
						splitToStringVector(labelVec, value, ",");
					if (tag.equals("lengthVector"))
						splitToFloatVector(lengthVec, value, ",");
				}

				if (divide < 1) {
					throw new SAXException(makeError("The 'divide' field of a "
							+ "timesteplevel must be a positive integer >= 1"));
				}

				if (lengthVec.size() != labelVec.size()) {
					throw new SAXException(
							makeError("Timestep:Number of element mismatch "
									+ "between the length and label vector."));
				}

				if (appIfc.m_TopTSL == null) {
					//The first timesteplevel is already created and must have divide = 1
					if (divide != 1) {
						throw new SAXException(
								makeError("The 'divide' field of "
										+ "the toplevel must be 1"));
					}
					appIfc.m_TopTSL = appIfc.m_Model.getTopTimesteplevel();
					appIfc.m_Model.changeTimesteplevel(appIfc.m_TopTSL.toInt(),
							name, divide, labelVec, lengthVec);
				} else {
					if (!appIfc.m_Model.addTimesteplevel(name, divide,
							labelVec, lengthVec, true)) {
						throw new SAXException(makeError("The timesteplevel '"
								+ name + "' is already defined"));
					}
				}
			} else if (m_Tag.equals("flow")) {
				String id = null;
				String from = null;
				String to = null;
				String resource = null;
				String label = null;
				for (ix = 0; ix < numAttr(); ix++) {
					String tag = getAttrTag(ix);
					String value = getAttr(ix);
					if (tag.equals("id"))
						id = value;
					if (tag.equals("from"))
						from = value;
					if (tag.equals("to"))
						to = value;
					if (tag.equals("resource.type"))
						resource = value;
					if (tag.equals("label"))
						label = value;
				}
				if ((from == null) || (to == null)) {
					throw new SAXException(
							makeError("A flow must have booth 'from' and 'to' attributes"));
				}

				ID fromID = (ID) appIfc.m_NodeIds.get(from);
				ID toID = (ID) appIfc.m_NodeIds.get(to);
				if (fromID == null)
					throw new SAXException(
							makeError("This flow has an Invalid 'from' ID"));
				if (toID == null)
					throw new SAXException(
							makeError("This flow has an Invalid 'to' ID"));

				ID flowID;
				if (id == null) {
					flowID = appIfc.m_Model.addFlow(fromID, toID);
				} else {
					flowID = appIfc.m_Model.addFlowWithID(id, fromID, toID);
				}

				/* reMIND 2004 */
				try {
					appIfc.m_Model.setFlowLabel(flowID, label);
				} catch (ModelException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					System.exit(1);
				}

				appIfc.m_GraphModel.add(flowID, fromID, toID);

				if (resource != null) {
					ID resourceID = appIfc.m_Model.getResourceID(resource);
					if (resourceID == null) {
						throw new SAXException(makeError("The resource '"
								+ resource + "' is not defined"));
					}
					//Set the resource
					appIfc.m_Model.setResource(flowID, resourceID);
				}
			} else if (m_Tag.equals("functions")) {
				// Get the node attributes that the functions are going to need
				addAttr("id", parent.getAttr("id"));
				addAttr("tLevel", parent.getAttr("tLevel"));
			} else if (m_Tag.equals("guiFlow")) {
				Vector hPoints = new Vector();
				String strID = null;
				int x, y;

				for (ix = 0; ix < numAttr(); ix++) {
					String tag = getAttrTag(ix);
					String value = getAttr(ix);
					if (tag.equals("handle")){						
						String [] arr= value.split(",");
						try {
							x = Integer.parseInt(arr[0]);
							y = Integer.parseInt(arr[1]);
							hPoints.add(new Point(x,y));
						} catch (Exception e) {
							throw new SAXException(makeError("Error when trying "
									+ "to parse a flow handles coordinates"));
						}
					}
					if (tag.equals("id"))
						strID = value;
				}

				try {
					ID flowID = (ID) (appIfc.m_GraphModel.getFlowWithIdString(strID));
					ExtendedGraphFlow egf = m_AppIfc.m_GraphModel.getGraphFlow(flowID);
					for (Iterator iter = hPoints.iterator(); iter.hasNext();) {
						Point pt = (Point) iter.next();
						egf.addHandle(pt);
					}
				} catch (Exception e) {
					//We should not get any errors here!
					System.out.println(e);
					e.printStackTrace(System.out);
					throw new SAXException(makeError("Error when trying "
							+ "to parse a flow handle"));
				}
			} else if (m_Tag.equals("guiNode")) {
				String sX = null;
				String sY = null;
				String sID = null;

				for (ix = 0; ix < numAttr(); ix++) {
					String tag = getAttrTag(ix);
					String value = getAttr(ix);
					if (tag.equals("x"))
						sX = value;
					if (tag.equals("y"))
						sY = value;
					if (tag.equals("id"))
						sID = value;
				}

				ID nodeID = (ID) (appIfc.m_NodeIds.get(sID));
				if (nodeID == null) {
					throw new SAXException(makeError("This node (" + sID
							+ ") is not defined in the model"));
				}
				int x, y;
				try {
					x = Integer.parseInt(sX);
					y = Integer.parseInt(sY);
				} catch (Exception e) {
					throw new SAXException(
							makeError("Coodinates must be integers > 0"));
				}
				if (x < 0 || y < 0) {
					throw new SAXException(
							makeError("Coodinates must be integers > 0"));
				}

				try {
					ID move[] = new ID[1];
					move[0] = nodeID;
					appIfc.m_GraphModel.moveTo(move, x, y);
				} catch (Exception e) {
					//We should not get any errors here!
					System.out.println(e);
					e.printStackTrace(System.out);
					throw new SAXException(makeError("Error when trying "
							+ "to move a node (this cannot happen)"));
				}
			} else if (m_Tag.equals("guiprefs")) {
				//System.out.println("zoom = " + getAttr(0));
				GUI.getStaticToolbar().c_combo.setSelectedIndex(Integer
						.parseInt(getAttr(0)) / 10 - 1);
			}

			// Also set up the child objects, if any
			for (ix = 0; ix < numChildren(); ix++) {
				((RmdXmlObject) m_Children.get(ix)).doSetup(this, appIfc);
			}
		}
	}

	public class RmdXmlFunction extends RmdXmlObject {
		public RmdXmlFunction(String name) {
			super(name);
		}

		public void doSetup(RmdXmlObject parent, RmdXmlAppIfc appIfc)
				throws SAXException {
			// Called after completed parsing of the input file.
			LinkedList list = new LinkedList();
			int nTimeStep = 0;
			int ix;

			addToList(list);

			// Let's trim the list
			// First remove the function tag
			list.removeFirst();

			// Check and reduce timestamp info
			for (ix = 0; ix < list.size(); ix++) {
				if (((String) list.get(ix)).startsWith("timestep.")) {
					// 'timestep.... nr="x"' in the timestep is reduced to "T" before parsing
					// so that the node parser can recognize the timestep data.
					Object oName = list.remove(ix);
					Object oNr = list.remove(ix);
					String sValue = (String) list.remove(ix);
					nTimeStep++;
					if (nTimeStep != Integer.parseInt(sValue)) {
						throw new SAXException(
								makeError("All timesteps must be in order (want step "
										+ nTimeStep + ")"));
					}
					list.add(ix, new String("T"));
				}
			}

			Timesteplevel tsl = null;
			String s = parent.getAttr("tLevel");

			if (appIfc.m_TopTSL != null) {
				tsl = appIfc.m_TopTSL.findLevel(s);
			} else if (appIfc.m_Model != null) {
				tsl = appIfc.m_Model.getTimesteplevel(s);
			}

			if (tsl == null) {
				throw new SAXException(
						makeError("Timesteplevel is null when reading function(s)"));
			} else {
				if (nTimeStep > 0) {
					int last = tsl.timestepDifference(tsl.getTopLevel());
					if (nTimeStep < last) {
						throw new SAXException(
								makeError("To few timesteps defined in function (got "
										+ nTimeStep + ", want " + last + ")"));
					} else if (nTimeStep > last) {
						throw new SAXException(
								makeError("To many timesteps defined in function (got "
										+ nTimeStep + ", want " + last + ")"));
					}
				}
			}
			//we must pass the timesteplevel to the new function too
			list.add(tsl);

			//This line is nice when writing a parseData for a new function
			// System.out.println("Functiondata=" + list);

			try {
				if (appIfc.m_Node != null) {
					appIfc.m_Node.parseAndAddFunction(getTag(), list,
							appIfc.m_ResCntl, true);
				}
				if (appIfc.m_Model != null) {
					ID nodeID = (ID) appIfc.m_NodeIds.get(parent.getAttr("id"));
					appIfc.m_Model.parseAndAddFunction(nodeID, getTag(), list);
				}
			} catch (RmdParseException e) {
				throw new SAXException(makeError(e.getMessage()));
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw new SAXException(
						makeError("This function has illegal attributes"));
			}
		}
	}

	//===========================================================
	// SAX DocumentHandler methods
	//===========================================================

	public void startDocument() throws SAXException {
		// System.out.println("*** Parsing rmd file ***");

		data = new Stack();
		m_AppIfc.m_NodeIds = new Hashtable();
	}

	public void endDocument() throws SAXException {
		// System.out.println("*** Parsing complete *** (last state=" + state + ")");
		m_AppIfc.m_Model = null;
		m_AppIfc.m_GraphModel = null;
		m_AppIfc.m_Node = null;
		m_AppIfc.m_ResCntl = null;
		m_AppIfc.m_TopTSL = null;
		m_AppIfc.m_NodeIds = null;
		m_AppIfc = null;
		data = null;
	}

	public void startElement(String namespaceURI, String lName, // local name
			String qName, // qualified name
			Attributes attrs) throws SAXException {
		boolean isFuntion = false;
		String eName = lName;
		RmdXmlObject curObj;

		if ((lName == null) || (lName.length() == 0))
			eName = qName;

		if (data.size() > 0) {
			curObj = (RmdXmlObject) data.peek();
			if (curObj.getTag().equals("functions")) {
				isFuntion = true;
			}
		}
		if (isFuntion) {
			curObj = new RmdXmlFunction(eName);
		} else {
			curObj = new RmdXmlObject(eName);
		}

		for (int ix = 0; ix < attrs.getLength(); ix++) {
			String name = attrs.getLocalName(ix);
			String value = attrs.getValue(ix);
			if (name.length() == 0)
				name = attrs.getQName(ix);

			curObj.addAttr(name, value);
		}

		data.push(curObj);
	}

	public void endElement(String namespaceURI, String sName, String qName)
			throws SAXException {
		//The end of an element, parse the element (if there is any)
		if (charStr != null) {
			characters(charStr);
			charStr = null;
		}

		String eName = sName;
		if ((eName == null) || (eName.length() == 0))
			eName = qName;

		// System.out.println("Element " + eName + " <-");

		if (data.size() > 0) {
			RmdXmlObject topObj = (RmdXmlObject) data.pop();
			if (data.size() == 0) {
				topObj.doSetup(null, m_AppIfc);
			} else {
				((RmdXmlObject) data.peek()).addObject(topObj); // Push previous top into current top object.
			}
		}
	}

	public void characters(char buf[], int offset, int len) throws SAXException {
		if (charStr == null) {
			charStr = new String();
		}

		charStr = charStr + new String(buf, offset, len);
	}

	private void characters(String s) throws SAXException {
		if (data.size() > 0) {
			RmdXmlObject curObj = (RmdXmlObject) data.peek();
			String name = curObj.getTag();
			curObj.addAttr(name, s);
		}
	}

	public InputSource resolveEntity(String publicId, String systemId) {
		// Construct an InputSource from the system ID
		InputSource input = null;

		if (publicId != null) {
			String sResource = getClass().getResource("dtd/" + publicId)
					.toString();
			input = new InputSource(sResource);
		}

		return input;
	}

	public void setDocumentLocator(Locator loc) {
		locator = loc;
	}

	public void warning(SAXParseException err) throws SAXException {
		throw new SAXException(makeError("SAX Warning: " + err.getMessage()));
	}

	public void error(SAXParseException err) throws SAXException {
		throw new SAXException(makeError("SAX Error: " + err.getMessage()));
	}

	public void fatalError(SAXParseException err) throws SAXException {
		throw new SAXException(makeError("SAX Fatal: " + err.getMessage()));
	}

	//===========================================================
	// Utility Methods ...
	//===========================================================

	private String makeError(String description) {
		return makeError(description, 0);
	}

	private String makeError(String description, int lineAdjust) {
		String message = "Error when reading rmd file!" +
		//"\nFile: " + locator.getSystemId() +
				"\nLine: " + (locator.getLineNumber() + lineAdjust)
				+ "\nError: " + description;
		return message;
	}

	//Compares String s1 until a dot is found with String s2
	//Example: s1="abc.xyz", s2="abc" -> true; s1="aba.xyz", s2="abc" -> false
	/*
	 private boolean strEqualsUntilDot(String s1, String s2) {
	 int i;
	 if ((i=s1.indexOf('.')) == -1)
	 return s2.equals(s1);
	 else
	 return s2.equals(s1.substring(0,i));
	 }
	 */
}