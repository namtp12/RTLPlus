package org.uet.dse.rtlplus.matching;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.mm.MOperation;
import org.tzi.use.uml.sys.MObject;
import org.tzi.use.uml.sys.MSystemState;

/**
 * Searches the system for objects and links that match the specification. Is
 * quite slow :(
 */

public class MatchFinder {

	private MSystemState systemState;
	private MOperation operation;
	private List<MObject> ruleObjects;
	private List<Map<String, MObject>> matches;

	public MatchFinder(MSystemState systemState, MOperation operation, List<MObject> ruleObjects) {
		super();
		this.systemState = systemState;
		this.operation = operation;
		this.ruleObjects = ruleObjects;
		this.matches = new ArrayList<Map<String, MObject>>();
	}

	/* Start finding a match */
	public List<? extends Map<String, MObject>> run() {
		if (ruleObjects.isEmpty())
			return null;
		findMatchAtPosition(new LinkedHashMap<String, MObject>(), 0);
		// TODO: this is empty when a match fails because findMatchAtPosition is not
		// called recursively anymore, but not empty when there are actually 0 objects to
		// match because if (...) returns true. Hm.
		return matches;
	}
	
	public List<? extends Map<String, MObject>> run(List<MObject> objs) {
		if (ruleObjects.isEmpty())
			return null;
		findMatchAtPosition(new LinkedHashMap<String, MObject>(), 0, objs);
		return matches;
	}

	private void findMatchAtPosition(Map<String, MObject> objs, int position, List<MObject> objects) {
		// TODO: What to do when there are no objects of a class X?
		// TODO: Improve this by checking for links/conditions
		// TODO: CorrRule?
		// TODO: Filter by object?
		if (position >= ruleObjects.size()) {
			for (MObject object : objects) {
				if (objs.containsValue(object)) {
					Map<String, MObject> objMap = new LinkedHashMap<String, MObject>(objs);
					matches.add(objMap);
				}
			}
			// System.out.println(matches);
		} else {
			MClass cls = ruleObjects.get(position).cls();
			for (MObject obj : systemState.objectsOfClassAndSubClasses(cls)) {
				String varName = ruleObjects.get(position).name();
				if (!objs.containsValue(obj)) {
					objs.put(varName, obj);
					findMatchAtPosition(objs, position + 1);
					objs.remove(varName);
				}
			}
		}
	}

	private void findMatchAtPosition(Map<String, MObject> objs, int position) {
		// TODO: What to do when there are no objects of a class X?
		// TODO: Improve this by checking for links/conditions
		// TODO: CorrRule?
		// TODO: Filter by object?
		if (position >= ruleObjects.size()) {
			Map<String, MObject> objMap = new LinkedHashMap<String, MObject>(objs);
			matches.add(objMap);
			//System.out.println(matches);
		} else {
			MClass cls = ruleObjects.get(position).cls();
			for (MObject obj : systemState.objectsOfClassAndSubClasses(cls)) {
				String varName = ruleObjects.get(position).name();
				if (!objs.containsValue(obj)) {
					objs.put(varName, obj);
					findMatchAtPosition(objs, position + 1);
					objs.remove(varName);
				}
			}
		}
	}
}
